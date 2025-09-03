package aephyr.identity.domain

import aephyr.kernel.id.UserId

import java.time.Instant

final case class User(
  id: UserId,
  userName: String,
  displayName: String,
  status: User.Status,
  createdAt: Instant,
  updatedAt: Instant
)

object User:
  
  opaque type EmailAddress = String

  object EmailAddress:
    def apply(email: String): EmailAddress = email

  extension (x: EmailAddress)
    def normalized: EmailAddress             = x.trim.nn.toLowerCase.nn
  given CanEqual[EmailAddress, EmailAddress] = CanEqual.derived

  enum Status derives CanEqual:
    case Pending, Active, Disabled
