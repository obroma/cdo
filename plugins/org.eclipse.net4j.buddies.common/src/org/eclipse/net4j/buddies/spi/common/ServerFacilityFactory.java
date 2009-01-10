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
package org.eclipse.net4j.buddies.spi.common;

import org.eclipse.net4j.util.factory.Factory;

/**
 * @author Eike Stepper
 */
public abstract class ServerFacilityFactory extends Factory
{
  public static final String PRODUCT_GROUP = "org.eclipse.net4j.buddies.serverFacilities";

  public ServerFacilityFactory(String type)
  {
    super(PRODUCT_GROUP, type);
  }
}
