[![Java-Build Action Status](https://github.com/Hotmoka/io-hotmoka-marshalling/actions/workflows/java_build.yml/badge.svg)](https://github.com/Hotmoka/io-hotmoka-marshalling/actions)
[![Maven Central](https://img.shields.io/maven-central/v/io.hotmoka.marshalling/io-hotmoka-marshalling-api.svg?label=Maven%20Central)](https://central.sonatype.com/search?smo=true&q=g:io.hotmoka.marshalling)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)

# Efficient serialization/deserialization of objects into bytes in Java

This project provides efficient serialization/deserialization of objects into bytes.
The resulting array of bytes is in general shorter than what is obtained with standard
Java serialization, but it requires to specify explicitly how to marshal and unmarshal the objects.

One can defined a marshallable object by subclassing the `AbstractMarshallable` class, defining
a method `into()` for marshalling an object into bytes and a constructor (or a factory method)
for unmarshalling the object from bytes:

```java
public class MyMarshallable extends AbstractMarshallable {
  private final String name;
  private final String surname;
  private final int yearOfBirth;

  public MyMarshallable(String name, String surname, int yearOfBirth) {
    this.name = name;
    this.surname = surname;
    this.yearOfBirth = yearOfBirth;
  }

  public MyMarshallable(UnmarshallingContext context) throws IOException {
    // data must be unmarshalled in the same order as it was marshalled inside the into() method
    this(context.readStringShared(), context.readStringShared(), context.readCompactInt());
  }

  @Override
  public void into(MarshallingContext context) throws IOException {
    // by writing shared strings, we could reduce the size of the marshalled data
    // if there are repeated strings marshalled into the same context
    context.writeStringShared(name);
    context.writeStringShared(surname);
    context.writeCompactInt(yearOfBirth);
  }

  @Override
  public boolean equals(Object other) {
    return other instanceof MyMarshallable mm &&
      Objects.equals(name, mm.name) && Objects.equals(surname, mm.surname) && yearOfBirth == mm.yearOfBirth;
  }
}
```

The following testcase shows how a `MyMarshallable` can be marshalled into bytes and later
unmarshalled from bytes:

```java
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
```

<p align="center"><img width="100" src="https://mirrors.creativecommons.org/presskit/buttons/88x31/png/by.png" alt="This documentation is licensed under a Creative Commons Attribution 4.0 International License"></p><p align="center">This document is licensed under a Creative Commons Attribution 4.0 International License.</p>

<p align="center">Copyright 2024 by Fausto Spoto (fausto.spoto@hotmoka.io)</p>