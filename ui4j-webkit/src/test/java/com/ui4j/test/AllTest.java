package com.ui4j.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ ElementTest.class,
                SizzleTest.class, DialogTest.class,
                JavaScriptEngineTest.class, UserAgentTest.class,
                WaitTest.class, ChildTest.class })
public class AllTest {

}
