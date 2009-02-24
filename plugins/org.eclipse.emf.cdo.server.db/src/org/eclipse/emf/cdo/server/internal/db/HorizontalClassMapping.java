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
package org.eclipse.emf.cdo.server.internal.db;

import org.eclipse.emf.cdo.common.TODO;
import org.eclipse.emf.cdo.common.id.CDOID;
import org.eclipse.emf.cdo.common.revision.CDORevision;
import org.eclipse.emf.cdo.server.IPackageManager;
import org.eclipse.emf.cdo.server.IRepository;
import org.eclipse.emf.cdo.server.db.IDBStoreAccessor;

import org.eclipse.net4j.util.collection.Pair;
import org.eclipse.net4j.util.om.monitor.OMMonitor;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * @author Eike Stepper
 */
public class HorizontalClassMapping extends ClassMapping
{
  public HorizontalClassMapping(HorizontalMappingStrategy mappingStrategy, EClass cdoClass)
  {
    super(mappingStrategy, cdoClass, TODO.getAllPersistentFeatures(cdoClass));
  }

  @Override
  public HorizontalMappingStrategy getMappingStrategy()
  {
    return (HorizontalMappingStrategy)super.getMappingStrategy();
  }

  @Override
  public void writeRevision(IDBStoreAccessor accessor, CDORevision revision, OMMonitor monitor)
  {
    try
    {
      monitor.begin(5);
      super.writeRevision(accessor, revision, monitor.fork(4));
      if (revision.getVersion() == 1)
      {
        CDOID id = revision.getID();
        EClass type = revision.getEClass();
        getMappingStrategy().getObjectTypeCache().putObjectType(accessor, id, type);
      }

      // TODO Better monitoring
      monitor.worked();
    }
    finally
    {
      monitor.done();
    }
  }

  @Override
  protected void checkDuplicateResources(IDBStoreAccessor accessor, CDORevision revision) throws IllegalStateException
  {
    IRepository repository = getMappingStrategy().getStore().getRepository();
    IPackageManager packageManager = repository.getPackageManager();
    CDOResourceNodeClass resourceNodeClass = packageManager.getCDOResourcePackage().getCDOResourceNodeClass();
    EStructuralFeature resourceNameFeature = resourceNodeClass.getCDONameFeature();

    CDOID folderID = (CDOID)revision.data().getContainerID();
    String name = (String)revision.data().get(resourceNameFeature, 0);

    CDOID existingID = accessor.readResourceID(folderID, name, revision.getCreated());
    if (existingID != null && !existingID.equals(revision.getID()))
    {
      throw new IllegalStateException("Duplicate resource or folder: " + name + " in folder " + folderID);
    }
  }

  @Override
  public Object createReferenceMappingKey(EStructuralFeature cdoFeature)
  {
    return new Pair<EClass, EStructuralFeature>(getEClass(), cdoFeature);
  }

  public boolean hasFullRevisionInfo()
  {
    return true;
  }

  @Override
  protected void deleteRevision(IDBStoreAccessor accessor, CDOID id, OMMonitor monitor)
  {
    try
    {
      monitor.begin(2);
      super.deleteRevision(accessor, id, monitor.fork(1));
      getMappingStrategy().getObjectTypeCache().removeObjectType(accessor, id);
      monitor.worked(1);
    }
    finally
    {
      monitor.done();
    }
  }
}
