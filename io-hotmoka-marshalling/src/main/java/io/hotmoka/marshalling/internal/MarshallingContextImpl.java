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

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import io.hotmoka.marshalling.api.Marshallable;
import io.hotmoka.marshalling.api.MarshallingContext;
import io.hotmoka.marshalling.api.ObjectMarshaller;

/**
 * Implementation of a context used during object marshaling into bytes.
 */
public class MarshallingContextImpl implements MarshallingContext {
	private final DataOutputStream dos;
	private final Map<String, Integer> memoryString = new HashMap<>();

	/**
	 * Object marshallers for specific classes, if any.
	 */
	private final Map<Class<?>, ObjectMarshaller<?>> objectMarshallers = new HashMap<>();

	public MarshallingContextImpl(OutputStream oos) {
		this.dos = new DataOutputStream(oos);
	}

	/**
	 * Registers an object marshaller. It will be used to marshall its class.
	 * 
	 * @param om the object marshaller
	 */
	protected void registerObjectMarshaller(ObjectMarshaller<?> om) {
		objectMarshallers.put(om.clazz(), om);
	}

	@Override
	public <C> void writeObject(Class<C> clazz, C value) throws IOException {
		@SuppressWarnings("unchecked")
		var om = (ObjectMarshaller<C>) objectMarshallers.get(clazz);
		if (om == null)
			throw new IOException("Missing object marshaller for class " + clazz.getName());

		om.write(value, this);
	}

	@Override
	public void writeStringShared(String s) throws IOException {
		Integer index = memoryString.get(s);

		if (index != null) {
			if (index < 254)
				dos.writeByte(index);
			else {
				dos.writeByte(254);
				dos.writeInt(index);
			}
		}
		else {
			int next = memoryString.size();
			if (next == Integer.MAX_VALUE) // irrealistic
				throw new IOException("too many strings in the same context");

			memoryString.put(s, next);

			dos.writeByte(255);
			writeLengthAndBytes(s.getBytes(StandardCharsets.UTF_8));
		}
	}

	@Override
	public final void writeStringUnshared(String s) throws IOException {
		writeLengthAndBytes(s.getBytes(StandardCharsets.UTF_8));
	}

	@Override
	public void writeByte(int b) throws IOException {
		dos.writeByte(b);
	}

	@Override
	public void writeChar(int c) throws IOException {
		dos.writeChar(c);
	}

	@Override
	public void writeInt(int i) throws IOException {
		dos.writeInt(i);
	}

	@Override
	public void writeCompactInt(int i) throws IOException {
		if (i >= 0 && i < 254)
			writeByte(i);
		else if (i >= Short.MIN_VALUE && i <= Short.MAX_VALUE) {
			writeByte(254);
			writeShort((short) i);
		}
		else {
			writeByte(255);
			writeInt(i);
		}
	}

	@Override
	public void writeBytes(byte[] bytes) throws IOException {
		dos.write(bytes);
	}

	@Override
	public void writeLengthAndBytes(byte[] bytes) throws IOException {
		writeCompactInt(bytes.length);
		writeBytes(bytes);
	}

	@Override
	public void writeLengthAndArray(Marshallable[] marshallables) throws IOException {
		writeCompactInt(marshallables.length);

		for (var marshallable: marshallables)
			marshallable.into(this);
	}

	@Override
	public void writeDouble(double d) throws IOException {
		dos.writeDouble(d);
	}

	@Override
	public void writeFloat(float f) throws IOException {
		dos.writeFloat(f);
	}

	@Override
	public void writeLong(long l) throws IOException {
		dos.writeLong(l);
	}

	@Override
	public void writeCompactLong(long l) throws IOException {
		if (l >= 0 && l < 253)
			writeByte((int) l);
		else if (l >= Short.MIN_VALUE && l <= Short.MAX_VALUE) {
			writeByte(253);
			writeShort((short) l);
		}
		else if (l >= Integer.MIN_VALUE && l <= Integer.MAX_VALUE){
			writeByte(254);
			writeInt((int) l);
		}
		else {
			writeByte(255);
			writeLong(l);
		}
	}

	@Override
	public void writeShort(int s) throws IOException {
		dos.writeShort(s);
	}

	@Override
	public void writeBoolean(boolean b) throws IOException {
		dos.writeBoolean(b);
	}

	@Override
	public void writeBigInteger(BigInteger bi) throws IOException {
		short small = bi.shortValue();

		if (BigInteger.valueOf(small).equals(bi)) {
			if (0 <= small && small <= 251)
				writeByte(4 + small);
			else {
				writeByte(0);
				writeShort(small);
			}
		}
		else if (BigInteger.valueOf(bi.intValue()).equals(bi)) {
			writeByte(1);
			writeInt(bi.intValue());
		}
		else if (BigInteger.valueOf(bi.longValue()).equals(bi)) {
			writeByte(2);
			writeLong(bi.longValue());
		}
		else {
			writeByte(3);
			writeLengthAndBytes(bi.toString().getBytes());
		}
	}

	@Override
	public void flush() throws IOException {
		dos.flush();
	}

	@Override
	public void close() throws IOException {
		dos.close();
	}
}