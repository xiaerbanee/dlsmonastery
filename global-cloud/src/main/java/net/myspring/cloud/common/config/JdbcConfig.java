package net.myspring.cloud.common.config;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.google.common.collect.Maps;
import net.myspring.cloud.common.dataSource.DynamicDataSource;
import net.myspring.cloud.common.enums.DataSourceTypeEnum;
import net.myspring.cloud.modules.sys.domain.KingdeeBook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

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
        List<KingdeeBook> kingdeeBookList = getKingdeeBookList();
        for (KingdeeBook kingdeeBook:kingdeeBookList) {
            targetDataSources.put(DataSourceTypeEnum.KINGDEE.name() + "_" + kingdeeBook.getCompanyId(),getCloudDataSource(kingdeeBook));
        }
        targetDataSources.put(DataSourceTypeEnum.LOCAL.name(),getLocalDataSource());
        DynamicDataSource dataSource = new DynamicDataSource();
        dataSource.setTargetDataSources(targetDataSources);
        return dataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dynamicDataSource());
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

    private List<KingdeeBook> getKingdeeBookList() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(getLocalDataSource());
        String sql = "select * from sys_kingdee_book";
        List<KingdeeBook> kingdeeBookList = jdbcTemplate.query(sql,new BeanPropertyRowMapper<KingdeeBook>(KingdeeBook.class));
        return kingdeeBookList;
    }

    private DataSource getCloudDataSource(KingdeeBook kingdeeBook) {
        Properties props = new Properties();
        props.put("driverClassName", "net.sourceforge.jtds.jdbc.Driver");
        props.put("url", kingdeeBook.getKingdeeUrl());
        props.put("username", kingdeeBook.getKingdeeUsername());
        props.put("password", kingdeeBook.getKingdeePassword());
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
