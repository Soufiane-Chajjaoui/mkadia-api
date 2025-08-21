package fr.mkadia.mkadiaapi.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "app.clients")
public class ClientProperties {
    private List<Client> ips;
    @Getter @Setter
    public static class Client {
        private String name;
        private String ip;
    }

}
