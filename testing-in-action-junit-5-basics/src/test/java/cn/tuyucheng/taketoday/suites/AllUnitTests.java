package cn.tuyucheng.taketoday.suites;

import cn.tuyucheng.taketoday.ExceptionUnitTests;
import cn.tuyucheng.taketoday.FirstUnitTests;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.runner.RunWith;

// @SelectPackages("cn.tuyucheng.taketoday")
@RunWith(JUnitPlatform.class)
@SelectClasses({FirstUnitTests.class, ExceptionUnitTests.class})
public class AllUnitTests {

}