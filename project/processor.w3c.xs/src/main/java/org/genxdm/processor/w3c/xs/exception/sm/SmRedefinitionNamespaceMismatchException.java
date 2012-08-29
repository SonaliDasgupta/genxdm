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
package org.genxdm.processor.w3c.xs.exception.sm;

import org.genxdm.processor.w3c.xs.exception.src.SrcRedefinitionException;
import org.genxdm.xs.exceptions.SchemaException;
import org.genxdm.xs.resolve.LocationInSchema;

@SuppressWarnings("serial")
public class SmRedefinitionNamespaceMismatchException extends SrcRedefinitionException
{
    public SmRedefinitionNamespaceMismatchException(final LocationInSchema location)
    {
        super(PART_REDEFINTION_NAMESPACE_MISMATCH, location);
    }

    public SmRedefinitionNamespaceMismatchException(final LocationInSchema location, final SchemaException cause)
    {
        super(PART_REDEFINTION_NAMESPACE_MISMATCH, location, cause);
    }

    @Override
    public String getMessage()
    {
        return "If the redefined schema has a targetnamespace, the redefining schema must have the identical targetnamespace.";
    }
}
