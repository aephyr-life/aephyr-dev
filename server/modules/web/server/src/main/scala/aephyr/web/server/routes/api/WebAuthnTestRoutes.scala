package aephyr.web.server.routes.api

import scala.language.unsafeNulls

import sttp.capabilities.WebSockets
import sttp.capabilities.zio.ZioStreams
import sttp.tapir.generic.auto.*
import sttp.tapir.json.zio.*
import sttp.tapir.ztapir.*                      // Endpoint & ZIO server syntax
import sttp.tapir.server.ziohttp.ZioHttpInterpreter
import sttp.tapir.server.interceptor.cors.CORSInterceptor
import zio.*
import zio.json.*
import zio.json.ast.Json
import zio.http.{Routes, Response}
import sttp.tapir.server.ziohttp.ZioHttpServerOptions
import sttp.model.StatusCode

import com.yubico.webauthn.{
  RelyingParty,
  AssertionRequest,
  AssertionResult,
  FinishRegistrationOptions,
  FinishAssertionOptions,
  StartRegistrationOptions,
  StartAssertionOptions
}
import com.yubico.webauthn.data.{
  PublicKeyCredential,
  PublicKeyCredentialCreationOptions,
  PublicKeyCredentialRequestOptions,
  UserIdentity,
  AuthenticatorSelectionCriteria,
  ResidentKeyRequirement,
  UserVerificationRequirement,
  ByteArray
}

import aephyr.auth.ports.*
import aephyr.auth.domain.*
import aephyr.identity.domain.User
import zio.json.JsonCodec

final case class BeginRegInput(userId: String, username: String, displayName: String) derives JsonCodec
final case class BeginRegOutput(publicKey: Json, tx: String) derives JsonCodec
final case class FinishRegInput(tx: String, responseJson: String, label: Option[String]) derives JsonCodec

final case class BeginAuthInput(username: Option[String]) derives JsonCodec
final case class BeginAuthOutput(publicKey: Json, tx: String) derives JsonCodec
final case class FinishAuthInput(tx: String, responseJson: String) derives JsonCodec

object WebAuthnTestRoutes:

  // single, unified env so List[ZServerEndpoint[Env, …]] compiles
  private type Env = RelyingParty & ChallengeStore & UserHandleRepo & WebAuthnRepo

  private val base = endpoint.in("api" / "webauthn")

  private val eBeginReg   = base.post.in("registration"   / "options").in(jsonBody[BeginRegInput]).out(jsonBody[BeginRegOutput])
  private val eFinishReg  = base.post.in("registration"   / "verify" ).in(jsonBody[FinishRegInput]).out(statusCode)
  private val eBeginAuth  = base.post.in("authentication" / "options").in(jsonBody[BeginAuthInput]).out(jsonBody[BeginAuthOutput])
  private val eFinishAuth = base.post.in("authentication" / "verify" ).in(jsonBody[FinishAuthInput]).out(statusCode)

  import zio.json._
  import zio.json.ast.Json

  // If s == {"publicKey":{...}}, return just the inner {...}; otherwise return s unchanged.
  private def unwrapPublicKeyJsonString(s: String): String =
    s.fromJson[Json] match
      case Right(Json.Obj(fields)) =>
        // Try to extract the "publicKey" field
        fields.collectFirst { case (k, v) if k == "publicKey" => v }
          .map(_.toJson) // return only the inner JSON as a string
          .getOrElse(Json.Obj(fields).toJson) // keep the object as-is
      case Right(other) =>
        other.toJson // it’s valid JSON, just return it
      case Left(_) =>
        s // not JSON? leave the original string

  val routes: Routes[Env, Response] = {

    val beginRegEp: ZServerEndpoint[Env, ZioStreams & WebSockets] =
      eBeginReg.zServerLogic { in =>
        for {
          rp     <- ZIO.service[RelyingParty]
          txs    <- ZIO.service[ChallengeStore]
          uhRepo <- ZIO.service[UserHandleRepo]

          userId <- ZIO.attempt(User.Id(java.util.UUID.fromString(in.userId))).mapError(_ => ())

          uhOpt  <- uhRepo.get(userId).mapError(_ => ())
          uh     <- uhOpt match
            case Some(h) => ZIO.succeed(h)
            case None    =>
              for {
                bytes <- Random.nextBytes(32)
                h      = UserHandle.fromBytes(bytes.toArray)
                _     <- uhRepo.put(userId, h).mapError(_ => ())
              } yield h

          user = UserIdentity.builder()
            .name(in.username)
            .displayName(in.displayName)
            .id(new ByteArray(uh.bytes))
            .build()

          opts = StartRegistrationOptions.builder()
            .user(user)
            .authenticatorSelection(
              AuthenticatorSelectionCriteria
                .builder()
                .residentKey(ResidentKeyRequirement.PREFERRED)
                .userVerification(UserVerificationRequirement.PREFERRED)
                .build()
            )
            .build()

          req  <- ZIO.succeed(rp.startRegistration(opts))
          tx <- txs.putReg(uh.bytes, req.toJson)
          clientStr = unwrapPublicKeyJsonString(req.toCredentialsCreateJson)
          publicKeyAst <- ZIO.fromEither(clientStr.fromJson[Json]).mapError(_ => ())
        } yield BeginRegOutput(publicKeyAst, tx)
      }

    val finishRegEp: ZServerEndpoint[Env, ZioStreams & WebSockets] =
      eFinishReg.zServerLogic { in =>
        for {
          rp   <- ZIO.service[RelyingParty]
          txs  <- ZIO.service[ChallengeStore]
          repo <- ZIO.service[WebAuthnRepo]

          reqO  <- txs.getReg(in.tx)                                 // UIO[Option[(Array[Byte], String)]]
          tuple <- ZIO.fromOption(reqO).mapError(_ => ())
          uhBytes = tuple._1
          reqJson = tuple._2

          reqOpts <- ZIO.attempt(PublicKeyCredentialCreationOptions.fromJson(reqJson))
            .mapError(_ => ())
            .tapError(_ => ZIO.logError("bad request options json"))
          resp    <- ZIO.attempt(PublicKeyCredential.parseRegistrationResponseJson(in.responseJson))
            .mapError(_ => ())
            .tapError(_ => ZIO.logError("bad client response json"))
          res     <- ZIO.attempt(
            rp.finishRegistration(
              FinishRegistrationOptions.builder().request(reqOpts).response(resp).build()
            )
          )
            .mapError(_ => ())
            .tapError(_ => ZIO.logError("finish registration failed"))

          cred = Credential(
            id            = java.util.UUID.randomUUID(),
            userId        = User.Id(java.util.UUID.randomUUID()), // TODO: real mapping
            credentialId  = res.getKeyId.getId.getBytes,
            publicKeyCose = res.getPublicKeyCose.getBytes,
            signCount     = 0L,
            uvRequired    = false,
            transports    = Nil,
            label         = in.label,
            createdAt     = java.time.Instant.now(),
            updatedAt     = java.time.Instant.now(),
            lastUsedAt    = None
          )

          _  <- repo.insert(cred).mapError(_ => ())
          _  <- txs.delReg(in.tx)
        } yield StatusCode.Ok
      }

    val beginAuthEp: ZServerEndpoint[Env, ZioStreams & WebSockets] =
      eBeginAuth.zServerLogic { in =>
        for {
          rp <- ZIO.service[RelyingParty]
          req = rp.startAssertion(
            StartAssertionOptions
              .builder()
              .username(in.username.map(java.util.Optional.of).getOrElse(java.util.Optional.empty()))
              .userVerification(UserVerificationRequirement.REQUIRED)
              .build()
          )
          txs <- ZIO.service[ChallengeStore]
          tx  <- txs.putAuth(req.toJson)
          clientStr = unwrapPublicKeyJsonString(req.toCredentialsGetJson)
          publicKeyJs <- ZIO.fromEither(clientStr.fromJson[Json]).mapError(_ => ())
        } yield BeginAuthOutput(publicKeyJs, tx)
      }

    val finishAuthEp: ZServerEndpoint[Env, ZioStreams & WebSockets] =
      eFinishAuth.zServerLogic { in =>
        for {
          rp    <- ZIO.service[RelyingParty]
          txs   <- ZIO.service[ChallengeStore]
          repo  <- ZIO.service[WebAuthnRepo]

          reqJson <- txs.getAuth(in.tx).someOrFail(())                // IO[Unit, String]
          req     <- ZIO.attempt(AssertionRequest.fromJson(reqJson)).mapError(_ => ())

          cred <- ZIO.attempt(PublicKeyCredential.parseAssertionResponseJson(in.responseJson)).mapError(_ => ())

          res  <- ZIO.attempt(
            rp.finishAssertion(
              FinishAssertionOptions
                .builder()
                .request(req)
                .response(cred)
                .build()
            )
          ).mapError(_ => ())

          _ <- repo.updateSignCount(
            res.getCredential.getCredentialId.getBytes,
            res.getCredential.getSignatureCount.toLong
          ).mapError(_ => ())

          _ <- txs.delAuth(in.tx)
        } yield StatusCode.Ok
      }

    // uniform R across the list
    val epsAny: List[ZServerEndpoint[Env, ZioStreams & WebSockets]] =
      List(beginRegEp, finishRegEp, beginAuthEp, finishAuthEp)

    val serverOptions: ZioHttpServerOptions[Env] = ZioHttpServerOptions
      .default
      .prependInterceptor(CORSInterceptor.default)

    // Return Http[Any, Response] — provide layers here or at the call site
    ZioHttpInterpreter[Env](serverOptions).toHttp(epsAny)
  }
