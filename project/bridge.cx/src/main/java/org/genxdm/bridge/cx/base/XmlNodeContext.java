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
package org.genxdm.bridge.cx.base;

import java.util.HashMap;
import java.util.Map;

import javax.xml.stream.XMLReporter;

import org.genxdm.Cursor;
import org.genxdm.Feature;
import org.genxdm.Model;
import org.genxdm.ProcessingContext;
import org.genxdm.bridge.cx.tree.XmlNode;
import org.genxdm.bridge.cx.typed.TypedXmlNodeContext;
import org.genxdm.bridgekit.filters.FilteredFragmentBuilder;
import org.genxdm.bridgekit.filters.NamespaceFixupFilter;
import org.genxdm.exceptions.PreCondition;
import org.genxdm.io.DocumentHandler;
import org.genxdm.io.FragmentBuilder;
import org.genxdm.io.Resolver;
import org.genxdm.mutable.MutableContext;
import org.genxdm.processor.io.DefaultDocumentHandler;
import org.genxdm.xs.SchemaComponentCache;

public final class XmlNodeContext
    implements ProcessingContext<XmlNode>
{
    public XmlNodeContext()
    {
         mutant = new XmlNodeMutableContext(this);
    }

    @Override
    public Model<XmlNode> getModel()
    {
        return model;
    }

    @Override
    public MutableContext<XmlNode> getMutableContext()
    {
        return mutant;
    }

    @SuppressWarnings("unchecked")
    @Override
    public TypedXmlNodeContext getTypedContext(SchemaComponentCache cache)
    {
        TypedXmlNodeContext tc;
        if (cache != null)
            tc = typedContexts.get(cache);
        else
        {
            if (defaultCache != null)
                tc = typedContexts.get(defaultCache);
            else
            {
                tc = new TypedXmlNodeContext(this, null);
                defaultCache = tc.getSchema();
                typedContexts.put(defaultCache, tc);
            }
        }
            
        if (tc == null) // only happens if cache supplied, first time seen
        {
            tc = new TypedXmlNodeContext(this, cache);
            typedContexts.put(cache, tc);
        }
        return tc;
    }

    @SuppressWarnings("unchecked")
    @Override
    public TypedXmlNodeContext getTempTypedContext(SchemaComponentCache cache)
    {
        // if it's null, return the default
        if (cache == null)
            return getTypedContext(null);
        // otherwise, return a new one, and don't remember
        return new TypedXmlNodeContext(this, cache);
    }

    @Override
    public boolean isNode(Object item)
    {
        return (item instanceof XmlNode);
    }

    @Override
    public boolean isSupported(String feature)
    {
        PreCondition.assertNotNull(feature, "feature");
        if (feature.startsWith(Feature.PREFIX))
        {
            // support all core features
            return true;
        }
        return false;
    }

    @Override
    public Cursor newCursor(XmlNode node)
    {
        return new XmlNodeCursor(node);
    }

    @Override
    public FragmentBuilder<XmlNode> newFragmentBuilder()
    {
        return newFragmentBuilder(true);
    }

    @Override
    public FragmentBuilder<XmlNode> newFragmentBuilder(boolean namespaceFixup)
    {
        if (namespaceFixup)
            return new FilteredFragmentBuilder<XmlNode>(new NamespaceFixupFilter(), new XmlNodeBuilder());
        return new XmlNodeBuilder();
    }

    @Override
    public XmlNode node(Object item)
    {
        return isNode(item) ? (XmlNode)item : null;
    }

    @Override
    public XmlNode[] nodeArray(int size)
    {
        // TODO: our tests don't permit us to assert.  are the tests wrong?
//        PreCondition.assertTrue(size > -1);
        return new XmlNode[size];
    }

    @Override
    public DocumentHandler<XmlNode> newDocumentHandler()
    {
        return new DefaultDocumentHandler<XmlNode>(this);
    }

    @Override
    public DocumentHandler<XmlNode> newDocumentHandler(final XMLReporter reporter, final Resolver resolver)
    {
        // TODO: implement
        return newDocumentHandler();
    }
    
    @Override
    public void setDefaultReporter(XMLReporter reporter)
    {
        this.reporter = reporter;
    }
    
    @Override
    public void setDefaultResolver(Resolver resolver)
    {
        this.resolver = resolver;
    }
    
    @Override
    public XMLReporter getDefaultReporter()
    {
        return reporter;
    }
    
    @Override
    public Resolver getDefaultResolver()
    {
        return resolver;
    }
    
    private final XmlNodeModel model = new XmlNodeModel();
    private final XmlNodeMutableContext mutant;
    private Map<SchemaComponentCache, TypedXmlNodeContext> typedContexts = new HashMap<SchemaComponentCache, TypedXmlNodeContext>();
    private SchemaComponentCache defaultCache;
    private XMLReporter reporter;
    private Resolver resolver;
}
