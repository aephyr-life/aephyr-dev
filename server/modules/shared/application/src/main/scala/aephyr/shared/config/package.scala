//------------------------------------------------------------------------------
//  SPDX-License-Identifier: Aephyr-SAL-1.0
//
//  Licensed under the Aephyr Source Available License
//  See LICENSE file in the project root for license text.
//------------------------------------------------------------------------------

package aephyr.shared

import java.net.URI
import scala.util.Try

package object config {
  opaque type BaseUrl = String

  object BaseUrl {
    /** Ensure absolute  URL, drop trailing slash */
    def parse(raw: String): Either[String, BaseUrl] =
      val s = raw.trim.nn
      Try(new URI(s))
        .toEither
        .left
        .map(_.getMessage.nn)
        .flatMap { uri =>
          if (!uri.isAbsolute) Left("BaseUrl must be absolute")
          else if (s.endsWith("/")) Left("BaseUrl can not have a trailing slash")
          else Right(s)
        }

    def unsafe(raw: String): BaseUrl =
      parse(raw).fold(err => throw new IllegalArgumentException(err), identity)

    extension (b: BaseUrl)
      def value: String = b
      def /(segment: String): String =
        val seg = segment.stripPrefix("/")
        s"${b.value}/$seg"
  }
}


