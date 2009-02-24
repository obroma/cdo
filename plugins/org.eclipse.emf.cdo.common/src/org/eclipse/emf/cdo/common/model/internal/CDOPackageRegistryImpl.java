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
package org.eclipse.emf.cdo.common.model.internal;

import org.eclipse.emf.cdo.common.id.CDOID;
import org.eclipse.emf.cdo.common.id.CDOIDMetaRange;
import org.eclipse.emf.cdo.common.id.CDOIDTemp;
import org.eclipse.emf.cdo.common.id.CDOIDTempMeta;
import org.eclipse.emf.cdo.common.id.CDOIDUtil;
import org.eclipse.emf.cdo.common.model.CDOPackageAdapter;
import org.eclipse.emf.cdo.common.model.CDOPackageInfo;
import org.eclipse.emf.cdo.common.model.CDOPackageUnit;
import org.eclipse.emf.cdo.common.model.CDOPackageUnitManager;
import org.eclipse.emf.cdo.common.model.EMFUtil;
import org.eclipse.emf.cdo.internal.common.bundle.OM;

import org.eclipse.net4j.util.ReflectUtil.ExcludeFromDump;
import org.eclipse.net4j.util.om.trace.ContextTracer;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.EPackageRegistryImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Eike Stepper
 */
public class CDOPackageRegistryImpl extends EPackageRegistryImpl implements InternalCDOPackageRegistry
{
  private static final long serialVersionUID = 1L;

  private static final ContextTracer TRACER = new ContextTracer(OM.DEBUG, CDOPackageRegistryImpl.class);

  private CDOPackageUnitManager packageUnitManager;

  @ExcludeFromDump
  private transient Map<CDOID, InternalEObject> idToMetaInstanceMap = new HashMap<CDOID, InternalEObject>();

  @ExcludeFromDump
  private transient Map<InternalEObject, CDOID> metaInstanceToIDMap = new HashMap<InternalEObject, CDOID>();

  @ExcludeFromDump
  private transient int lastTempMetaID;

  private transient Object lastTempMetaIDLock = new Object();

  public CDOPackageRegistryImpl()
  {
  }

  public CDOPackageUnitManager getPackageUnitManager()
  {
    return packageUnitManager;
  }

  public void setPackageUnitManager(CDOPackageUnitManager packageUnitManager)
  {
    this.packageUnitManager = packageUnitManager;
  }

  public void addPackageDescriptors(List<CDOPackageInfo> packageInfos)
  {
    // TODO: implement CDOPackageRegistryImpl.addPackageDescriptors(packageInfos)
    throw new UnsupportedOperationException();
  }

  public void putEPackageBasic(EPackage ePackage)
  {
    if (TRACER.isEnabled())
    {
      TRACER.format("Registering package: {0}", ePackage);
    }

    super.put(ePackage.getNsURI(), ePackage);
  }

  public void putEPackage(EPackage ePackage)
  {
    put(ePackage.getNsURI(), ePackage);
  }

  @Override
  public Object put(String nsURI, Object value)
  {
    if (value instanceof EPackage)
    {
      EPackage ePackage = (EPackage)value;
      CDOPackageAdapter adapter = getPackageAdapter(ePackage);
      if (adapter != null)
      {
        return ePackage;
      }

      CDOPackageUnit packageUnit = createPackageUnit(ePackage);
      ((InternalCDOPackageUnitManager)packageUnitManager).addPackageUnit(packageUnit);
      return null;
    }

    return super.put(nsURI, value);
  }

  protected CDOPackageAdapter getPackageAdapter(EPackage ePackage)
  {
    EList<Adapter> adapters = ePackage.eAdapters();
    for (int i = 0, size = adapters.size(); i < size; ++i)
    {
      Adapter adapter = adapters.get(i);
      if (adapter instanceof CDOPackageAdapter)
      {
        CDOPackageAdapter packageAdapter = (CDOPackageAdapter)adapter;
        if (packageAdapter.getPackageRegistry() == this)
        {
          return packageAdapter;
        }
      }
    }

    return null;
  }

  protected CDOPackageUnit createPackageUnit(EPackage topLevelPackage)
  {
    if (EMFUtil.isDynamicEPackage(topLevelPackage))
    {
      return createDynamicPackageUnit(topLevelPackage);
    }

    return createGeneratedPackageUnit(topLevelPackage);
  }

  protected CDOPackageUnit createDynamicPackageUnit(EPackage topLevelPackage)
  {
    return new CDOPackageUnitImpl.Dynamic(this, topLevelPackage);
  }

  protected CDOPackageUnit createGeneratedPackageUnit(EPackage topLevelPackage)
  {
    return new CDOPackageUnitImpl.Generated(this, topLevelPackage);
  }

  public CDOIDMetaRange getTempMetaIDRange(int count)
  {
    CDOIDTemp lowerBound;
    synchronized (lastTempMetaIDLock)
    {
      lowerBound = CDOIDUtil.createTempMeta(lastTempMetaID + 1);
      lastTempMetaID += count;
    }

    return CDOIDUtil.createMetaRange(lowerBound, count);
  }

  public InternalEObject lookupMetaInstance(CDOID id)
  {
    InternalEObject metaInstance = idToMetaInstanceMap.get(id);
    if (metaInstance == null)
    {
      for (CDOPackageUnit packageUnit : packageUnitManager.getPackageUnits())
      {
        for (CDOPackageInfo packageInfo : packageUnit.getPackageInfos())
        {
          CDOIDMetaRange metaIDRange = packageInfo.getMetaIDRange();
          if (metaIDRange != null && metaIDRange.contains(id))
          {
            EPackage ePackage = getEPackage(packageInfo.getPackageURI());
            mapMetaInstances(ePackage);
            metaInstance = idToMetaInstanceMap.get(id);
            break;
          }
        }
      }
    }

    return metaInstance;
  }

  public CDOID lookupMetaInstanceID(InternalEObject metaInstance)
  {
    return metaInstanceToIDMap.get(metaInstance);
  }

  public void mapMetaInstances(EPackage ePackage, CDOIDMetaRange metaIDRange)
  {
    if (metaIDRange.isTemporary())
    {
      throw new IllegalArgumentException("metaIDRange.isTemporary()");
    }

    CDOIDMetaRange range = CDOIDUtil.createMetaRange(metaIDRange.getLowerBound(), 0);
    range = mapMetaInstance((InternalEObject)ePackage, range, idToMetaInstanceMap, metaInstanceToIDMap);
    if (range.size() != metaIDRange.size())
    {
      throw new IllegalStateException("range.size() != metaIDRange.size()");
    }
  }

  public CDOIDMetaRange mapMetaInstances(EPackage ePackage)
  {
    synchronized (lastTempMetaIDLock)
    {
      CDOIDMetaRange range = mapMetaInstances(ePackage, lastTempMetaID + 1, idToMetaInstanceMap, metaInstanceToIDMap);
      lastTempMetaID = ((CDOIDTempMeta)range.getUpperBound()).getIntValue();
      return range;
    }
  }

  public CDOIDMetaRange mapMetaInstances(EPackage ePackage, int firstMetaID,
      Map<CDOID, InternalEObject> idToMetaInstances, Map<InternalEObject, CDOID> metaInstanceToIDs)
  {
    CDOIDTemp lowerBound = CDOIDUtil.createTempMeta(firstMetaID);
    CDOIDMetaRange range = CDOIDUtil.createMetaRange(lowerBound, 0);
    range = mapMetaInstance((InternalEObject)ePackage, range, idToMetaInstances, metaInstanceToIDs);
    return range;
  }

  public CDOIDMetaRange mapMetaInstance(InternalEObject metaInstance, CDOIDMetaRange range,
      Map<CDOID, InternalEObject> idToMetaInstances, Map<InternalEObject, CDOID> metaInstanceToIDs)
  {
    range = range.increase();
    CDOID id = range.getUpperBound();
    if (TRACER.isEnabled())
    {
      TRACER.format("Registering meta instance: {0} <-> {1}", id, metaInstance);
    }

    if (idToMetaInstances != null)
    {
      if (idToMetaInstances.put(id, metaInstance) != null)
      {
        throw new IllegalStateException("Duplicate meta ID: " + id + " --> " + metaInstance);
      }
    }

    if (metaInstanceToIDs != null)
    {
      if (metaInstanceToIDs.put(metaInstance, id) != null)
      {
        throw new IllegalStateException("Duplicate metaInstance: " + metaInstance + " --> " + id);
      }
    }

    for (EObject content : metaInstance.eContents())
    {
      range = mapMetaInstance((InternalEObject)content, range, idToMetaInstances, metaInstanceToIDs);
    }

    return range;
  }

  public void remapMetaInstance(CDOID oldID, CDOID newID)
  {
    InternalEObject metaInstance = idToMetaInstanceMap.remove(oldID);
    if (metaInstance == null)
    {
      throw new IllegalArgumentException("Unknown meta instance id: " + oldID);
    }

    if (TRACER.isEnabled())
    {
      TRACER.format("Remapping meta instance: {0} --> {1} <-> {2}", oldID, newID, metaInstance);
    }

    idToMetaInstanceMap.put(newID, metaInstance);
    metaInstanceToIDMap.put(metaInstance, newID);
  }
}
