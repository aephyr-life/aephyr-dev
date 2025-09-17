package aephyr.auth.ports

import zio.*
import aephyr.auth.domain.Credential
import aephyr.auth.domain.webauthn.{ CredentialId, Label }
import aephyr.kernel.id.UserId

import java.time.Instant

trait WebAuthnRepo {
  def insert(c: Credential): Task[Unit]
  def findByUser(userId: UserId): Task[List[Credential]]
  def findByCredentialId(credId: CredentialId): Task[Option[Credential]]
  def updateSignCount(credId: CredentialId, newCount: Long): Task[Unit]
  def updateUsage(
    credentialId: CredentialId,
    newSignCount: Long,
    now: Instant
  ): Task[Unit]
  def rename(credentialId: CredentialId, label: Option[Label]): Task[Unit]
  def disable(credentialId: CredentialId): Task[Unit]
  def delete(credentialId: CredentialId): Task[Unit]
}
