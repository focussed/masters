package tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	// add test classes here
	AttemptsAtCommsNotRegYet.class,
	CruiserTests.class,
	LeavePort.class,
	LeavePort2Ships.class,
	LeavePort4Ships.class,
	RegisterTests.class,
	RequestToBerthTests.class,
	RogueCommands.class,
	RoughSeas.class,
	SignalandNackTests.class,
	CodeCoverageTest.class,
})

public class AllTests {
	// leave empty

}
