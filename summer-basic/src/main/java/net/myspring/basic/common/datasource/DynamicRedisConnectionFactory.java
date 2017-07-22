package net.myspring.basic.common.datasource;

public class DynamicRedisConnectionFactory extends AbstractRoutingRedisConnectionFactory{

    @Override
    protected String determineCurrentLookupKey() {
        return DbContextHolder.get().getCompanyName();
    }
}
