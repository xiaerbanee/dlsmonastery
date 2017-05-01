package net.myspring.cloud.common.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

/**
 * Created by liuj on 2017/4/20.
 */
@Configuration
public class MongoConfig {

    @Autowired
    private MongoDbFactory mongoDbFactory;
    @Autowired
    private MongoMappingContext mongoMappingContext;

    @Bean
    public GridFsTemplate tempGridFsTemplate() {
        MappingMongoConverter converter = new MappingMongoConverter(new DefaultDbRefResolver(mongoDbFactory), mongoMappingContext);
        converter.setTypeMapper(new DefaultMongoTypeMapper(null));
        GridFsTemplate gridFsTemplate = new GridFsTemplate(mongoDbFactory, converter,"temp");
        return gridFsTemplate;
    }

    @Bean
    public GridFsTemplate storageGridFsTemplate() {
        MappingMongoConverter converter = new MappingMongoConverter(new DefaultDbRefResolver(mongoDbFactory), mongoMappingContext);
        converter.setTypeMapper(new DefaultMongoTypeMapper(null));
        GridFsTemplate gridFsTemplate = new GridFsTemplate(mongoDbFactory, converter,"storage");
        return gridFsTemplate;
    }
}
