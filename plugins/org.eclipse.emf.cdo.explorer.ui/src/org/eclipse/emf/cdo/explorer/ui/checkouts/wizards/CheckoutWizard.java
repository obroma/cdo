/*
 * Copyright (c) 2009-2013 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.emf.cdo.explorer.ui.checkouts.wizards;

import org.eclipse.emf.cdo.explorer.CDOExplorerUtil;
import org.eclipse.emf.cdo.explorer.checkouts.CDOCheckout;
import org.eclipse.emf.cdo.explorer.checkouts.CDOCheckoutManager;
import org.eclipse.emf.cdo.explorer.repositories.CDORepository;
import org.eclipse.emf.cdo.explorer.repositories.CDORepositoryElement;
import org.eclipse.emf.cdo.explorer.ui.bundle.OM;

import org.eclipse.net4j.util.AdapterUtil;
import org.eclipse.net4j.util.ui.UIUtil;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.IPageChangedListener;
import org.eclipse.jface.dialogs.PageChangedEvent;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardContainer;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;

import java.util.Properties;

/**
 * @author Eike Stepper
 */
public class CheckoutWizard extends Wizard implements IImportWizard, IPageChangedListener
{
  private CDORepositoryElement selectedElement;

  private CheckoutRepositoryPage repositoryPage;

  private CheckoutTypePage typePage;

  private CheckoutBranchPointPage branchPointPage;

  private CheckoutRootObjectPage rootObjectPage;

  private CheckoutLabelPage labelPage;

  public CheckoutWizard()
  {
    setWindowTitle("New Checkout");
  }

  public final CheckoutRepositoryPage getRepositoryPage()
  {
    return repositoryPage;
  }

  public final CheckoutTypePage getTypePage()
  {
    return typePage;
  }

  public final CheckoutBranchPointPage getBranchPointPage()
  {
    return branchPointPage;
  }

  public final CheckoutRootObjectPage getRootObjectPage()
  {
    return rootObjectPage;
  }

  public final CheckoutLabelPage getLabelPage()
  {
    return labelPage;
  }

  public void init(IWorkbench workbench, IStructuredSelection selection)
  {
    if (selection.size() == 1)
    {
      Object element = selection.getFirstElement();
      selectedElement = AdapterUtil.adapt(element, CDORepositoryElement.class);
    }
  }

  @Override
  public void setContainer(IWizardContainer wizardContainer)
  {
    if (getContainer() instanceof WizardDialog)
    {
      ((WizardDialog)getContainer()).removePageChangedListener(this);
    }

    super.setContainer(wizardContainer);

    if (getContainer() instanceof WizardDialog)
    {
      ((WizardDialog)getContainer()).addPageChangedListener(this);
    }
  }

  public void pageChanged(PageChangedEvent event)
  {
    Object page = event.getSelectedPage();
    if (page instanceof CheckoutWizardPage)
    {
      CheckoutWizardPage checkoutWizardPage = (CheckoutWizardPage)page;
      checkoutWizardPage.pageActivated();
    }
  }

  @Override
  public void addPages()
  {
    addPage(repositoryPage = new CheckoutRepositoryPage());
    addPage(typePage = new CheckoutTypePage());
    addPage(branchPointPage = new CheckoutBranchPointPage());
    addPage(rootObjectPage = new CheckoutRootObjectPage());
    addPage(labelPage = new CheckoutLabelPage());

    if (selectedElement != null)
    {
      CDORepository repository = selectedElement.getRepository();
      repositoryPage.setRepository(repository);

      int branchID = selectedElement.getBranchID();
      long timeStamp = selectedElement.getTimeStamp();
      branchPointPage.setBranchPoint(branchID, timeStamp);
    }
  }

  @Override
  public boolean performFinish()
  {
    final Properties properties = new Properties();
    repositoryPage.fillProperties(properties);
    typePage.fillProperties(properties);
    branchPointPage.fillProperties(properties);
    rootObjectPage.fillProperties(properties);
    labelPage.fillProperties(properties);

    new Job("Checkout")
    {
      @Override
      protected IStatus run(IProgressMonitor monitor)
      {
        try
        {
          CDOCheckoutManager checkoutManager = CDOExplorerUtil.getCheckoutManager();
          CDOCheckout checkout = checkoutManager.addCheckout(properties);
          checkout.open();
        }
        catch (Exception ex)
        {
          OM.LOG.error(ex);

          final IStatus status = new Status(IStatus.ERROR, OM.BUNDLE_ID, ex.getMessage(), ex);
          UIUtil.getDisplay().asyncExec(new Runnable()
          {
            public void run()
            {
              ErrorDialog.openError(getShell(), "Error", "An error occured while creating the checkout.", status);
            }
          });

          return Status.OK_STATUS;
        }
        finally
        {
        }

        return Status.OK_STATUS;
      }
    }.schedule();

    return true;
  }
}
