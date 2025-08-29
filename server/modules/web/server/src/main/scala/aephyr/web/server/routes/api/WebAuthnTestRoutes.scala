package aephyr.web.server.routes.api

import scala.language.unsafeNulls

import sttp.capabilities.WebSockets
import sttp.capabilities.zio.ZioStreams
import sttp.tapir.generic.auto.*
import sttp.tapir.json.zio.*
import sttp.tapir.ztapir.*
import sttp.tapir.server.ziohttp.ZioHttpInterpreter
import sttp.tapir.server.interceptor.cors.CORSInterceptor
import sttp.tapir.server.ziohttp.ZioHttpServerOptions
import sttp.model.StatusCode
import zio.*
import zio.http.{Routes, Response}
import zio.json.*
import zio.json.ast.Json

import com.yubico.webauthn.{
  RelyingParty,
  AssertionRequest,
  FinishRegistrationOptions,
  FinishAssertionOptions,
  StartRegistrationOptions,
  StartAssertionOptions
}
import com.yubico.webauthn.data.{
  PublicKeyCredential,
  PublicKeyCredentialCreationOptions,
  UserIdentity,
  AuthenticatorSelectionCriteria,
  ResidentKeyRequirement,
  UserVerificationRequirement,
  ByteArray
}

import aephyr.auth.ports.*
import aephyr.auth.domain.*
import aephyr.identity.domain.User

final case class BeginRegInput(userId: String, username: String, displayName: String) derives JsonCodec
final case class BeginRegOutput(publicKey: Json, tx: String) derives JsonCodec
final case class FinishRegInput(tx: String, responseJson: String, label: Option[String]) derives JsonCodec

final case class BeginAuthInput(username: Option[String]) derives JsonCodec
final case class BeginAuthOutput(publicKey: Json, tx: String) derives JsonCodec
final case class FinishAuthInput(tx: String, responseJson: String) derives JsonCodec

object WebAuthnTestRoutes:

  private type Env = RelyingParty & ChallengeStore & UserHandleRepo & WebAuthnRepo

  private val base = endpoint.in("api" / "webauthn")

  private val eBeginReg   = base.post.in("registration"   / "options").in(jsonBody[BeginRegInput]).out(jsonBody[BeginRegOutput])
  private val eFinishReg  =
    base.post.in("registration"   / "verify" )
      .in(jsonBody[FinishRegInput])
      .out(statusCode)
      .errorOut(stringBody)
  private val eBeginAuth  = base.post.in("authentication" / "options").in(jsonBody[BeginAuthInput]).out(jsonBody[BeginAuthOutput])
  private val eFinishAuth =
    base.post.in("authentication" / "verify" )
      .in(jsonBody[FinishAuthInput])
      .out(statusCode.and(stringBody))
      .errorOut(stringBody)

  import zio.json._
  import zio.json.ast.Json

  // If s == {"publicKey":{...}}, return just the inner {...}; otherwise return s unchanged.
  private def unwrapPublicKeyJsonString(s: String): String =
    s.fromJson[Json] match
      case Right(Json.Obj(fields)) =>
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
          rp <- ZIO.service[RelyingParty]
          txs <- ZIO.service[ChallengeStore]
          uhRepo <- ZIO.service[UserHandleRepo]

          userId <- ZIO.attempt(User.Id(java.util.UUID.fromString(in.userId))).mapError(_ => ())

          uhOpt <- uhRepo.get(userId).mapError(_ => ())
          uh <- uhOpt match
            case Some(h) => ZIO.succeed(h)
            case None =>
              for {
                bytes <- Random.nextBytes(32)
                h = UserHandle(bytes.toArray)
                _ <- uhRepo.put(userId, h).mapError(_ => ())
              } yield h

          user = UserIdentity.builder()
            .name(in.username)
            .displayName(in.displayName)
            .id(new ByteArray(uh.bytesCopy))
            .build()

//          regExt = RegistrationExtensionInputs.builder()
//            .credProps()   // request
//            .build()

          opts = StartRegistrationOptions.builder()
            .user(user)
            //.extensions(regExt)
            .authenticatorSelection(
              AuthenticatorSelectionCriteria.builder()
                .residentKey(ResidentKeyRequirement.REQUIRED)
                .userVerification(UserVerificationRequirement.REQUIRED)
                .build()
            )
            .build()

          req <- ZIO.succeed(rp.startRegistration(opts))

          tx <- txs.putReg(uh.bytesCopy, req.toJson)

          clientStr = unwrapPublicKeyJsonString(req.toCredentialsCreateJson)
          publicKey <- ZIO.fromEither(clientStr.fromJson[Json]).mapError(_ => ())
        } yield BeginRegOutput(publicKey, tx)
      }

    // keep your Env = RelyingParty & ChallengeStore & UserHandleRepo & WebAuthnRepo

    val finishRegEp: ZServerEndpoint[Env, ZioStreams & WebSockets] =
      eFinishReg.zServerLogic { in =>
        for {
          rp <- ZIO.service[RelyingParty]
          txs <- ZIO.service[ChallengeStore]
          repo <- ZIO.service[WebAuthnRepo]
          uhRepo <- ZIO.service[UserHandleRepo]

          // unify the error type right here
          tuple <- txs.getReg(in.tx).flatMap {
            case Some(t) => ZIO.succeed(t)
            case None => ZIO.fail("unknown tx")
          } // ZIO[ChallengeStore, Err, (Array[Byte], String)]
          (uhBytes, reqJson) = tuple

          reqOpts <- ZIO
            .attempt(PublicKeyCredentialCreationOptions.fromJson(reqJson))
            .mapError(e => s"bad request options json: ${e.getMessage}")
          resp <- ZIO
            .attempt(PublicKeyCredential.parseRegistrationResponseJson(in.responseJson))
            .mapError(e => s"bad client response json: ${e.getMessage}")
          res <- ZIO
            .attempt(
              rp.finishRegistration(
                FinishRegistrationOptions.builder().request(reqOpts).response(resp).build()
              )
            )
            .mapError(e => s"finishRegistration failed: ${e.getMessage}")

          userId <- uhRepo
            .findByHandle(UserHandle(uhBytes))
            .mapError(e => s"findByHandle failed: ${e.getMessage}")
            .someOrFail("no user for handle")

          cred = Credential(
            id = java.util.UUID.randomUUID(),
            userId = userId,
            credentialId = res.getKeyId.getId.getBytes,
            publicKeyCose = res.getPublicKeyCose.getBytes,
            signCount = 0L,
            userHandleBytes = uhBytes,
            uvRequired = false,
            transports = Nil,
            label = in.label,
            createdAt = java.time.Instant.now(),
            updatedAt = java.time.Instant.now(),
            lastUsedAt = None
          )

          _ <- repo.insert(cred).mapError(e => s"insert failed: $e")
          _ <- txs.delReg(in.tx) // .mapError(e => s"delReg failed: $e")
        } yield StatusCode.Ok
      }

    val beginAuthEp: ZServerEndpoint[Env, ZioStreams & WebSockets] =
      eBeginAuth.zServerLogic { in =>
        for {
          rp <- ZIO.service[RelyingParty]
          req = rp.startAssertion(
            StartAssertionOptions
              .builder()
              .userVerification(UserVerificationRequirement.REQUIRED)
              .build()
          )
          txs <- ZIO.service[ChallengeStore]
          tx  <- txs.putAuth(req.toJson)
          clientStr = unwrapPublicKeyJsonString(req.toCredentialsGetJson)
          publicKeyJs <- ZIO.fromEither(clientStr.fromJson[Json]).mapError(_ => ())
        } yield BeginAuthOutput(publicKeyJs, tx)
      }

    def msg(e: Throwable, prefix: String) =
      s"$prefix: ${Option(e.getMessage).filter(_.nonEmpty).getOrElse(e.toString)}"

    val finishAuthEp: ZServerEndpoint[Env, ZioStreams & WebSockets] =
      eFinishAuth.zServerLogic { in =>
        (for {
          rp    <- ZIO.service[RelyingParty]
          txs   <- ZIO.service[ChallengeStore]
          repo  <- ZIO.service[WebAuthnRepo]

          reqJson <- txs.getAuth(in.tx).someOrFail("unknown tx")
          req     <- ZIO.attempt(AssertionRequest.fromJson(reqJson))
            .mapError(e => msg(e, "bad request options json"))

          cred <- ZIO.attempt(PublicKeyCredential.parseAssertionResponseJson(in.responseJson))
            .mapError(e => msg(e, "bad client response json"))

          res  <- ZIO.attempt(
            rp.finishAssertion(
              FinishAssertionOptions
                .builder()
                .request(req)
                .response(cred)
                .build()
            )
          ).mapError(e => msg(e, "finishAssertion failed"))

          _ <- repo.updateSignCount(
            res.getCredential.getCredentialId.getBytes,
            res.getCredential.getSignatureCount.toLong
          ).mapError(e => s"updateSignCount failed: $e")

          _ <- txs.delAuth(in.tx)
        } yield (StatusCode.Ok, s"ok"))
          .tapErrorCause(c => ZIO.logError(s"finishAuth failed: ${c.prettyPrint}"))
          .mapError {
            case s: String if (s.nonEmpty) => s
            case _                         => "unknown error"
          }
      }

    val epsAny: List[ZServerEndpoint[Env, ZioStreams & WebSockets]] =
      List(beginRegEp, finishRegEp, beginAuthEp, finishAuthEp)

    val serverOptions: ZioHttpServerOptions[Env] = ZioHttpServerOptions
      .default
      .prependInterceptor(CORSInterceptor.default)

    ZioHttpInterpreter[Env](serverOptions).toHttp(epsAny)
  }
