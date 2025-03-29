package com.ikiwq.blog.api.config.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "app")
public class AppProperties {
    private String uploadFolder = "uploads/";
    private String domain = "http://localhost:8080";
}
