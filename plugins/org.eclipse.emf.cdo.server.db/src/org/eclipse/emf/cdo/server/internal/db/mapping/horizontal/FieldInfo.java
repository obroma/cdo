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
package org.eclipse.emf.cdo.server.internal.db.mapping.horizontal;

import org.eclipse.net4j.db.DBType;
import org.eclipse.net4j.db.ddl.IDBField;

/**
 * Used by subclasses to indicate which fields should be in the table. I.e. just a triple of name, DBType and precision.
 *
 * @author Stefan Winkler
 */
public final class FieldInfo
{
  private final String name;

  private final DBType dbType;

  private final int precision;

  public FieldInfo(String name, DBType dbType, int precision)
  {
    this.name = name;
    this.dbType = dbType;
    this.precision = precision;
  }

  public FieldInfo(String name, DBType dbType)
  {
    this(name, dbType, IDBField.DEFAULT);
  }

  public String getName()
  {
    return name;
  }

  public DBType getDbType()
  {
    return dbType;
  }

  public int getPrecision()
  {
    return precision;
  }
}
