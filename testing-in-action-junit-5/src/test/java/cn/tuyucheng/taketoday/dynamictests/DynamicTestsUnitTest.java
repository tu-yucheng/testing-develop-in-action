package cn.tuyucheng.taketoday.dynamictests;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.function.ThrowingConsumer;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DynamicTestsUnitTest {

    @TestFactory
    Collection<DynamicTest> dynamicTestsWithCollections() {
        return Arrays.asList(
                DynamicTest.dynamicTest("Add test", () -> assertEquals(2, Math.addExact(1, 1))),
                DynamicTest.dynamicTest("Multiply test", () -> assertEquals(4, Math.multiplyExact(2, 2)))
        );
    }

    @TestFactory
    Iterable<DynamicTest> dynamicTestsWithIterable() {
        return Arrays.asList(
                DynamicTest.dynamicTest("Add test", () -> assertEquals(2, Math.addExact(1, 1))),
                DynamicTest.dynamicTest("Multiply Test", () -> assertEquals(4, Math.multiplyExact(2, 2)))
        );
    }

    @TestFactory
    Iterator<DynamicTest> dynamicTestsWithIterator() {
        return Arrays.asList(
                DynamicTest.dynamicTest("Add test", () -> assertEquals(2, Math.addExact(1, 1))),
                DynamicTest.dynamicTest("Multiply Test", () -> assertEquals(4, Math.multiplyExact(2, 2)))
        ).iterator();
    }

    @TestFactory
    Stream<DynamicTest> dynamicTestFromIntStream() {
        return IntStream.iterate(0, n -> n + 2).limit(10)
                .mapToObj(n -> DynamicTest.dynamicTest("test" + n, () -> assertEquals(0, n % 2)));
    }

    @TestFactory
    Stream<DynamicTest> dynamicTestsFromStream() {
        List<String> inputList = Arrays.asList("www.somedomain.com", "www.anotherdomain.com", "www.yetanotherdomain.com");
        List<String> outputList = Arrays.asList("154.174.10.56", "211.152.104.132", "178.144.120.156");

        // input generator that generates inputs using inputList
        Iterator<String> inputGenerator = inputList.iterator();

        // a display name generator that creates a different name based on the input
        Function<String, String> displayNameGenerator = (input) -> "Resolving: " + input;

        // the test executor, which actually has the logic of how to execute the test case
        DomainNameResolver resolver = new DomainNameResolver();
        ThrowingConsumer<String> testExecutor = (input) -> {
            int id = inputList.indexOf(input);
            assertEquals(outputList.get(id), resolver.resolveDomain(input));
        };

        // combine everything and return a Stream of DynamicTest
        return DynamicTest.stream(inputGenerator, displayNameGenerator, testExecutor);
    }

    @TestFactory
    Stream<DynamicTest> dynamicTestsFromStreamInJava8() {

        DomainNameResolver resolver = new DomainNameResolver();
        List<String> inputList = Arrays.asList("www.somedomain.com", "www.anotherdomain.com", "www.yetanotherdomain.com");
        List<String> outputList = Arrays.asList("154.174.10.56", "211.152.104.132", "178.144.120.156");
        return inputList.stream().map(domain -> DynamicTest.dynamicTest("Resolving: " + domain, () -> {
            int id = inputList.indexOf(domain);
            assertEquals(outputList.get(id), resolver.resolveDomain(domain));
        }));
    }

    @TestFactory
    Stream<DynamicTest> dynamicTestsForEmployeeWorkflows() {
        List<Employee> inputList = Arrays.asList(
                new Employee(1, "Fred"),
                new Employee(2),
                new Employee(3, "John")
        );
        EmployeeDao employeeDao = new EmployeeDao();

        Stream<DynamicTest> saveEmployeeStream = inputList.stream()
                .map(employee -> DynamicTest.dynamicTest("saveEmployee: " + employee, () -> {
                    Employee returned = employeeDao.save(employee.getId());
                    assertEquals(returned.getId(), employee.getId());
                }));

        Stream<DynamicTest> saveEmployeeWithFirstNameStream = inputList.stream()
                .filter(employee -> !employee.getFirstName().isEmpty())
                .map(employee -> DynamicTest.dynamicTest("saveEmployeeWithName: " + employee, () -> {
                    Employee returned = employeeDao.save(employee.getId(), employee.getFirstName());
                    assertEquals(returned.getId(), employee.getId());
                    assertEquals(returned.getFirstName(), employee.getFirstName());
                }));
        return Stream.concat(saveEmployeeStream, saveEmployeeWithFirstNameStream);
    }

    private static class DomainNameResolver {
        private final Map<String, String> ipByDomainName = new HashMap<>();

        DomainNameResolver() {
            this.ipByDomainName.put("www.somedomain.com", "154.174.10.56");
            this.ipByDomainName.put("www.anotherdomain.com", "211.152.104.132");
            this.ipByDomainName.put("www.yetanotherdomain.com", "178.144.120.156");
        }

        public String resolveDomain(String domainName) {
            return ipByDomainName.get(domainName);
        }
    }
}