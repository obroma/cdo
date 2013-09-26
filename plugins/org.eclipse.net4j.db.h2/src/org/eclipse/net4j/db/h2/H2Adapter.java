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
package org.eclipse.net4j.db.h2;

import org.eclipse.net4j.db.DBType;
import org.eclipse.net4j.db.DBUtil;
import org.eclipse.net4j.db.DBUtil.RunnableWithConnection;
import org.eclipse.net4j.db.IDBAdapter;
import org.eclipse.net4j.db.ddl.IDBField;
import org.eclipse.net4j.db.ddl.IDBIndex.Type;
import org.eclipse.net4j.db.ddl.IDBTable;
import org.eclipse.net4j.spi.db.DBAdapter;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * A {@link IDBAdapter DB adapter} for <a href="http://www.h2database.com/html/main.html">H2</a> databases.
 *
 * @author Eike Stepper
 * @since 2.0
 */
public class H2Adapter extends DBAdapter
{
  private static final String NAME = "h2"; //$NON-NLS-1$

  public static final String VERSION = "1.1.114"; //$NON-NLS-1$

  public H2Adapter()
  {
    super(NAME, VERSION);
  }

  @Override
  protected String getTypeName(IDBField field)
  {
    DBType type = field.getType();
    switch (type)
    {
    case BIT:
      return "SMALLINT"; //$NON-NLS-1$

    case FLOAT:
      return "REAL"; //$NON-NLS-1$

    case LONGVARCHAR:
      return "VARCHAR"; //$NON-NLS-1$

    case NUMERIC:
      return "DECIMAL"; //$NON-NLS-1$

    case LONGVARBINARY:
    case VARBINARY:
      return "BLOB"; //$NON-NLS-1$
    }

    return super.getTypeName(field);
  }

  public String[] getReservedWords()
  {
    return getSQL92ReservedWords();
  }

  @Override
  protected boolean isPrimaryKeyShadow(Connection connection, IDBTable table, String name, Type type, IDBField[] fields)
  {
    if (!name.toUpperCase().startsWith("PRIMARY_KEY"))
    {
      return false;
    }

    return super.isPrimaryKeyShadow(connection, table, name, type, fields);
  }

  @Override
  public boolean isDuplicateKeyException(SQLException ex)
  {
    String sqlState = ex.getSQLState();
    return "23001".equals(sqlState) || "23505".equals(sqlState);
  }

  @Override
  public String sqlRenameField(IDBField field, String oldName)
  {
    return "ALTER TABLE " + field.getTable() + " ALTER COLUMN " + oldName + " RENAME TO " + field;
  }

  /**
   * See H2 bug http://code.google.com/p/h2database/issues/detail?id=508
   */
  @Override
  public int convertRowNumberToDriver(int row)
  {
    return row - 1;
  }

  /**
   * @since 4.2
   */
  public static void createSchema(DataSource dataSource, final String name, final boolean dropIfExists)
  {
    DBUtil.execute(DBUtil.createConnectionProvider(dataSource), new RunnableWithConnection<Object>()
    {
      public Object run(Connection connection) throws SQLException
      {
        if (dropIfExists)
        {
          DBUtil.execute(connection, "DROP SCHEMA IF EXISTS " + name);
        }

        DBUtil.execute(connection, "CREATE SCHEMA IF NOT EXISTS " + name);
        return null;
      }
    });
  }
}
