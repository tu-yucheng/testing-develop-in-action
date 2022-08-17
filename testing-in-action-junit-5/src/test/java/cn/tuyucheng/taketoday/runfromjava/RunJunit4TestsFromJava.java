package cn.tuyucheng.taketoday.runfromjava;

import junit.extensions.RepeatedTest;
import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import org.junit.internal.TextListener;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

public class RunJunit4TestsFromJava {
    
    public static void main(String[] args) {
        // JUnitCore jUnit = new JUnitCore();
        // jUnit.addListener(new TextListener(System.out));
        // jUnit.run(Junit4FirstUnitTest.class);
        
        // JUnitCore junit = new JUnitCore();
        // junit.addListener(new TextListener(System.out));
        // Result result = junit.run(Junit4FirstUnitTest.class, Junit4SecondUnitTest.class);
        // resultReport(result);

        // JUnitCore junit = new JUnitCore();
        // junit.addListener(new TextListener(System.out));
        // Result result = junit.run(Junit4TestSuite.class);
        // resultReport(result);

        Test test = new JUnit4TestAdapter(Junit4FirstUnitTest.class);
        RepeatedTest repeatedTest = new RepeatedTest(test, 5);
        JUnitCore junit = new JUnitCore();
        junit.addListener(new TextListener(System.out));
        junit.run(repeatedTest);

        // TestSuite mySuite = new ActiveTestSuite();
        // JUnitCore junit = new JUnitCore();
        // junit.addListener(new TextListener(System.out));
        // mySuite.addTest(new RepeatedTest(new JUnit4TestAdapter(Junit4FirstUnitTest.class), 5));
        // mySuite.addTest(new RepeatedTest(new JUnit4TestAdapter(Junit4SecondUnitTest.class), 3));
        // junit.run(mySuite);
    }

    public static void resultReport(Result result) {
        System.out.println("Finished. Result: Failures: " +
                result.getFailureCount() + ". Ignored: " +
                result.getIgnoreCount() + ". Tests run: " +
                result.getRunCount() + ". Time: " +
                result.getRunTime() + "ms.");
    }
}