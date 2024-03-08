package io.hotmoka.marshalling.tests;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.hotmoka.marshalling.MarshallingContexts;
import io.hotmoka.marshalling.UnmarshallingContexts;
import io.hotmoka.testing.AbstractLoggedTests;

public class MarshallUnmarshalTests extends AbstractLoggedTests {

    @Test
    @DisplayName("short marshal unmarshal works")
    public void shortMarshalUnmarshalWorks() throws IOException {
        short expected = 42;

        ByteArrayOutputStream stream;
        try (var baos = stream = new ByteArrayOutputStream(); var context = MarshallingContexts.of(baos)) {
            context.writeShort(expected);
        }

        byte[] marshalled = stream.toByteArray();

        try (var bais = new ByteArrayInputStream(marshalled); var context = UnmarshallingContexts.of(bais)) {
        	short actual = context.readShort();
        	assertEquals(expected, actual);
        }
    }

    @Test
    @DisplayName("int marshal unmarshal works")
    public void intMarshalUnmarshalWorks() throws IOException {
    	int expected = 42;

    	ByteArrayOutputStream stream;
        try (var baos = stream = new ByteArrayOutputStream(); var context = MarshallingContexts.of(baos)) {
            context.writeInt(expected);
        }

        byte[] marshalled = stream.toByteArray();

        try (var bais = new ByteArrayInputStream(marshalled); var context = UnmarshallingContexts.of(bais)) {
        	int actual = context.readInt();
        	assertEquals(expected, actual);
        }
    }

    @Test
    @DisplayName("long marshal unmarshal works")
    public void longMarshalUnmarshalWorks() throws IOException {
    	long expected = 92L;

    	ByteArrayOutputStream stream;
        try (var baos = stream = new ByteArrayOutputStream(); var context = MarshallingContexts.of(baos)) {
            context.writeLong(expected);
        }

        byte[] marshalled = stream.toByteArray();

        try (var bais = new ByteArrayInputStream(marshalled); var context = UnmarshallingContexts.of(bais)) {
        	long actual = context.readLong();
        	assertEquals(expected, actual);
        }
    }

    @Test
    @DisplayName("longer long marshal unmarshal works")
    public void longerMarshalUnmarshalWorks() throws IOException {
    	long expected = 1000129L;

    	ByteArrayOutputStream stream;
        try (var baos = stream = new ByteArrayOutputStream(); var context = MarshallingContexts.of(baos)) {
            context.writeLong(expected);
        }

        byte[] marshalled = stream.toByteArray();

        try (var bais = new ByteArrayInputStream(marshalled); var context = UnmarshallingContexts.of(bais)) {
        	long actual = context.readLong();
        	assertEquals(expected, actual);
        }
    }

    @Test
    @DisplayName("longest long marshal unmarshal works")
    public void longestMarshalUnmarshalWorks() throws IOException {
    	long expected = 9007199254740991L;

    	ByteArrayOutputStream stream;
        try (var baos = stream = new ByteArrayOutputStream(); var context = MarshallingContexts.of(baos)) {
            context.writeLong(expected);
        }

        byte[] marshalled = stream.toByteArray();

        try (var bais = new ByteArrayInputStream(marshalled); var context = UnmarshallingContexts.of(bais)) {
        	long actual = context.readLong();
        	assertEquals(expected, actual);
        }
    }

    @Test
    @DisplayName("large BigInteger marshal unmarshal works")
    public void largeBigIntegerMarshalUnmarshalWorks() throws IOException {
    	BigInteger expected = BigInteger.valueOf(9007199254740991L);

    	ByteArrayOutputStream stream;
        try (var baos = stream = new ByteArrayOutputStream(); var context = MarshallingContexts.of(baos)) {
            context.writeBigInteger(expected);
        }

        byte[] marshalled = stream.toByteArray();

        try (var bais = new ByteArrayInputStream(marshalled); var context = UnmarshallingContexts.of(bais)) {
        	BigInteger actual = context.readBigInteger();
        	assertEquals(expected, actual);
        }
    }

    @Test
    @DisplayName("small BigInteger marshal unmarshal works")
    public void smallBigIntegerMarshalUnmarshalWorks() throws IOException {
    	BigInteger expected = BigInteger.valueOf(9L);

    	ByteArrayOutputStream stream;
        try (var baos = stream = new ByteArrayOutputStream(); var context = MarshallingContexts.of(baos)) {
            context.writeBigInteger(expected);
        }

        byte[] marshalled = stream.toByteArray();

        try (var bais = new ByteArrayInputStream(marshalled); var context = UnmarshallingContexts.of(bais)) {
        	BigInteger actual = context.readBigInteger();
        	assertEquals(expected, actual);
        }
    }

    @Test
    @DisplayName("medium BigInteger marshal unmarshal works")
    public void mediumBigIntegerMarshalUnmarshalWorks() throws IOException {
    	BigInteger expected = BigInteger.valueOf(7654319L);

    	ByteArrayOutputStream stream;
        try (var baos = stream = new ByteArrayOutputStream(); var context = MarshallingContexts.of(baos)) {
            context.writeBigInteger(expected);
        }

        byte[] marshalled = stream.toByteArray();

        try (var bais = new ByteArrayInputStream(marshalled); var context = UnmarshallingContexts.of(bais)) {
        	BigInteger actual = context.readBigInteger();
        	assertEquals(expected, actual);
        }
    }

    @Test
    @DisplayName("larger BigInteger marshal unmarshal works")
    public void largerBigIntegerMarshalUnmarshalWorks() throws IOException {
    	BigInteger expected = new BigInteger("9007199254740991765896");

    	ByteArrayOutputStream stream;
        try (var baos = stream = new ByteArrayOutputStream(); var context = MarshallingContexts.of(baos)) {
            context.writeBigInteger(expected);
        }

        byte[] marshalled = stream.toByteArray();

        try (var bais = new ByteArrayInputStream(marshalled); var context = UnmarshallingContexts.of(bais)) {
        	BigInteger actual = context.readBigInteger();
        	assertEquals(expected, actual);
        }
    }

    @Test
    @DisplayName("double marshal unmarshal works")
    public void doubleMarshalUnmarshalWorks() throws IOException {
    	double expected = 33.8;

    	ByteArrayOutputStream stream;
        try (var baos = stream = new ByteArrayOutputStream(); var context = MarshallingContexts.of(baos)) {
            context.writeDouble(expected);
        }

        byte[] marshalled = stream.toByteArray();

        try (var bais = new ByteArrayInputStream(marshalled); var context = UnmarshallingContexts.of(bais)) {
        	double actual = context.readDouble();
        	assertEquals(expected, actual);
        }
    }

    @Test
    @DisplayName("float marshal unmarshal works")
    public void floatMarshalUnmarshalWorks() throws IOException {
    	float expected = 33.8f;

    	ByteArrayOutputStream stream;
        try (var baos = stream = new ByteArrayOutputStream(); var context = MarshallingContexts.of(baos)) {
            context.writeFloat(expected);
        }

        byte[] marshalled = stream.toByteArray();

        try (var bais = new ByteArrayInputStream(marshalled); var context = UnmarshallingContexts.of(bais)) {
        	float actual = context.readFloat();
        	assertEquals(expected, actual);
        }
    }

    @Test
    @DisplayName("true marshal unmarshal works")
    public void trueMarshalUnmarshalWorks() throws IOException {
    	boolean expected = true;

    	ByteArrayOutputStream stream;
        try (var baos = stream = new ByteArrayOutputStream(); var context = MarshallingContexts.of(baos)) {
            context.writeBoolean(expected);
        }

        byte[] marshalled = stream.toByteArray();

        try (var bais = new ByteArrayInputStream(marshalled); var context = UnmarshallingContexts.of(bais)) {
        	boolean actual = context.readBoolean();
        	assertEquals(expected, actual);
        }
    }

    @Test
    @DisplayName("false marshal unmarshal works")
    public void falseMarshalUnmarshalWorks() throws IOException {
    	boolean expected = false;

    	ByteArrayOutputStream stream;
        try (var baos = stream = new ByteArrayOutputStream(); var context = MarshallingContexts.of(baos)) {
            context.writeBoolean(expected);
        }

        byte[] marshalled = stream.toByteArray();

        try (var bais = new ByteArrayInputStream(marshalled); var context = UnmarshallingContexts.of(bais)) {
        	boolean actual = context.readBoolean();
        	assertEquals(expected, actual);
        }
    }

    @Test
    @DisplayName("char marshal unmarshal works")
    public void charMarshalUnmarshalWorks() throws IOException {
    	char expected = 'd';

    	ByteArrayOutputStream stream;
        try (var baos = stream = new ByteArrayOutputStream(); var context = MarshallingContexts.of(baos)) {
            context.writeChar(expected);
        }

        byte[] marshalled = stream.toByteArray();

        try (var bais = new ByteArrayInputStream(marshalled); var context = UnmarshallingContexts.of(bais)) {
        	char actual = context.readChar();
        	assertEquals(expected, actual);
        }
    }

    @Test
    @DisplayName("unshared String marshal unmarshal works")
    public void unsharedStringMarshalUnmarshalWorks() throws IOException {
    	String expected = "hello world";

    	ByteArrayOutputStream stream;
        try (var baos = stream = new ByteArrayOutputStream(); var context = MarshallingContexts.of(baos)) {
            context.writeStringUnshared(expected);
        }

        byte[] marshalled = stream.toByteArray();

        try (var bais = new ByteArrayInputStream(marshalled); var context = UnmarshallingContexts.of(bais)) {
        	String actual = context.readStringUnshared();
        	assertEquals(expected, actual);
        }
    }

    @Test
    @DisplayName("unshared long String marshal unmarshal works")
    public void unsharedLongStringMarshalUnmarshalWorks() throws IOException {
    	String expected = "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";

    	ByteArrayOutputStream stream;
        try (var baos = stream = new ByteArrayOutputStream(); var context = MarshallingContexts.of(baos)) {
            context.writeStringUnshared(expected);
        }

        byte[] marshalled = stream.toByteArray();

        try (var bais = new ByteArrayInputStream(marshalled); var context = UnmarshallingContexts.of(bais)) {
        	String actual = context.readStringUnshared();
        	assertEquals(expected, actual);
        }
    }

    @Test
    @DisplayName("shared String marshal unmarshal works")
    public void sharedStringMarshalUnmarshalWorks() throws IOException {
    	String expected = "hello world";

    	ByteArrayOutputStream stream;
        try (var baos = stream = new ByteArrayOutputStream(); var context = MarshallingContexts.of(baos)) {
            context.writeStringShared(expected);
        }

        byte[] marshalled = stream.toByteArray();

        try (var bais = new ByteArrayInputStream(marshalled); var context = UnmarshallingContexts.of(bais)) {
        	String actual = context.readStringShared();
        	assertEquals(expected, actual);
        }
    }

    @Test
    @DisplayName("byte[] marshal unmarshal works")
    public void byteArrayMarshalUnmarshalWorks() throws IOException {
    	byte[] expected = "hello world".getBytes();

    	ByteArrayOutputStream stream;
        try (var baos = stream = new ByteArrayOutputStream(); var context = MarshallingContexts.of(baos)) {
            context.writeBytes(expected);
        }

        byte[] marshalled = stream.toByteArray();

        try (var bais = new ByteArrayInputStream(marshalled); var context = UnmarshallingContexts.of(bais)) {
        	byte[] actual = context.readBytes(expected.length, "length mismatch error");
        	assertArrayEquals(expected, actual);
        }
    }

    @Test
    @DisplayName("byte[] and length marshal unmarshal works")
    public void byteArrayAndLengthMarshalUnmarshalWorks() throws IOException {
    	byte[] expected = "hello world".getBytes();

    	ByteArrayOutputStream stream;
        try (var baos = stream = new ByteArrayOutputStream(); var context = MarshallingContexts.of(baos)) {
            context.writeLengthAndBytes(expected);
        }

        byte[] marshalled = stream.toByteArray();

        try (var bais = new ByteArrayInputStream(marshalled); var context = UnmarshallingContexts.of(bais)) {
        	byte[] actual = context.readLengthAndBytes("length mismatch error");
        	assertArrayEquals(expected, actual);
        }
    }

    @Test
    @DisplayName("compact int marshal unmarshal works")
    public void compactIntMarshalUnmarshalWorks() throws IOException {
    	int expected = 30006;

    	ByteArrayOutputStream stream;
        try (var baos = stream = new ByteArrayOutputStream(); var context = MarshallingContexts.of(baos)) {
            context.writeCompactInt(expected);
        }

        byte[] marshalled = stream.toByteArray();

        try (var bais = new ByteArrayInputStream(marshalled); var context = UnmarshallingContexts.of(bais)) {
        	int actual = context.readCompactInt();
        	assertEquals(expected, actual);
        }
    }
}