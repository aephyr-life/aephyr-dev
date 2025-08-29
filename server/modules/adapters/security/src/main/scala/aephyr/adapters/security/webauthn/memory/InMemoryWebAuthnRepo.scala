package aephyr.adapters.security.webauthn.memory

import zio.*
import java.util.Base64
import aephyr.auth.ports.WebAuthnRepo
import aephyr.auth.domain.Credential
import aephyr.identity.domain.User

object B64Url {
  private val enc = Base64.getUrlEncoder.nn.withoutPadding().nn
  private val dec = Base64.getUrlDecoder.nn
  def encBytes(b: Array[Byte]): String = enc.encodeToString(b).nn
  def decStr(s: String): Array[Byte]   = dec.decode(s).nn
}

final case class InMemoryWebAuthnRepo(
                                       byCredId: Ref[Map[String, Credential]],       // key: b64url(credentialId)
                                       byUser:   Ref[Map[User.Id, List[Credential]]]
                                     ) extends WebAuthnRepo {

  def insert(c: Credential): Task[Unit] = {
    val key = B64Url.encBytes(c.credentialId)
    byUser.update(m => m.updated(c.userId, c :: m.getOrElse(c.userId, Nil))) *>
      byCredId.update(_ + (key -> c)).unit
  }

  def findByUser(userId: User.Id): Task[List[Credential]] =
    byUser.get.map(_.getOrElse(userId, Nil))

  def findByCredentialId(credId: Array[Byte]): Task[Option[Credential]] =
    byCredId.get.map(_.get(B64Url.encBytes(credId)))

  def updateSignCount(credId: Array[Byte], newCount: Long): Task[Unit] = {
    val key = B64Url.encBytes(credId)
    byCredId.update { m =>
      m.get(key) match {
        case Some(c) => m.updated(key, c.copy(signCount = newCount))
        case None    => m
      }
    }.unit
  }
}

object InMemoryWebAuthnRepo {
  val live: ZLayer[Any, Nothing, WebAuthnRepo] =
    ZLayer.fromZIO(
      for {
        c <- Ref.make(Map.empty[String, Credential])
        u <- Ref.make(Map.empty[User.Id, List[Credential]])
      } yield InMemoryWebAuthnRepo(c, u)
    )
}
