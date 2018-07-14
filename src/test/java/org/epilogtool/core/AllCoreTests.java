package org.epilogtool.core;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ LoadProjectTest.class, EpitheliumTest.class, IntegrationFunctionsTest.class, 
	ModelUpdateTest.class, NeighboursTest.class, InitialConditionTest.class, 
	PerturbationTest.class, EpitheliumCellTest.class, SimulationTest.class })
public class AllCoreTests {

}
