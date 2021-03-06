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
package org.genxdm.processor.w3c.xs.exception.scc;

import javax.xml.namespace.QName;

import org.genxdm.exceptions.PreCondition;
import org.genxdm.xs.enums.ValidationOutcome;
import org.genxdm.xs.exceptions.ComponentConstraintException;
import org.genxdm.xs.exceptions.SimpleTypeException;


@SuppressWarnings("serial")
public abstract class SccAttributeDeclarationException extends ComponentConstraintException
{
    private final QName m_attributeName;

    public static final String PART_PROPERTIES = "1";
    public static final String PART_VALUE_CONSTRAINT = "2";
    public static final String PART_DERIVED_FROM_ID = "3";

    public SccAttributeDeclarationException(final String partNumber, final QName attributeName)
    {
        super(ValidationOutcome.SCC_Attribute_Declaration_Properties_Correct, partNumber);
        m_attributeName = PreCondition.assertArgumentNotNull(attributeName, "attributeName");
    }

    public SccAttributeDeclarationException(final String partNumber, final QName attributeName, final SimpleTypeException cause)
    {
        super(ValidationOutcome.SCC_Attribute_Declaration_Properties_Correct, partNumber, cause);
        m_attributeName = PreCondition.assertArgumentNotNull(attributeName, "attributeName");
    }

    public final QName getAttributeName()
    {
        return m_attributeName;
    }
}
