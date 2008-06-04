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
package org.eclipse.net4j.tests.signal;

import org.eclipse.net4j.signal.SignalProtocol;
import org.eclipse.net4j.signal.SignalReactor;

/**
 * @author Eike Stepper
 */
public class TestSignalProtocol extends SignalProtocol
{
  public static final String PROTOCOL_NAME = "signal.protocol";

  public static final short SIGNAL_INT = 1;

  public static final short SIGNAL_INT_FAIL = 2;

  public static final short SIGNAL_ARRAY = 3;

  public static final short SIGNAL_STRING = 4;

  public static final short SIGNAL_ASYNC = 5;

  public TestSignalProtocol()
  {
  }

  public String getType()
  {
    return PROTOCOL_NAME;
  }

  @Override
  protected SignalReactor createSignalReactor(short signalID)
  {
    switch (signalID)
    {
    case SIGNAL_INT:
      return new IntIndication();
    case SIGNAL_INT_FAIL:
      return new IntFailIndication();
    case SIGNAL_ARRAY:
      return new ArrayIndication();
    case SIGNAL_STRING:
      return new StringIndication();
    case SIGNAL_ASYNC:
      return new AsyncIndication();
    }
    return null;
  }
}
