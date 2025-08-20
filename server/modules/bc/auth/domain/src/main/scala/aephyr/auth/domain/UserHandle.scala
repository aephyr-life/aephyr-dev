package aephyr.auth.domain

import java.util.{Arrays => JArrays}

/** 
 * Opaque binary user handle used by WebAuthn (stable per user). 
 * */
final case class UserHandle private (bytes: Array[Byte]) {
  
  override def equals(o: Any): Boolean =
    o.isInstanceOf[UserHandle] &&
      JArrays.equals(bytes, o.asInstanceOf[UserHandle].bytes)
  
  override def hashCode(): Int =
    JArrays.hashCode(bytes)
}

object UserHandle {
  
  def fromBytes(b: Array[Byte]): UserHandle =
    UserHandle(JArrays.copyOf(b, b.length).nn)
}
