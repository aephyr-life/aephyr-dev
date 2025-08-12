package aephyr.adapters.db

// scalafix:off
import scala.language.unsafeNulls

import java.sql.{
  Connection,
  PreparedStatement,
  ResultSet,
  SQLException,
  Timestamp
}
import java.time.Instant
import java.util.UUID
import javax.sql.DataSource

import zio.*

import aephyr.kernel.PersistenceError
import aephyr.kernel.PersistenceError.*
// scalafix:on

object JdbcMini {

  extension (i: Instant)
    def asTimestamp: Timestamp = new Timestamp(i.toEpochMilli)

  private def toPersistenceError(t: Throwable): PersistenceError =
    t match
      case e: SQLException => DbError.from(e)
      case other           => PersistenceError.Unknown(other.getMessage, other)

  def fromSqlException(ex: SQLException): PersistenceError =
    ex.getSQLState match {
      case "23505" =>
        UniqueViolation(ex.getMessage) // TODO the cause get swallowed
      case "23503" => ForeignKeyViolation(ex.getMessage)
      case "40001" => SerializationFailure(ex.getMessage)
      case "40P01" => DeadlockDetected(ex.getMessage)
      case "23514" => CheckViolation(ex.getMessage)
      case "57014" => Timeout(ex.getMessage) // statement_timeout
      case _       => Unknown(ex.getMessage, ex)
    }

  /** Acquire a Connection, run the effect, always close it. No Scope/Throwable
    * leaks.
    */
  def withConnection[R](ds: DataSource)(
    effect: ZIO[Connection, PersistenceError, R]
  ): IO[PersistenceError, R] =
    ZIO.scoped {
      // acquire: guarantee a non-null Connection
      val acquire: IO[PersistenceError, Connection] =
        ZIO
          .attemptBlocking(ds.getConnection)
          .mapError(toPersistenceError)
          .flatMap {
            c =>
              ZIO
                .fromOption(Option(c))
                .orElseFail(
                  PersistenceError
                    .Unknown("DataSource#getConnection returned null")
                )
          }

      ZIO
        .acquireRelease(acquire)(
          c => ZIO.attemptBlocking(c.close()).orDie
        )
        // IMPORTANT: annotate the param so it stays `Connection` (not `Connection | Null`)
        .flatMap {
          (conn: Connection) =>
            effect.provideEnvironment(ZEnvironment[Connection](conn))
        }
    }

  /** Env‑based transaction helper (uses the Connection from environment). */
  def tx[R](
    effect: ZIO[Connection, PersistenceError, R]
  ): ZIO[Connection, PersistenceError, R] =
    ZIO.service[Connection].flatMap {
      conn =>
        ZIO
          .attemptBlocking(conn.setAutoCommit(false))
          .mapError(toPersistenceError) *>
          effect
            .tapBoth(
              _ => ZIO.attemptBlocking(conn.rollback()).ignore,
              _ => ZIO.attemptBlocking(conn.commit()).ignore
            )
            .ensuring(ZIO.attemptBlocking(conn.setAutoCommit(true)).ignore)
    }

  // ---------- Errors (map common SQLState codes) ----------
  object DbError {

    def from(ex: SQLException): PersistenceError =
      ex.getSQLState match {
        case "23505" =>
          UniqueViolation(ex.getMessage) // TODO the cause get swallowed
        case "23503" => ForeignKeyViolation(ex.getMessage)
        case "40001" => SerializationFailure(ex.getMessage)
        case "40P01" => DeadlockDetected(ex.getMessage)
        case "23514" => CheckViolation(ex.getMessage)
        case "57014" => Timeout(ex.getMessage) // statement_timeout
        case _       => Unknown(ex.getMessage, ex)
      }
  }

  // ---------- Small utilities ----------
  def qs(n: Int): String = List.fill(n)("?").mkString(", ")

  private def bindOne(
    ps: PreparedStatement,
    idx: Int,
    v: Option[Any]
  ): Unit =
    v.fold {
      ps.setObject(idx, null)
    } {
      case s: String      => ps.setString(idx, s)
      case b: Boolean     => ps.setBoolean(idx, b)
      case i: Int         => ps.setInt(idx, i)
      case l: Long        => ps.setLong(idx, l)
      case u: UUID        => ps.setObject(idx, u)
      case t: Instant     => ps.setTimestamp(idx, Timestamp.from(t))
      case a: Array[Byte] => ps.setBytes(idx, a)
      case other          => ps.setObject(idx, other)
    }

  private def prepare(
    conn: Connection,
    sql: String,
    params: Seq[Option[Any]],
    queryTimeoutSeconds: Int
  ): PreparedStatement = {
    val ps = conn.prepareStatement(sql)
    if (queryTimeoutSeconds > 0) ps.setQueryTimeout(queryTimeoutSeconds)
    var i = 1;
    params.foreach {
      p =>
        bindOne(ps, i, p); i += 1
    }
    ps
  }

  import zio.*
  import java.sql.{ Connection, SQLException }

  // ---------- Runners (on blocking pool, with error mapping) ----------
  def queryOne[A](
    sql: String,
    params: Seq[Option[Any]] = Nil,
    timeoutSeconds: Int = 0
  )(
    read: ResultSet => A
  ): ZIO[Connection, PersistenceError, Option[A]] =
    ZIO.service[Connection].flatMap {
      conn =>
        ZIO
          .attemptBlocking {
            val ps = prepare(conn, sql, params, timeoutSeconds)
            try {
              val rs = ps.executeQuery()
              if (rs.next()) Some(read(rs)) else None
            } finally ps.close()
          }
          .mapError {
            case e: SQLException => DbError.from(e)
            case t               => PersistenceError.Unknown(t.getMessage, t)
          }
    }

  def queryList[A](
    sql: String,
    params: Seq[Option[Any]] = Nil,
    timeoutSeconds: Int = 0
  )(read: ResultSet => A): ZIO[Connection, PersistenceError, List[A]] =
    ZIO.service[Connection].flatMap {
      conn =>
        ZIO
          .attemptBlocking {
            val ps = prepare(conn, sql, params, timeoutSeconds)
            try {
              val rs = ps.executeQuery()
              val b  = List.newBuilder[A]
              while (rs.next()) b += read(rs)
              b.result()
            } finally ps.close()
          }
          .mapError {
            case e: SQLException => DbError.from(e)
            case t               => PersistenceError.Unknown(t.getMessage, t)
          }
    }

  def execute(
    sql: String,
    params: Seq[Option[Any]] = Nil,
    timeoutSeconds: Int = 0
  ): ZIO[Connection, PersistenceError, Int] =
    ZIO.service[Connection].flatMap {
      conn =>
        ZIO
          .attemptBlocking {
            val ps = prepare(conn, sql, params, timeoutSeconds)
            try ps.executeUpdate()
            finally ps.close()
          }
          .mapError {
            case e: SQLException => DbError.from(e)
            case t               => PersistenceError.Unknown(t.getMessage, t)
          }
    }
}
