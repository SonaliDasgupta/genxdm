/*
 * Copyright (c) 2010-2011 TIBCO Software Inc.
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
package org.genxdm.processor.convert;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;

import org.genxdm.exceptions.AtomCastException;
import org.genxdm.exceptions.GenXDMException;
import org.genxdm.exceptions.PreCondition;
import org.genxdm.io.DtdAttributeKind;
import org.genxdm.typed.io.SequenceHandler;
import org.genxdm.typed.types.AtomBridge;

public class SequenceConversionFilter<Asrc, Atrg>
    implements SequenceHandler<Asrc>
{
    public SequenceConversionFilter(final SequenceHandler<Atrg> target, final AtomBridge<Asrc> sb, final AtomBridge<Atrg> tb)
    {
        this.target = PreCondition.assertNotNull(target, "target");
        this.srcBridge = PreCondition.assertNotNull(sb, "srcBridge");
        this.trgBridge = PreCondition.assertNotNull(tb, "trgBridge");
    }

    public void attribute(String namespaceURI, String localName, String prefix, List<? extends Asrc> data, QName type)
        throws GenXDMException
    {
        List<Atrg> atoms = convertAtomList(data);
        target.attribute(namespaceURI, localName, prefix, atoms, type);
    }

    public void startElement(String namespaceURI, String localName, String prefix, QName type)
        throws GenXDMException
    {
        target.startElement(namespaceURI, localName, prefix, type);
    }

    public void text(List<? extends Asrc> data)
        throws GenXDMException
    {
        List<Atrg> atoms = convertAtomList(data);
        target.text(atoms);
    }

    public void attribute(String namespaceURI, String localName, String prefix, String value, DtdAttributeKind type)
        throws GenXDMException
    {
        target.attribute(namespaceURI, localName, prefix, value, type);
    }

    public void comment(String value)
        throws GenXDMException
    {
        target.comment(value);
    }

    public void endDocument()
        throws GenXDMException
    {
        target.endDocument();
    }

    public void endElement()
        throws GenXDMException
    {
        target.endElement();
    }

    public void namespace(String prefix, String namespaceURI)
        throws GenXDMException
    {
        target.namespace(prefix, namespaceURI);
    }

    public void processingInstruction(String pi, String data)
        throws GenXDMException
    {
        target.processingInstruction(pi, data);
    }

    public void startDocument(final URI documentURI, final String docTypeDecl)
        throws GenXDMException
    {
        target.startDocument(documentURI, docTypeDecl);
    }

    public void startElement(String namespaceURI, String localName, String prefix)
        throws GenXDMException
    {
        target.startElement(namespaceURI, localName, prefix);
    }

    public void text(String data)
        throws GenXDMException
    {
        target.text(data);
    }

    public void close()
        throws IOException
    {
        target.close();
    }

    public void flush()
        throws IOException
    {
        target.flush();
    }
    
    private Atrg convertAtom(final Asrc atom)
        throws GenXDMException
    {
        try
        {
            return trgBridge.compile(srcBridge.getC14NForm(atom), srcBridge.getNativeType(atom));
        }
        catch (AtomCastException gace)
        {
            throw new GenXDMException(gace);
        }
    }
    
    private List<Atrg> convertAtomList(final List<? extends Asrc> data)
    {
        List<Atrg> atoms = new ArrayList<Atrg>(data.size());
        for (Asrc atom : data)
        {
            atoms.add(convertAtom(atom));
        }
        return atoms;
    }

    private final SequenceHandler<Atrg> target;
    private final AtomBridge<Asrc> srcBridge;
    private final AtomBridge<Atrg> trgBridge;
}
