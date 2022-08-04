package cn.tuyucheng.taketoday.tags;

import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
@SelectPackages("cn.tuyucheng.taketoday.tags")
@IncludeTags("UnitTest")
public class EmployeeDAOSuiteTest {

}