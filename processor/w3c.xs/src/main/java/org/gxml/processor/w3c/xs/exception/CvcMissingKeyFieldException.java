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
package org.gxml.processor.w3c.xs.exception;

import javax.xml.namespace.QName;

import org.gxml.exceptions.PreCondition;
import org.gxml.xs.resolve.SmLocation;

@SuppressWarnings("serial")
public final class CvcMissingKeyFieldException extends CvcIdentityConstraintException
{
	private final Integer m_index;

	public CvcMissingKeyFieldException(final QName constraintName, final Integer index, final SmLocation location)
	{
		super(constraintName, PART_TODO, location);
		m_index = PreCondition.assertArgumentNotNull(index, "index");
	}

	@Override
	public String getMessage()
	{
		return "Missing key field #" + m_index + " for identity constraint " + getConstraintName() + ".";
	}
}
