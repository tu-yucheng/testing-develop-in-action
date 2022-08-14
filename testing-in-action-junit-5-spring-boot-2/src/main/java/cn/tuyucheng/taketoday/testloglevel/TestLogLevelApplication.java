package cn.tuyucheng.taketoday.testloglevel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(scanBasePackages = {"cn.tuyucheng.taketoday.testloglevel", "cn.tuyucheng.taketoday.component"},exclude = SecurityAutoConfiguration.class)
public class TestLogLevelApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestLogLevelApplication.class, args);
    }
}