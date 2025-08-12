//------------------------------------------------------------------------------
//  SPDX-License-Identifier: Aephyr-SAL-1.0
//
//  Licensed under the Aephyr Source Available License
//  See LICENSE file in the project root for license text.
//------------------------------------------------------------------------------

package aephyr.adapters.db.events

import javax.sql.DataSource

import aephyr.adapters.db.{ JdbcMini => J }
import aephyr.kernel.PersistenceError
import aephyr.kernel.events._
import zio._

// Bind F[_] = ZIO[Connection, EventStoreError, *]
final class JdbcEventStore(ds: DataSource)
  extends EventStore[[A] =>> IO[EventStoreError, A]] {

  private inline def p(xs: Any*): Seq[Option[Any]] = xs.map(Some(_)).toSeq

  private def toStorage(pe: PersistenceError): EventStoreError =
    EventStoreError.StorageFailure(pe.toString, Some(pe))

  private def lift[R, A](
    zio: ZIO[R, PersistenceError, A]
  ): ZIO[R, EventStoreError, A] = zio.mapError(toStorage)

  private def toRow(rs: java.sql.ResultSet): StoredEvent =
    StoredEvent(
      streamId = StreamId(rs.getString("stream_id")),
      version = rs.getLong("version"),
      eventType = EventType(rs.getString("event_type")),
      data = Data(rs.getString("payload")),
      metadata = Metadata(rs.getString("metadata")),
      recordedAt = rs.getTimestamp("recorded_at").toInstant
    )

  private def getHeadVersionSimple(
    streamId: StreamId
  ): IO[PersistenceError, Long] =
    J.withConnection(ds) {
      J.queryOne(
        """select coalesce(max(version), 0) as v
          |from events.events
          |where stream_id = ?
          |""".stripMargin,
        p(streamId.value)
      )(
        rs => rs.getLong("v")
      ).map(_.getOrElse(0L))
    }

  override def append(
    streamId: StreamId,
    expectedVersion: Option[Long],
    events: List[EventData]
  ): IO[EventStoreError, Unit] =
    J.withConnection(ds) {
      J.tx {
        for {
          head <- getHeadVersionSimple(streamId)
          _ <- expectedVersion match {
            case Some(ev) if ev != head =>
              ZIO.fail(PersistenceError.OptimisticLock(ev, head))
            case _ => ZIO.unit
          }
          _ <- ZIO.foreachDiscard(events.zipWithIndex) {
            case (e, i) =>
              val v = head + i + 1
              val sql =
                """insert into events.events
                  |  (stream_id, version, event_type, payload, metadata)
                  |values (?, ?, ?, ?::jsonb, ?::jsonb)
                  |""".stripMargin
              J.execute(
                sql,
                p(
                  streamId.value,
                  v: java.lang.Long,
                  e.eventType.value,
                  e.data.value,
                  e.metadata.value
                )
              ).unit
          }
        } yield ()
      }
    }.mapError {
      case PersistenceError.OptimisticLock(ev, head) =>
        EventStoreError.VersionConflict(ev, head)
      case pe => toStorage(pe)
    }

  override def load(
    streamId: StreamId
  ): IO[EventStoreError, List[StoredEvent]] =
    lift {
      J.withConnection(ds) {
        J.queryList(
          """select stream_id, version, event_type, payload, metadata, recorded_at
            |from events.events
            |where stream_id = ?
            |order by version asc
            |""".stripMargin,
          p(streamId.value)
        )(toRow)
      }
    }

  override def loadSince(
    streamId: StreamId,
    fromExclusive: Long,
    limit: Int
  ): IO[EventStoreError, List[StoredEvent]] =
    lift {
      J.withConnection(ds) {
        J.queryList(
          """select stream_id, version, event_type, payload, metadata, recorded_at
            |from events.events
            |where stream_id = ? and version > ?
            |order by version asc
            |limit ?
            |""".stripMargin,
          p(
            streamId.value,
            fromExclusive: java.lang.Long,
            limit: java.lang.Integer
          )
        )(toRow)
      }
    }

  override def headVersion(
    streamId: StreamId
  ): IO[EventStoreError, Long] =
    lift {
      getHeadVersionSimple(streamId)
    }
}

object JdbcEventStore {
  type ES[A] = IO[EventStoreError, A]
  val layer: ZLayer[DataSource, Nothing, EventStore[ES]] =
    ZLayer.fromFunction(
      ds => new JdbcEventStore(ds)
    )
}
