package net.myspring.tool.common.dataSource;

public class DynamicRedisConnectionFactory extends AbstractRoutingRedisConnectionFactory{

    @Override
    protected String determineCurrentLookupKey() {
        return DbContextHolder.get().getCompanyName();
    }
}
