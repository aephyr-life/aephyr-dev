package aephyr.auth.domain.webauthn

sealed trait WebAuthnError extends Product with Serializable

object WebAuthnError {
  case object InvalidTx                 extends WebAuthnError
  case object NotRegistered             extends WebAuthnError
  case object CredentialAlreadyExists   extends WebAuthnError
  final case class BadRequest(msg: String) extends WebAuthnError
  final case class Server(msg: Option[String])     extends WebAuthnError
  object Server {
    def apply(msg: String): Server = Server(Some(msg))
  }
}
