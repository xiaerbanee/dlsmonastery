package net.myspring.cloud.common.dataSource;

public class DynamicRedisConnectionFactory extends AbstractRoutingRedisConnectionFactory{

    @Override
    protected String determineCurrentLookupKey() {
        return DbContextHolder.get().getCurrentLookupKey();
    }
}
