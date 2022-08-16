package cn.tuyucheng.taketoday.extensions;

import org.junit.jupiter.api.extension.ConditionEvaluationResult;
import org.junit.jupiter.api.extension.ExecutionCondition;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.io.IOException;
import java.util.Properties;

public class EnvironmentExtension implements ExecutionCondition {

    @Override
    public ConditionEvaluationResult evaluateExecutionCondition(ExtensionContext context) {
        Properties properties = new Properties();
        try {
            properties.load(EnvironmentExtension.class.getResourceAsStream("application.properties"));
            String env = properties.getProperty("env");
            if ("qa".equalsIgnoreCase(env))
                return ConditionEvaluationResult.disabled("Test disabled on QA environment");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ConditionEvaluationResult.enabled("Test enabled on QA environment");
    }
}