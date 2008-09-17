/**
 * <copyright>
 * </copyright>
 *
 * $Id: ImplSingleRefNonContainerNPLImpl.java,v 1.1.2.1 2008-09-17 13:04:14 estepper Exp $
 */
package org.eclipse.emf.cdo.tests.legacy.model4.impl;

import org.eclipse.emf.cdo.tests.legacy.model4.model4Package;
import org.eclipse.emf.cdo.tests.model4.ImplSingleRefNonContainerNPL;
import org.eclipse.emf.cdo.tests.model4interfaces.IContainedElementNoParentLink;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Impl Single Ref Non Container NPL</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.emf.cdo.tests.legacy.model4.impl.ImplSingleRefNonContainerNPLImpl#getElement <em>Element</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ImplSingleRefNonContainerNPLImpl extends EObjectImpl implements ImplSingleRefNonContainerNPL
{
  /**
   * The cached value of the '{@link #getElement() <em>Element</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getElement()
   * @generated
   * @ordered
   */
  protected IContainedElementNoParentLink element;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected ImplSingleRefNonContainerNPLImpl()
  {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected EClass eStaticClass()
  {
    return model4Package.Literals.IMPL_SINGLE_REF_NON_CONTAINER_NPL;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IContainedElementNoParentLink getElement()
  {
    eFireRead(model4Package.IMPL_SINGLE_REF_NON_CONTAINER_NPL__ELEMENT);
    if (element != null && element.eIsProxy())
    {
      InternalEObject oldElement = (InternalEObject)element;
      element = (IContainedElementNoParentLink)eResolveProxy(oldElement);
      if (element != oldElement)
      {
        if (eNotificationRequired())
          eNotify(new ENotificationImpl(this, Notification.RESOLVE,
              model4Package.IMPL_SINGLE_REF_NON_CONTAINER_NPL__ELEMENT, oldElement, element));
      }
    }
    return element;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public IContainedElementNoParentLink basicGetElement()
  {
    eFireRead(model4Package.IMPL_SINGLE_REF_NON_CONTAINER_NPL__ELEMENT);
    return element;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setElement(IContainedElementNoParentLink newElement)
  {
    eFireWrite(model4Package.IMPL_SINGLE_REF_NON_CONTAINER_NPL__ELEMENT);
    IContainedElementNoParentLink oldElement = element;
    element = newElement;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, model4Package.IMPL_SINGLE_REF_NON_CONTAINER_NPL__ELEMENT,
          oldElement, element));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object eGet(int featureID, boolean resolve, boolean coreType)
  {
    switch (featureID)
    {
    case model4Package.IMPL_SINGLE_REF_NON_CONTAINER_NPL__ELEMENT:
      if (resolve)
        return getElement();
      return basicGetElement();
    }
    return super.eGet(featureID, resolve, coreType);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eSet(int featureID, Object newValue)
  {
    switch (featureID)
    {
    case model4Package.IMPL_SINGLE_REF_NON_CONTAINER_NPL__ELEMENT:
      setElement((IContainedElementNoParentLink)newValue);
      return;
    }
    super.eSet(featureID, newValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eUnset(int featureID)
  {
    switch (featureID)
    {
    case model4Package.IMPL_SINGLE_REF_NON_CONTAINER_NPL__ELEMENT:
      setElement((IContainedElementNoParentLink)null);
      return;
    }
    super.eUnset(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public boolean eIsSet(int featureID)
  {
    switch (featureID)
    {
    case model4Package.IMPL_SINGLE_REF_NON_CONTAINER_NPL__ELEMENT:
      return element != null;
    }
    return super.eIsSet(featureID);
  }

} //ImplSingleRefNonContainerNPLImpl
