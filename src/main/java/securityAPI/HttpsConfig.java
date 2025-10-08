package securityAPI;

import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HttpsConfig {

    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> containerCustomizer() {
        return server -> server.addConnectorCustomizers(connector -> {
            connector.setScheme("http");
            connector.setSecure(false);
            connector.setRedirectPort(8443); 
        });
    }
}