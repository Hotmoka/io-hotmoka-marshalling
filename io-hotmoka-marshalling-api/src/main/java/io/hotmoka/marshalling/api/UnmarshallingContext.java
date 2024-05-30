/*
Copyright 2023 Fausto Spoto

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package io.hotmoka.marshalling.api;

import java.io.IOException;
import java.math.BigInteger;
import java.util.function.Function;

/**
 * A context used during bytes unmarshalling into objects.
 */
public interface UnmarshallingContext extends AutoCloseable {

	/**
	 * Yields the number of bytes that can be read without blocking.
	 * 
	 * @return the number of bytes
	 * @throws IOException if an I/O error occurs
	 */
	int available() throws IOException;

	/**
	 * Extracts an object from this context, which must have an object unmarshaller
	 * registered for the class of the object.
	 * 
	 * @param <C> the type of the object
	 * @param clazz the class of the object
	 * @return the unmarshalled object
	 * @throws IOException if an I/O error occurs
	 */
	<C> C readObject(Class<C> clazz) throws IOException;

	/**
	 * Extracts length and an array of marshallables from this context, assuming
	 * that it has been saved with {@link MarshallingContext#writeLengthAndArray(Marshallable[])}.
	 * 
	 * @param <T> the type of the marshallables
	 * @param unmarshaller the object that unmarshals a single marshallable
	 * @param supplier the creator of the resulting array of marshallables
	 * @return the array
	 * @throws IOException if an I/O error occurs
	 */
	<T extends Marshallable> T[] readLengthAndArray(Unmarshaller<T> unmarshaller, Function<Integer, T[]> supplier) throws IOException;

	/**
	 * Extracts the next {@code length} bytes from this context.
	 * 
	 * @param length the number of bytes to extract
	 * @param mismatchErrorMessage the message of the exception thrown if it was not possible
	 *                     to read {@code length} bytes
	 * @return the bytes
	 * @throws IOException if an I/O error occurs
	 */
	byte[] readBytes(int length, String mismatchErrorMessage) throws IOException;

	/**
	 * Extracts an array of bytes from this context, assuming that it has been saved
	 * with {@link MarshallingContext#writeLengthAndBytes(byte[])}.
	 * 
	 * @param mismatchErrorMessage the error message in case of mismatched array length
	 * @return the array
	 * @throws IOException if an I/O error occurs
	 */
	byte[] readLengthAndBytes(String mismatchErrorMessage) throws IOException;

	/**
	 * Reads the requested number of bytes into the given byte array.
	 *
	 * @param  b the byte array into which the data is read
	 * @param  off the start offset in {@code b} at which the data is written
	 * @param  len the maximum number of bytes to read
	 * @return the actual number of bytes read into the buffer
	 * @throws IOException if an I/O error occurs
	 * @throws IndexOutOfBoundsException If {@code off} is negative, {@code len}
	 *         is negative, or {@code len} is greater than {@code b.length - off}
	 */
	int readNBytes(byte[] b, int off, int len) throws IOException;

	/**
	 * Reads all remaining bytes.
	 *
	 * @return a byte array containing all remaining bytes
	 * @throws IOException if an I/O error occurs
	 */
	byte[] readAllBytes() throws IOException;

	/**
	 * Extracts the next byte from this context.
	 * 
	 * @return the next byte
	 * @throws IOException if an I/O error occurs
	 */
	byte readByte() throws IOException;

	/**
	 * Extracts the next character from this context.
	 * 
	 * @return the next character
	 * @throws IOException if an I/O error occurs
	 */
	char readChar() throws IOException;

	/**
	 * Extracts the next boolean from this context.
	 * 
	 * @return the next boolean
	 * @throws IOException if an I/O error occurs
	 */
	boolean readBoolean() throws IOException;

	/**
	 * Extracts the next integer from this context. This requires that the
	 * integer was previously marshalled through {@link MarshallingContext#writeInt(int)}.
	 * 
	 * @return the next integer
	 * @throws IOException if an I/O error occurs
	 */
	int readInt() throws IOException;

	/**
	 * Extracts the next integer from this context, in an optimized way,
	 * that tries to use smaller representations for frequent cases.
	 * This requires that the integer was previously marshalled through
	 * {@link MarshallingContext#writeCompactInt(int)}.
	 * 
	 * @return the next integer
	 * @throws IOException if an I/O error occurs
	 */
	int readCompactInt() throws IOException;

	/**
	 * Extracts the next short from this context.
	 * 
	 * @return the next short
	 * @throws IOException if an I/O error occurs
	 */
	short readShort() throws IOException;

	/**
	 * Extracts the next long from this context.
	 * 
	 * @return the next long
	 * @throws IOException if an I/O error occurs
	 */
	long readLong() throws IOException;

	/**
	 * Extracts the next long from this context, in an optimized way,
	 * that tries to use smaller representations for frequent cases.
	 * This requires that the long was previously marshalled through
	 * {@link MarshallingContext#writeCompactLong(long)}.
	 * 
	 * @return the next long
	 * @throws IOException if an I/O error occurs
	 */
	long readCompactLong() throws IOException;

	/**
	 * Extracts the next float from this context.
	 * 
	 * @return the next float
	 * @throws IOException if an I/O error occurs
	 */
	float readFloat() throws IOException;

	/**
	 * Extracts the next double from this context.
	 * 
	 * @return the next double
	 * @throws IOException if an I/O error occurs
	 */
	double readDouble() throws IOException;

	/**
	 * Extracts the next string from this context. This requires that
	 * the string was previously marshalled through {@link MarshallingContext#writeStringUnshared(String)}.
	 * In comparison to {@link #readStringShared()}, this representation might be smaller
	 * if the string is unlikely to be repeated in the same marshalled object.
	 * 
	 * @return the next string
	 * @throws IOException if an I/O error occurs
	 */
	String readStringUnshared() throws IOException;

	/**
	 * Extracts the next string from this context. This requires that
	 * the string was previously marshalled through {@link MarshallingContext#writeStringShared(String)}.
	 * In comparison to {@link #readStringUnshared()}, this representation might
	 * be smaller if the same string is repeated in the same marshalled object.
	 * 
	 * @return the next string
	 * @throws IOException if an I/O error occurs
	 */
	String readStringShared() throws IOException;

	/**
	 * Extracts the next big integer from this context,  in an optimized way,
	 * that tries to use smaller representations for frequent cases.
	 * 
	 * @return the next big integer
	 * @throws IOException if an I/O error occurs
	 */
	BigInteger readBigInteger() throws IOException;

	@Override
	void close() throws IOException;
}