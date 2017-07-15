package net.myspring.cloud.common.dataSource;

import org.apache.commons.lang.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisClusterConnection;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisSentinelConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.util.Assert;

import java.util.Map;

public abstract class AbstractRoutingRedisConnectionFactory implements RedisConnectionFactory {

    private Map<String, JedisConnectionFactory> targetConnections;

    private String defaultLookupKey;

    @Override
    public RedisConnection getConnection() {
        return determineTargetConnection().getConnection();
    }

    @Override
    public RedisClusterConnection getClusterConnection() {
        return determineTargetConnection().getClusterConnection();
    }

    @Override
    public boolean getConvertPipelineAndTxResults() {
        return determineTargetConnection().getConvertPipelineAndTxResults();
    }

    @Override
    public RedisSentinelConnection getSentinelConnection() {
        return determineTargetConnection().getSentinelConnection();
    }

    @Override
    public DataAccessException translateExceptionIfPossible(RuntimeException ex) {
        return determineTargetConnection().translateExceptionIfPossible(ex);
    }

    public Map<String, JedisConnectionFactory> getTargetConnections() {
        return targetConnections;
    }

    public void setTargetConnections(Map<String, JedisConnectionFactory> targetConnections) {
        this.targetConnections = targetConnections;
    }

    public String getDefaultLookupKey() {
        return defaultLookupKey;
    }

    public void setDefaultLookupKey(String defaultLookupKey) {
        this.defaultLookupKey = defaultLookupKey;
    }

    protected JedisConnectionFactory determineTargetConnection() {
        Assert.notNull(this.targetConnections, "Connection router not initialized");
        String lookupKey = determineCurrentLookupKey();
        if(StringUtils.isBlank(lookupKey)) {
            lookupKey = defaultLookupKey;
        }
        JedisConnectionFactory jedisConnectionFactory= this.targetConnections.get(lookupKey);
        if (jedisConnectionFactory == null) {
            throw new IllegalStateException("Cannot determine target Connection for lookup key [" + lookupKey + "]");
        }
        return jedisConnectionFactory;
    }

    protected abstract String determineCurrentLookupKey();
}
