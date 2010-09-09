/**
 * Copyright (c) 2009-2010 TIBCO Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.gxml.processor.w3c.xs.validation.api;

import java.io.IOException;
import java.net.URI;
import java.util.LinkedList;
import java.util.List;

import javax.xml.namespace.QName;

import org.gxml.xs.exceptions.SmAbortException;
import org.gxml.xs.exceptions.SmExceptionHandler;

public interface VxValidator<A>
{
	void characters(char ch[], int start, int length) throws IOException, SmAbortException;

	void endDocument() throws IOException, SmAbortException;

	VxPSVI<A> endElement() throws IOException, SmAbortException;

	void reset();

	void setExceptionHandler(final SmExceptionHandler handler);

	void setOutputHandler(VxOutputHandler<A> handler);

	void startDocument(URI documentURI) throws IOException, SmAbortException;

	void startElement(final QName elementName, final LinkedList<VxMapping<String, String>> namespaces, final LinkedList<VxMapping<QName, String>> attributes) throws IOException, SmAbortException;

	void text(List<? extends A> value) throws IOException, SmAbortException;
}
