/**
 * Copyright (c) 2004 - 2009 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    Andre Dietisheim - initial API and implementation
 *
 * $Id: Net4jDefsFactory.java,v 1.2 2009-01-10 14:57:25 estepper Exp $
 */
package org.eclipse.net4j.defs;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc --> The <b>Factory</b> for the model. It provides a create method for each non-abstract class of
 * the model. <!-- end-user-doc -->
 * 
 * @see org.eclipse.net4j.defs.Net4jDefsPackage
 * @generated
 */
public interface Net4jDefsFactory extends EFactory
{
  /**
   * The singleton instance of the factory. <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @generated
   */
  Net4jDefsFactory eINSTANCE = org.eclipse.net4j.defs.impl.Net4jDefsFactoryImpl.init();

  /**
   * Returns a new object of class '<em>TCP Connector Def</em>'. <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @return a new object of class '<em>TCP Connector Def</em>'.
   * @generated
   */
  TCPConnectorDef createTCPConnectorDef();

  /**
   * Returns a new object of class '<em>TCP Acceptor Def</em>'. <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @return a new object of class '<em>TCP Acceptor Def</em>'.
   * @generated
   */
  TCPAcceptorDef createTCPAcceptorDef();

  /**
   * Returns a new object of class '<em>JVM Acceptor Def</em>'. <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @return a new object of class '<em>JVM Acceptor Def</em>'.
   * @generated
   */
  JVMAcceptorDef createJVMAcceptorDef();

  /**
   * Returns a new object of class '<em>JVM Connector Def</em>'. <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @return a new object of class '<em>JVM Connector Def</em>'.
   * @generated
   */
  JVMConnectorDef createJVMConnectorDef();

  /**
   * Returns a new object of class '<em>HTTP Connector Def</em>'. <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @return a new object of class '<em>HTTP Connector Def</em>'.
   * @generated
   */
  HTTPConnectorDef createHTTPConnectorDef();

  /**
   * Returns a new object of class '<em>TCP Selector Def</em>'. <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @return a new object of class '<em>TCP Selector Def</em>'.
   * @generated
   */
  TCPSelectorDef createTCPSelectorDef();

  /**
   * Returns a new object of class '<em>Buffer Pool Def</em>'. <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @return a new object of class '<em>Buffer Pool Def</em>'.
   * @generated
   */
  BufferPoolDef createBufferPoolDef();

  /**
   * Returns the package supported by this factory. <!-- begin-user-doc --> <!-- end-user-doc -->
   * 
   * @return the package supported by this factory.
   * @generated
   */
  Net4jDefsPackage getNet4jDefsPackage();

} // Net4jDefsFactory
