package aephyr.kernel.userlabels

import aephyr.kernel.id.UserId

import java.security.MessageDigest
import java.util.Locale

object UserLabels {
  private val adjectives = Vector(
    "brisk",
    "calm",
    "clever",
    "cosmic",
    "eager",
    "gentle",
    "lucky",
    "noble",
    "quiet",
    "rapid",
    "soft",
    "swift",
    "vivid",
    "witty",
    "zesty"
  )
  private val animals = Vector(
    "otter",
    "lynx",
    "panda",
    "koala",
    "falcon",
    "fox",
    "whale",
    "tiger",
    "owl",
    "yak",
    "zebra",
    "eagle",
    "wolf",
    "hare",
    "seal"
  )

  private def sha1Hex(s: String): Array[Byte] =
    MessageDigest.getInstance("SHA-1").nn.digest(s.getBytes("UTF-8")).nn

  /** Deterministic base on userId so reloads produce the same base handle. */
  def from(
    userId: UserId
  ): (String /*username base*/, String /*displayName*/ ) = {
    val hash = sha1Hex(userId.value.toString)
    val ai = (hash(0) & 0xff) % adjectives.size
    val ni = (hash(1) & 0xff) % animals.size
    val tail = f"${hash(2) & 0xff}%02x${hash(3) & 0xff}%02x" // 4 hex
    val adj = adjectives(ai)
    val ani = animals(ni)
    val usernameBase =
      s"$adj-$ani-$tail".toLowerCase(Locale.ROOT).nn // e.g., calm-otter-3fa2
    val displayName = s"${adj.capitalize} ${ani.capitalize}"
    (usernameBase, displayName)
  }
}
