package aephyr.kernel.types

import scala.collection.immutable.ArraySeq

opaque type Bytes = ArraySeq[Byte]

object Bytes {
  def fromArray(a: Array[Byte]): Bytes =
    ArraySeq.unsafeWrapArray(a.clone())

  def unsafeView(a: Array[Byte]): Bytes =
    ArraySeq.unsafeWrapArray(a)

  extension (b: Bytes)
    def isEmpty: Boolean = b.isEmpty
    def toArray: Array[Byte] = b.toArray
    def size: Int = b.length
    def apply(i: Int): Byte = b(i)
}
