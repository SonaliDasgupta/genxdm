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
package org.genxdm.processor.xpath.v10.tests;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.namespace.QName;

import junit.framework.TestCase;

import org.gxml.bridgekit.ProcessingContextFactory;
import org.genxdm.xpath.v10.XPathCompiler;
import org.genxdm.xpath.v10.XPathToolkit;
import org.genxdm.xpath.v10.XPathToolkitFactory;
import org.genxdm.xpath.v10.expressions.BooleanExpr;
import org.genxdm.xpath.v10.expressions.ExprContextDynamic;
import org.genxdm.xpath.v10.expressions.ExprContextDynamicArgs;
import org.genxdm.xpath.v10.expressions.ExprContextStatic;
import org.genxdm.xpath.v10.expressions.ExprContextStaticArgs;
import org.genxdm.xpath.v10.expressions.ExprException;
import org.genxdm.xpath.v10.expressions.ExprParseException;
import org.genxdm.xpath.v10.expressions.NodeSetExpr;
import org.genxdm.xpath.v10.expressions.NumberExpr;
import org.genxdm.xpath.v10.expressions.StringExpr;
import org.genxdm.xpath.v10.iterators.NodeIterator;
import org.genxdm.xpath.v10.variants.BooleanVariant;
import org.genxdm.xpath.v10.variants.NumberVariant;
import org.genxdm.xpath.v10.variants.StringVariant;
import org.genxdm.Feature;
import org.genxdm.Resolver;
import org.genxdm.base.Model;
import org.genxdm.base.ProcessingContext;
import org.genxdm.base.io.FragmentBuilder;
import org.genxdm.processor.xpath.v10.XPathToolkitFactoryImpl;

public abstract class XPathTestBase<N> 
    extends TestCase 
    implements ProcessingContextFactory<N>
{
        public Resolver getResolver()
        {
                // TODO Auto-generated method stub
                throw new AssertionError();
        }

        public void testErrorHandling() throws Exception
        {
                final XPathToolkitFactory factory = new XPathToolkitFactoryImpl();

                final XPathToolkit tools = factory.newXPathToolkit();

                final XPathCompiler compiler = tools.newXPathCompiler();

                final ExprContextStaticArgs sargs = tools.newExprContextStaticArgs();

                final ExprContextStatic statEnv = sargs.build();

                try
                {
                        compiler.compileStringExpr("1+", statEnv);

                        fail();
                }
                catch (final ExprParseException e)
                {

                }

                try
                {
                        compiler.compileStringExpr("(1", statEnv);

                        fail();
                }
                catch (final ExprParseException e)
                {

                }
        }

        public void testBooleanAPI() throws Exception
        {
                final ProcessingContext<N> pcx = newProcessingContext();
                final XPathToolkitFactory factory = new XPathToolkitFactoryImpl();

                final XPathToolkit tools = factory.newXPathToolkit();

                final XPathCompiler compiler = tools.newXPathCompiler();

                final ExprContextStaticArgs sargs = tools.newExprContextStaticArgs();

                final N contextNode = null;

                sargs.declareVariable(new QName("http://www.example.com", "x"));
                sargs.declareNamespace("p", "http://www.example.com");

                final ExprContextStatic statEnv = sargs.build();

                final StringExpr stringExpr = compiler.compileStringExpr("concat('Hello',', ',$p:x,'!')", statEnv);

                final ExprContextDynamicArgs<N> dargs = tools.newExprContextDynamicArgs();

                dargs.bindVariableValue(new QName("http://www.example.com", "x"), new BooleanVariant<N>(true));

                final ExprContextDynamic<N> dynEnv = dargs.build();

                final String s = stringExpr.stringFunction(pcx.getModel(), contextNode, dynEnv);

                assertEquals("Hello, true!", s);
        }

        public void testBooleanVariable() throws Exception
        {
                final ProcessingContext<N> pcx = newProcessingContext();
                final XPathToolkitFactory factory = new XPathToolkitFactoryImpl();

                final XPathToolkit tools = factory.newXPathToolkit();

                final XPathCompiler compiler = tools.newXPathCompiler();

                final ExprContextStaticArgs sargs = tools.newExprContextStaticArgs();

                sargs.declareVariable(new QName("http://www.example.com", "x"));
                sargs.declareNamespace("p", "http://www.example.com");

                final ExprContextStatic statEnv = sargs.build();

                final BooleanExpr compiledExpr = compiler.compileBooleanExpr("$p:x", statEnv);

                final ExprContextDynamicArgs<N> dargs = tools.newExprContextDynamicArgs();

                dargs.bindVariableValue(new QName("http://www.example.com", "x"), new BooleanVariant<N>(true));

                final ExprContextDynamic<N> dynEnv = dargs.build();

                final boolean value = compiledExpr.booleanFunction(pcx.getModel(), null, dynEnv);

                assertEquals(true, value);
        }

        public void testConcatFunction() throws Exception
        {
                final ProcessingContext<N> pcx = newProcessingContext();
                final XPathToolkitFactory factory = new XPathToolkitFactoryImpl();

                final XPathToolkit tools = factory.newXPathToolkit();

                final XPathCompiler compiler = tools.newXPathCompiler();

                final ExprContextStaticArgs sargs = tools.newExprContextStaticArgs();

                sargs.declareVariable(new QName("http://www.example.com", "x"));
                sargs.declareNamespace("p", "http://www.example.com");

                final ExprContextStatic statEnv = sargs.build();

                final StringExpr stringExpr = compiler.compileStringExpr("concat('Hello',', ',$p:x,'!')", statEnv);

                final ExprContextDynamicArgs<N> dargs = tools.newExprContextDynamicArgs();

                dargs.bindVariableValue(new QName("http://www.example.com", "x"), new StringVariant<N>("World"));

                final ExprContextDynamic<N> dynEnv = dargs.build();

                final String s = stringExpr.stringFunction(pcx.getModel(), null, dynEnv);

                assertEquals("Hello, World!", s);
        }

        public void testDoubleAPI() throws Exception
        {
                final ProcessingContext<N> pcx = newProcessingContext();
                final XPathToolkitFactory factory = new XPathToolkitFactoryImpl();

                final XPathToolkit tools = factory.newXPathToolkit();

                final XPathCompiler compiler = tools.newXPathCompiler();

                final ExprContextStaticArgs sargs = tools.newExprContextStaticArgs();

                sargs.declareVariable(new QName("http://www.example.com", "x"));
                sargs.declareNamespace("p", "http://www.example.com");

                final ExprContextStatic statEnv = sargs.build();

                final StringExpr stringExpr = compiler.compileStringExpr("concat('Hello',', ',$p:x,'!')", statEnv);

                final ExprContextDynamicArgs<N> dargs = tools.newExprContextDynamicArgs();

                dargs.bindVariableValue(new QName("http://www.example.com", "x"), new NumberVariant<N>(23));

                final ExprContextDynamic<N> dynEnv = dargs.build();

                final String s = stringExpr.stringFunction(pcx.getModel(), null, dynEnv);

                assertEquals("Hello, 23!", s);
        }

        public void testOperators() throws Exception
        {
                final ProcessingContext<N> pcx = newProcessingContext();

                final XPathToolkitFactory factory = new XPathToolkitFactoryImpl();

                final XPathToolkit tools = factory.newXPathToolkit();

                final XPathCompiler compiler = tools.newXPathCompiler();

                final ExprContextStaticArgs sargs = tools.newExprContextStaticArgs();

                final ExprContextStatic statEnv = sargs.build();

                final NumberExpr expr = compiler.compileNumberExpr("1 + 2 * 3", statEnv);

                final ExprContextDynamicArgs<N> dargs = tools.newExprContextDynamicArgs();

                final ExprContextDynamic<N> dynEnv = dargs.build();

                final double result = expr.numberFunction(pcx.getModel(), null, dynEnv);
                assertEquals(7.0d, result);
        }

        public void testLastFunction()
        {
                final ProcessingContext<N> pcx = newProcessingContext();
                final XPathToolkitFactory factory = new XPathToolkitFactoryImpl();

                final XPathToolkit tools = factory.newXPathToolkit();

                final XPathCompiler compiler = tools.newXPathCompiler();

                final ExprContextStaticArgs sargs = tools.newExprContextStaticArgs();

                final ExprContextStatic statEnv = sargs.build();

                final NumberExpr numberExpr;
                try
                {
                        numberExpr = compiler.compileNumberExpr("last()", statEnv);
                }
                catch (final ExprParseException e)
                {
                        e.printStackTrace();
                        fail(e.getMessage());
                        return;
                }

                final ExprContextDynamicArgs<N> dargs = tools.newExprContextDynamicArgs();

                dargs.setContextSize(7);

                final ExprContextDynamic<N> dynEnv = dargs.build();

                try
                {
                        final double x = numberExpr.numberFunction(pcx.getModel(), null, dynEnv);

                        assertEquals(7.0, x);
                }
                catch (final ExprException e)
                {
                        fail(e.getMessage());
                        return;
                }
        }

        public void testNamespaceAxis() throws IOException
        {
                final ProcessingContext<N> pcx = newProcessingContext();
                final Model<N> model = pcx.getModel();
                final FragmentBuilder<N> builder = pcx.newFragmentBuilder();

                builder.startDocument(null, null);
                try
                {
                        builder.startElement("http://a.example", "foo", "n0");
                        try
                        {
                                builder.namespace("n0", "http://a.example");
                                builder.startElement("http://b.example", "bar", "n1");
                                try
                                {
                                        builder.namespace("n1", "http://b.example");
                                        builder.text("content");
                                }
                                finally
                                {
                                        builder.endElement();
                                }
                        }
                        finally
                        {
                                builder.endElement();
                        }
                }
                finally
                {
                        builder.endDocument();
                }

                final N documentNode = builder.getNodes().get(0);
                final N fooElement = model.getFirstChildElement(documentNode);
                final N barElement = model.getFirstChildElement(fooElement);

                // final GxSerializerFactory<N> sf = pcx.newSerializerFactory();

                // sf.setIndent(true);

                // final StringWriter sw = new StringWriter();

                // final GxSerializer<N> serializer = sf.newSerializer(sw);

                // serializer.write(documentNode);

                // System.out.println(sw);

                final XPathToolkitFactory factory = new XPathToolkitFactoryImpl();

                final XPathToolkit tools = factory.newXPathToolkit();

                final XPathCompiler compiler = tools.newXPathCompiler();

                final ExprContextStaticArgs sargs = tools.newExprContextStaticArgs();

                sargs.declareNamespace("nsb", "http://b.example");

                final ExprContextStatic statEnv = sargs.build();

                final NodeSetExpr expr;
                try
                {
                        expr = compiler.compileNodeSetExpr("namespace::*", statEnv);
                }
                catch (final ExprParseException e)
                {
                        e.printStackTrace();
                        fail(e.getMessage());
                        return;
                }

                final ExprContextDynamicArgs<N> dargs = tools.newExprContextDynamicArgs();

                dargs.setContextPosition(5);

                final ExprContextDynamic<N> dynEnv = dargs.build();

                if (pcx.isSupported(Feature.NAMESPACE_AXIS))
                {
                        try
                        {
                                final NodeIterator<N> x = expr.nodeIterator(model, barElement, dynEnv);

                                final HashMap<String, String> mappings = new HashMap<String, String>();
                                final ArrayList<N> nodes = new ArrayList<N>();

                                N namespace = x.next();
                                while (null != namespace)
                                {
                                        final String prefix = model.getLocalName(namespace);
                                        final String uri = model.getStringValue(namespace);
                                        // TODO: This should be reinstated...
                                        // assertTrue("The element must be the parent of the namespace node.", model.isSameNode(barElement,
                                        // model.getParent(namespace)));
                                        assertNotNull("prefix", prefix);
                                        assertNotNull("uri", uri);

                                        mappings.put(prefix, uri);
                                        nodes.add(namespace);
                                        namespace = x.next();
                                }
                                assertEquals(3, mappings.size());
                                assertEquals(mappings.size(), nodes.size());
                                assertNotNull(mappings.get("n0"));
                                assertNotNull(mappings.get("n1"));
                                assertNotNull(mappings.get("xml"));
                        }
                        catch (final ExprException e)
                        {
                                fail(e.getMessage());
                                return;
                        }
                }
        }

        public void testPositionFunction()
        {
                final ProcessingContext<N> pcx = newProcessingContext();
                final XPathToolkitFactory factory = new XPathToolkitFactoryImpl();

                final XPathToolkit tools = factory.newXPathToolkit();

                final XPathCompiler compiler = tools.newXPathCompiler();

                final ExprContextStaticArgs sargs = tools.newExprContextStaticArgs();

                final N contextNode = null;

                final ExprContextStatic statEnv = sargs.build();

                final NumberExpr numberExpr;
                try
                {
                        numberExpr = compiler.compileNumberExpr("position()", statEnv);
                }
                catch (final ExprParseException e)
                {
                        e.printStackTrace();
                        fail(e.getMessage());
                        return;
                }

                final ExprContextDynamicArgs<N> dargs = tools.newExprContextDynamicArgs();

                dargs.setContextPosition(5);
                dargs.setContextSize(10);

                final ExprContextDynamic<N> dynEnv = dargs.build();

                try
                {
                        final double x = numberExpr.numberFunction(pcx.getModel(), contextNode, dynEnv);

                        assertEquals(5.0, x);
                }
                catch (final ExprException e)
                {
                        e.printStackTrace();
                        fail(e.getMessage());
                        return;
                }
        }

        public void testStringAPI()
        {
                final ProcessingContext<N> pcx = newProcessingContext();
                final XPathToolkitFactory factory = new XPathToolkitFactoryImpl();

                final N contextNode = null;

                final XPathToolkit tools = factory.newXPathToolkit();

                final XPathCompiler compiler = tools.newXPathCompiler();

                final ExprContextStaticArgs sargs = tools.newExprContextStaticArgs();

                sargs.declareVariable(new QName("http://www.example.com", "x"));
                sargs.declareNamespace("p", "http://www.example.com");

                final ExprContextStatic statEnv = sargs.build();

                final StringExpr stringExpr;
                try
                {
                        stringExpr = compiler.compileStringExpr("concat('Hello',', ',$p:x,'!')", statEnv);
                }
                catch (final ExprParseException e)
                {
                        e.printStackTrace();
                        fail(e.getMessage());
                        return;
                }

                final ExprContextDynamicArgs<N> dargs = tools.newExprContextDynamicArgs();

                dargs.bindVariableValue(new QName("http://www.example.com", "x"), new StringVariant<N>("World"));

                final ExprContextDynamic<N> dynEnv = dargs.build();

                try
                {
                        final String s = stringExpr.stringFunction(pcx.getModel(), contextNode, dynEnv);

                        assertEquals("Hello, World!", s);
                }
                catch (final ExprException e)
                {
                        fail(e.getMessage());
                        return;
                }
        }
}
