/*
Copyright 2024 Fausto Spoto

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

package io.hotmoka.marshalling.tests;

import java.io.IOException;
import java.util.Objects;

import io.hotmoka.marshalling.AbstractMarshallable;
import io.hotmoka.marshalling.api.MarshallingContext;
import io.hotmoka.marshalling.api.UnmarshallingContext;

/**
 * An example of marshallable object.
 */
public class MyMarshallable extends AbstractMarshallable {
	private final String name;
	private final String surname;
	private final int yearOfBirth;

	/**
	 * Creates the marshallable object.
	 * 
	 * @param name the name field value
	 * @param surname the surname field value
	 * @param yearOfBirth the year of birth field value
	 */
	public MyMarshallable(String name, String surname, int yearOfBirth) {
		this.name = name;
		this.surname = surname;
		this.yearOfBirth = yearOfBirth;
	}

	/**
	 * Unmarshals a {@link MyMarshallable} from the given context.
	 * This could be presented as a factory method instead of a constructor.
	 * 
	 * @param context the unmarshalling context
	 * @throws IOException if the object could not be unmarshalled
	 */
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