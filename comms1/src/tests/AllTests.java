package tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	// add test classes here
	RegisterTests.class,
	RequestToBerthTests.class,
	RoughSeas.class,
	LeavePort.class,
	LeavePort4Ships.class,
	AttemptsAtCommsNotRegYet.class,
	CruiserTests.class,
	SignalandNackTests.class,
	CodeCoverageTest.class
})

public class AllTests {
	// leave empty

}
