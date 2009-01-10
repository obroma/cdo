/***************************************************************************
 * Copyright (c) 2004 - 2009 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    Eike Stepper - initial API and implementation
 *    Simon McDuff - http://bugs.eclipse.org/213402
 **************************************************************************/
package org.eclipse.emf.cdo.eresource;

import org.eclipse.emf.cdo.view.CDOViewSet;

import org.eclipse.emf.ecore.resource.Resource;

/**
 * @author Eike Stepper
 */
public interface CDOResourceFactory extends Resource.Factory
{
  /**
   * @since 2.0
   */
  public CDOViewSet getViewSet();

  /**
   * @since 2.0
   */
  public void setViewSet(CDOViewSet viewSet);
}
