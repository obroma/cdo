/***************************************************************************
 * Copyright (c) 2004 - 2009 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    Eike Stepper - initial API and implementation
 **************************************************************************/
package org.eclipse.emf.cdo.examples;

import org.eclipse.emf.cdo.eresource.CDOResource;
import org.eclipse.emf.cdo.session.CDOSession;
import org.eclipse.emf.cdo.session.CDOSessionConfiguration;
import org.eclipse.emf.cdo.tests.model1.Model1Factory;
import org.eclipse.emf.cdo.tests.model1.Model1Package;
import org.eclipse.emf.cdo.transaction.CDOTransaction;
import org.eclipse.emf.cdo.util.CDOUtil;

import org.eclipse.net4j.Net4jUtil;
import org.eclipse.net4j.connector.IConnector;
import org.eclipse.net4j.tcp.TCPUtil;
import org.eclipse.net4j.util.container.ContainerUtil;
import org.eclipse.net4j.util.container.IManagedContainer;
import org.eclipse.net4j.util.om.OMPlatform;
import org.eclipse.net4j.util.om.log.PrintLogHandler;
import org.eclipse.net4j.util.om.trace.PrintTraceHandler;

import org.eclipse.emf.ecore.EObject;

/**
 * @author Eike Stepper
 */
public class StandaloneContainerExample
{
  public static void main(String[] args)
  {
    // Enable logging and tracing
    OMPlatform.INSTANCE.setDebugging(true);
    OMPlatform.INSTANCE.addLogHandler(PrintLogHandler.CONSOLE);
    OMPlatform.INSTANCE.addTraceHandler(PrintTraceHandler.CONSOLE);

    // Prepare container
    IManagedContainer container = ContainerUtil.createContainer();
    Net4jUtil.prepareContainer(container); // Register Net4j factories
    TCPUtil.prepareContainer(container); // Register TCP factories
    CDOUtil.prepareContainer(container); // Register CDO factories
    container.activate();

    // Create connector
    IConnector connector = TCPUtil.getConnector(container, "localhost:2036");

    // Create configuration
    CDOSessionConfiguration configuration = CDOUtil.createSessionConfiguration();
    configuration.setConnector(connector);
    configuration.setRepositoryName("repo1");

    // Open session
    CDOSession session = configuration.openSession();
    session.getPackageRegistry().putEPackage(Model1Package.eINSTANCE);

    // Open transaction
    CDOTransaction transaction = session.openTransaction();

    // Get or create resource
    CDOResource resource = transaction.getOrCreateResource("/path/to/my/resource");

    // Work with the resource and commit the transaction
    EObject object = Model1Factory.eINSTANCE.createCompany();
    resource.getContents().add(object);
    transaction.commit();

    // Cleanup
    session.close();
    connector.close();
    container.deactivate();
  }
}
