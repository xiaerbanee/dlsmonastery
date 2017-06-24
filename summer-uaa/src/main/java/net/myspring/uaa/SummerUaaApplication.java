package net.myspring.uaa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

@EnableDiscoveryClient
@SpringBootApplication
@SessionAttributes("authorizationRequest")
public class SummerUaaApplication {

	public static void main(String[] args) {
		SpringApplication.run(SummerUaaApplication.class, args);
	}
}