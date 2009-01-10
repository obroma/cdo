/***************************************************************************
 * Copyright (c) 2004 - 2009 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    Simon McDuff - initial API and implementation
 **************************************************************************/
package org.eclipse.emf.cdo.tests.bugzilla;

import org.eclipse.emf.cdo.eresource.CDOResource;
import org.eclipse.emf.cdo.session.CDOSession;
import org.eclipse.emf.cdo.tests.AbstractCDOTest;
import org.eclipse.emf.cdo.transaction.CDOTransaction;

/**
 * : Multiple resources creation bug
 * <p>
 * See https://bugs.eclipse.org/258278
 * 
 * @author Simon McDuff
 */
public class Bugzilla_258278_Test extends AbstractCDOTest
{
  public void testBugzilla_258278() throws Exception
  {
    {
      CDOSession session = openModel1Session();
      CDOTransaction transaction = session.openTransaction();
      transaction.createResource("/root/folder1/resource1");
      transaction.createResource("/root/folder1/resource2");
      transaction.commit();
      session.close();
    }

    CDOSession session = openModel1Session();

    CDOTransaction transaction = session.openTransaction();
    CDOResource resource1 = transaction.getResource("/root/folder1/resource1");
    assertNotNull(resource1);
    CDOResource resource2 = transaction.getResource("/root/folder1/resource2");
    assertNotNull(resource2);
  }
}
