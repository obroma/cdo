/*
 * Copyright (c) 2007, 2011, 2012 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.net4j.util.transaction;

/**
 * @author Eike Stepper
 */
public interface ITransaction<CONTEXT>
{
  public boolean isActive();

  public CONTEXT getContext();

  public void execute(ITransactionalOperation<CONTEXT> operation) throws TransactionException;

  public void commit();

  public void rollback();
}
