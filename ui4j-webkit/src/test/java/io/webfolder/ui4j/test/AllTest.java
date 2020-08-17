package io.webfolder.ui4j.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
        ChildTest.class,
        ClearCookiesTest.class,
        ClientSideCookieListTest.class,
        ClientSideCookieTest.class,
        DialogTest.class,
        ElementTest.class,
        FormClearTest.class,
        FrameTest.class,
        HeadTest.class,
        HiddenTest.class,
        ImageOffsetTest.class,
        JavaScriptEngineTest.class,
        JSObjectTest.class,
        ReadTest.class,
        SetInnerHTMLTest.class,
        SiblingTest.class,
        SizzleTest.class,
        UserAgentTest.class,
        WaitTest.class
    })
public class AllTest {

}
