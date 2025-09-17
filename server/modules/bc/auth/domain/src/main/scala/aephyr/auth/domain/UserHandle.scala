package aephyr.auth.domain

import aephyr.kernel.types.Bytes

import java.util.Arrays as JArrays
import scala.CanEqual

/** Opaque binary user handle used by WebAuthn (stable per user). */
final case class UserHandle(bytes: Bytes) {

  /** Unsafe view — avoid exposing mutable internal state. Prefer `bytesCopy`. */
  def bytesUnsafe: Array[Byte] = bytes.toArray

  /** Defensive copy for external use. */
  def bytesCopy: Array[Byte] =
    JArrays.copyOf(bytes.toArray, bytes.toArray.length).nn

  override def equals(o: Any): Boolean = o match
    case that: UserHandle => bytes.equals(that.bytes)
    case _                => false

  override def hashCode(): Int = JArrays.hashCode(bytes.toArray)

  override def toString: String = s"UserHandle(${bytes.toArray.length} bytes)"
}

object UserHandle {
  // Allow == / != under Scala 3 strict equality
  given CanEqual[UserHandle, UserHandle] = CanEqual.derived

  /** Create from raw bytes (defensive copy). */
  def apply(b: Array[Byte]): UserHandle =
    new UserHandle(Bytes.fromArray(b))

  /** Build from a read-only view (e.g., java.nio buffers) without extra copy. */
  def unsafeFromBytesView(b: Array[Byte]): UserHandle =
    new UserHandle(Bytes.unsafeView(b)) // caller must ensure immutability
}
