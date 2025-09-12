package aephyr.auth.ports

import aephyr.auth.domain.AuthTx
import aephyr.kernel.types.Bytes
import zio.*

/** Store short-lived WebAuthn option blobs (as JSON) keyed by a tx id. */
trait ChallengeStore {
  // registration (store options JSON; also store the userHandle bytes for convenience if you like)
  def putReg(userHandle: Bytes, requestJson: String): UIO[AuthTx]
  def getReg(tx: AuthTx): UIO[Option[(Bytes, String)]]  // (userHandle, requestJson)
  def delReg(tx: AuthTx): UIO[Unit]

  // authentication
  def putAuth(requestJson: String): UIO[AuthTx]
  def getAuth(tx: AuthTx): UIO[Option[String]]
  def delAuth(tx: AuthTx): UIO[Unit]
}
