package net.myspring.uaa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class SummerUaaApplication {

	public static void main(String[] args) {
		SpringApplication.run(SummerUaaApplication.class, args);
	}
}
