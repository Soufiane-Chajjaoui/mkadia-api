package fr.mkadia.mkadiaapi.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "file")
@Getter
@Setter
public class FileConfig {
    private String uploadDir;
    private List<String> allowedExtensions;
}
