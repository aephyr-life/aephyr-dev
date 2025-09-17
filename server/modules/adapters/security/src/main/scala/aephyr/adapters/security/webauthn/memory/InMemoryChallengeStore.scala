package aephyr.adapters.security.webauthn.memory

import aephyr.auth.domain.AuthTx
import zio.*

import java.time.Instant
import java.util.concurrent.TimeUnit
import aephyr.auth.ports.ChallengeStore
import aephyr.kernel.types.Bytes

final case class InMemoryChallengeStore(
                                         reg: Ref[Map[AuthTx, (Bytes, String, Instant)]],  // tx -> (userHandle, json, expires)
                                         auth: Ref[Map[AuthTx, (String, Instant)]],              // tx -> (json, expires)
                                         ttl: zio.Duration
                                       ) extends ChallengeStore {

  private def cleanReg: UIO[Unit] =
    Clock.instant.flatMap { now =>
      reg.update(_.filter { case (_, (_, _, exp)) => exp.isAfter(now) }).unit
    }

  private def cleanAuth: UIO[Unit] =
    Clock.instant.flatMap { now =>
      auth.update(_.filter { case (_, (_, exp)) => exp.isAfter(now) }).unit
    }

  def putReg(userHandle: Bytes, requestJson: String): UIO[AuthTx] =
    for {
      _     <- cleanReg
      tx     = AuthTx.generate()
      nowMs <- Clock.currentTime(TimeUnit.MILLISECONDS)
      exp    = Instant.ofEpochMilli(nowMs + ttl.toMillis).nn
      _     <- reg.update(_ + (tx -> (userHandle, requestJson, exp)))
    } yield tx

  def getReg(tx: AuthTx): UIO[Option[(Bytes, String)]] =
    reg.get.map(_.get(tx).map { case (uh, json, _) => (uh, json) })

  def delReg(tx: AuthTx): UIO[Unit] =
    reg.update(_ - tx).unit

  def putAuth(requestJson: String): UIO[AuthTx] =
    for {
      _     <- cleanAuth
      tx     = AuthTx.generate()
      nowMs <- Clock.currentTime(TimeUnit.MILLISECONDS)
      exp    = Instant.ofEpochMilli(nowMs + ttl.toMillis).nn
      _     <- auth.update(_ + (tx -> (requestJson, exp)))
    } yield tx

  def getAuth(tx: AuthTx): UIO[Option[String]] =
    auth.get.map(_.get(tx).map(_._1))

  def delAuth(tx: AuthTx): UIO[Unit] =
    auth.update(_ - tx).unit
}

object InMemoryChallengeStore {
  def live(ttl: zio.Duration = 5.minutes): ZLayer[Any, Nothing, ChallengeStore] =
    ZLayer.fromZIO(
      for {
        r <- Ref.make(Map.empty[AuthTx, (Bytes, String, Instant)])
        a <- Ref.make(Map.empty[AuthTx, (String, Instant)])
      } yield InMemoryChallengeStore(r, a, ttl)
    )
}
