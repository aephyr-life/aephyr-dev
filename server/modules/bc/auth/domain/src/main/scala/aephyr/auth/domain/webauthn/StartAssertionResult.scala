package aephyr.auth.domain.webauthn

final case class StartAssertionResult(
                            clientJson: String,
                            serverJson: String
                          ) derives CanEqual
