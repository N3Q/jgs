package tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ LocalMapTest.class, 
				tests.handleStmtTests.AssignLocalsSuccess.class,
				tests.handleStmtTests.AssignLocalsFail.class,
				tests.handleStmtTests.InternalMethods.class,
				tests.handleStmtTests.AssignFieldsSuccess.class,
				tests.handleStmtTests.AssignFieldsFail.class,
				tests.handleStmtTests.InvokeSuccess.class,
				tests.handleStmtTests.InvokeFail.class,
				tests.handleStmtTests.ReturnStmtSuccess.class,
				tests.handleStmtTests.ReturnStmtFail.class,
				tests.handleStmtTests.IfStmtSuccess.class,
				tests.handleStmtTests.IfStmtFail.class,
				tests.handleStmtTests.WhileStmtSuccess.class,
				tests.handleStmtTests.WhileStmtFail.class,
				tests.handleStmtTests.SwitchStmtSuccess.class,
				tests.handleStmtTests.SwitchStmtFail.class,
				ObjectMapTest.class, 
				JimpleInjectorTest.class })

public class AllTests {

}
