/**
 * <copyright>
 * </copyright>
 *
 * $Id: RefMultiContained.java,v 1.2.8.3 2008-09-17 13:04:14 estepper Exp $
 */
package org.eclipse.emf.cdo.tests.model4;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Ref Multi Contained</b></em>'. <!-- end-user-doc
 * -->
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.emf.cdo.tests.model4.RefMultiContained#getElements <em>Elements</em>}</li>
 * </ul>
 * </p>
 * 
 * @see org.eclipse.emf.cdo.tests.model4.model4Package#getRefMultiContained()
 * @model
 * @generated
 */
public interface RefMultiContained extends EObject
{
  /**
   * Returns the value of the '<em><b>Elements</b></em>' containment reference list. The list contents are of type
   * {@link org.eclipse.emf.cdo.tests.model4.MultiContainedElement}. It is bidirectional and its opposite is '
   * {@link org.eclipse.emf.cdo.tests.model4.MultiContainedElement#getParent <em>Parent</em>}'. <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Elements</em>' containment reference list isn't clear, there really should be more of a
   * description here...
   * </p>
   * <!-- end-user-doc -->
   * 
   * @return the value of the '<em>Elements</em>' containment reference list.
   * @see org.eclipse.emf.cdo.tests.model4.model4Package#getRefMultiContained_Elements()
   * @see org.eclipse.emf.cdo.tests.model4.MultiContainedElement#getParent
   * @model opposite="parent" containment="true"
   * @generated
   */
  EList<MultiContainedElement> getElements();

} // RefMultiContained
