package aephyr.adapters.security.webauthn.memory

import aephyr.auth.domain.UserHandle

import scala.language.unsafeNulls
import zio.*

import scala.jdk.CollectionConverters.*
import com.yubico.webauthn.*
import com.yubico.webauthn.data.{ByteArray, PublicKeyCredentialDescriptor}

import com.yubico.webauthn.data.*

import aephyr.auth.ports.WebAuthnRepo
import aephyr.auth.ports.UserHandleRepo
import aephyr.shared.config.AppConfig

object InMemoryRelyingParty {
  val live: ZLayer[WebAuthnRepo & UserHandleRepo & AppConfig, Throwable, RelyingParty] =
    ZLayer.fromZIO {
      for {
        creds <- ZIO.service[WebAuthnRepo]
        handles <- ZIO.service[UserHandleRepo]
        cfg  <- ZIO.service[AppConfig]
        rt   <- ZIO.runtime[Any]
      } yield {
        val credRepo = new CredentialRepository {

          import java.util.{Collections, Optional}

          override def getCredentialIdsForUsername(username: String)
          : java.util.Set[PublicKeyCredentialDescriptor] =
            Collections.emptySet()

          override def getUserHandleForUsername(username: String): Optional[ByteArray] =
            Optional.empty()

          override def getUsernameForUserHandle(userHandle: ByteArray): Optional[String] = {
            // Use your UserHandleRepo to resolve handle -> userId -> username/alias
            val z = handles
              .findByHandle(UserHandle(userHandle.getBytes))
              .flatMap {
                case Some(uid) => handles.usernameFor(uid)
                case None      => ZIO.succeed(None)
              }
              .orElseSucceed(None)

            val value =
              Unsafe.unsafe { implicit u => rt.unsafe.run(z).getOrThrowFiberFailure() }
            Optional.ofNullable(value.orNull)
          }

          // Helper to run Task already bound to concrete services
          private def runTask[A](io: zio.Task[A]): A =
            Unsafe.unsafe { implicit u => rt.unsafe.run(io).getOrThrowFiberFailure() }

          // Called when authenticator supplies userHandle; we still ignore it and
          // return what we have in DB (safer, canonical).
          override def lookup(credentialId: ByteArray, userHandle: ByteArray)
          : Optional[RegisteredCredential] = {
            val cOpt = runTask(creds.findByCredentialId(credentialId.getBytes))
            Optional.ofNullable(
              cOpt.map { c =>
                RegisteredCredential.builder()
                  .credentialId(credentialId)
                  .userHandle(new ByteArray(c.userHandleBytes.toArray))
                  .publicKeyCose(new ByteArray(c.publicKeyCose.toArray))
                  .signatureCount(c.signCount.toInt)
                  .build()
              }.orNull
            )
          }

          // Called when authenticator omits userHandle (common case you’re seeing).
          // MUST return a RegisteredCredential with the STORED userHandle bytes.
          override def lookupAll(credentialId: ByteArray): java.util.Set[RegisteredCredential] = {
            val cOpt = runTask(creds.findByCredentialId(credentialId.getBytes))
            cOpt.map { c =>
              Collections.singleton(
                RegisteredCredential.builder()
                  .credentialId(credentialId)
                  .userHandle(new ByteArray(c.userHandleBytes.toArray))
                  .publicKeyCose(new ByteArray(c.publicKeyCose.toArray))
                  .signatureCount(c.signCount.toInt)
                  .build()
              )
            }.getOrElse(Collections.emptySet())
          }
        }

        val rpId = RelyingPartyIdentity
          .builder()
          .id(cfg.auth.webauthn.rpId)
          .name(cfg.auth.webauthn.rpName)
          .build()
        RelyingParty.builder()
          .identity(rpId)
          .credentialRepository(credRepo)
          .origins(cfg.auth.webauthn.origins.asJava)
          .allowUntrustedAttestation(true)  // dev
          .build()
      }
    }
}
