package aephyr.es

import aephyr.es.json.{JsonMetadata, JsonPayload}

import java.time.Instant
import java.util.UUID

type AggregateType = String

final case class EventRecord(
                              aggregateType: AggregateType,
                              aggregateId:   UUID,
                              version:       Long, // 1..N
                              eventType:     String,
                              payloadJson:   JsonPayload,
                              metadataJson:  JsonMetadata,
                              occurredAt:    Instant
                            )

final case class ExpectedVersion(value: Long) extends AnyVal
final case class VersionConflict(current: Long)
  extends Exception(s"expected different version; current=$current")

trait EventStore {
  def load(aggType: AggregateType, id: UUID, fromVersion: Long = 1L)
  : UIO[Chunk[EventRecord]]

  def currentVersion(aggType: AggregateType, id: UUID)
  : UIO[Long] // 0 if no stream

  def append(
              aggType: AggregateType,
              id: UUID,
              expected: ExpectedVersion,
              events: Chunk[(eventType: String, payloadJson: String, metadataJson: String)]
  ): IO[VersionConflict, Unit]
}