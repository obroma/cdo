/*
 * Copyright (c) 2010-2012 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.emf.cdo.explorer;

import org.eclipse.emf.cdo.common.id.CDOID;

import org.eclipse.net4j.util.container.IContainer;
import org.eclipse.net4j.util.event.IEvent;

/**
 * Manages a set of {@link CDOCheckout checkouts}.
 *
 * @author Eike Stepper
 * @since 4.4
 * @apiviz.composedOf {@link CDOCheckout}
 */
public interface CDOCheckoutManager extends IContainer<CDOCheckout>
{
  public CDOCheckout[] getCheckouts();

  public CDOCheckout connect(String label, CDORepository repository, String branchPath, long timeStamp,
      boolean readOnly, CDOID rootID);

  /**
   * @author Eike Stepper
   */
  public interface CheckoutOpenEvent extends IEvent
  {
    public CDOCheckoutManager getSource();

    public CDOCheckout getCheckout();

    public boolean isOpen();
  }
}
