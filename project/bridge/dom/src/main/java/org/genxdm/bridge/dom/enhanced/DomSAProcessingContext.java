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
package org.genxdm.bridge.dom.enhanced;

import javax.xml.namespace.QName;

import org.genxdm.ProcessingContext;
import org.genxdm.bridge.dom.DomProcessingContext;
import org.genxdm.bridgekit.atoms.XmlAtom;
import org.genxdm.bridgekit.atoms.XmlAtomBridge;
import org.genxdm.bridgekit.tree.CursorOnTypedModel;
import org.genxdm.bridgekit.xs.MetaBridgeOnSchemaTypeBridgeAdapter;
import org.genxdm.bridgekit.xs.SchemaTypeBridgeFactory;
import org.genxdm.exceptions.PreCondition;
import org.genxdm.names.NameSource;
import org.genxdm.typed.TypedContext;
import org.genxdm.typed.TypedCursor;
import org.genxdm.typed.TypedModel;
import org.genxdm.typed.io.SequenceBuilder;
import org.genxdm.typed.types.AtomBridge;
import org.genxdm.typed.types.MetaBridge;
import org.genxdm.typed.variant.VariantBridge;
import org.genxdm.xs.SchemaTypeBridge;
import org.genxdm.xs.components.AttributeDefinition;
import org.genxdm.xs.components.AttributeGroupDefinition;
import org.genxdm.xs.components.ComponentBag;
import org.genxdm.xs.components.ElementDefinition;
import org.genxdm.xs.components.ModelGroup;
import org.genxdm.xs.components.NotationDefinition;
import org.genxdm.xs.constraints.IdentityConstraint;
import org.genxdm.xs.types.AtomicType;
import org.genxdm.xs.types.AtomicUrType;
import org.genxdm.xs.types.ComplexType;
import org.genxdm.xs.types.ComplexUrType;
import org.genxdm.xs.types.NativeType;
import org.genxdm.xs.types.SimpleType;
import org.genxdm.xs.types.SimpleUrType;
import org.genxdm.xs.types.Type;
import org.w3c.dom.Node;

public final class DomSAProcessingContext 
    implements TypedContext<Node, XmlAtom>
{
	public DomSAProcessingContext(DomProcessingContext parent)
	{
		this.atomBridge = new XmlAtomBridge(this, new NameSource());
		this.m_cache = new SchemaTypeBridgeFactory<XmlAtom>(atomBridge).newMetaBridge();
		this.m_metaBridge = new MetaBridgeOnSchemaTypeBridgeAdapter<XmlAtom>(m_cache, atomBridge);
		this.m_model = new DomSAModel(this);
		this.parent = PreCondition.assertNotNull(parent);
	}

	public XmlAtom atom(final Object item)
	{
		// Delegate to the atom bridge because we are generic wrt atoms.
		return atomBridge.atom(item);
	}
	
	public void declareAttribute(final AttributeDefinition<XmlAtom> attribute)
	{
		PreCondition.assertArgumentNotNull(attribute, "attribute");
		PreCondition.assertFalse(isLocked(), "isLocked()");
		m_cache.declareAttribute(attribute);
	}
	
	public void declareElement(final ElementDefinition<XmlAtom> element)
	{
		PreCondition.assertArgumentNotNull(element, "element");
		PreCondition.assertFalse(isLocked(), "isLocked()");
		m_cache.declareElement(element);
	}
	
	public void declareNotation(final NotationDefinition<XmlAtom> notation)
	{
		PreCondition.assertArgumentNotNull(notation, "notation");
		PreCondition.assertFalse(isLocked(), "isLocked()");
		m_cache.declareNotation(notation);
	}
	
	public void defineAttributeGroup(final AttributeGroupDefinition<XmlAtom> attributeGroup)
	{
		PreCondition.assertArgumentNotNull(attributeGroup, "attributeGroup");
		PreCondition.assertFalse(isLocked(), "isLocked()");
		m_cache.defineAttributeGroup(attributeGroup);
	}
	
	public void defineComplexType(final ComplexType<XmlAtom> complexType)
	{
		PreCondition.assertArgumentNotNull(complexType, "complexType");
		PreCondition.assertFalse(isLocked(), "isLocked()");
		m_cache.defineComplexType(complexType);
	}
	
	public void defineIdentityConstraint(final IdentityConstraint<XmlAtom> identityConstraint)
	{
		PreCondition.assertArgumentNotNull(identityConstraint, "identityConstraint");
		PreCondition.assertFalse(isLocked(), "isLocked()");
		m_cache.defineIdentityConstraint(identityConstraint);
	}

	public void defineModelGroup(final ModelGroup<XmlAtom> modelGroup)
	{
		PreCondition.assertArgumentNotNull(modelGroup, "modelGroup");
		PreCondition.assertFalse(isLocked(), "isLocked()");
		m_cache.defineModelGroup(modelGroup);
	}

	public void defineSimpleType(final SimpleType<XmlAtom> simpleType)
	{
		PreCondition.assertArgumentNotNull(simpleType, "simpleType");
		PreCondition.assertFalse(isLocked(), "isLocked()");
		m_cache.defineSimpleType(simpleType);
	}
	
	public QName generateUniqueName()
	{
		return m_cache.generateUniqueName();
	}

	public AtomBridge<XmlAtom> getAtomBridge()
	{
		return atomBridge;
	}

	public AtomicType<XmlAtom> getAtomicType(final QName name)
	{
		return m_cache.getAtomicType(name);
	}

	public AtomicType<XmlAtom> getAtomicType(final NativeType name)
	{
		return m_cache.getAtomicType(name);
	}

	public AtomicUrType<XmlAtom> getAtomicUrType()
	{
		return m_cache.getAtomicUrType();
	}

	public AttributeDefinition<XmlAtom> getAttributeDeclaration(final QName attributeName)
	{
		return m_cache.getAttributeDeclaration(attributeName);
	}

	public AttributeGroupDefinition<XmlAtom> getAttributeGroup(final QName name)
	{
		return m_cache.getAttributeGroup(name);
	}

	public Iterable<AttributeGroupDefinition<XmlAtom>> getAttributeGroups()
	{
		return m_cache.getAttributeGroups();
	}

	public Iterable<AttributeDefinition<XmlAtom>> getAttributes()
	{
		return m_cache.getAttributes();
	}

	public ComplexType<XmlAtom> getComplexType(final QName name)
	{
		return m_cache.getComplexType(name);
	}

	public Iterable<ComplexType<XmlAtom>> getComplexTypes()
	{
		return m_cache.getComplexTypes();
	}

	public ComplexUrType<XmlAtom> getComplexUrType()
	{
		return m_cache.getComplexUrType();
	}
	
	public ElementDefinition<XmlAtom> getElementDeclaration(final QName elementName)
	{
		return m_cache.getElementDeclaration(elementName);
	}

	public Iterable<ElementDefinition<XmlAtom>> getElements()
	{
		return m_cache.getElements();
	}

	public IdentityConstraint<XmlAtom> getIdentityConstraint(final QName name)
	{
		return m_cache.getIdentityConstraint(name);
	}

	public Iterable<IdentityConstraint<XmlAtom>> getIdentityConstraints()
	{
		return m_cache.getIdentityConstraints();
	}

	public MetaBridge<XmlAtom> getMetaBridge()
	{
		return m_metaBridge;
	}

	public TypedModel<Node, XmlAtom> getModel()
	{
		return m_model;
	}

	public ModelGroup<XmlAtom> getModelGroup(final QName name)
	{
		return m_cache.getModelGroup(name);
	}

	public Iterable<ModelGroup<XmlAtom>> getModelGroups()
	{
		return m_cache.getModelGroups();
	}
	
	public Iterable<String> getNamespaces()
	{
		return m_cache.getNamespaces();
	}

	public NotationDefinition<XmlAtom> getNotationDeclaration(final QName name)
	{
		return m_cache.getNotationDeclaration(name);
	}
	
	public Iterable<NotationDefinition<XmlAtom>> getNotations()
	{
		return m_cache.getNotations();
	}
	
    public ProcessingContext<Node> getProcessingContext()
	{
	    return parent;
	}

	public SimpleType<XmlAtom> getSimpleType(final QName name)
	{
		return m_cache.getSimpleType(name);
	}

	public SimpleType<XmlAtom> getSimpleType(final NativeType name)
	{
		return m_cache.getSimpleType(name);
	}

	public Iterable<SimpleType<XmlAtom>> getSimpleTypes()
	{
		return m_cache.getSimpleTypes();
	}

	public SimpleUrType<XmlAtom> getSimpleUrType()
	{
		return m_cache.getSimpleUrType();
	}
	
	public Type<XmlAtom> getTypeDefinition(final QName typeName)
	{
		return m_cache.getTypeDefinition(typeName);
	}

	public Type<XmlAtom> getTypeDefinition(final NativeType nativeType)
	{
		return m_cache.getTypeDefinition(nativeType);
	}
	
	public VariantBridge<Node, XmlAtom> getVariantBridge()
	{
	    return new DomValueBridge();
	}

	public boolean hasAttribute(final QName name)
	{
		return m_cache.hasAttribute(name);
	}

	public boolean hasAttributeGroup(final QName name)
	{
		return m_cache.hasAttributeGroup(name);
	}

	public boolean hasComplexType(final QName name)
	{
		return m_cache.hasComplexType(name);
	}

	public boolean hasElement(final QName name)
	{
		return m_cache.hasElement(name);
	}

	public boolean hasIdentityConstraint(final QName name)
	{
		return m_cache.hasIdentityConstraint(name);
	}

	public boolean hasModelGroup(final QName name)
	{
		return m_cache.hasModelGroup(name);
	}

	public boolean hasNotation(final QName name)
	{
		return m_cache.hasNotation(name);
	}

	public boolean hasSimpleType(final QName name)
	{
		return m_cache.hasSimpleType(name);
	}

	public boolean hasType(final QName name)
	{
		return m_cache.hasType(name);
	}

	public boolean isAtom(final Object item)
	{
		// Delegate to the atom bridge because we are generic wrt atoms.
		return atomBridge.isAtom(item);
	}

	public boolean isLocked()
	{
		return locked;
	}

	public void lock()
	{
		locked = true;
	}

	public TypedCursor<Node, XmlAtom> newCursor(final Node node)
	{
	    return new CursorOnTypedModel<Node, XmlAtom>(node, m_model);
	}

	public SequenceBuilder<Node, XmlAtom> newSequenceBuilder()
	{
		return new DomSequenceBuilder<XmlAtom>(parent.getDocumentBuilderFactory(), this);
	}

	public void register(final ComponentBag<XmlAtom> components)
	{
		PreCondition.assertFalse(isLocked(), "isLocked()");
		m_cache.register(components);
	}

	private final DomProcessingContext parent;
    private final XmlAtomBridge atomBridge;
	private final SchemaTypeBridge<XmlAtom> m_cache;
	private final MetaBridge<XmlAtom> m_metaBridge;

	private final DomSAModel m_model;

    private boolean locked;
}