package net.myspring.general;

import net.myspring.general.common.repository.BaseRepositoryImpl;
import org.activiti.spring.boot.JpaProcessEngineAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EntityScan(basePackageClasses = {SummerGeneralApplication.class, Jsr310JpaConverters.class})
@EnableJpaRepositories(repositoryBaseClass = BaseRepositoryImpl.class)
public class SummerGeneralApplication {

	public static void main(String[] args) {
		SpringApplication.run(SummerGeneralApplication.class, args);
	}
}