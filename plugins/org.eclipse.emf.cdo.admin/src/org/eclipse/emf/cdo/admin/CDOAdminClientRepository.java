/*
 * Copyright (c) 2004 - 2012 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.emf.cdo.admin;

import org.eclipse.emf.cdo.common.admin.CDOAdminRepository;
import org.eclipse.emf.cdo.net4j.CDONet4jSession;

/**
 * @author Eike Stepper
 */
public interface CDOAdminClientRepository extends CDOAdminRepository
{
  public CDOAdminClient getAdmin();

  public CDONet4jSession openSession();
}
