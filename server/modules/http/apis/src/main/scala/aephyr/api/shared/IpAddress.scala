package aephyr.api.shared

opaque type IpAddress = String

object IpAddress {
  def apply(value: String): IpAddress = value
  extension (ip: IpAddress) def value: String = ip
}
