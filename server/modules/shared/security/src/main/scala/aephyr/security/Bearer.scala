package aephyr.security

opaque type Bearer = String

object Bearer {
  def apply(value: String): Bearer = value
  extension (b: Bearer) def value: String = b
}
