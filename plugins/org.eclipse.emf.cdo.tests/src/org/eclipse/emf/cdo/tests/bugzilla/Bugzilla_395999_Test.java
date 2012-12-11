/*
 * Copyright (c) 2004 - 2012 Esteban Dugueperoux and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Esteban Dugueperoux - initial API and implementation
 */
package org.eclipse.emf.cdo.tests.bugzilla;

import org.eclipse.emf.cdo.eresource.CDOResourceFactory;
import org.eclipse.emf.cdo.net4j.CDONet4jUtil;
import org.eclipse.emf.cdo.tests.AbstractCDOTest;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

import java.util.Collections;

/**
 * ResourceSet.getResource(URI,true) fails when called several times. See bug 395999.
 *
 * @author Esteban Dugueperoux
 */
public class Bugzilla_395999_Test extends AbstractCDOTest
{
  public void testTwiceGetCDOResourceOnResourceSetImpl() throws Exception
  {
    Resource.Factory.Registry registry = Resource.Factory.Registry.INSTANCE;
    registry.getProtocolToFactoryMap().put(CDONet4jUtil.PROTOCOL_TCP, CDOResourceFactory.INSTANCE);

    try
    {
      URI uri = URI.createURI(getURIPrefix() + "/" + getRepository().getName() + getResourcePath("/res1")
          + "?transactional=true");

      ResourceSet resourceSet = new ResourceSetImpl();
      Resource resource = resourceSet.createResource(uri);
      resource.save(Collections.emptyMap());

      loadTwiceAndSaveResource(uri);
    }
    finally
    {
      registry.getProtocolToFactoryMap().remove(CDONet4jUtil.PROTOCOL_TCP);
    }
  }

  public void testTwiceGetXMIResourceOnResourceSetImpl() throws Exception
  {
    Resource.Factory.Registry registry = Resource.Factory.Registry.INSTANCE;
    registry.getExtensionToFactoryMap().put("model1", new XMIResourceFactoryImpl());

    try
    {
      ResourceSet resourceSet = new ResourceSetImpl();

      URI uri = URI.createFileURI(createTempFile("resource", ".model1").getCanonicalPath());
      Resource resource = resourceSet.createResource(uri);
      resource.save(Collections.emptyMap());

      loadTwiceAndSaveResource(uri);
    }
    finally
    {
      registry.getExtensionToFactoryMap().remove("model1");
    }
  }

  private void loadTwiceAndSaveResource(URI resourceURI) throws Exception
  {
    ResourceSet resourceSet = new ResourceSetImpl();
    Resource resource = resourceSet.getResource(resourceURI, true);

    assertEquals("The ResourceSetImpl should returns the same Resource as in the first call", resource,
        resourceSet.getResource(resourceURI, true));

    resource.save(Collections.emptyMap());
  }
}
