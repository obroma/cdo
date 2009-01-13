/***************************************************************************
 * Copyright (c) 2004 - 2008 Eike Stepper, Germany.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    Eike Stepper - initial API and implementation
 **************************************************************************/
package org.eclipse.emf.internal.cdo.net4j.protocol;

import org.eclipse.emf.cdo.CDOObject;
import org.eclipse.emf.cdo.common.id.CDOID;
import org.eclipse.emf.cdo.common.model.CDOFeature;
import org.eclipse.emf.cdo.common.model.CDOPackage;
import org.eclipse.emf.cdo.common.protocol.CDOProtocolConstants;
import org.eclipse.emf.cdo.common.revision.CDORevision;
import org.eclipse.emf.cdo.common.util.TransportException;
import org.eclipse.emf.cdo.internal.common.protocol.CDOProtocolImpl;
import org.eclipse.emf.cdo.session.CDOSession;
import org.eclipse.emf.cdo.spi.common.revision.InternalCDORevision;
import org.eclipse.emf.cdo.transaction.CDOTimeStampContext;
import org.eclipse.emf.cdo.view.CDOView;

import org.eclipse.emf.internal.cdo.bundle.OM;
import org.eclipse.emf.internal.cdo.session.CDORevisionManagerImpl;

import org.eclipse.net4j.signal.RemoteException;
import org.eclipse.net4j.signal.RequestWithConfirmation;
import org.eclipse.net4j.signal.SignalReactor;
import org.eclipse.net4j.util.WrappedException;
import org.eclipse.net4j.util.concurrent.RWLockManager.LockType;
import org.eclipse.net4j.util.om.monitor.OMMonitor;
import org.eclipse.net4j.util.om.trace.PerfTracer;

import org.eclipse.emf.spi.cdo.AbstractQueryIterator;
import org.eclipse.emf.spi.cdo.CDOSessionProtocol;
import org.eclipse.emf.spi.cdo.InternalCDOObject;
import org.eclipse.emf.spi.cdo.InternalCDOTransaction.InternalCDOCommitContext;
import org.eclipse.emf.spi.cdo.InternalCDOXATransaction.InternalCDOXACommitContext;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Eike Stepper
 */
public class CDOClientProtocol extends CDOProtocolImpl implements CDOSessionProtocol
{
  private static final PerfTracer REVISION_LOADING = new PerfTracer(OM.PERF_REVISION_LOADING,
      CDORevisionManagerImpl.class);

  public CDOClientProtocol()
  {
  }

  @Override
  public CDOSession getSession()
  {
    return (CDOSession)super.getSession();
  }

  public OpenSessionResult openSession(String repositoryName, boolean passiveUpdateEnabled)
  {
    open();
    return send(new OpenSessionRequest(this, repositoryName, passiveUpdateEnabled));
  }

  public void loadLibraries(Set<String> missingLibraries, File cacheFolder)
  {
    send(new LoadLibrariesRequest(this, missingLibraries, cacheFolder));
  }

  public void setPassiveUpdate(Map<CDOID, CDORevision> allRevisions, int initialChunkSize, boolean passiveUpdateEnabled)
  {
    send(new SetPassiveUpdateRequest(this, allRevisions, initialChunkSize, passiveUpdateEnabled));
  }

  public RepositoryTimeResult getRepositoryTime()
  {
    return send(new RepositoryTimeRequest(this));
  }

  public void loadPackage(CDOPackage cdoPackage, boolean onlyEcore)
  {
    send(new LoadPackageRequest(this, cdoPackage, onlyEcore));
  }

  public Object loadChunk(InternalCDORevision revision, CDOFeature feature, int accessIndex, int fetchIndex,
      int fromIndex, int toIndex)
  {
    return send(new LoadChunkRequest(this, revision, feature, accessIndex, fetchIndex, fromIndex, toIndex));
  }

  public List<InternalCDORevision> loadRevisions(Collection<CDOID> ids, int referenceChunk)
  {
    return send(new LoadRevisionRequest(this, ids, referenceChunk));
  }

  public List<InternalCDORevision> loadRevisionsByTime(Collection<CDOID> ids, int referenceChunk, long timeStamp)
  {
    return send(new LoadRevisionByTimeRequest(this, ids, referenceChunk, timeStamp));
  }

  public InternalCDORevision loadRevisionByVersion(CDOID id, int referenceChunk, int version)
  {
    return send(new LoadRevisionByVersionRequest(this, id, referenceChunk, version)).get(0);
  }

  public List<InternalCDORevision> verifyRevision(List<InternalCDORevision> revisions) throws TransportException
  {
    return send(new VerifyRevisionRequest(this, revisions));
  }

  public Collection<CDOTimeStampContext> syncRevisions(Map<CDOID, CDORevision> allRevisions, int initialChunkSize)
  {
    return send(new SyncRevisionsRequest(this, allRevisions, initialChunkSize));
  }

  public void openView(int viewId, byte protocolViewType, long timeStamp)
  {
    send(new ViewsChangedRequest(this, viewId, protocolViewType, timeStamp));
  }

  public void closeView(int viewId)
  {
    send(new ViewsChangedRequest(this, viewId));
  }

  public boolean[] setAudit(int viewId, long timeStamp, List<InternalCDOObject> invalidObjects)
  {
    return send(new SetAuditRequest(this, viewId, timeStamp, invalidObjects));
  }

  public void changeSubscription(int viewId, List<CDOID> cdoIDs, boolean subscribeMode, boolean clear)
  {
    send(new ChangeSubscriptionRequest(this, viewId, cdoIDs, subscribeMode, clear));
  }

  public List<Object> query(int viewID, AbstractQueryIterator<?> queryResult)
  {
    return send(new QueryRequest(this, viewID, queryResult));
  }

  public boolean cancelQuery(int queryId)
  {
    try
    {
      return new QueryCancelRequest(this, queryId).send();
    }
    catch (Exception ignore)
    {
      return false;
    }
  }

  public void lockObjects(CDOView view, Collection<? extends CDOObject> objects, long timeout, LockType lockType)
      throws InterruptedException
  {
    InterruptedException interruptedException = null;
    RuntimeException runtimeException = null;

    try
    {
      new LockObjectsRequest(this, view, objects, timeout, lockType).send();
    }
    catch (RemoteException ex)
    {
      if (ex.getCause() instanceof RuntimeException)
      {
        runtimeException = (RuntimeException)ex.getCause();
      }
      else if (ex.getCause() instanceof InterruptedException)
      {
        interruptedException = (InterruptedException)ex.getCause();
      }
    }
    catch (Exception ex)
    {
      throw WrappedException.wrap(ex);
    }

    if (interruptedException != null)
    {
      throw interruptedException;
    }

    if (runtimeException != null)
    {
      throw runtimeException;
    }
  }

  public void unlockObjects(CDOView view, Collection<? extends CDOObject> objects, LockType lockType)
  {
    send(new UnlockObjectsRequest(this, view, objects, lockType));
  }

  public boolean isObjectLocked(CDOView view, CDOObject object, LockType lockType)
  {
    return send(new ObjectLockedRequest(this, view, object, lockType));
  }

  public CommitTransactionResult commitTransaction(InternalCDOCommitContext commitContext, OMMonitor monitor)
  {
    return send(new CommitTransactionRequest(this, commitContext), monitor);
  }

  public CommitTransactionResult commitTransactionPhase1(InternalCDOXACommitContext xaContext, OMMonitor monitor)
  {
    return send(new CommitTransactionPhase1Request(this, xaContext), monitor);
  }

  public CommitTransactionResult commitTransactionPhase2(InternalCDOXACommitContext xaContext, OMMonitor monitor)
  {
    return send(new CommitTransactionPhase2Request(this, xaContext), monitor);
  }

  public CommitTransactionResult commitTransactionPhase3(InternalCDOXACommitContext xaContext, OMMonitor monitor)
  {
    return send(new CommitTransactionPhase3Request(this, xaContext), monitor);
  }

  public CommitTransactionResult commitTransactionCancel(InternalCDOXACommitContext xaContext, OMMonitor monitor)
  {
    return send(new CommitTransactionCancelRequest(this, xaContext), monitor);
  }

  @Override
  protected SignalReactor createSignalReactor(short signalID)
  {
    switch (signalID)
    {
    case CDOProtocolConstants.SIGNAL_COMMIT_NOTIFICATION:
      return new CommitNotificationIndication(this);

    default:
      return super.createSignalReactor(signalID);
    }
  }

  @Override
  protected void doBeforeActivate() throws Exception
  {
    super.doBeforeActivate();
    if (!(getInfraStructure() instanceof CDOSession))
    {
      throw new IllegalStateException("No session");
    }
  }

  private <RESULT> RESULT send(RequestWithConfirmation<RESULT> request)
  {
    try
    {
      return request.send();
    }
    catch (RuntimeException ex)
    {
      throw ex;
    }
    catch (Exception ex)
    {
      throw new TransportException(ex);
    }
  }

  private CommitTransactionResult send(CommitTransactionRequest request, OMMonitor monitor)
  {
    try
    {
      return request.send(monitor);
    }
    catch (RuntimeException ex)
    {
      throw ex;
    }
    catch (Exception ex)
    {
      throw new TransportException(ex);
    }
  }

  private List<InternalCDORevision> send(LoadRevisionRequest request)
  {
    try
    {
      REVISION_LOADING.start(request);
      return send((RequestWithConfirmation<List<InternalCDORevision>>)request);
    }
    finally
    {
      REVISION_LOADING.stop(request);
    }
  }
}
