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

  opaque type Id = UUID
  object Id:
    def apply(): Id = UUID.randomUUID()
    def apply(uuid: UUID): Id = uuid

  extension (x: Id)
    def value: UUID = x

  opaque type EmailAddress = String

  object EmailAddress:
    def apply(email: String): EmailAddress = email

  extension (x: EmailAddress)
    def normalized: EmailAddress = x.trim.toLowerCase

  enum Status:
    case Pending, Active, Disabled

