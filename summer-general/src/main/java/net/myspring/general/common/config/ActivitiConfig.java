package net.myspring.general.common.config;

import com.google.common.collect.Lists;
import net.myspring.general.common.activiti.CustomGroupManager;
import net.myspring.general.common.activiti.CustomGroupManagerFactory;
import net.myspring.general.common.activiti.CustomUserManager;
import net.myspring.general.common.activiti.CustomUserManagerFactory;
import org.activiti.engine.impl.interceptor.SessionFactory;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.activiti.spring.boot.AbstractProcessEngineAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.util.List;

/**
 * Created by admin on 2016/12/27.
 */
@Configuration
public class ActivitiConfig extends AbstractProcessEngineAutoConfiguration {

    @Autowired
    private DataSource dataSource;

    @Bean
    public SpringProcessEngineConfiguration processEngineConfiguration(){
        SpringProcessEngineConfiguration springProcessEngineConfiguration=new SpringProcessEngineConfiguration();
        springProcessEngineConfiguration.setDataSource(dataSource);
        springProcessEngineConfiguration.setTransactionManager(new DataSourceTransactionManager(dataSource));
        springProcessEngineConfiguration.setDatabaseType("mysql");
        springProcessEngineConfiguration.setDatabaseSchemaUpdate("false");
        springProcessEngineConfiguration.setJobExecutorActivate(false);
        springProcessEngineConfiguration.setHistory("full");
        springProcessEngineConfiguration.setActivityFontName("宋体");
        List<SessionFactory> customSessionFactories= Lists.newArrayList();
        CustomGroupManagerFactory customGroupManagerFactory=new CustomGroupManagerFactory();
        customGroupManagerFactory.setGroupEntityManager(new CustomGroupManager());
        CustomUserManagerFactory customUserManagerFactory=new CustomUserManagerFactory();
        customUserManagerFactory.setUserEntityManager(new CustomUserManager());
        customSessionFactories.add(customGroupManagerFactory);
        customSessionFactories.add(customUserManagerFactory);
        springProcessEngineConfiguration.setCustomSessionFactories(customSessionFactories);
        return springProcessEngineConfiguration;
    }
}
