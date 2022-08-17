## 1. 概述

在本教程中，我们将演示如何直接从Java代码运行JUnit测试 - 在某些场景中，这种方法会派上用场。

## 2. maven依赖

我们需要几个基本的依赖来运行JUnit 4和JUnit 5测试：

```
<dependencies>
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter-engine</artifactId>
        <version>5.8.1</version>
        <scope>test</scope>
    </dependency>
    <dependency>
        <groupId>org.junit.platform</groupId>
        <artifactId>junit-platform-launcher</artifactId>
        <version>1.2.0</version>
    </dependency>
</dependencies>

// for JUnit 4
<dependency> 
    <groupId>junit</groupId> 
    <artifactId>junit</artifactId> 
    <version>4.12</version> 
    <scope>test</scope> 
</dependency>
```

## 3. 运行Junit 4测试

### 3.1 测试场景

首先我们编写几个简单的测试类：

```java
public class Junit4FirstUnitTest {

    @Test
    public void whenThis_thenThat() {
        assertTrue(true);
    }

    @Test
    public void whenSomething_thenSomething() {
        assertTrue(true);
    }

    @Test
    public void whenSomethingElse_thenSomethingElse() {
        assertTrue(true);
    }
}

public class Junit4SecondUnitTest {

    @Test
    public void whenSomething_thenSomething() {
        assertTrue(true);
    }

    public void whenSomethingElse_thenSomethingElse() {
        assertTrue(true);
    }
}
```

使用JUnit 4时，我们创建测试类，为每个测试方法添加Junit 4中的@Test注解。

我们还可以添加其他有用的注解，例如@Before或@After，但这不在本文的讲解范围内。

### 3.2 运行一个单一的测试类

要从Java代码运行JUnit 4测试，我们可以使用JUnitCore类(添加了一个TextListener对象，用于在控制台中显示输出)：

```java
public class RunJunit4TestsFromJava {

    public static void main(String[] args) {
        JUnitCore jUnit = new JUnitCore();
        jUnit.addListener(new TextListener(System.out));
        jUnit.run(Junit4FirstUnitTest.class);
    }
}
```

在控制台上，我们将看到一条非常简单的消息，表明测试成功：

```
...
Time: 0.006

OK (3 tests)
```

### 3.3 运行多个测试类

如果我们想用JUnit 4指定多个测试类，我们可以使用与运行单个类相同的代码，只需添加额外的类：

```java
public class RunJunit4TestsFromJava {

    public static void main(String[] args) {
        JUnitCore junit = new JUnitCore();
        junit.addListener(new TextListener(System.out));
        Result result = junit.run(Junit4FirstUnitTest.class, Junit4SecondUnitTest.class);
        resultReport(result);
    }
}
```

请注意，运行结果存储在JUnit的Result类的一个实例中，我们使用一个简单的工具方法将其打印出来：

```java
public class RunJunit4TestsFromJava {

    public static void resultReport(Result result) {
        System.out.println("Finished. Result: Failures: " +
                result.getFailureCount() + ". Ignored: " +
                result.getIgnoreCount() + ". Tests run: " +
                result.getRunCount() + ". Time: " +
                result.getRunTime() + "ms.");
    }
}
```

### 3.4 运行一个测试套件

如果我们需要对一些测试类进行分组以便隔离运行它们，我们可以创建一个TestSuite。
这只是一个空类，我们使用@Suite.SuiteClasses注解指定该测试套件需要运行的所有类：

```java

@RunWith(Suite.class)
@Suite.SuiteClasses({Junit4FirstUnitTest.class, Junit4SecondUnitTest.class})
public class Junit4TestSuite {

}
```

为了运行这些测试，我们再次使用与之前相同的代码，这次我们指定的是一个测试套件类：

```java
public class RunJunit4TestsFromJava {

    public static void main(String[] args) {
        JUnitCore junit = new JUnitCore();
        junit.addListener(new TextListener(System.out));
        Result result = junit.run(Junit4TestSuite.class);
        resultReport(result);
    }
}
```

### 3.5 运行重复测试

JUnit的一个有趣特性是我们可以通过创建RepeatedTest的实例来重复测试。这在我们测试随机值或性能检查时非常有用。

在下面的例子中，我们指定Junit4FirstUnitTest运行测试五次：

```java
public class RunJunit4TestsFromJava {

    public static void main(String[] args) {
        Test test = new JUnit4TestAdapter(Junit4FirstUnitTest.class);
        RepeatedTest repeatedTest = new RepeatedTest(test, 5);
        JUnitCore junit = new JUnitCore();
        junit.addListener(new TextListener(System.out));
        junit.run(repeatedTest);
    }
}
```

在这里，我们使用JUnit4TestAdapter作为测试类的包装器。

我们甚至可以通过重复测试以编程方式创建测试套件：

```java
public class RunJunit4TestsFromJava {

    public static void main(String[] args) {
        TestSuite mySuite = new ActiveTestSuite();
        JUnitCore junit = new JUnitCore();
        junit.addListener(new TextListener(System.out));
        mySuite.addTest(new RepeatedTest(new JUnit4TestAdapter(Junit4FirstUnitTest.class), 5));
        mySuite.addTest(new RepeatedTest(new JUnit4TestAdapter(Junit4SecondUnitTest.class), 3));
        junit.run(mySuite);
    }
}
```

## 4. 运行Junit 5测试

### 4.1 测试场景

对于JUnit 5，我们需要使用Junit 5中相应的API和注解。

### 4.2 运行单个测试

要从Java代码运行JUnit 5测试，我们首先设置LauncherDiscoveryRequest的实例。
它使用一个名为LauncherDiscoveryRequestBuilder构建器类，我们必须在其中设置包选择器和测试类名称过滤器，以获取我们想要运行的所有测试类。

然后，此请求与Launcher相关联，并且在执行测试之前，我们还将设置测试计划和执行监听器。

这两个都将提供有关要执行的测试和结果的信息：

```java
public class RunJUnit5TestsFromJava {
    SummaryGeneratingListener listener = new SummaryGeneratingListener();

    public void runOne() {
        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
                .selectors(selectClass(Junit5FirstUnitTest.class))
                .build();
        Launcher launcher = LauncherFactory.create();
        TestPlan testPlan = launcher.discover(request);
        launcher.registerTestExecutionListeners(listener);
        launcher.execute(request);
    }
}
```

### 4.3 运行多个测试类

我们可以为请求设置选择器和过滤器以运行多个测试类。

让我们看看如何设置包选择器和测试类名过滤器，以获取我们想要运行的所有测试类：

```java
public class RunJUnit5TestsFromJava {
    SummaryGeneratingListener listener = new SummaryGeneratingListener();

    public void runAll() {
        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
                .selectors(selectPackage("cn.tuyucheng.taketoday.runfromjava"))
                .filters(includeClassNamePatterns(".*Test"))
                .build();
        Launcher launcher = LauncherFactory.create();
        TestPlan testPlan = launcher.discover(request);
        launcher.registerTestExecutionListeners(listener);
        launcher.execute(request);
    }
}
```

### 4.4 测试输出

在main()方法中，我们调用我们的类，并且使用监听器获取结果，结果保存在TestExecutionSummary中。提取其信息的最简单方法是打印到控制台：

```
RunJUnit5TestsFromJava runner = new RunJUnit5TestsFromJava();
runner.runAll();

TestExecutionSummary summary = runner.listener.getSummary();
summary.printTo(new PrintWriter(System.out));
```

这会为我们提供测试运行的详细信息：

```
Test run finished after 111 ms
[         8 containers found      ]
[         0 containers skipped    ]
[         8 containers started    ]
[         0 containers aborted    ]
[         8 containers successful ]
[         0 containers failed     ]
[        23 tests found           ]
[         0 tests skipped         ]
[        23 tests started         ]
[         0 tests aborted         ]
[        23 tests successful      ]
[         0 tests failed          ]
```

## 5. 总结

在本文中，我们演示了如何从Java代码以编程方式运行JUnit测试，分别介绍了Junit 4和Junit 5中的具体细节。