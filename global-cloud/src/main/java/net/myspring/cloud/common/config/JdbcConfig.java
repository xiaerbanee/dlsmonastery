package net.myspring.cloud.common.config;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.google.common.collect.Maps;
import net.myspring.cloud.common.dataSource.DynamicDataSource;
import net.myspring.cloud.common.enums.DataSourceTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Properties;

@Configuration
public class JdbcConfig {

     @Autowired
     private Environment environment;

    @Bean
    public DynamicDataSource dynamicDataSource() {
        Map<Object, Object> targetDataSources = Maps.newHashMap();
        targetDataSources.put(DataSourceTypeEnum.LOCAL.name(),getDataSource("spring.datasource.local"));
        targetDataSources.put("KINGDEE_JXOPPO",getDataSource("spring.datasource.kingdee.JXOPPO"));
        DynamicDataSource dataSource = new DynamicDataSource();
        dataSource.setTargetDataSources(targetDataSources);
        return dataSource;
    }

    private DataSource getDataSource(String prefix) {
        Properties props = new Properties();
        props.put("driverClassName", environment.getProperty(prefix + ".driver-class-name"));
        props.put("url", environment.getProperty(prefix + ".url"));
        props.put("username", environment.getProperty(prefix + ".username"));
        props.put("password", environment.getProperty(prefix + ".password"));
        try {
            return DruidDataSourceFactory.createDataSource(props);
        } catch (Exception e) {
            return null;
        }
    }


    @Bean
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager jpaTransactionManager =  new JpaTransactionManager();
        jpaTransactionManager.setDataSource(dynamicDataSource());
        return jpaTransactionManager;
    }

    @Bean
    public JdbcTemplate JdbcTemplate() {
        return new JdbcTemplate(dynamicDataSource());
    }

    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate() {
        return new NamedParameterJdbcTemplate(dynamicDataSource());
    }
}
