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
package org.gxml.processor.w3c.xs.validation.regex.impl.logging;

import org.gxml.processor.w3c.xs.validation.regex.api.RegExPattern;

public class GenericRegExStepLoggerFactory<E, T> implements RegExStepLoggerFactory<E, T>
{
    public GenericRegExStepLoggerFactory(final String prefix)
    {
        m_prefix = prefix;
    }

    public RegExStepLogger<E, T> newLogger(RegExPattern<E, T> pattern)
    {
        return new GenericRegExStepLogger<E, T>(m_prefix);
    }

    private String m_prefix;
}
