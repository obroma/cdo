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
package org.eclipse.emf.cdo.tests.config;

import org.eclipse.emf.cdo.tests.config.impl.Config;
import org.eclipse.emf.cdo.tests.config.impl.ContainerConfig;
import org.eclipse.emf.cdo.tests.config.impl.ModelConfig;
import org.eclipse.emf.cdo.tests.config.impl.RepositoryConfig;
import org.eclipse.emf.cdo.tests.config.impl.SessionConfig;

/**
 * @author Eike Stepper
 */
public interface IConstants
{
  public static final Config[][] CONFIGS = { //
  ContainerConfig.CONFIGS, //
      RepositoryConfig.CONFIGS, //
      SessionConfig.CONFIGS, //
      ModelConfig.CONFIGS };

  public static final ContainerConfig COMBINED = ContainerConfig.Combined.INSTANCE;

  public static final ContainerConfig SEPARATED = ContainerConfig.Separated.INSTANCE;

  public static final RepositoryConfig MEM = RepositoryConfig.MEM.INSTANCE;

  public static final RepositoryConfig DB_HSQL_HORIZONTAL = RepositoryConfig.DBHsqldbNonPrepStmt.HSQLDB_HORIZONTAL;

  public static final RepositoryConfig DB_DERBY_HORIZONTAL = RepositoryConfig.DBDerbyNonPrepStmt.DERBY_HORIZONTAL;

  public static final RepositoryConfig DB_MYSQL_HORIZONTAL = RepositoryConfig.DBMysqlNonPrepStmt.MYSQL_HORIZONTAL;

  public static final SessionConfig JVM = SessionConfig.JVM.INSTANCE;

  public static final SessionConfig TCP = SessionConfig.TCP.INSTANCE;

  public static final ModelConfig NATIVE = ModelConfig.Native.INSTANCE;
}
