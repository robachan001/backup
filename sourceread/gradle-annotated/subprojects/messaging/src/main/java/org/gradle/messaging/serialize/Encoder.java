/*
 * Copyright 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradle.messaging.serialize;

import org.gradle.api.Nullable;

import java.io.IOException;
import java.io.OutputStream;

public interface Encoder {
    /**
     * Returns an OutputStream that can be used to write byte content to the stream.
     */
    OutputStream getOutputStream();

    /**
     * Writes a byte value to the stream.
     */
    void writeByte(byte value) throws IOException;

    /**
     * Writes the given bytes to the stream. Does not encode any length information.
     */
    void writeBytes(byte[] bytes) throws IOException;

    /**
     * Writes the given bytes to the stream. Does not encode any length information.
     */
    void writeBytes(byte[] bytes, int offset, int count) throws IOException;

    /**
     * Writes the given byte array to the stream. Encodes the bytes and some length information.
     */
    void writeBinary(byte[] bytes) throws IOException;

    /**
     * Writes the given byte array to the stream. Encodes the bytes and some length information.
     */
    void writeBinary(byte[] bytes, int offset, int count) throws IOException;

    /**
     * Writes a signed 64 bit long value. The implementation may encode the value as a variable number of bytes, not necessarily as 8 bytes.
     */
    void writeLong(long value) throws IOException;

    /**
     * Writes a signed 32 bit int value. The implementation may encode the value as a variable number of bytes, not necessarily as 4 bytes.
     */
    void writeInt(int value) throws IOException;

    /**
     * Writes a signed 32 bit int value whose value is likely to be small and positive but may not be. The implementation may encode the value as a variable number of bytes, not necessarily as 4
     * bytes.
     */
    void writeSizeInt(int value) throws IOException;

    /**
     * Writes a boolean value.
     */
    void writeBoolean(boolean value) throws IOException;

    /**
     * Writes a non-null string value.
     */
    void writeString(CharSequence value) throws IOException;

    /**
     * Writes a nullable string value.
     */
    void writeNullableString(@Nullable CharSequence value) throws IOException;
}
