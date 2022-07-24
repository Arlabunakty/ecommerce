package ua.training.ecommerce.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("jwt")
@Getter
@Setter
public class SecurityProperties {
    private String header;
    private String appName;
    private String secret;
    private int expiresIn;
}
