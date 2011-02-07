package org.genxdm.bridge.axiom.tests;

import org.apache.axiom.om.impl.llom.factory.OMLinkedListImplFactory;
import org.genxdm.base.ProcessingContext;
import org.genxdm.bridge.axiom.AxiomProcessingContext;
import org.genxdm.bridgetest.nodes.BookmarkBase;

public class AxiomBookmarkTest
    extends BookmarkBase<Object>
{

    @Override
    public ProcessingContext<Object> newProcessingContext() {
        return new AxiomProcessingContext(new OMLinkedListImplFactory());
    }
    

}