package aephyr.adapters.security

import aephyr.shared.security.SecureRandom

import zio.*

import java.security.{ SecureRandom => JavaRandom }

final case class SecureRandomLive(random: JavaRandom) extends SecureRandom:

  override def nextBytes(length: Int): UIO[Array[Byte]] =
    ZIO.succeed {
      val arr = new Array[Byte](length)
      random.nextBytes(arr)
      arr
    }

object SecureRandomLive:

  val layer: ULayer[SecureRandom] =
    ZLayer.succeed(SecureRandomLive(new JavaRandom()))
