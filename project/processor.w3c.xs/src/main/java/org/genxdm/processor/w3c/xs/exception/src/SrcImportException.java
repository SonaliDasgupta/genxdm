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
package org.genxdm.processor.w3c.xs.exception.src;

import org.genxdm.processor.w3c.xs.exception.sm.SmLocationException;
import org.genxdm.xs.enums.ValidationOutcome;
import org.genxdm.xs.exceptions.SchemaException;
import org.genxdm.xs.resolve.LocationInSchema;

/**
 * Used for reporting problems with imported schemas.
 */
@SuppressWarnings("serial")
public abstract class SrcImportException extends SmLocationException
{
    public static final String PART_NOT_WELL_FORMED = "2";

    public SrcImportException(final String partNumber, final LocationInSchema location)
    {
        super(ValidationOutcome.SRC_Import, partNumber, location);
    }

    public SrcImportException(final String partNumber, final LocationInSchema location, final SchemaException cause)
    {
        super(ValidationOutcome.SRC_Import, partNumber, location, cause);
    }

}
