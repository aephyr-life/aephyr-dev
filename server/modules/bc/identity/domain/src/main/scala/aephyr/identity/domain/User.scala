package aephyr.identity.domain

import java.time.Instant
import java.util.UUID

final case class User(
  id: User.Id,
  emailNorm: User.EmailAddress,
  status: User.Status,
  createdAt: Instant,
  updatedAt: Instant
)

object User:

  opaque type Id = UUID // TODO use UserId from kernel
  object Id:
    def apply(): Id           = UUID.randomUUID().nn
    def apply(uuid: UUID): Id = uuid

  extension (x: Id) def value: UUID = x
  given CanEqual[UUID, UUID]        = CanEqual.derived

  opaque type EmailAddress = String

  object EmailAddress:
    def apply(email: String): EmailAddress = email

  extension (x: EmailAddress)
    def normalized: EmailAddress             = x.trim.nn.toLowerCase.nn
  given CanEqual[EmailAddress, EmailAddress] = CanEqual.derived

  enum Status derives CanEqual:
    case Pending, Active, Disabled
