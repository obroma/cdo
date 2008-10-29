/***************************************************************************
 * Copyright (c) 2004 - 2008 Eike Stepper, Germany.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    Simon McDuff - initial API and implementation
 *    Eike Stepper - maintenance
 **************************************************************************/
package org.eclipse.emf.cdo.internal.server;

import org.eclipse.emf.cdo.common.id.CDOID;
import org.eclipse.emf.cdo.common.model.CDOFeature;
import org.eclipse.emf.cdo.common.revision.CDORevision;
import org.eclipse.emf.cdo.server.IStoreAccessor;
import org.eclipse.emf.cdo.spi.common.InternalCDORevision;

import java.util.List;

/**
 * @author Simon McDuff
 */
public class MEMStoreChunkReader extends StoreChunkReader
{
  /**
   * @since 2.0
   */
  public MEMStoreChunkReader(IStoreAccessor accessor, CDORevision revision, CDOFeature feature)
  {
    super(accessor, revision, feature);
  }

  public List<Chunk> executeRead()
  {
    MEMStore store = getAccessor().getStore();
    List<Chunk> chunks = getChunks();
    for (Chunk chunk : chunks)
    {
      int startIndex = chunk.getStartIndex();
      InternalCDORevision revision = (InternalCDORevision)store.getRevision(getRevision().getID());
      for (int i = 0; i < chunk.size(); i++)
      {
        Object object = revision.get(getFeature(), startIndex + i);
        chunk.addID(i, (CDOID)object);
      }
    }

    return chunks;
  }

  /**
   * @since 2.0
   */
  @Override
  public MEMStoreAccessor getAccessor()
  {
    return (MEMStoreAccessor)super.getAccessor();
  }
}
