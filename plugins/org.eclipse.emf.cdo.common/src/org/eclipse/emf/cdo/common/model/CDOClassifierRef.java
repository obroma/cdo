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
package org.eclipse.emf.cdo.common.model;

import org.eclipse.emf.cdo.common.TODO;
import org.eclipse.emf.cdo.common.io.CDODataInput;
import org.eclipse.emf.cdo.common.io.CDODataOutput;

import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EPackage;

import java.io.IOException;
import java.text.MessageFormat;

/**
 * TODO Optimize transfer of EClassRef instances
 * 
 * @author Eike Stepper
 */
public final class CDOClassifierRef
{
  private String packageURI;

  private int classifierID;

  public CDOClassifierRef()
  {
  }

  public CDOClassifierRef(EClassifier classifier)
  {
    this(classifier.getEPackage().getNsURI(), classifier.getClassifierID());
  }

  public CDOClassifierRef(String packageURI, int classifierID)
  {
    this.packageURI = packageURI;
    this.classifierID = classifierID;
  }

  public CDOClassifierRef(CDODataInput in) throws IOException
  {
    packageURI = in.readEPackageURI();
    classifierID = in.readInt();
  }

  public void write(CDODataOutput out) throws IOException
  {
    out.writeEPackageURI(packageURI);
    out.writeInt(classifierID);
  }

  public String getPackageURI()
  {
    return packageURI;
  }

  public int getClassifierID()
  {
    return classifierID;
  }

  public EClassifier resolve(EPackage.Registry packageManager)
  {
    EPackage cdoPackage = packageManager.getEPackage(packageURI);
    if (cdoPackage != null)
    {
      return TODO.getClassifier(cdoPackage, classifierID);
    }

    return null;
  }

  @Override
  public String toString()
  {
    return MessageFormat.format("EClassRef({0}, {1})", packageURI, classifierID);
  }
}
