package aephyr.auth.ports

import zio.*
import aephyr.auth.domain.Credential
import aephyr.kernel.id.UserId

trait WebAuthnRepo {
  def insert(c: Credential): Task[Unit]
  def findByUser(userId: UserId): Task[List[Credential]]
  def findByCredentialId(credId: Array[Byte]): Task[Option[Credential]]
  def updateSignCount(credId: Array[Byte], newCount: Long): Task[Unit]
}
