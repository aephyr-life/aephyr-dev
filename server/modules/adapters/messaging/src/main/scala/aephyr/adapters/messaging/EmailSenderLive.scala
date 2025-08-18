package aephyr.adapters.messaging

import aephyr.identity.application.ports.EmailSender
import aephyr.identity.domain.User
import zio.{ UIO, ZIO, ZLayer }

final case class EmailSenderLive( /* smtp cfg */ ) extends EmailSender:

  def send(
    to: User.EmailAddress,
    subject: String,
    html: String,
    text: String
  ): UIO[Unit] = ZIO.logInfo(s"Simulated email to $to\n$subject\n\n$text")

object EmailSenderLive:

  val layer: ZLayer[Any, Nothing, EmailSender] =
    ZLayer.succeed(EmailSenderLive())
