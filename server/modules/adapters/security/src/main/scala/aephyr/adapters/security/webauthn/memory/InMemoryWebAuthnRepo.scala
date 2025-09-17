package aephyr.adapters.security.webauthn.memory

import zio.*

import java.util.Base64
import aephyr.auth.ports.WebAuthnRepo
import aephyr.auth.domain.Credential
import aephyr.auth.domain.webauthn.{CredentialId, Label}
import aephyr.kernel.id.UserId

import java.time.Instant

object B64Url {
  private val enc = Base64.getUrlEncoder.nn.withoutPadding().nn
  private val dec = Base64.getUrlDecoder.nn
  def encBytes(b: Array[Byte]): String = enc.encodeToString(b).nn
  def decStr(s: String): Array[Byte]   = dec.decode(s).nn
}

final case class InMemoryWebAuthnRepo(
                                       byCredId: Ref[Map[String, Credential]],       // key: b64url(credentialId)
                                       byUser:   Ref[Map[UserId, List[Credential]]]
                                     ) extends WebAuthnRepo {

  def insert(c: Credential): Task[Unit] = {
    val key = B64Url.encBytes(c.credentialId.bytes.toArray)
    byUser.update(m => m.updated(c.userId, c :: m.getOrElse(c.userId, Nil))) *>
      byCredId.update(_ + (key -> c)).unit
  }

  override def findByUser(userId: UserId): Task[List[Credential]] =
    byUser.get.map(_.getOrElse(userId, Nil))

  override def findByCredentialId(credId: CredentialId): Task[Option[Credential]] =
    byCredId.get.map(_.get(B64Url.encBytes(credId.bytes.toArray)))

  override def updateSignCount(credId: CredentialId, newCount: Long): Task[Unit] = {
    val key = B64Url.encBytes(credId.bytes.toArray)
    byCredId.update { m =>
      m.get(key) match {
        case Some(c) => m.updated(key, c.copy(signCount = newCount))
        case None    => m
      }
    }.unit
  }

  override def updateUsage(credentialId: CredentialId, newSignCount: Long, now: Instant): Task[Unit] =
    val key = B64Url.encBytes(credentialId.bytes.toArray)

    byCredId.update { m =>
      m.get(key) match {
        case Some(c) =>
          // Never decrease the stored counter; clamp negatives to 0 just in case
          val nextCount = math.max(c.signCount, math.max(0L, newSignCount))
          val updated = c.copy(
            signCount = nextCount,
            lastUsedAt = Some(now),
            updatedAt = now
          )
          m.updated(key, updated)

        case None =>
          // Credential not found -> keep map unchanged (no-op), like your updateSignCount
          m
      }
    }.unit

  override def rename(credentialId: CredentialId, label: Option[Label]): Task[Unit] = ???

  override def disable(credentialId: CredentialId): Task[Unit] = ???

  override def delete(credentialId: CredentialId): Task[Unit] = ???
}

object InMemoryWebAuthnRepo {
  val live: ZLayer[Any, Nothing, WebAuthnRepo] =
    ZLayer.fromZIO(
      for {
        c <- Ref.make(Map.empty[String, Credential])
        u <- Ref.make(Map.empty[UserId, List[Credential]])
      } yield InMemoryWebAuthnRepo(c, u)
    )
}
