/***************************************************************************
 * Copyright (c) 2004 - 2009 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Simon McDuff - initial API and implementation
 **************************************************************************/
package org.eclipse.emf.internal.cdo.transaction;

import org.eclipse.emf.cdo.common.revision.CDOReferenceAdjuster;
import org.eclipse.emf.cdo.spi.common.revision.CDOIDMapper;
import org.eclipse.emf.cdo.transaction.CDOSavepoint;

import org.eclipse.emf.internal.cdo.bundle.OM;

import org.eclipse.net4j.util.om.monitor.EclipseMonitor;
import org.eclipse.net4j.util.om.monitor.OMMonitor;
import org.eclipse.net4j.util.om.trace.ContextTracer;
import org.eclipse.net4j.util.transaction.TransactionException;

import org.eclipse.emf.spi.cdo.CDOTransactionStrategy;
import org.eclipse.emf.spi.cdo.InternalCDOTransaction;
import org.eclipse.emf.spi.cdo.CDOSessionProtocol.CommitTransactionResult;
import org.eclipse.emf.spi.cdo.InternalCDOTransaction.InternalCDOCommitContext;

import org.eclipse.core.runtime.IProgressMonitor;

/**
 * @author Simon McDuff
 * @since 2.0
 */
public class CDOSingleTransactionStrategyImpl implements CDOTransactionStrategy
{
  public static final CDOSingleTransactionStrategyImpl INSTANCE = new CDOSingleTransactionStrategyImpl();

  private static final ContextTracer TRACER = new ContextTracer(OM.DEBUG_TRANSACTION,
      CDOSingleTransactionStrategyImpl.class);

  public CDOSingleTransactionStrategyImpl()
  {
  }

  public void commit(InternalCDOTransaction transaction, IProgressMonitor progressMonitor) throws Exception
  {
    InternalCDOCommitContext commitContext = transaction.createCommitContext();
    if (TRACER.isEnabled())
    {
      TRACER.format("CDOCommitContext.preCommit"); //$NON-NLS-1$
    }

    CDOIDMapper idMapper = new CDOIDMapper();
    CDOReferenceAdjuster referenceAdjuster = commitContext.createAdjuster(idMapper);
    CDOSavepointImpl.applyReferenceAdjuster(commitContext, referenceAdjuster);

    CommitTransactionResult result = null;

    try
    {
      commitContext.preCommit();

      if (commitContext.getTransaction().isDirty())
      {
        OMMonitor monitor = new EclipseMonitor(progressMonitor);
        result = transaction.getSession().getSessionProtocol().commitTransaction(commitContext, monitor);

        String rollbackMessage = result.getRollbackMessage();
        if (rollbackMessage != null)
        {
          throw new TransactionException(rollbackMessage);
        }
      }

      if (TRACER.isEnabled())
      {
        TRACER.format("CDOCommitContext.postCommit"); //$NON-NLS-1$
      }
    }
    catch (Exception ex)
    {
      idMapper.reverseIDMappings();
      CDOSavepointImpl.applyReferenceAdjuster(commitContext, idMapper);
      throw ex;
    }

    commitContext.postCommit(result);
  }

  public void rollback(InternalCDOTransaction transaction, CDOSavepoint savepoint)
  {
    transaction.handleRollback(savepoint);
  }

  public CDOSavepoint setSavepoint(InternalCDOTransaction transaction)
  {
    return transaction.handleSetSavepoint();
  }

  public void setTarget(InternalCDOTransaction transaction)
  {
    // Do nothing
  }

  public void unsetTarget(InternalCDOTransaction transaction)
  {
    // Do nothing
  }
}
