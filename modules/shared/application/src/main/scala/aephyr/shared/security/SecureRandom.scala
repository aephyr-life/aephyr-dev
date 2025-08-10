package aephyr.shared.security

import zio.*

trait SecureRandom:
  def nextBytes(length: Int): UIO[Array[Byte]]
