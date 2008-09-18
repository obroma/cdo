/***************************************************************************
 * Copyright (c) 2004 - 2008 Eike Stepper, Germany.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    Eike Stepper - initial API and implementation
 *    Simon McDuff - http://bugs.eclipse.org/201266
 *    Simon McDuff - http://bugs.eclipse.org/233273    
 *    Simon McDuff - http://bugs.eclipse.org/233490    
 **************************************************************************/
package org.eclipse.emf.cdo.internal.server;

import org.eclipse.emf.cdo.common.CDOProtocolConstants;
import org.eclipse.emf.cdo.common.id.CDOID;
import org.eclipse.emf.cdo.common.id.CDOIDMetaRange;
import org.eclipse.emf.cdo.common.id.CDOIDUtil;
import org.eclipse.emf.cdo.common.query.CDOQueryInfo;
import org.eclipse.emf.cdo.internal.server.bundle.OM;
import org.eclipse.emf.cdo.server.IQueryHandler;
import org.eclipse.emf.cdo.server.IQueryHandlerProvider;
import org.eclipse.emf.cdo.server.IRepository;
import org.eclipse.emf.cdo.server.IRepositoryElement;
import org.eclipse.emf.cdo.server.IStore;
import org.eclipse.emf.cdo.server.StoreThreadLocal;

import org.eclipse.net4j.util.StringUtil;
import org.eclipse.net4j.util.container.Container;
import org.eclipse.net4j.util.container.IPluginContainer;
import org.eclipse.net4j.util.lifecycle.LifecycleUtil;
import org.eclipse.net4j.util.om.OMPlatform;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Eike Stepper
 * @since 2.0
 */
public class Repository extends Container<IRepositoryElement> implements IRepository
{
  private String name;

  private IStore store;

  private String uuid;

  private Map<String, String> properties;

  private Boolean supportingRevisionDeltas;

  private Boolean supportingAudits;

  private Boolean verifyingRevisions;

  private PackageManager packageManager = createPackageManager();

  private SessionManager sessionManager = createSessionManager();

  private ResourceManager resourceManager = createResourceManager();

  private RevisionManager revisionManager = createRevisionManager();

  private QueryManager queryManager = createQueryManager();

  private NotificationManager notificationManager = createNotificationManager();

  private CommitManager commitManager = createCommitManager();

  private IQueryHandlerProvider queryHandlerProvider;

  private IRepositoryElement[] elements;

  private transient long lastMetaID;

  public Repository()
  {
  }

  public String getName()
  {
    return name;
  }

  public void setName(String name)
  {
    this.name = name;
  }

  public IStore getStore()
  {
    return store;
  }

  public void setStore(IStore store)
  {
    this.store = store;
    store.setRepository(this);
  }

  public String getUUID()
  {
    if (uuid == null)
    {
      String value = getProperties().get(Props.PROP_OVERRIDE_UUID);
      uuid = StringUtil.isEmpty(value) ? UUID.randomUUID().toString() : value;
    }

    return uuid;
  }

  public synchronized Map<String, String> getProperties()
  {
    if (properties == null)
    {
      properties = new HashMap<String, String>();
    }

    return properties;
  }

  public synchronized void setProperties(Map<String, String> properties)
  {
    this.properties = properties;
  }

  public boolean isSupportingRevisionDeltas()
  {
    if (supportingRevisionDeltas == null)
    {
      String value = getProperties().get(Props.PROP_SUPPORTING_REVISION_DELTAS);
      supportingRevisionDeltas = value == null ? false : Boolean.valueOf(value);
    }

    return supportingRevisionDeltas;
  }

  public boolean isSupportingAudits()
  {
    if (supportingAudits == null)
    {
      String value = getProperties().get(Props.PROP_SUPPORTING_AUDITS);
      supportingAudits = value == null ? false : Boolean.valueOf(value);
    }

    return supportingAudits;
  }

  public boolean isVerifyingRevisions()
  {
    if (verifyingRevisions == null)
    {
      String value = getProperties().get(Props.PROP_VERIFYING_REVISIONS);
      verifyingRevisions = value == null ? false : Boolean.valueOf(value);
    }

    return verifyingRevisions;
  }

  public PackageManager getPackageManager()
  {
    return packageManager;
  }

  public SessionManager getSessionManager()
  {
    return sessionManager;
  }

  public ResourceManager getResourceManager()
  {
    return resourceManager;
  }

  public RevisionManager getRevisionManager()
  {
    return revisionManager;
  }

  /**
   * @since 2.0
   */
  public QueryManager getQueryManager()
  {
    return queryManager;
  }

  /**
   * @since 2.0
   */
  public NotificationManager getNotificationManager()
  {
    return notificationManager;
  }

  /**
   * @since 2.0
   */
  public CommitManager getCommitManager()
  {
    return commitManager;
  }

  /**
   * @since 2.0
   */
  public IQueryHandlerProvider getQueryHandlerProvider()
  {
    return queryHandlerProvider;
  }

  /**
   * @since 2.0
   */
  public void setQueryHandlerProvider(IQueryHandlerProvider queryHandlerProvider)
  {
    this.queryHandlerProvider = queryHandlerProvider;
  }

  /**
   * @since 2.0
   */
  public IQueryHandler getQueryHandler(CDOQueryInfo info)
  {
    IQueryHandler handler = null;
    if (CDOProtocolConstants.QUERY_LANGUAGE_RESOURCES.equals(info.getQueryLanguage()))
    {
      handler = new ResourcesQueryHandler();
    }

    if (handler == null)
    {
      if (queryHandlerProvider != null)
      {
        handler = queryHandlerProvider.getQueryHandler(info);
      }
      else if (OMPlatform.INSTANCE.isOSGiRunning())
      {
        try
        {
          IQueryHandlerProvider provider = new ContainerQueryHandlerProvider(IPluginContainer.INSTANCE);
          handler = provider.getQueryHandler(info);
        }
        catch (Throwable t)
        {
          OM.LOG.warn("Problem with ContainerQueryHandlerProvider: " + t.getMessage());
        }
      }
    }

    if (handler == null)
    {
      handler = StoreThreadLocal.getStoreReader();
    }

    return handler;
  }

  public IRepositoryElement[] getElements()
  {
    return elements;
  }

  @Override
  public boolean isEmpty()
  {
    return false;
  }

  public synchronized CDOIDMetaRange getMetaIDRange(int count)
  {
    CDOID lowerBound = CDOIDUtil.createMeta(lastMetaID + 1);
    lastMetaID += count;
    return CDOIDUtil.createMetaRange(lowerBound, count);
  }

  public long getLastMetaID()
  {
    return lastMetaID;
  }

  public void setLastMetaID(long lastMetaID)
  {
    this.lastMetaID = lastMetaID;
  }

  @Override
  public String toString()
  {
    return MessageFormat.format("Repository[{0}]", name);
  }

  protected PackageManager createPackageManager()
  {
    return new PackageManager(this);
  }

  protected SessionManager createSessionManager()
  {
    return new SessionManager(this);
  }

  protected ResourceManager createResourceManager()
  {
    return new ResourceManager(this);
  }

  protected RevisionManager createRevisionManager()
  {
    return new RevisionManager(this);
  }

  /**
   * @since 2.0
   */
  protected QueryManager createQueryManager()
  {
    return new QueryManager(this);
  }

  /**
   * @since 2.0
   */
  protected NotificationManager createNotificationManager()
  {
    return new NotificationManager(this);
  }

  /**
   * @since 2.0
   */
  protected CommitManager createCommitManager()
  {
    return new CommitManager(this);
  }

  @Override
  protected void doBeforeActivate() throws Exception
  {
    super.doBeforeActivate();
    if (StringUtil.isEmpty(name))
    {
      throw new IllegalArgumentException("name is null or empty");
    }

    if (store == null)
    {
      throw new IllegalArgumentException("store is null");
    }

    if (isSupportingRevisionDeltas() && !store.hasWriteDeltaSupport())
    {
      throw new IllegalStateException("Store without revision delta support");
    }

    if (isSupportingAudits() && !store.hasAuditingSupport())
    {
      throw new IllegalStateException("Store without auditing support");
    }

    elements = new IRepositoryElement[] { packageManager, sessionManager, resourceManager, revisionManager,
        queryManager, notificationManager, commitManager, store };
  }

  @Override
  protected void doActivate() throws Exception
  {
    super.doActivate();
    activateRepository();
  }

  @Override
  protected void doDeactivate() throws Exception
  {
    deactivateRepository();
    super.doDeactivate();
  }

  protected void activateRepository() throws Exception
  {
    LifecycleUtil.activate(store);
    LifecycleUtil.activate(packageManager);
    if (store.wasCrashed())
    {
      OM.LOG.info("Crash of repository " + name + " detected");
      store.repairAfterCrash();
    }

    setLastMetaID(store.getLastMetaID());

    LifecycleUtil.activate(sessionManager);
    LifecycleUtil.activate(resourceManager);
    LifecycleUtil.activate(revisionManager);
    LifecycleUtil.activate(queryManager);
    LifecycleUtil.activate(notificationManager);
    LifecycleUtil.activate(commitManager);
    LifecycleUtil.activate(queryHandlerProvider);
  }

  protected void deactivateRepository()
  {
    LifecycleUtil.deactivate(queryHandlerProvider);
    LifecycleUtil.deactivate(commitManager);
    LifecycleUtil.deactivate(notificationManager);
    LifecycleUtil.deactivate(queryManager);
    LifecycleUtil.deactivate(revisionManager);
    LifecycleUtil.deactivate(resourceManager);
    LifecycleUtil.deactivate(sessionManager);

    LifecycleUtil.deactivate(packageManager);
    LifecycleUtil.deactivate(store);
  }
}
