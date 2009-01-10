/***************************************************************************
 * Copyright (c) 2004 - 2009 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    Eike Stepper - initial API and implementation
 **************************************************************************/
package org.eclipse.emf.cdo.tests.config;

import org.eclipse.emf.cdo.server.IRepository;

import java.util.Map;

/**
 * @author Eike Stepper
 */
public interface IRepositoryConfig extends IConfig
{
  public static final String REPOSITORY_NAME = "repo1";

  public Map<String, String> getRepositoryProperties();

  public IRepository getRepository(String name);
}
