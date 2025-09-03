package aephyr.kernel.id

import java.util.UUID

opaque type UserId = UUID

object UserId {
  
  def apply(uuid: UUID): UserId = uuid

  def random: UserId = UUID.randomUUID().nn

  def fromString(s: String): Either[String, UserId] =
    try Right(UUID.fromString(s).nn)
    catch case e: IllegalArgumentException => 
      Left(e.getMessage.nn)

  extension (id: UserId)
    def value: UUID = id
    def asString: String = id.toString

  given CanEqual[UUID, UUID] = CanEqual.derived
}
