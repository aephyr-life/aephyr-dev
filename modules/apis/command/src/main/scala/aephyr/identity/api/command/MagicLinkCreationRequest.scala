package aephyr.identity.api.command

import aephyr.identity.domain.User
import aephyr.identity.domain.User.EmailAddress
import com.github.plokhotnyuk.jsoniter_scala.core._
import com.github.plokhotnyuk.jsoniter_scala.macros._

final case class MagicLinkCreationRequest(email: User.EmailAddress)

object MagicLinkCreationRequest:

  given JsonValueCodec[EmailAddress] with
    override def encodeValue(x: EmailAddress, out: JsonWriter): Unit =
      out.writeVal(x.toString)

    override def decodeValue(
      in: JsonReader,
      default: EmailAddress
    ): EmailAddress = EmailAddress(in.readString(default.toString))

    override def nullValue: EmailAddress = EmailAddress("")

  implicit val codec: JsonValueCodec[MagicLinkCreationRequest] =
    JsonCodecMaker.make[MagicLinkCreationRequest](CodecMakerConfig)
