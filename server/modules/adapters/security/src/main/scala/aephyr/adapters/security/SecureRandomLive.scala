package aephyr.adapters.security

import aephyr.shared.security.SecureRandom
import zio.*

import java.security.SecureRandom as JavaRandom
import java.util.Base64

final case class SecureRandomLive(random: JavaRandom) extends SecureRandom:

  override def nextBytes(length: Int): UIO[Array[Byte]] = {
    ZIO.succeed {
      val arr = new Array[Byte](length)
      random.nextBytes(arr)
      arr
    }
  }
  override def nextBytesAsBase64(length: Int): UIO[String] = {
    nextBytes(length).map(arr =>
      Base64.getUrlEncoder.nn.withoutPadding.nn
        .encodeToString(arr).nn
    )
  }

object SecureRandomLive:

  val layer: ULayer[SecureRandom] =
    ZLayer.succeed(SecureRandomLive(new JavaRandom()))
