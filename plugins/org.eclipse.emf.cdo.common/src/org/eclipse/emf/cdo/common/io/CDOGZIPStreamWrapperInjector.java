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
package org.eclipse.emf.cdo.common.io;

import org.eclipse.emf.cdo.common.protocol.CDOProtocolConstants;

import org.eclipse.net4j.signal.wrapping.GZIPStreamWrapperInjector;

/**
 * @author Eike Stepper
 * @since 2.0
 */
public class CDOGZIPStreamWrapperInjector extends GZIPStreamWrapperInjector
{
  public CDOGZIPStreamWrapperInjector()
  {
    super(CDOProtocolConstants.PROTOCOL_NAME);
  }
}
