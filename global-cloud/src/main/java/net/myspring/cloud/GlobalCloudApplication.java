package net.myspring.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@SpringBootApplication
@EnableResourceServer
@EnableDiscoveryClient
@EnableFeignClients
@EnableBinding(Sink.class)
public class GlobalCloudApplication {

	public static void main(String[] args) {
		SpringApplication.run(GlobalCloudApplication.class, args);
	}

	@StreamListener(Sink.INPUT)
	public void log(String message) {
		System.out.println(message);
	}
}