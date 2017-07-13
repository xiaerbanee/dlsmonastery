package net.myspring.cloud.common.config;

import com.alibaba.druid.pool.DruidDataSource;
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
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
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
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(environment.getProperty(prefix + ".driver-class-name"));
        dataSource.setUrl(environment.getProperty(prefix + ".url"));
        dataSource.setUsername(environment.getProperty(prefix + ".username"));
        dataSource.setPassword(environment.getProperty(prefix + ".password"));
        dataSource.setMaxActive(100);
        dataSource.setMinIdle(10);
        dataSource.setInitialSize(10);
        dataSource.setRemoveAbandonedTimeout(600);
        return dataSource;
    }


    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dynamicDataSource());
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
