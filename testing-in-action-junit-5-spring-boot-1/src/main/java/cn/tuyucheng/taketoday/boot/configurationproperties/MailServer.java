package cn.tuyucheng.taketoday.boot.configurationproperties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.HashMap;

@Validated
@Configuration
@ConfigurationProperties(prefix = "validate")
@PropertySource("classpath:property-validation.properties")
public class MailServer {
    @NotNull
    @NotEmpty
    private HashMap<String, @NotBlank String> propertiesMap;

    @Valid
    private MailConfig mailConfig = new MailConfig();

    public HashMap<String, String> getPropertiesMap() {
        return propertiesMap;
    }

    public void setPropertiesMap(HashMap<String, String> propertiesMap) {
        this.propertiesMap = propertiesMap;
    }

    public MailConfig getMailConfig() {
        return mailConfig;
    }

    public void setMailConfig(MailConfig mailConfig) {
        this.mailConfig = mailConfig;
    }

    public static class MailConfig {
        @Email
        @NotBlank
        private String address;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }
    }
}