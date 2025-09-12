package aephyr.auth.domain

opaque type AuthTx = String

object AuthTx {

  def apply(value: String): AuthTx = value

  // Generate a new token (URL-safe, 20 bytes ~ 27 chars)
  def generate(bytes: Int = 20): AuthTx = {
    val buf = new Array[Byte](bytes)
    java.security.SecureRandom().nextBytes(buf) // TODO use my own SecureRandom
    java.util.Base64.getUrlEncoder.nn.withoutPadding().nn.encodeToString(buf).nn
  }

  // Validate/parsing for input (path/query)
  private val B64URL = "^[A-Za-z0-9_-]{16,128}$".r

  def parse(s: String): Either[String, AuthTx] =
    if (B64URL.matches(s)) Right(AuthTx(s)) else Left("invalid_auth_tx")

  extension (t: AuthTx)
    def value: String = t

  given CanEqual[String, String] = CanEqual.derived
}
