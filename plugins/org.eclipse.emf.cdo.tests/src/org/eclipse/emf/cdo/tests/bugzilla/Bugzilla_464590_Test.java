/*
 * Copyright (c) 2015 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Esteban Dugueperoux - initial API and implementation
 */
package org.eclipse.emf.cdo.tests.bugzilla;

import org.eclipse.emf.cdo.tests.AbstractCDOTest;
import org.eclipse.emf.cdo.tests.model6.ContainmentObject;
import org.eclipse.emf.cdo.tests.model6.ReferenceObject;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.ECrossReferenceAdapter;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

import org.junit.Assert;

import java.util.Collections;

/**
 * Bug 464590 about {@link EObject#eIsSet(org.eclipse.emf.ecore.EStructuralFeature)} which should not resolve EMF proxy in {@link XMIResource}.
 *
 * @author Esteban Dugueperoux
 */
public class Bugzilla_464590_Test extends AbstractCDOTest
{
  /**
   * Test {@link EObject#eIsSet(org.eclipse.emf.ecore.EStructuralFeature)} with EMF proxies in XMIResource.
   */
  public void testEObjectEIsSetWithXMIResource() throws Exception
  {
    Resource.Factory.Registry registry = Resource.Factory.Registry.INSTANCE;
    registry.getExtensionToFactoryMap().put("model6", new XMIResourceFactoryImpl());
    ResourceSet resourceSet = new ResourceSetImpl();
    resourceSet.eAdapters().add(new NonResolvingECrossReferenceAdapter());
    URI localFragmentResourceURI = URI.createFileURI(createTempFile("fragment", ".model6").getCanonicalPath());
    URI localMainResourceURI = URI.createFileURI(createTempFile("main", ".model6").getCanonicalPath());
    Resource localFragmentResource = resourceSet.createResource(localFragmentResourceURI);
    Resource localMainResource = resourceSet.createResource(localMainResourceURI);

    ContainmentObject mainContainmentObject = getModel6Factory().createContainmentObject();
    ContainmentObject rootContainmentObject = getModel6Factory().createContainmentObject();
    mainContainmentObject.getContainmentList().add(rootContainmentObject);
    ReferenceObject childReferenceObject = getModel6Factory().createReferenceObject();
    rootContainmentObject.getContainmentList().add(childReferenceObject);
    childReferenceObject.setReferenceOptional(rootContainmentObject);

    localMainResource.getContents().add(mainContainmentObject);
    localFragmentResource.getContents().add(rootContainmentObject);
    localMainResource.save(Collections.emptyMap());
    localFragmentResource.save(Collections.emptyMap());

    // Test
    Assert.assertFalse(rootContainmentObject.eIsProxy());
    Assert.assertFalse(childReferenceObject.eIsProxy());

    localFragmentResource.unload();

    Assert.assertFalse(localFragmentResource.isLoaded());
    Assert.assertTrue(rootContainmentObject.eIsProxy());
    Assert.assertTrue(childReferenceObject.eIsProxy());

  }

  private static class NonResolvingECrossReferenceAdapter extends ECrossReferenceAdapter
  {

    @Override
    protected boolean resolve()
    {
      return false;
    }
  }
}
