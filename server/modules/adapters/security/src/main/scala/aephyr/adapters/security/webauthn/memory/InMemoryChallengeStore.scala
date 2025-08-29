package aephyr.adapters.security.webauthn.memory

import zio.*
import java.time.Instant
import java.util.concurrent.TimeUnit
import java.util.UUID
import aephyr.auth.ports.ChallengeStore

final case class InMemoryChallengeStore(
                                         reg: Ref[Map[String, (Array[Byte], String, Instant)]],  // tx -> (userHandle, json, expires)
                                         auth: Ref[Map[String, (String, Instant)]],              // tx -> (json, expires)
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

  def putReg(userHandle: Array[Byte], requestJson: String): UIO[String] =
    for {
      _     <- cleanReg
      tx     = UUID.randomUUID().toString
      nowMs <- Clock.currentTime(TimeUnit.MILLISECONDS)
      exp    = Instant.ofEpochMilli(nowMs + ttl.toMillis).nn
      _     <- reg.update(_ + (tx -> (userHandle, requestJson, exp)))
    } yield tx

  def getReg(tx: String): UIO[Option[(Array[Byte], String)]] =
    reg.get.map(_.get(tx).map { case (uh, json, _) => (uh, json) })

  def delReg(tx: String): UIO[Unit] =
    reg.update(_ - tx).unit

  def putAuth(requestJson: String): UIO[String] =
    for {
      _     <- cleanAuth
      tx     = UUID.randomUUID().toString
      nowMs <- Clock.currentTime(TimeUnit.MILLISECONDS)
      exp    = Instant.ofEpochMilli(nowMs + ttl.toMillis).nn
      _     <- auth.update(_ + (tx -> (requestJson, exp)))
    } yield tx

  def getAuth(tx: String): UIO[Option[String]] =
    auth.get.map(_.get(tx).map(_._1))

  def delAuth(tx: String): UIO[Unit] =
    auth.update(_ - tx).unit
}

object InMemoryChallengeStore {
  def live(ttl: zio.Duration = 5.minutes): ZLayer[Any, Nothing, ChallengeStore] =
    ZLayer.fromZIO(
      for {
        r <- Ref.make(Map.empty[String, (Array[Byte], String, Instant)])
        a <- Ref.make(Map.empty[String, (String, Instant)])
      } yield InMemoryChallengeStore(r, a, ttl)
    )
}
