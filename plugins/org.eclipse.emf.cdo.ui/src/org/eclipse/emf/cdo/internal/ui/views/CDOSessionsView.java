package org.eclipse.emf.cdo.internal.ui.views;

import org.eclipse.emf.cdo.CDOSession;
import org.eclipse.emf.cdo.protocol.CDOProtocolConstants;

import org.eclipse.net4j.internal.ui.ContainerItemProvider;
import org.eclipse.net4j.internal.ui.ContainerView;
import org.eclipse.net4j.internal.ui.IElementFilter;
import org.eclipse.net4j.internal.ui.SafeAction;
import org.eclipse.net4j.transport.IPluginTransportContainer;
import org.eclipse.net4j.transport.ITransportContainer;

import org.eclipse.emf.internal.cdo.CDOSessionFactory;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IToolBarManager;

public class CDOSessionsView extends ContainerView
{
  private Action openSessionAction = new SafeAction("Open Session", "Open a CDO session", getAddImageDescriptor())
  {
    @Override
    protected void doRun() throws Exception
    {
      IPluginTransportContainer.INSTANCE.getElement(CDOSessionFactory.SESSION_GROUP,
          CDOProtocolConstants.PROTOCOL_NAME, "tcp://127.0.0.1:2036/repo1");
    }
  };

  public CDOSessionsView()
  {
  }

  @Override
  protected ITransportContainer getContainer()
  {
    return IPluginTransportContainer.INSTANCE;
  }

  @Override
  protected ContainerItemProvider createContainerItemProvider()
  {
    return new CDOItemProvider(new IElementFilter()
    {
      public boolean filter(Object element)
      {
        return element instanceof CDOSession;
      }
    });
  }

  @Override
  protected void fillLocalToolBar(IToolBarManager manager)
  {
    manager.add(openSessionAction);
    super.fillLocalToolBar(manager);
  }
}