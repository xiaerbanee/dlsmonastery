package net.myspring.future.common.datasource;

public class DynamicRedisConnectionFactory extends AbstractRoutingRedisConnectionFactory{

    @Override
    protected String determineCurrentLookupKey() {
        return DbContextHolder.get().getCompanyName();
    }
}
