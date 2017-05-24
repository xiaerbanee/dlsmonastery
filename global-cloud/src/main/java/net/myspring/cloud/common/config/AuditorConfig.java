package net.myspring.cloud.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Created by liuj on 2017/5/19.
 */
@Configuration
@EnableJpaAuditing
public class AuditorConfig {

    @Bean
    public AuditorAware<String> auditorProvider() {
        return new SecurityAuditorConfig();
    }
}
