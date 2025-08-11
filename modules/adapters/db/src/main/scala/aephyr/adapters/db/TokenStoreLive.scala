package aephyr.adapters.db

import aephyr.identity.application.ports.TokenStore
import aephyr.identity.domain.User
import aephyr.identity.domain.auth.TokenStoreError.InvalidOrExpired
import aephyr.identity.domain.auth.{TokenRecord, TokenStoreError}
import zio.{IO, UIO, ZIO, ZLayer}

import java.util.UUID
import java.time.Instant
import javax.sql.DataSource

final case class TokenStoreLive(ds: DataSource) extends TokenStore:

  def put(
           hash: String,
           userId: User.Id,
           purpose: String,
           expiresAt: Instant,
           singleUse: Boolean
         ): IO[TokenStoreError, Unit] =

    import JdbcMini.*

    val sql =
      """insert into tech.magic_links
        | (hash, user_id, purpose, expires_at, created_at, used_at)
        | values (?, ?, ?, ?, now(), null)
        | on conflict (hash) do update
        |   set user_id = excluded.user_id,
        |       purpose  = excluded.purpose,
        |       expires_at = excluded.expires_at,
        |       used_at = null
        |""".stripMargin

    JdbcMini.withConnection(ds) {
      JdbcMini.execute(sql,
        Seq(hash, userId, purpose, expiresAt.asTimestamp)
      ).unit
    }.mapError(TokenStoreError.DbError.apply)

  override def consumeSingleUse(
                                 hash: String,
                                 now: Instant
                               ): IO[TokenStoreError, TokenRecord] = {

    import JdbcMini.*

    // Atomic claim: set used_at only if currently unused and not expired
    val sql =
      """update tech.magic_links ml
        |   set used_at = now()
        | where ml.hash = ?
        |   and ml.used_at is null
        |   and ml.expires_at > ?
        | returning ml.hash, ml.user_id, ml.purpose, ml.expires_at
        |""".stripMargin

    JdbcMini.withConnection(ds) {
      JdbcMini.queryOne(sql, Seq(hash, now.asTimestamp)) { rs =>
        TokenRecord(
          rs.getString("hash"),
          User.Id(rs.getObject("user_id", classOf[UUID])),
          rs.getString("purpose"),
          rs.getTimestamp("expires_at").toInstant
        )
      }
    }.flatMap {
      case Some(rec) => ZIO.succeed(rec)
      case None => ZIO.fail(TokenStoreError.InvalidOrExpired)
    }.mapError {
      case TokenStoreError.InvalidOrExpired => TokenStoreError.InvalidOrExpired
      case e => TokenStoreError.DbError(e)
    }
  }

object TokenStoreLive:

  val layer: ZLayer[DataSource, Nothing, TokenStore] =
    ZLayer.fromFunction(TokenStoreLive.apply)
