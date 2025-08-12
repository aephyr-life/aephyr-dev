//------------------------------------------------------------------------------
//  SPDX-License-Identifier: Aephyr-SAL-1.0
//
//  Licensed under the Aephyr Source Available License
//  See LICENSE file in the project root for license text.
//------------------------------------------------------------------------------

package aephyr.kernel.events

import java.time.Instant

import scala.CanEqual

// -------- IDs & kinds --------
opaque type StreamId  = String
opaque type EventType = String

object StreamId:
  def apply(s: String): StreamId                   = s
  extension (x: StreamId) inline def value: String = x
  given CanEqual[StreamId, StreamId]               = CanEqual.derived

object EventType:
  def apply(s: String): EventType                   = s
  extension (x: EventType) inline def value: String = x
  given CanEqual[EventType, EventType]              = CanEqual.derived

// -------- Payloads as String wrappers --------
opaque type Data     = String // e.g. JSON string
opaque type Metadata = String // e.g. JSON string (headers, causation, …)

object Data:
  def apply(s: String): Data                   = s
  extension (d: Data) inline def value: String = d

object Metadata:
  def apply(s: String): Metadata                   = s
  extension (m: Metadata) inline def value: String = m

// -------- Event records --------
final case class EventData(
  eventType: EventType,
  data: Data,
  metadata: Metadata
)

final case class StoredEvent(
  streamId: StreamId,
  version: Long, // 1-based sequence within stream
  eventType: EventType,
  data: Data,
  metadata: Metadata,
  recordedAt: Instant
)

// -------- Errors --------
sealed trait EventStoreError extends Throwable
object EventStoreError:
  final case class VersionConflict(expected: Long, actual: Long)
    extends Exception(s"version conflict: expected=$expected actual=$actual")
    with EventStoreError
  final case class StreamNotFound(id: StreamId)
    extends Exception(s"stream not found: ${id.value}")
    with EventStoreError
  final case class StorageFailure(msg: String, cause: Option[Throwable] = None)
    extends Exception(msg)
    with EventStoreError:
    cause.foreach(initCause)

trait EventStore[F[_]]:
  def append(
    streamId: StreamId,
    expectedVersion: Option[Long],
    events: List[EventData]
  ): F[Unit]

  def load(streamId: StreamId): F[List[StoredEvent]]

  def loadSince(
    streamId: StreamId,
    fromExclusive: Long,
    limit: Int
  ): F[List[StoredEvent]]

  def headVersion(streamId: StreamId): F[Long]
