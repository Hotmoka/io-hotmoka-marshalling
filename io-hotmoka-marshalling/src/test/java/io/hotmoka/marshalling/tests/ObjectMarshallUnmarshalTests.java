package io.hotmoka.marshalling.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.hotmoka.marshalling.MarshallingContexts;
import io.hotmoka.marshalling.UnmarshallingContexts;
import io.hotmoka.testing.AbstractLoggedTests;

public class ObjectMarshallUnmarshalTests extends AbstractLoggedTests {

    @Test
    @DisplayName("MyMarshallable marshal unmarshal works")
    public void myMarshallableMarshalUnmarshalWorks() throws IOException {
    	MyMarshallable expected = new MyMarshallable("Albert", "Einstein", 1879);

        ByteArrayOutputStream stream;
        try (var baos = stream = new ByteArrayOutputStream(); var context = MarshallingContexts.of(baos)) {
        	expected.into(context);
        }

        byte[] marshalled = stream.toByteArray();

        try (var bais = new ByteArrayInputStream(marshalled); var context = UnmarshallingContexts.of(bais)) {
        	MyMarshallable actual = new MyMarshallable(context);
        	assertEquals(expected, actual);
        }
    }
}