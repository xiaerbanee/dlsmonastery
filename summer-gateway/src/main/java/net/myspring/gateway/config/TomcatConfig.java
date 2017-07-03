package net.myspring.gateway.config;

import org.apache.catalina.connector.Connector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TomcatConfig {

	@Value("${server.http.port}")
	private int httpPort;

	@Bean
	public EmbeddedServletContainerCustomizer containerCustomizer() {
		return container -> {
            if (container instanceof TomcatEmbeddedServletContainerFactory) {
                TomcatEmbeddedServletContainerFactory containerFactory =
                        (TomcatEmbeddedServletContainerFactory) container;

                Connector connector = new Connector(TomcatEmbeddedServletContainerFactory.DEFAULT_PROTOCOL);
                connector.setPort(httpPort);
                containerFactory.addAdditionalTomcatConnectors(connector);
            }
        };
	}
}