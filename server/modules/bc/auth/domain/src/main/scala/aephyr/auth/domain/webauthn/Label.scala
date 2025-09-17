package aephyr.auth.domain.webauthn

opaque type Label = String

object Label:

  def apply(s: String): Either[String, Label] =
    val t = s.trim.nn
    if t.isEmpty then Left("label must not be blank")
    else if t.length > 120 then Left("label too long")
    else Right(t)

  def unsafe(s: String): Label = s.trim.nn

  extension (l: Label) inline def value: String = l
