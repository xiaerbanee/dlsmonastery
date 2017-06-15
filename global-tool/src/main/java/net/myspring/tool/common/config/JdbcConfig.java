package net.myspring.tool.common.config;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.google.common.collect.Maps;
import net.myspring.tool.common.dataSource.DynamicDataSource;
import net.myspring.tool.common.enums.DataSourceTypeEnum;
import net.myspring.tool.modules.sys.domain.Factory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@Configuration
public class JdbcConfig {
    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;

    @Bean
    public DynamicDataSource dynamicDataSource() {
        Map<Object, Object> targetDataSources = Maps.newHashMap();
        List<Factory> factoryList = getFactoryList();
        for (Factory factory:factoryList) {
            targetDataSources.put(DataSourceTypeEnum.FACTORY.name() + "_" + factory.getCompanyId(),getFactoryDataSource(factory));
        }
        targetDataSources.put(DataSourceTypeEnum.LOCAL.name(),getLocalDataSource());
        DynamicDataSource dataSource = new DynamicDataSource();
        dataSource.setTargetDataSources(targetDataSources);
        return dataSource;
    }

    private DataSource getLocalDataSource() {
        Properties props = new Properties();
        props.put("driverClassName", "com.mysql.jdbc.Driver");
        props.put("url", url);
        props.put("username", username);
        props.put("password", password);
        try {
            return DruidDataSourceFactory.createDataSource(props);
        } catch (Exception e) {
            return null;
        }
    }

    private List<Factory> getFactoryList() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(getLocalDataSource());
        String sql = "select * from sys_factory";
        List<Factory> factoryList = jdbcTemplate.query(sql,new BeanPropertyRowMapper<Factory>(Factory.class));
        return factoryList;
    }

    private DataSource getFactoryDataSource(Factory factory) {
        Properties props = new Properties();
        props.put("driverClassName", "net.sourceforge.jtds.jdbc.Driver");
        props.put("url", factory.getUrl());
        props.put("username", factory.getUsername());
        props.put("password", factory.getPassword());
        try {
            return DruidDataSourceFactory.createDataSource(props);
        } catch (Exception e) {
            return null;
        }
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
