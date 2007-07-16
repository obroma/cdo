package org.eclipse.emf.cdo.internal.ui.actions;

import org.eclipse.emf.cdo.CDOView;
import org.eclipse.emf.cdo.internal.ui.editor.CDOEditor;
import org.eclipse.emf.cdo.internal.ui.views.CDOViewHistory;
import org.eclipse.emf.cdo.internal.ui.views.CDOViewHistory.Entry;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IWorkbenchPage;

/**
 * @author Eike Stepper
 */
public final class ShowEntryAction extends EntryAction
{
  public ShowEntryAction(IWorkbenchPage page, String text, String toolTipText, ImageDescriptor image,
      CDOViewHistory.Entry entry)
  {
    super(page, "Show Editor", "Show a CDO editor", null, entry);
  }

  @Override
  protected void doRun(IWorkbenchPage page, IProgressMonitor monitor) throws Exception
  {
    CDOView view = getEntry().getView();
    String resourcePath = getEntry().getResourcePath();
    CDOEditor.open(page, view, resourcePath);
  }
}