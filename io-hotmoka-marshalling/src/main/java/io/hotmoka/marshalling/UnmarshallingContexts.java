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

package io.hotmoka.marshalling;

import java.io.IOException;
import java.io.InputStream;

import io.hotmoka.marshalling.api.UnmarshallingContext;
import io.hotmoka.marshalling.internal.UnmarshallingContextImpl;

/**
 * Providers of unmarshalling contexts.
 */
public final class UnmarshallingContexts {

	private UnmarshallingContexts() {}

	/**
	 * Yields an unmarshalling context.
	 * 
	 * @param is the input stream of the context
	 * @return the unmarshalling context
	 * @throws IOException if the context cannot be created
	 */
	public static UnmarshallingContext of(InputStream is) throws IOException {
		return new UnmarshallingContextImpl(is);
	}
}