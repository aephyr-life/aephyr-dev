package aephyr.auth.ports

import zio.*
import aephyr.auth.domain.Credential
import aephyr.identity.domain.User

trait WebAuthnRepo {
  def insert(c: Credential): Task[Unit]
  def findByUser(userId: User.Id): Task[List[Credential]]
  def findByCredentialId(credId: Array[Byte]): Task[Option[Credential]]
  def updateSignCount(credId: Array[Byte], newCount: Long): Task[Unit]
}
