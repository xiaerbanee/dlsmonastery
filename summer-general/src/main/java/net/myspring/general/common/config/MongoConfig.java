package net.myspring.general.common.config;

import com.google.common.collect.Lists;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;

import java.util.List;

/**
 * Created by liuj on 2017/4/20.
 */
@Configuration
public class MongoConfig extends AbstractMongoConfiguration {

    @Value("${mongo.host}")
    private String mongoHost;

    @Value("${mongo.port}")
    private Integer mongoPort;

    @Value("${mongo.name}")
    private String mongoName;

    @Override
    protected String getDatabaseName() {
        return mongoName;
    }

    @Override
    public Mongo mongo() throws Exception {
        ServerAddress serverAddress = new ServerAddress(mongoHost,mongoPort);
        List<MongoCredential> credentials = Lists.newArrayList();
        return new MongoClient(serverAddress, credentials);
    }
}
