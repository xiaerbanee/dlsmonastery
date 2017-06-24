package net.myspring.tool;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@SpringBootApplication
@EnableResourceServer
@EnableDiscoveryClient
@EnableFeignClients
@EntityScan(basePackageClasses = {GlobalToolApplication.class, Jsr310JpaConverters.class})
@EnableJpaRepositories(repositoryBaseClass = BaseRepositoryImpl.class)
@EnableOAuth2Client
public class GlobalToolApplication {

	public static void main(String[] args) {
		SpringApplication.run(GlobalToolApplication.class, args);
	}
}
