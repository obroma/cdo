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
package org.eclipse.emf.cdo.internal.common.id;

import org.eclipse.emf.cdo.common.id.CDOIDExternal;
import org.eclipse.emf.cdo.spi.common.id.AbstractCDOID;

import org.eclipse.net4j.util.ObjectUtil;
import org.eclipse.net4j.util.io.ExtendedDataInput;
import org.eclipse.net4j.util.io.ExtendedDataOutput;

import java.io.IOException;

/**
 * @author Simon McDuff
 */
public class CDOIDExternalImpl extends AbstractCDOID implements CDOIDExternal
{
  private static final long serialVersionUID = 1L;

  private String uri;

  public CDOIDExternalImpl(String uri)
  {
    this.uri = uri;
  }

  public String getURI()
  {
    return uri;
  }

  public Type getType()
  {
    return Type.EXTERNAL_OBJECT;
  }

  @Override
  public String toString()
  {
    return "oid" + toURIFragment();
  }

  @Override
  public void read(String fragmentPart)
  {
    uri = fragmentPart;
  }

  @Override
  public void read(ExtendedDataInput in) throws IOException
  {
    uri = in.readString();
  }

  @Override
  public void write(ExtendedDataOutput out) throws IOException
  {
    out.writeString(uri);
  }

  public String toURIFragment()
  {
    return uri;
  }

  @Override
  public boolean equals(Object obj)
  {
    if (obj == this)
    {
      return true;
    }
    // Could CDOIDExternalTempImpl and CDOIDExternalImpl have the same uri. We don't want to mixed them.
    if (obj != null && obj.getClass() == getClass())
    {
      CDOIDExternal that = (CDOIDExternal)obj;
      return ObjectUtil.equals(getURI(), that.getURI());
    }

    return false;
  }

  @Override
  public int hashCode()
  {
    return getClass().hashCode() ^ uri.hashCode();
  }

}
