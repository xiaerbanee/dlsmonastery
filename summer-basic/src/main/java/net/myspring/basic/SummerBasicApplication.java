package net.myspring.basic;

import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@SpringBootApplication
@EnableResourceServer
@EnableDiscoveryClient
public class SummerBasicApplication {

	private static ApplicationContext applicationContext;


	public static void main(String[] args) {
		final ApplicationContext applicationContext = SpringApplication.run(SummerBasicApplication.class, args);
		SummerBasicApplication.applicationContext = applicationContext;
	}

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public static  <T> T getBean(Class<T> requiredType) throws BeansException {
		return getApplicationContext().getBean(requiredType);
	}
}
