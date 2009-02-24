/**
 * Copyright (c) 2004 - 2009 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.emf.internal.cdo;

import org.eclipse.emf.cdo.common.model.EMFUtil;
import org.eclipse.emf.cdo.util.CDOFactory;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;

/**
 * @author Eike Stepper
 */
public class CDOFactoryImpl extends EFactoryImpl implements CDOFactory
{
  public CDOFactoryImpl(EPackage ePackage)
  {
    this.ePackage = ePackage;
  }

  @Override
  protected EObject basicCreate(EClass eClass)
  {
    return new DynamicCDOObjectImpl(eClass);
  }

  /**
   * @since 2.0
   */
  public static boolean prepareDynamicEPackage(EPackage ePackage)
  {
    if (EMFUtil.isDynamicEPackage(ePackage))
    {
      ePackage.setEFactoryInstance(new CDOFactoryImpl(ePackage));
      return true;
    }

    return false;
  }
}
