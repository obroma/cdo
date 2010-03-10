/**
 * Copyright (c) 2004 - 2010 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Andre Dietisheim - initial API and implementation
 */
package org.eclipse.emf.cdo.ui.internal.branch.layout;

import org.eclipse.emf.cdo.common.branch.CDOBranch;
import org.eclipse.emf.cdo.ui.internal.branch.item.AbstractBranchPointNode;
import org.eclipse.emf.cdo.ui.internal.branch.item.BranchTreeUtils;
import org.eclipse.emf.cdo.ui.internal.branch.item.RootNode;

import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.AbstractLayoutAlgorithm;
import org.eclipse.zest.layouts.dataStructures.DisplayIndependentRectangle;
import org.eclipse.zest.layouts.dataStructures.InternalNode;
import org.eclipse.zest.layouts.dataStructures.InternalRelationship;

/**
 * A layout algorythm that builds a tree of branch nodes. Nodes on the same branch are centered horizontally below each
 * other. Nodes on a new branch are shifted to the left/right - they sprout to the left/right. The description above
 * applies to a vertical layout of the branch tree. A horizontal strategy is not implemented yet.
 * 
 * @author Andre Dietisheim
 */
public class BranchTreeLayoutAlgorithm extends AbstractLayoutAlgorithm
{
  private static final int LAYOUT_STEPS = 2;

  private RootNode rootNode;

  private AbstractBranchPointNode latestNode;

  private DisplayIndependentRectangle layoutBounds = null;

  /**
   * Constructs a new TreeLayoutAlgorithm object.
   */
  public BranchTreeLayoutAlgorithm(int styles)
  {
    super(styles);
  }

  /**
   * Tree layout algorithm Constructor with NO Style
   */
  public BranchTreeLayoutAlgorithm()
  {
    this(LayoutStyles.NONE);
  }

  @Override
  public void setLayoutArea(double x, double y, double width, double height)
  {
    throw new RuntimeException();
  }

  @Override
  protected int getCurrentLayoutStep()
  {
    return 0;
  }

  @Override
  protected int getTotalNumberOfLayoutSteps()
  {
    return LAYOUT_STEPS;
  }

  @Override
  protected void preLayoutAlgorithm(InternalNode[] entitiesToLayout, InternalRelationship[] relationshipsToConsider,
      double x, double y, double width, double height)
  {
    layoutBounds = new DisplayIndependentRectangle(x, y, width, height);

    if (entitiesToLayout.length > 0)
    {
      initRootAndLatestNode(entitiesToLayout);
    }
  }

  /**
   * Searches the given entities and stores the root node and the node with the latest time stamp
   * 
   * @param entitiesToLayout
   *          the entities to layout
   * @see RootNode
   * @see AbstractBranchPointNode
   */
  private void initRootAndLatestNode(InternalNode[] entitiesToLayout)
  {
    for (InternalNode internalNode : entitiesToLayout)
    {
      AbstractBranchPointNode node = BranchTreeUtils.getBranchTreeNode(internalNode);
      if (node != null)
      {
        if (node instanceof RootNode)
        {
          rootNode = (RootNode)node.getLatter(rootNode);
        }
        latestNode = node.getLatter(latestNode);
      }
    }
  }

  @Override
  protected void applyLayoutInternal(InternalNode[] entitiesToLayout, InternalRelationship[] relationshipsToConsider,
      double boundsX, double boundsY, double boundsWidth, double boundsHeight)
  {
    if (entitiesToLayout.length > 0)
    {
      buildBranch(rootNode);
      fireProgressEvent(1, LAYOUT_STEPS);
      defaultFitWithinBounds(entitiesToLayout, layoutBounds);
    }
  }

  private BranchView buildBranch(AbstractBranchPointNode branchRootNode)
  {
    CDOBranch branch = branchRootNode.getBranch();
    return new BranchView(branch, branchRootNode, new BranchLayoutStrategy());
  }

  @Override
  protected void postLayoutAlgorithm(InternalNode[] entitiesToLayout, InternalRelationship[] relationshipsToConsider)
  {
    updateLayoutLocations(entitiesToLayout);
    fireProgressEvent(LAYOUT_STEPS, LAYOUT_STEPS);
  }

  @Override
  protected boolean isValidConfiguration(boolean asynchronous, boolean continueous)
  {
    if (asynchronous && continueous)
    {
      return false;
    }
    else if (asynchronous && !continueous)
    {
      return true;
    }
    else if (!asynchronous && continueous)
    {
      return false;
    }
    else if (!asynchronous && !continueous)
    {
      return true;
    }

    return false;
  }

}
