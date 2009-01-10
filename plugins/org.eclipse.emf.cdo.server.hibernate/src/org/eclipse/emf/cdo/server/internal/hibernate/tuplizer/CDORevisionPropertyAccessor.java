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
package org.eclipse.emf.cdo.server.internal.hibernate.tuplizer;

import org.eclipse.emf.cdo.common.model.CDOFeature;
import org.eclipse.emf.cdo.spi.common.revision.InternalCDORevision;

import org.hibernate.HibernateException;
import org.hibernate.PropertyNotFoundException;
import org.hibernate.engine.SessionFactoryImplementor;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.property.Getter;
import org.hibernate.property.PropertyAccessor;
import org.hibernate.property.Setter;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author Eike Stepper
 */
public class CDORevisionPropertyAccessor implements PropertyAccessor
{
  private CDORevisionTuplizer tuplizer;

  public CDORevisionPropertyAccessor(CDORevisionTuplizer tuplizer)
  {
    this.tuplizer = tuplizer;
  }

  public CDORevisionTuplizer getTuplizer()
  {
    return tuplizer;
  }

  @SuppressWarnings("unchecked")
  public Getter getGetter(Class theClass, String propertyName) throws PropertyNotFoundException
  {
    return new CDORevisionGetter(this, propertyName);
  }

  @SuppressWarnings("unchecked")
  public Setter getSetter(Class theClass, String propertyName) throws PropertyNotFoundException
  {
    return new CDORevisionSetter(this, propertyName);
  }

  /**
   * @author Eike Stepper
   */
  public static class BaseAccessor
  {
    private CDORevisionPropertyAccessor propertyAccessor;

    private CDOFeature cdoFeature;

    public BaseAccessor(CDORevisionPropertyAccessor propertyAccessor, String propertyName)
    {
      this.propertyAccessor = propertyAccessor;
      cdoFeature = propertyAccessor.getTuplizer().getCDOClass().lookupFeature(propertyName);
      if (cdoFeature == null)
      {
        throw new IllegalStateException("Feature not found: " + propertyName);
      }
    }

    public CDORevisionPropertyAccessor getPropertyAccessor()
    {
      return propertyAccessor;
    }

    public CDOFeature getCDOFeature()
    {
      return cdoFeature;
    }
  }

  /**
   * @author Eike Stepper
   */
  public static class CDORevisionGetter extends BaseAccessor implements Getter
  {
    private static final long serialVersionUID = 1L;

    public CDORevisionGetter(CDORevisionPropertyAccessor propertyAccessor, String propertyName)
    {
      super(propertyAccessor, propertyName);
    }

    public Object get(Object target) throws HibernateException
    {
      InternalCDORevision revision = (InternalCDORevision)target;
      return revision.getValue(getCDOFeature());
    }

    @SuppressWarnings("unchecked")
    public Object getForInsert(Object target, Map mergeMap, SessionImplementor session) throws HibernateException
    {
      return get(target);
    }

    public Method getMethod()
    {
      return null;
    }

    public String getMethodName()
    {
      return null;
    }

    @SuppressWarnings("unchecked")
    public Class getReturnType()
    {
      return Object.class;
    }
  }

  /**
   * @author Eike Stepper
   */
  public static class CDORevisionSetter extends BaseAccessor implements Setter
  {
    private static final long serialVersionUID = 1L;

    public CDORevisionSetter(CDORevisionPropertyAccessor propertyAccessor, String propertyName)
    {
      super(propertyAccessor, propertyName);
    }

    public Method getMethod()
    {
      return null;
    }

    public String getMethodName()
    {
      return null;
    }

    public void set(Object target, Object value, SessionFactoryImplementor factory) throws HibernateException
    {
      InternalCDORevision revision = (InternalCDORevision)target;
      revision.setValue(getCDOFeature(), value);
    }
  }
}
