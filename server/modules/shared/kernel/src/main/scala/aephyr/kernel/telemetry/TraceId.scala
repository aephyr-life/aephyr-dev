package aephyr.kernel.telemetry

opaque type TraceId = String

object TraceId {
  def apply(value: String): TraceId = value
  extension (id: TraceId) def value: String = id
}
