//------------------------------------------------------------------------------
//  SPDX-License-Identifier: Aephyr-SAL-1.0
//
//  Licensed under the Aephyr Source Available License
//  See LICENSE file in the project root for license text.
//------------------------------------------------------------------------------

package aephyr.es

import java.nio.charset.StandardCharsets.UTF_8

object json:
  opaque type JsonPayload  = String
  opaque type JsonMetadata = String

  object JsonPayload:
    def unsafe(s: String): JsonPayload = s                   // assume valid JSON
    def fromUtf8Bytes(b: Array[Byte]): JsonPayload =
      new String(b, UTF_8)                                   // assume valid UTF-8 JSON
    extension (p: JsonPayload)
      def value: String = p                                  // for JDBC setString
      def bytes: Array[Byte] = p.getBytes(UTF_8)

  object JsonMetadata:
    def unsafe(s: String): JsonMetadata = s
    def fromUtf8Bytes(b: Array[Byte]): JsonMetadata =
      new String(b, UTF_8)
    extension (m: JsonMetadata)
      def value: String = m
      def bytes: Array[Byte] = m.getBytes(UTF_8)
