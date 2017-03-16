package net.myspring.discovery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class SummerDiscoveryApplication {

	public static void main(String[] args) {
		SpringApplication.run(SummerDiscoveryApplication.class, args);
	}
}
