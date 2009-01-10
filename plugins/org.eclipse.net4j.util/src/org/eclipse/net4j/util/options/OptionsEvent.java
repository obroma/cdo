/***************************************************************************
 * Copyright (c) 2004 - 2009 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Victor Roldan Betancort - initial API and implementation
 *    Eike Stepper - maintenance
 **************************************************************************/
package org.eclipse.net4j.util.options;

import org.eclipse.net4j.util.event.Event;

/**
 * @author Victor Roldan Betancort
 * @since 2.0
 */
public class OptionsEvent extends Event implements IOptionsEvent
{
  private static final long serialVersionUID = 1L;

  public OptionsEvent(IOptions source)
  {
    super(source);
  }

  @Override
  public IOptions getSource()
  {
    return getSource();
  }
}
