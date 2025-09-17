package aephyr.api.shared

opaque type UserAgent = String

object UserAgent {
  def apply(value: String): UserAgent = value
  extension (id: UserAgent) def value: String = id
}
