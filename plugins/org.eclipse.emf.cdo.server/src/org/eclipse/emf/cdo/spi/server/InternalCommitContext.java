/**
 * Copyright (c) 2004 - 2010 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.emf.cdo.spi.server;

import org.eclipse.emf.cdo.common.id.CDOID;
import org.eclipse.emf.cdo.server.IStoreAccessor;
import org.eclipse.emf.cdo.spi.common.model.InternalCDOPackageUnit;
import org.eclipse.emf.cdo.spi.common.revision.InternalCDORevision;
import org.eclipse.emf.cdo.spi.common.revision.InternalCDORevisionDelta;

import org.eclipse.net4j.util.om.monitor.OMMonitor;
import org.eclipse.net4j.util.om.monitor.ProgressDistributable;
import org.eclipse.net4j.util.om.monitor.ProgressDistributor;

/**
 * @author Eike Stepper
 * @since 3.0
 */
public interface InternalCommitContext extends IStoreAccessor.CommitContext
{
  @SuppressWarnings("unchecked")
  public static final ProgressDistributable<InternalCommitContext>[] OPS = ProgressDistributor.array( //
      new ProgressDistributable.Default<InternalCommitContext>()
      {
        public void runLoop(int index, InternalCommitContext commitContext, OMMonitor monitor) throws Exception
        {
          commitContext.write(monitor.fork());
        }
      }, //

      new ProgressDistributable.Default<InternalCommitContext>()
      {
        public void runLoop(int index, InternalCommitContext commitContext, OMMonitor monitor) throws Exception
        {
          if (commitContext.getRollbackMessage() == null)
          {
            commitContext.commit(monitor.fork());
          }
          else
          {
            monitor.worked();
          }
        }
      });

  public InternalTransaction getTransaction();

  public void preWrite();

  public void write(OMMonitor monitor);

  public void commit(OMMonitor monitor);

  public void rollback(String message);

  public void postCommit(boolean success);

  public void setNewPackageUnits(InternalCDOPackageUnit[] newPackageUnits);

  public void setNewObjects(InternalCDORevision[] newObjects);

  public void setDirtyObjectDeltas(InternalCDORevisionDelta[] dirtyObjectDeltas);

  public void setDetachedObjects(CDOID[] detachedObjects);

  public void setAutoReleaseLocksEnabled(boolean on);

  public void setCommitComment(String comment);

  public void addIDMapping(CDOID oldID, CDOID newID);

  public void applyIDMappings(OMMonitor monitor);
}
