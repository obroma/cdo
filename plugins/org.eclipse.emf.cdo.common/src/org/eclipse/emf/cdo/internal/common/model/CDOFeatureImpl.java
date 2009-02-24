/**
 * Copyright (c) 2004 - 2009 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.emf.cdo.internal.common.model;

import org.eclipse.emf.cdo.common.io.CDODataInput;
import org.eclipse.emf.cdo.common.io.CDODataOutput;
import org.eclipse.emf.cdo.common.model.EClass;
import org.eclipse.emf.cdo.common.model.EClassProxy;
import org.eclipse.emf.cdo.common.model.EClassRef;
import org.eclipse.emf.cdo.common.model.EPackage;
import org.eclipse.emf.cdo.common.model.CDOPackageManager;
import org.eclipse.emf.cdo.common.model.CDOType;
import org.eclipse.emf.cdo.internal.common.bundle.OM;
import org.eclipse.emf.cdo.spi.common.model.InternalEClass;
import org.eclipse.emf.cdo.spi.common.model.InternalCDOFeature;
import org.eclipse.emf.cdo.spi.common.revision.InternalCDORevision;

import org.eclipse.net4j.util.om.trace.ContextTracer;

import java.io.IOException;
import java.text.MessageFormat;

/**
 * @author Eike Stepper
 */
public class EStructuralFeatureImpl extends EModelElementImpl implements InternalCDOFeature
{
  private static final int UNKNOWN_FEATURE_INDEX = Integer.MIN_VALUE;

  private static final ContextTracer MODEL_TRACER = new ContextTracer(OM.DEBUG_MODEL, EStructuralFeatureImpl.class);

  private static final ContextTracer PROTOCOL_TRACER = new ContextTracer(OM.DEBUG_PROTOCOL, EStructuralFeatureImpl.class);

  private EClass containingClass;

  private int featureID;

  private int featureIndex = UNKNOWN_FEATURE_INDEX;

  private CDOType type;

  private boolean many;

  private boolean containment;

  private EClassProxy referenceTypeProxy;

  private Object defaultValue;

  /**
   * Creates an uninitialized instance.
   */
  public EStructuralFeatureImpl()
  {
  }

  /**
   * Creates an attribute feature.
   */
  public EStructuralFeatureImpl(EClass containingClass, int featureID, String name, CDOType type, Object defaultValue,
      boolean many)
  {
    super(name);
    if (type == CDOType.OBJECT)
    {
      throw new IllegalArgumentException("type == OBJECT");
    }

    this.containingClass = containingClass;
    this.featureID = featureID;
    this.type = type;
    this.defaultValue = defaultValue;
    this.many = many;
    if (MODEL_TRACER.isEnabled())
    {
      MODEL_TRACER.format("Created {0}", this);
    }
  }

  /**
   * Creates a reference feature.
   */
  public EStructuralFeatureImpl(EClass containingClass, int featureID, String name, EClassProxy referenceTypeProxy,
      boolean many, boolean containment)
  {
    super(name);
    if (referenceTypeProxy == null)
    {
      throw new IllegalArgumentException("referenceTypeProxy == null");
    }

    this.containingClass = containingClass;
    this.featureID = featureID;
    type = CDOType.OBJECT;
    this.many = many;
    this.containment = containment;
    this.referenceTypeProxy = referenceTypeProxy;
    if (MODEL_TRACER.isEnabled())
    {
      MODEL_TRACER.format("Created {0}", this);
    }
  }

  /**
   * Reads a feature from a stream.
   */
  public EStructuralFeatureImpl(EClass containingClass, CDODataInput in) throws IOException
  {
    this.containingClass = containingClass;
    read(in);
  }

  @Override
  public void read(CDODataInput in) throws IOException
  {
    super.read(in);
    featureID = in.readInt();
    type = in.readCDOType();
    if (in.readBoolean())
    {
      defaultValue = type.readValue(in);
    }

    many = in.readBoolean();
    containment = in.readBoolean();
    if (PROTOCOL_TRACER.isEnabled())
    {
      PROTOCOL_TRACER.format("Read feature: ID={0}, name={1}, type={2}, many={3}, containment={4}", featureID,
          getName(), type, many, containment);
    }

    if (isReference())
    {
      EClassRef classRef = in.readEClassRef();
      if (PROTOCOL_TRACER.isEnabled())
      {
        PROTOCOL_TRACER.format("Read reference type: classRef={0}", classRef);
      }

      referenceTypeProxy = new EClassProxy(classRef, containingClass.getContainingPackage().getPackageManager());
    }
  }

  @Override
  public void write(CDODataOutput out) throws IOException
  {
    if (PROTOCOL_TRACER.isEnabled())
    {
      PROTOCOL_TRACER.format("Writing feature: ID={0}, name={1}, type={2}, many={3}, containment={4}", featureID,
          getName(), type, many, containment);
    }

    super.write(out);
    out.writeInt(featureID);
    out.writeCDOType(type);
    if (defaultValue != null)
    {
      out.writeBoolean(true);
      type.writeValue(out, defaultValue);
    }
    else
    {
      out.writeBoolean(false);
    }

    out.writeBoolean(many);
    out.writeBoolean(containment);

    if (isReference())
    {
      EClassRef classRef = referenceTypeProxy.getClassRef();
      if (PROTOCOL_TRACER.isEnabled())
      {
        PROTOCOL_TRACER.format("Writing reference type: classRef={0}", classRef);
      }

      out.writeEClassRef(classRef);
    }
  }

  public CDOPackageManager getPackageManager()
  {
    return getContainingPackage().getPackageManager();
  }

  public EPackage getContainingPackage()
  {
    return containingClass.getContainingPackage();
  }

  public EClass getContainingClass()
  {
    return containingClass;
  }

  public void setContainingClass(EClass containingClass)
  {
    this.containingClass = containingClass;
  }

  public int getFeatureID()
  {
    return featureID;
  }

  public void setFeatureID(int featureID)
  {
    this.featureID = featureID;
  }

  public int getFeatureIndex()
  {
    if (featureIndex == UNKNOWN_FEATURE_INDEX)
    {
      featureIndex = ((InternalEClass)containingClass).getFeatureIndex(featureID);
    }

    return featureIndex;
  }

  public void setFeatureIndex(int featureIndex)
  {
    this.featureIndex = featureIndex;
  }

  public String getQualifiedName()
  {
    return getContainingClass().getQualifiedName() + "." + getName();
  }

  public CDOType getType()
  {
    return type;
  }

  public void setType(CDOType type)
  {
    this.type = type;
  }

  public boolean isMany()
  {
    return many;
  }

  public Object getDefaultValue()
  {
    return defaultValue;
  }

  public void setDefaultValue(Object defaultValue)
  {
    this.defaultValue = defaultValue;
  }

  public void setMany(boolean many)
  {
    this.many = many;
  }

  public boolean isReference()
  {
    return type == CDOType.OBJECT;
  }

  public boolean isContainment()
  {
    return containment;
  }

  public void setContainment(boolean containment)
  {
    this.containment = containment;
  }

  public EClass getReferenceType()
  {
    if (referenceTypeProxy == null)
    {
      return null;
    }

    return referenceTypeProxy.getCdoClass();
  }

  public void setReferenceType(EClassRef cdoClassRef)
  {
    referenceTypeProxy = new EClassProxy(cdoClassRef, getPackageManager());
  }

  public EClassProxy getReferenceTypeProxy()
  {
    return referenceTypeProxy;
  }

  @Override
  public String toString()
  {
    if (type == CDOType.OBJECT)
    {
      return MessageFormat.format("EStructuralFeature(ID={0}, name={1}, type={2})", featureID, getName(), referenceTypeProxy);
    }
    else
    {
      return MessageFormat.format("EStructuralFeature(ID={0}, name={1}, type={2})", featureID, getName(), type);
    }
  }

  public Object readValue(CDODataInput in) throws IOException
  {
    CDOType type = getType();
    if (type.canBeNull() && !isMany())
    {
      if (in.readBoolean())
      {
        return InternalCDORevision.NIL;
      }
    }

    return type.readValue(in);
  }

  public void writeValue(CDODataOutput out, Object value) throws IOException
  {
    // TODO We could certainly optimized this: When a feature is a reference, NIL is only possible in the case where
    // unsettable == true. (TO be verified)
    if (type.canBeNull())
    {
      if (!isMany())
      {
        if (value == InternalCDORevision.NIL)
        {
          out.writeBoolean(true);
          return;
        }
        else
        {
          out.writeBoolean(false);
        }
      }
    }
    else
    {
      if (value == null)
      {
        value = getDefaultValue();
      }
    }

    type.writeValue(out, value);
  }
}
