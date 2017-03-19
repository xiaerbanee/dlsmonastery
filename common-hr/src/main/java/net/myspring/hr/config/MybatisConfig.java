package net.myspring.hr.config;

import net.myspring.mybatis.context.MybatisContext;
import net.myspring.mybatis.dialect.Dialect;
import net.myspring.mybatis.dialect.MySQLDialect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by liuj on 2017/3/19.
 */
@Configuration
public class MybatisConfig {
    @Bean
    public MybatisContext mybatisContext() {
        return new MybatisContext() {
            @Override
            public String getAccountId() {
                return "1";
            }
            @Override
            public Dialect getDialect() {
                return new MySQLDialect();
            }
        };
    }


}
