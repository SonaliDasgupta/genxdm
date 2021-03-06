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
package org.genxdm.processor.w3c.xs.validation;

import java.net.URI;
import java.util.LinkedList;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;

import org.genxdm.bridgekit.misc.StringToURIParser;
import org.genxdm.exceptions.PreCondition;
import org.genxdm.processor.w3c.xs.validation.api.VxMapping;
import org.genxdm.processor.w3c.xs.validation.api.VxValidator;
import org.genxdm.typed.io.SAXValidator;
import org.genxdm.typed.io.SequenceHandler;
import org.genxdm.xs.SchemaComponentCache;
import org.genxdm.xs.exceptions.SchemaExceptionHandler;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;


public final class SAXContentValidatorImpl<A> implements SAXValidator<A>
{
	public SAXContentValidatorImpl(final VxValidator<A> kernel)
	{
		m_kernel = kernel;
	}

    @Override
	public void characters(final char ch[], final int start, final int length) throws SAXException
	{
		try
		{
			m_kernel.characters(ch, start, length);
		}
		catch (final Exception e)
		{
			throw new SAXException(e);
		}
	}

    @Override
	public void endDocument() throws SAXException
	{
		try
		{
			m_kernel.endDocument();
		}
		catch (final Exception e)
		{
			throw new SAXException(e);
		}
	}

    @Override
	public void endElement(final String uri, final String localName, final String qName) throws SAXException
	{
		try
		{
			m_kernel.endElement();
		}
		catch (final Exception e)
		{
			e.printStackTrace();
			throw new SAXException(e);
		}
	}

    @Override
	public void endPrefixMapping(final String prefix)
	{
	}

    @Override
	public void ignorableWhitespace(final char ch[], final int start, final int length)
	{
		// Ignore
	}

    @Override
	public void processingInstruction(final String target, final String data)
	{
		// Ignore
	}

    @Override
	public void reset()
	{
		m_kernel.reset();
	}

    @Override
	public void setDocumentLocator(final Locator locator)
	{
		m_locator = locator;
	}
	
	@Override
	public void setIgnores(Iterable<QName> elementNames)
	{
	    m_kernel.setIgnoredElements(elementNames);
	}

    @Override
	public void skippedEntity(final String name)
	{
	}

    @Override
	public void startDocument() throws SAXException
	{
//System.out.println("Here's a document.");
		final URI documentURI;
		try
		{
			documentURI = (m_locator != null) ? ((m_locator.getSystemId() != null) ? StringToURIParser.parse(m_locator.getSystemId()) : null) : null;
		}
		catch (final IllegalArgumentException e)
		{
			throw new SAXException(e);
		}

		try
		{
			m_kernel.startDocument(documentURI);
		}
		catch (final Exception e)
		{
			throw new SAXException(e);
		}
	}

    @Override
	public void startElement(final String uri, final String localName, final String qName, final Attributes attributes) throws SAXException
	{
        // here's an interesting factoid:
	    // if you're dealing with something in the default (global) namespace,
	    // such as the po.xml instance document from the schema primer,
	    // SAX returns empty string for uri and localName, and puts the
	    // name in qName.  this sort of needs to be handled, eh?
	    final String ns = (uri == null) ? "" : uri;
	    final String name = ( (localName == null) || (localName.length() == 0)) ? getLocalPart(qName) : localName;
		m_attributes.clear();

		if ((attributes != null ) && (attributes.getLength() > 0))
		{
			for (int i = 0; i < attributes.getLength(); i++)
			{
				final String attributeQN = attributes.getQName(i);

				if (isNamespaceAttribute(attributeQN))
				{
					// Ignore namespace attributes which are carried by prefix mappings.
				}
				else
				{
					final String attributeNS = attributes.getURI(i);
					final String attributeLN = attributes.getLocalName(i);
					final String attributePH = getPrefix(attributeQN);
					final String attributeSV = attributes.getValue(i);

					final QName aqName = new QName(attributeNS, attributeLN, attributePH);

					m_attributes.add(new VxMapping<QName, String>(aqName, attributeSV));
				}
			}
		}
		try
		{
			m_kernel.startElement(new QName(ns, name, getPrefix(qName)), m_namespaces, m_attributes, null);
		}
		catch (final Exception e)
		{
			//e.printStackTrace();
			throw new SAXException(e);
		}
		finally
		{
			m_namespaces.clear();
			m_attributes.clear();
		}
	}

    @Override
	public void startPrefixMapping(final String prefix, final String uri)
	{
		m_namespaces.add(new VxMapping<String, String>(prefix, uri));
	}

    @Override
    public SchemaExceptionHandler getSchemaExceptionHandler()
    {
        return errors;
    }

    @Override
    public void setSchema(SchemaComponentCache cache)
    {
        m_kernel.setComponentProvider(cache.getComponentProvider());
    }

    @Override
    public void setSchemaExceptionHandler(SchemaExceptionHandler errors)
    {
        this.errors = errors;
        m_kernel.setExceptionHandler(this.errors);
    }

    @Override
    public void setSequenceHandler(SequenceHandler<A> handler)
    {
        m_kernel.setOutputHandler(new OutputAdapter<A>(handler));
    }

    /**
     * Determines if the attribute is in the "http://www.w3.org/2000/xmlns/" namespace. This is determined by seeing if
     * the attribute qualified name string identically matches "xmlns" or whether it starts with "xmlns" and is followed
     * by a colon. A null attribute qualified name string is defined to be an illegal argument.
     * 
     * @param qname
     *            The qualified name string of the attribute.
     * @return <CODE>true</CODE> if the attribute is in this namespace, otherwise <CODE>false</CODE>.
     * @throws IllegalArgumentException
     *             If the qname is null.
     */
    private static boolean isNamespaceAttribute(final String qname) throws IllegalArgumentException
    {
        PreCondition.assertArgumentNotNull(qname, "qname");
        if (XMLConstants.XMLNS_ATTRIBUTE.equals(qname))
        {
            return true;
        }
        else
        {
            return (qname.startsWith(XMLNS_COLON));
        }
    }

    private static String getPrefix(final String qualifiedName)
    {
        final int index = qualifiedName.indexOf(':');
        if (index == -1)
            return XMLConstants.DEFAULT_NS_PREFIX;
        return qualifiedName.substring(0, index);
    }
    
    private static String getLocalPart(final String qualifiedName)
    {
        final int index = qualifiedName.indexOf(':');
        if (index == -1)
            return qualifiedName;
        return qualifiedName.substring(index+1);
    }

    /**
     * The standard "prefix" for xmlns attributes followed by a colon.
     */
    private static final String XMLNS_COLON = XMLConstants.XMLNS_ATTRIBUTE + ":";

    private final LinkedList<VxMapping<QName, String>> m_attributes = new LinkedList<VxMapping<QName, String>>();
    private final VxValidator<A> m_kernel;
    private Locator m_locator;
    private SchemaExceptionHandler errors;

    private final LinkedList<VxMapping<String, String>> m_namespaces = new LinkedList<VxMapping<String, String>>();

}
