package aephyr.identity.application.ports

import aephyr.identity.domain.User
import zio.UIO

trait EmailSender:
  def send(
    to: User.EmailAddress,
    subject: String,
    html: String,
    text: String = ""
  ): UIO[Unit]

//object EmailSender:
//  def send(to: String, subject: String, html: String, text: String = ""): ZIO[EmailSender, Nothing, Unit] =
//    ZIO.serviceWithZIO[EmailSender](_.send(to, subject, html, text))
