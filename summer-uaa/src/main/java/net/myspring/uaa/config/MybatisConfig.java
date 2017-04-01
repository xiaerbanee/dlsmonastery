package net.myspring.uaa.config;

import net.myspring.mybatis.context.ProviderMapperAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by liuj on 2017/3/19.
 */
@Configuration
public class MybatisConfig {

    @Bean
    public ProviderMapperAspect providerMapperAspect() {
        return new ProviderMapperAspect();
    }
}
