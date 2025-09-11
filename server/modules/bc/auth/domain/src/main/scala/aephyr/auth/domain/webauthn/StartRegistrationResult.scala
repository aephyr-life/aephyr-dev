package aephyr.auth.domain.webauthn

final case class StartRegistrationResult(
  clientJson: String,
  serverJson: String
) derives CanEqual
