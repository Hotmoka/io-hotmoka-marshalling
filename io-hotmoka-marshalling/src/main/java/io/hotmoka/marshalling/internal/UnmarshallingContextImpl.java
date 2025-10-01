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

package io.hotmoka.marshalling.internal;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import io.hotmoka.marshalling.api.Marshallable;
import io.hotmoka.marshalling.api.ObjectUnmarshaller;
import io.hotmoka.marshalling.api.Unmarshaller;
import io.hotmoka.marshalling.api.UnmarshallingContext;

/**
 * Implementation of a context used during bytes unmarshalling into objects.
 */
public class UnmarshallingContextImpl implements UnmarshallingContext {
	private final DataInputStream dis;

	/**
	 * A memory to avoid duplicated strings in the marshalled bytes.
	 */
	private final Map<Integer, String> memoryString = new HashMap<>();

	/**
	 * Object marshallers for specific classes, if any.
	 */
	private final Map<Class<?>, ObjectUnmarshaller<?>> objectUnmarshallers = new HashMap<>();

	/**
	 * Creates an unmarshalling context.
	 * 
	 * @param is the input stream of the context
	 */
	public UnmarshallingContextImpl(InputStream is) {
		this.dis = new DataInputStream(new BufferedInputStream(is));
	}

	/**
	 * Registers an object unmarshaller. It will be used to unmarshall its class.
	 * 
	 * @param om the object unmarhaller
	 */
	protected void registerObjectUnmarshaller(ObjectUnmarshaller<?> ou) {
		objectUnmarshallers.put(ou.clazz(), ou);
	}

	@Override
	public int available() throws IOException {
		return dis.available();
	}

	@Override
	public <C> C readObject(Class<C> clazz) throws IOException {
		@SuppressWarnings("unchecked")
		var ou = (ObjectUnmarshaller<C>) objectUnmarshallers.get(clazz);
		Objects.requireNonNull(ou, "Missing object unmarshaller for class " + clazz.getName());

		return ou.read(this);
	}

	@Override
	public <T extends Marshallable> T[] readLengthAndArray(Unmarshaller<T> unmarshaller, Function<Integer,T[]> supplier) throws IOException {
		int length = readCompactInt();
		T[] result = supplier.apply(length);
		for (int pos = 0; pos < length; pos++)
			result[pos] = unmarshaller.from(this);

		return result;
	}

	@Override
	public byte[] readLengthAndBytes(String mismatchErrorMessage) throws IOException {
		return readBytes(readCompactInt(), mismatchErrorMessage);
	}

	@Override
	public byte readByte() throws IOException {
		return dis.readByte();
	}

	@Override
	public char readChar() throws IOException {
		return dis.readChar();
	}

	@Override
	public boolean readBoolean() throws IOException {
		return dis.readBoolean();
	}

	@Override
	public int readInt() throws IOException {
		return dis.readInt();
	}

	@Override
	public int readCompactInt() throws IOException {
		int i = readByte();
		if (i < 0)
			i += 256;

		switch (i) {
		case 255: return readInt();
		case 254: return readShort();
		default: return i;
		}
	}

	@Override
	public short readShort() throws IOException {
		return dis.readShort();
	}

	@Override
	public long readLong() throws IOException {
		return dis.readLong();
	}

	@Override
	public long readCompactLong() throws IOException {
		int i = readByte();
		if (i < 0)
			i += 256;

		switch (i) {
		case 255: return readLong();
		case 254: return readInt();
		case 253: return readShort();
		default: return i;
		}
	}

	@Override
	public float readFloat() throws IOException {
		return dis.readFloat();
	}

	@Override
	public double readDouble() throws IOException {
		return dis.readDouble();
	}

	@Override
	public String readStringUnshared() throws IOException {
		return new String(readLengthAndBytes("String length mismatch"), StandardCharsets.UTF_8);
	}

	@Override
	public String readStringShared() throws IOException {
		int selector = readByte();
		if (selector < 0)
			selector = 256 + selector;
	
		if (selector == 254)
			return memoryString.get(readInt());
		else if (selector == 255) {
			String s = new String(readLengthAndBytes("String length mismatch"), StandardCharsets.UTF_8);
			memoryString.put(memoryString.size(), s);
			return s;
		}
		else
			return memoryString.get(selector);
	}

	@Override
	public byte[] readBytes(int length, String mismatchErrorMessage) throws IOException {
		var bytes = new byte[length];
		if (length != dis.read(bytes))
			throw new IOException(mismatchErrorMessage);

		return bytes;
	}

	@Override
	public byte[] readAllBytes() throws IOException {
		return dis.readAllBytes();
	}

	@Override
	public int readNBytes(byte[] b, int off, int len) throws IOException {
		return dis.readNBytes(b, off, len);
	}

	@Override
	public BigInteger readBigInteger() throws IOException {
		byte selector = readByte();
		switch (selector) {
		case 0: return BigInteger.valueOf(readShort());
		case 1: return BigInteger.valueOf(readInt());
		case 2: return BigInteger.valueOf(readLong());
		case 3: {
			int numBytes = readCompactInt();
			return new BigInteger(new String(readBytes(numBytes, "BigInteger length mismatch")));
		}
		default: {
			if (selector - 4 < 0)
				return BigInteger.valueOf(selector + 252);
			else
				return BigInteger.valueOf(selector - 4);
		}
		}
	}

	@Override
	public void close() throws IOException {
		dis.close();
	}
}