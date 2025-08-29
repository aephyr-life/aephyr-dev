package aephyr.auth.domain

import java.util.{Arrays => JArrays}
import scala.CanEqual

/** Opaque binary user handle used by WebAuthn (stable per user). */
final case class UserHandle private (private val bytes0: Array[Byte]) {

  /** Unsafe view — avoid exposing mutable internal state. Prefer `bytesCopy`. */
  def bytesUnsafe: Array[Byte] = bytes0

  /** Defensive copy for external use. */
  def bytesCopy: Array[Byte] = JArrays.copyOf(bytes0, bytes0.length).nn

  override def equals(o: Any): Boolean = o match
    case that: UserHandle => JArrays.equals(this.bytes0, that.bytes0)
    case _                => false

  override def hashCode(): Int = JArrays.hashCode(bytes0)

  override def toString: String = s"UserHandle(${bytes0.length} bytes)"
}

object UserHandle {
  // Allow == / != under Scala 3 strict equality
  given CanEqual[UserHandle, UserHandle] = CanEqual.derived

  /** Create from raw bytes (defensive copy). */
  def apply(b: Array[Byte]): UserHandle =
    new UserHandle(JArrays.copyOf(b, b.length).nn)

  /** Build from a read-only view (e.g., java.nio buffers) without extra copy. */
  def unsafeFromBytesView(b: Array[Byte]): UserHandle =
    new UserHandle(b) // caller must ensure immutability
}
