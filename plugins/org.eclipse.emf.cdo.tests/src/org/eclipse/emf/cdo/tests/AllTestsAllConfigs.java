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
package org.eclipse.emf.cdo.tests;

import org.eclipse.emf.cdo.tests.bugzilla.Bugzilla_241464_Test;
import org.eclipse.emf.cdo.tests.bugzilla.Bugzilla_243310_Test;
import org.eclipse.emf.cdo.tests.bugzilla.Bugzilla_246442_Test;
import org.eclipse.emf.cdo.tests.bugzilla.Bugzilla_246456_Test;
import org.eclipse.emf.cdo.tests.bugzilla.Bugzilla_246622_Test;
import org.eclipse.emf.cdo.tests.config.ConfigTest;
import org.eclipse.emf.cdo.tests.config.ConfigTestSuite;

import java.util.List;

import junit.framework.Test;

/**
 * @author Eike Stepper
 */
public class AllTestsAllConfigs extends ConfigTestSuite
{
  public static Test suite()
  {
    return new AllTestsAllConfigs().getTestSuite("CDO Tests (All Configs)");
  }

  @Override
  protected void initTestClasses(List<Class<? extends ConfigTest>> testClasses)
  {
    // General
    testClasses.add(InitialTest.class);
    testClasses.add(ComplexTest.class);
    testClasses.add(AttributeTest.class);
    testClasses.add(EnumTest.class);
    testClasses.add(StateMachineTest.class);
    testClasses.add(ViewTest.class);
    testClasses.add(ResourceTest.class);
    testClasses.add(ContainmentTest.class);
    testClasses.add(InvalidationTest.class);
    testClasses.add(RollbackTest.class);
    testClasses.add(CrossReferenceTest.class);
    testClasses.add(ChunkingTest.class);
    testClasses.add(ChunkingWithMEMTest.class);
    testClasses.add(TransactionDeadLockTest.class);
    testClasses.add(PackageRegistryTest.class);
    testClasses.add(MetaTest.class);
    testClasses.add(RevisionDeltaWithDeltaSupportTest.class);
    testClasses.add(RevisionDeltaWithoutDeltaSupportTest.class);
    testClasses.add(IndexReconstructionTest.class);
    testClasses.add(AutoAttacherTest.class);
    testClasses.add(SavepointTest.class);
    testClasses.add(ChangeSubscriptionTest.class);
    testClasses.add(DetachTest.class);
    testClasses.add(ExternalReferenceTest.class);
    testClasses.add(XATransactionTest.class);
    testClasses.add(TransactionHandlerTest.class);

    // Specific for MEMStore
    testClasses.add(QueryTest.class);

    // Bugzilla verifications
    testClasses.add(Bugzilla_241464_Test.class);
    testClasses.add(Bugzilla_243310_Test.class);
    testClasses.add(Bugzilla_246442_Test.class);
    testClasses.add(Bugzilla_246456_Test.class);
    testClasses.add(Bugzilla_246622_Test.class);

    // TODO testClasses.add(NonCDOResourceTest.class);
    // TODO testClasses.add(GeneratedEcoreTest.class);
  }
}
