## 1. 概述

在本文中，我们将介绍JUnit 5测试库中的Extension模型。顾名思义，Junit 5中Extension的目的是扩展测试类或方法的行为，并且这些可以重复用于多个测试。

在Junit 5之前，JUnit 4版本使用两种类型的组件来扩展测试：测试Runner和Rule。
相比之下，JUnit 5通过引入一个概念来简化扩展机制：Extension API。

## 2. Junit 5扩展模型

JUnit 5 Extension与测试执行中的某个事件相关，称为扩展点。当达到某个生命周期阶段时，JUnit engine会调用已注册的Extension。

我们可以使用五种主要类型的扩展点：

+ 测试实例后处理
+ 条件测试执行
+ 生命周期回调
+ 参数解析
+ 异常处理

我会在下面的章节中详细介绍其中的每一种。

## 3. maven依赖

首先，让我们添加所需的项目依赖，使用Junit 5主要的依赖是junit-jupiter-engine，另外两个用作工具库：

```
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter-engine</artifactId>
    <version>5.8.1</version>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.apache.logging.log4j</groupId>
    <artifactId>log4j-core</artifactId>
    <version>2.8.2</version>
</dependency>
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <version>1.4.200</version>
</dependency>
```

## 4. 创建Junit 5 Extension

要创建JUnit 5 Extension，我们需要定义一个类，该类实现与JUnit 5扩展点对应的一个或多个接口。所有这些接口都继承了主Extension接口，它只是一个标记接口。

### 4.1 TestInstancePostProcessor Extension

这种类型的Extension在创建测试实例后执行。要实现的接口是TestInstancePostProcessor，
它有一个postProcessTestInstance()方法可以重写。

此扩展的典型用例是将依赖项注入实例。例如，让我们创建一个实例化Logger对象的Extension，然后在测试实例上调用setLogger()方法：

```java
public class LoggingExtension implements TestInstancePostProcessor {

    @Override
    public void postProcessTestInstance(Object testInstance, ExtensionContext extensionContext) throws Exception {
        Logger logger = LoggerFactory.getLogger(testInstance.getClass());
        testInstance.getClass().getMethod("setLogger", Logger.class).invoke(testInstance, logger);
    }
}
```

从上面可以看出，postProcessTestInstance()方法提供了对测试实例(即testInstance参数)的访问，
并使用反射机制调用了测试类的setLogger()方法。

```java

@ExtendWith(LoggingExtension.class)
public class EmployeesUnitTest {
    private Logger logger;

    public void setLogger(Logger logger) {
        this.logger = logger;
    }
}
```

### 4.2 Conditional Test Execution

JUnit 5提供了一种可以控制是否应该运行测试的Extension，这是通过实现ExecutionCondition接口来定义的。

让我们创建一个实现此接口并重写evaluateExecutionCondition()方法的EnvironmentExtension类。

该方法验证当前属性env的值是否等于“qa”，如果是则在这种情况下禁用测试：

```java
public class EnvironmentExtension implements ExecutionCondition {

    @Override
    public ConditionEvaluationResult evaluateExecutionCondition(ExtensionContext extensionContext) {
        Properties properties = new Properties();
        try {
            properties.load(ExpandVetoException.class.getResourceAsStream("application.properties"));
            String env = properties.getProperty("env");
            if ("qa".equalsIgnoreCase(env))
                return ConditionEvaluationResult.disabled("Test disabled on QA environment");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ConditionEvaluationResult.enabled("Test enabled on QA environment");
    }
}
```

因此，使用@ExtendWith注解注册了此扩展的测试将不会在“qa”环境中运行。

**如果我们不想验证条件，我们可以通过将junit.conditions.deactivate设置为匹配条件的模式来停用它**。

这可以通过使用-Djunit.conditions.deactivate=<pattern\>属性启动JVM或通过向LauncherDiscoveryRequest添加配置参数来实现：

```java
public class TestLauncher {
    public static void main(String[] args) {

        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
                .selectors(selectClass("cn.tuyucheng.taketoday.EmployeesUnitTest"))
                .configurationParameter("junit.conditions.deactivate", "cn.tuyucheng.taketoday.extensions.*")
                .configurationParameter("junit.jupiter.extensions.autodetection.enabled", "true")
                .build();

        TestPlan plan = LauncherFactory.create().discover(request);
        Launcher launcher = LauncherFactory.create();
        SummaryGeneratingListener summaryGeneratingListener = new SummaryGeneratingListener();
        launcher.execute(request, summaryGeneratingListener);
        launcher.execute(request);
        summaryGeneratingListener.getSummary().printTo(new PrintWriter(System.out));
    }
}
```

### 4.3 生命周期回调

这组扩展与测试生命周期中的事件相关，可以通过实现以下接口来定义：

+ BeforeAllCallback和AfterAllCallback - 在所有测试方法执行之前和之后执行。
+ BeforeEachCallBack和AfterEachCallback - 在每个测试方法之前和之后执行。
+ BeforeTestExecutionCallback和AfterTestExecutionCallback - 在测试方法之前和之后立即执行。

如果测试类中本身定义了诸如@BeforeEach这样的生命周期方法，则执行顺序为：

1. BeforeAllCallback
2. @BeforeAll
3. BeforeEachCallback
4. @BeforeEach
5. BeforeTestExecutionCallback
6. @Test
7. AfterTestExecutionCallback
8. @AfterEach
9. AfterEachCallback
10. @AfterAll
11. AfterAllCallback

对于我们的例子，让我们定义一个类来实现其中一些接口并控制使用JDBC访问数据库的测试的行为。

首先，我们创建一个简单的Employee实体：

```java
public class Employee {
    private long id;
    private String firstName;
    // constructors, getters, setters
}
```

我们还需要一个基于properties文件创建JDBC Connection的工具类。

```java
public class JdbcConnectionUtil {

    private static Connection con;

    public static Connection getConnection() {
        if (con == null || isClosed(con)) {
            try {
                Properties props = new Properties();
                props.load(JdbcConnectionUtil.class.getResourceAsStream("jdbc.properties"));
                String jdbcUrl = props.getProperty("jdbc.url");
                String driver = props.getProperty("jdbc.driver");
                String username = props.getProperty("jdbc.user");
                String password = props.getProperty("jdbc.password");
                con = getConnection(jdbcUrl, driver, username, password);
                return con;
            } catch (IOException exc) {
                exc.printStackTrace();
            }
            return null;
        }
        return con;
    }

    private static boolean isClosed(Connection con) {
        try {
            return con.isClosed();
        } catch (SQLException e) {
            return true;
        }
    }
}
```

最后，我们添加一个简单的基于JDBC的Dao，用于处理Employee实体：

```java
public class EmployeeJdbcDao {
    private Connection con;

    public EmployeeJdbcDao(Connection con) {
        this.con = con;
    }

    public void createTable() throws SQLException {
        // ...
    }

    public void add(Employee emp) throws SQLException {
        // ...
    }

    public List<Employee> findAll() throws SQLException {
        // ...
    }
}
```

**接下来我们创建一个Extension，它实现了一些生命周期接口**：

```java
public class EmployeeDatabaseSetupExtension implements
        BeforeAllCallback, AfterAllCallback, BeforeEachCallback, AfterEachCallback {
    // ...
}
```

这些接口中的每一个都包含一个我们需要重写的方法。

对于BeforeAllCallback接口，我们需要重写beforeAll()方法，并在执行任何测试方法之前添加创建employee表的逻辑：

```java
public class EmployeeDatabaseSetupExtension implements BeforeAllCallback, AfterAllCallback, BeforeEachCallback, AfterEachCallback {

    private Connection con;
    private EmployeeJdbcDao employeeDao;

    public EmployeeDatabaseSetupExtension() {
        con = JdbcConnectionUtil.getConnection();
        employeeDao = new EmployeeJdbcDao(con);
    }

    @Override
    public void beforeAll(ExtensionContext context) throws SQLException {
        employeeDao.createTable();
    }
}
```

接下来，我们使用BeforeEachCallback和AfterEachCallback将每个测试方法包装在一个事务中。
这样做的目的是回滚在测试方法中执行的对数据库的任何更改，以便下一个测试在干净的数据库上运行。

在beforeEach()方法中，我们将创建一个savePoint，用于将数据库的状态回滚到此处：

```java
public class EmployeeDatabaseSetupExtension implements BeforeAllCallback, AfterAllCallback, BeforeEachCallback, AfterEachCallback {
    private Savepoint savepoint;

    @Override
    public void beforeEach(ExtensionContext context) throws SQLException {
        con.setAutoCommit(false);
        savepoint = con.setSavepoint("before");
    }
}
```

然后，在afterEach()方法中，我们将回滚在测试方法执行期间所做的数据库更改：

```java
public class EmployeeDatabaseSetupExtension implements BeforeAllCallback, AfterAllCallback, BeforeEachCallback, AfterEachCallback {
    @Override
    public void afterEach(ExtensionContext context) throws SQLException {
        con.rollback(savepoint);
    }
}
```

最后，为了关闭连接，我们使用afterAll()方法，该方法在所有测试完成后执行：

```java
public class EmployeeDatabaseSetupExtension implements BeforeAllCallback, AfterAllCallback, BeforeEachCallback, AfterEachCallback {
    @Override
    public void afterAll(ExtensionContext context) throws SQLException {
        if (con != null)
            con.close();
    }
}
```

### 4.4 参数解析

如果给测试类的构造函数或方法传递参数，则必须在运行时由ParameterResolver这个Extension解析。

让我们编写一个自定义ParameterResolver来解析EmployeeJdbcDao类型的参数：

```java
public class EmployeeDaoParameterResolver implements ParameterResolver {

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().equals(EmployeeJdbcDao.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return new EmployeeJdbcDao(JdbcConnectionUtil.getConnection());
    }
}
```

我们的解析器实现了ParameterResolver接口并重写了supportsParameter()和resolveParameter()方法。
其中前一个方法验证参数的类型，而第二个方法定义获取参数实例的逻辑。

### 4.5 异常处理

最后，TestExecutionExceptionHandler接口可用于定义测试在遇到某些类型的异常时的行为。

例如，我们可以创建一个Extension，它将记录并忽略所有FileNotFoundException类型的异常，同时重新抛出任何其他类型：

```java
public class IgnoreFileNotFoundExceptionExtension implements TestExecutionExceptionHandler {
    Logger logger = LoggerFactory.getLogger(IgnoreFileNotFoundExceptionExtension.class);

    @Override
    public void handleTestExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {
        if (throwable instanceof FileNotFoundException) {
            logger.error("File not found: " + throwable.getMessage());
            return;
        }
        throw throwable;
    }
}
```

## 5. 注册Extensions

目前我们定义了一些Extension，我们现在需要做的就是将它们注册到JUnit 5测试中。为此，我们可以使用@ExtendWith注解。

该注解可以多次添加到测试类上，或者使用extensions参数一次注册多个：

```java

@ExtendWith({EnvironmentExtension.class, EmployeeDatabaseSetupExtension.class, EmployeeDaoParameterResolver.class})
@ExtendWith(LoggingExtension.class)
@ExtendWith(IgnoreFileNotFoundExceptionExtension.class)
public class EmployeesUnitTest {

    private EmployeeJdbcDao employeeDao;

    private Logger logger;

    public EmployeesUnitTest(EmployeeJdbcDao employeeDao) {
        this.employeeDao = employeeDao;
    }

    @Test
    public void whenAddEmployee_thenGetEmployee() throws SQLException {
        Employee emp = new Employee(1, "john");
        employeeDao.add(emp);
        assertEquals(1, employeeDao.findAll().size());
    }

    @Test
    public void whenGetEmployees_thenEmptyList() throws SQLException {
        assertEquals(0, employeeDao.findAll().size());
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }
}
```

我们可以看到我们的测试类有一个带有EmployeeJdbcDao参数的构造函数，它会通过EmployeeDaoParameterResolver Extension来注入。

通过添加EnvironmentExtension，我们的测试将只在不同于“qa”的环境中执行。

EmployeeDatabaseSetupExtension负责创建员工表并将每个测试方法包装在事务中。
即使第一次执行whenAddEmployee_thenGetEmploee()测试，将一条记录添加到表中，第二次测试中findAll()方法返回的也是0。

Logger实例通过使用LoggingExtension添加到我们的类中。

最后，我们的测试类将忽略所有FileNotFoundException异常。

### 5.1 自动注册Extension

如果我们想为应用程序中的所有测试类注册这些Extension，
我们可以通过将Extension类的全限定名添加到/META-INF/services/org.junit.jupiter.api.extension.Extension文件来实现：

```

# src/test/resources/META-INF/services/org.junit.jupiter.api.extension.Extension
cn.tuyucheng.taketoday.extensions.LoggingExtension
```

要启用此机制，我们还需要将junit.jupiter.extensions.autodetection.enabled设置为true。
这可以通过使用–Djunit.jupiter.extensions.autodetection.enabled=true属性启动JVM或通过向LauncherDiscoveryRequest添加配置参数来完成：

```
LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
        .selectors(selectClass("cn.tuyucheng.taketoday.EmployeesUnitTest"))
        .configurationParameter("junit.conditions.deactivate", "cn.tuyucheng.taketoday.extensions.*")
        .configurationParameter("junit.jupiter.extensions.autodetection.enabled", "true")
        .build();
```

### 5.2 编程方式注册Extension

尽管使用注解注册Extension是一种更具声明性的方法，但它有一个明显的缺点：我们不能轻松地自定义Extension的行为。
例如，对于当前的Extension注册模型，我们不能接收来自我们配置的数据库连接属性创建Connection。

除了声明式的基于注解的方法之外，JUnit还提供了一个API来以编程方式注册Extension。
例如，我们可以改造JdbcConnectionUtil类来接收连接属性：

```java
public class JdbcConnectionUtil {

    private static Connection con;

    public static Connection getConnection(String jdbcUrl, String driver, String username, String password) {
        if (con == null || isClosed(con)) {
            try {
                Class.forName(driver);
                con = DriverManager.getConnection(jdbcUrl, username, password);
                return con;
            } catch (ClassNotFoundException | SQLException exc) {
                exc.printStackTrace();
            }
            return null;
        }
        return con;
    }
}
```

此外，我们应该为EmployeeDatabaseSetupExtension Extension添加一个新的构造函数以支持自定义的数据库属性：

```java
public class EmployeeDatabaseSetupExtension implements BeforeAllCallback, AfterAllCallback, BeforeEachCallback, AfterEachCallback {

    public EmployeeDatabaseSetupExtension(String jdbcUrl, String driver, String username, String password) {
        con = JdbcConnectionUtil.getConnection(jdbcUrl, driver, username, password);
        employeeDao = new EmployeeJdbcDao(con);
    }
}
```

**现在，要使用自定义数据库属性注册EmployeeDatabaseSetupExtension，
我们应该使用@RegisterExtension注解来标注Extension静态字段**：

```java

@ExtendWith({EnvironmentExtension.class, EmployeeDaoParameterResolver.class})
public class ProgrammaticEmployeesUnitTest {

    @RegisterExtension
    static EmployeeDatabaseSetupExtension DB = new EmployeeDatabaseSetupExtension("jdbc:h2:mem:AnotherDb;DB_CLOSE_DELAY=-1", "org.h2.Driver", "sa", "");
    private EmployeeJdbcDao employeeDao;
    // ...
}
```

这里使用的是内存数据库H2。

### 5.3 注册顺序

JUnit在注册使用@ExtendsWith注解以声明方式定义的扩展后注册@RegisterExtension静态字段。
我们也可以使用非静态字段进行编程注册，但它们将在测试方法实例化和后处理器之后进行注册。

如果我们通过@RegisterExtension以编程方式注册多个扩展，JUnit将以确定的顺序注册这些扩展。
尽管顺序是确定性的，但用于排序的算法是非显而易见的和内部的。要强制执行特定的注册顺序，我们可以使用@Order注解：

```java
public class MultipleExtensionsUnitTest {

    @Order(1)
    @RegisterExtension
    static EmployeeDatabaseSetupExtension SECOND_DB =
            new EmployeeDatabaseSetupExtension("jdbc:h2:mem:DbTwo;DB_CLOSE_DELAY=-1", "org.h2.Driver", "sa", "");

    @Order(0)
    @RegisterExtension
    static EmployeeDatabaseSetupExtension FIRST_DB =
            new EmployeeDatabaseSetupExtension("jdbc:h2:mem:DbOne;DB_CLOSE_DELAY=-1", "org.h2.Driver", "sa", "");

    @RegisterExtension
    static EmployeeDatabaseSetupExtension LAST_DB =
            new EmployeeDatabaseSetupExtension("jdbc:h2:mem:DbLast;DB_CLOSE_DELAY=-1", "org.h2.Driver", "sa", "");

    @Test
    public void justDemonstratingTheIdea() {

    }
}
```

**在这里，扩展是根据@Order注解配置的优先级排序的，其中较低的值比较高的值具有更高的优先级。
此外，没有@Order注解的扩展具有最低的优先级**。

## 6. 总结

在本教程中，我们演示了如何使用JUnit 5扩展模型来创建自定义的测试Extension。