/*
 * Copyright (c) 2004 - 2012 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.emf.cdo.compare;

import org.eclipse.emf.cdo.CDOObject;
import org.eclipse.emf.cdo.common.branch.CDOBranch;
import org.eclipse.emf.cdo.common.branch.CDOBranchPoint;
import org.eclipse.emf.cdo.common.id.CDOID;
import org.eclipse.emf.cdo.compare.CDOCompare.CDOIDFunction;
import org.eclipse.emf.cdo.compare.CDOComparisonScope.AllContents;
import org.eclipse.emf.cdo.compare.CDOComparisonScope.Minimal;
import org.eclipse.emf.cdo.eresource.CDOResource;
import org.eclipse.emf.cdo.eresource.CDOResourceFolder;
import org.eclipse.emf.cdo.eresource.CDOResourceNode;
import org.eclipse.emf.cdo.spi.common.branch.CDOBranchUtil;
import org.eclipse.emf.cdo.transaction.CDOTransaction;
import org.eclipse.emf.cdo.view.CDOView;

import org.eclipse.emf.compare.Comparison;
import org.eclipse.emf.compare.Match;
import org.eclipse.emf.compare.match.eobject.IEObjectMatcher;
import org.eclipse.emf.compare.scope.IComparisonScope;
import org.eclipse.emf.ecore.EObject;

import java.util.Set;

/**
 * Provides static factory methods that return CDO-scoped {@link Comparison comparisons}.
 * <p>
 * Two different {@link IComparisonScope scopes} are supported:
 * <ul>
 * <li>{@link AllContents CDOComparisonScope.AllContents} takes an arbitrary {@link CDOObject object} (including {@link CDOResourceNode resource nodes})
 * and returns {@link Match matches} for <b>all</b> elements of its {@link EObject#eAllContents() content tree}. This scope has the advantage that the comparison can
 * be rooted at specific objects that are different from (below of) the root resource. The disadvantage is that all the transitive children of this specific object are
 * matched, whether they differ or not. Major parts of huge repositories can be loaded to the client side easily, if no attention is paid.
 * The following method returns comparisons that are based on this scope algorithm:
 * <ul>
 * <li>{@link #compare(CDOObject, CDOView, CDOView[])}
 * </ul>
 * <li>{@link Minimal CDOComparisonScope.Minimal} takes a {@link CDOView view}/{@link CDOTransaction transaction}
 * and returns {@link Match matches} only for the <b>changed</b> elements of the entire content tree of its {@link CDOView#getRootResource() root resource}.
 * The advantage of this scope is that CDO-specific mechanisms are used to efficiently (remotely) determine the set of changed objects. Only those and their container
 * objects are considered as matches, making this scope scale seamlessly with the overall size of a repository.
 * The following method returns comparisons that are based on this scope algorithm:
 * <ul>
 * <li>{@link #compare(CDOView, CDOView, CDOView[])}
 * </ul>
 * </ul>
 * The {@link IComparisonScope#getOrigin() origin side} of a comparison is automatically {@link CDOBranchUtil#getAncestor(CDOBranchPoint, CDOBranchPoint) determined} by
 * inspecting the {@link CDOBranch branch tree} and used if its different from the left or right side.
 * <p>
 * The {@link IEObjectMatcher matcher} used by the comparisons is based on an {@link CDOIDFunction ID function} that considers the {@link CDOID}s of the {@link CDOObject objects}.
 * {@link CDOResource Resources} and {@link CDOResourceFolder folders} are treated as normal {@link EObject}s.
 *
 * @author Eike Stepper
 */
public final class CDOCompareUtil
{
  private CDOCompareUtil()
  {
  }

  public static Comparison compare(IComparisonScope scope)
  {
    return new CDOCompare().compare(scope);
  }

  /**
   * Takes an arbitrary {@link CDOObject object} (including {@link CDOResourceNode resource nodes}) and returns {@link Match matches} for <b>all</b> elements of its {@link EObject#eAllContents() content tree}. This scope has the advantage that the comparison can
   * be rooted at specific objects that are different from (below of) the root resource. The disadvantage is that all the transitive children of this specific object are
   * matched, whether they differ or not. Major parts of huge repositories can be loaded to the client side easily, if no attention is paid.
   */
  public static Comparison compare(CDOObject left, CDOView rightView, CDOView[] originView)
  {
    return compare(CDOComparisonScope.AllContents.create(left, rightView, originView));
  }

  /**
   * Takes a {@link CDOView view}/{@link CDOTransaction transaction}
   * and returns {@link Match matches} only for the <b>changed</b> elements of the entire content tree of its {@link CDOView#getRootResource() root resource}.
   * The advantage of this scope is that CDO-specific mechanisms are used to efficiently (remotely) determine the set of changed objects. Only those and their container
   * objects are considered as matches, making this scope scale seamlessly with the overall size of a repository.
   */
  public static Comparison compare(CDOView leftView, CDOView rightView, CDOView[] originView)
  {
    return compare(CDOComparisonScope.Minimal.create(leftView, rightView, originView));
  }

  public static Comparison compare(CDOView leftView, CDOView rightView, CDOView[] originView, Set<CDOID> ids)
  {
    return compare(CDOComparisonScope.Minimal.create(leftView, rightView, originView, ids));
  }

  public static Comparison compareUncommittedChanges(CDOTransaction transaction)
  {
    return compare(CDOComparisonScope.Minimal.create(transaction));
  }
}
