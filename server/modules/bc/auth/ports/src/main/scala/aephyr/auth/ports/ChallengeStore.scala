package aephyr.auth.ports

import zio.*

/** Store short-lived WebAuthn option blobs (as JSON) keyed by a tx id. */
trait ChallengeStore {
  // registration (store options JSON; also store the userHandle bytes for convenience if you like)
  def putReg(userHandle: Array[Byte], requestJson: String): UIO[String]
  def getReg(tx: String): UIO[Option[(Array[Byte], String)]]  // (userHandle, requestJson)
  def delReg(tx: String): UIO[Unit]

  // authentication
  def putAuth(requestJson: String): UIO[String]
  def getAuth(tx: String): UIO[Option[String]]
  def delAuth(tx: String): UIO[Unit]
}
