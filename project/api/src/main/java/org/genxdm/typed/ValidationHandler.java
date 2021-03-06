/*
 * Copyright (c) 2011 TIBCO Software Inc.
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
package org.genxdm.typed;

import javax.xml.namespace.QName;

import org.genxdm.typed.io.SequenceHandler;

/** Standard interface for validating or re-validating trees in memory.
 * 
 * @param <A> the Atom handle
 */
public interface ValidationHandler<A>
    extends SequenceHandler<A>, Validator<A>
{
    /** Set the QName of the initial element type, when validating a fragment
     * rooted at a single element of arbitrary name.
     * 
     * See {@link org.genxdm.typed.TypedContext.validate(N, ValidationHandler, QName)}
     * 
     * @param name the name of the type for the element (if null, equivalent to unsetting)
     */
    void setInitialElementType(QName name);
}
