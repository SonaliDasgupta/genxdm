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
package org.genxdm.bridgekit.xs.complex;

import org.genxdm.exceptions.PreCondition;
import org.genxdm.xs.components.ElementDefinition;
import org.genxdm.xs.components.ModelGroup;
import org.genxdm.xs.components.SchemaParticle;
import org.genxdm.xs.components.ParticleTerm;
import org.genxdm.xs.components.SchemaWildcard;
import org.genxdm.xs.constraints.ModelGroupUse;

public final class ParticleWithModelGroupTerm extends ParticleImpl implements ModelGroupUse
{
    public ParticleWithModelGroupTerm(final int minOccurs, final ModelGroup modelGroup)
    {
        super(minOccurs, -1, true, modelGroup);
    }

    public ParticleWithModelGroupTerm(final int minOccurs, final int maxOccurs, final ModelGroup modelGroup)
    {
        super(minOccurs, maxOccurs, false, modelGroup);
        PreCondition.assertTrue(maxOccurs >= 0, "maxOccurs >= 0");
    }

    public ModelGroup getTerm()
    {
        // We know this is safe by construction.
        return (ModelGroup)m_term;
    }

    public boolean isEmptiable()
    {
        return isEmptiable(this);
    }

    private static  boolean isEmptiable(final ModelGroupUse particle)
    {
        if (particle.getMinOccurs() > 0)
        {
            // Calculating the min part of the effective total range (EFT) is
            // the same for ALL, SEQUENCE, and CHOICE. The spec provides a
            // differenct mechanism for the max part, but we're not concerned
            // with that.

            // Since the minOccurs isn't 0, all of the wildcard and element decls must have
            // a minOccurs of zero, and all of the contained groups must have an EFT.minPart of zero.
            return isEmptiable(particle.getTerm());
        }
        return true;
    }

    private static  boolean isEmptiable(final ModelGroup group)
    {
        for (final SchemaParticle particle : group.getParticles())
        {
            final ParticleTerm term = particle.getTerm();
            if (term instanceof ElementDefinition || term instanceof SchemaWildcard)
            {
                if (particle.getMinOccurs() > 0)
                {
                    return false;
                }
            }
            else if (term instanceof ModelGroup)
            {
                if (particle.getMinOccurs() > 0)
                {
                    if (!isEmptiable((ModelGroup)term))
                    {
                        return false;
                    }
                }
            }
            else
            {
                throw new AssertionError(term);
            }
        }
        return true;
    }
}
