package aephyr.adapters.security.webauthn.memory

import scala.language.unsafeNulls
import zio.*
import scala.jdk.CollectionConverters.*
import com.yubico.webauthn.*
import com.yubico.webauthn.data.{ByteArray, PublicKeyCredentialDescriptor}
import java.util.{Collections, Optional}
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

          override def getCredentialIdsForUsername(username: String)
          : java.util.Set[PublicKeyCredentialDescriptor] =
            Collections.emptySet()

          override def getUserHandleForUsername(username: String): Optional[ByteArray] =
            Optional.empty()

          override def getUsernameForUserHandle(userHandle: ByteArray): Optional[String] =
            Optional.empty()

          // helper to run Task
          private def runTask[A](zio: ZIO[Any, Throwable, A]): A =
            Unsafe.unsafe { implicit u => rt.unsafe.run(zio).getOrThrowFiberFailure() }

          override def lookup(credentialId: ByteArray, userHandle: ByteArray)
          : Optional[RegisteredCredential] = {
            val cOpt = runTask(creds.findByCredentialId(credentialId.getBytes))
            val uh = new ByteArray(userHandle.getBytes)
            Optional.ofNullable(
              cOpt.map { c =>
                RegisteredCredential.builder()
                  .credentialId(credentialId)
                  .userHandle(uh)
                  .publicKeyCose(new ByteArray(c.publicKeyCose))
                  .signatureCount(c.signCount.toInt)
                  .build()
              }.orNull
            )
          }

          override def lookupAll(credentialId: ByteArray): java.util.Set[RegisteredCredential] = {
            val cOpt = runTask(creds.findByCredentialId(credentialId.getBytes))
            cOpt
              .map { c =>
                Collections.singleton(
                  RegisteredCredential.builder()
                    .credentialId(credentialId)
                    .userHandle(new ByteArray(Array.emptyByteArray))
                    .publicKeyCose(new ByteArray(c.publicKeyCose))
                    .signatureCount(c.signCount.toInt)
                    .build()
                )
              }
              .getOrElse(Collections.emptySet())
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
