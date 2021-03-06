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
package org.genxdm.processor.w3c.xs.exception.cvc;

import javax.xml.namespace.QName;

import org.genxdm.exceptions.PreCondition;
import org.genxdm.processor.w3c.xs.exception.sm.SmLocationException;
import org.genxdm.xs.enums.ValidationOutcome;
import org.genxdm.xs.resolve.LocationInSchema;

/**
 * Corresponds to the <b>Identity-constraint Satisfied</b> validation rule.
 */
@SuppressWarnings("serial")
public abstract class CvcIdentityConstraintException extends SmLocationException
{
    private final QName m_constraintName;

    public static final String PART_TODO = "?";
    public static final String PART_THREE = "3";

    public CvcIdentityConstraintException(final QName constraintName, final String partNumber, final LocationInSchema location)
    {
        super(ValidationOutcome.CVC_IdentityConstraint, partNumber, location);
        m_constraintName = PreCondition.assertArgumentNotNull(constraintName, "constraintName");
    }

    public final QName getConstraintName()
    {
        return m_constraintName;
    }
}
