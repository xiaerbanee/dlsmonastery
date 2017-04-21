package net.myspring.general.common.config;

import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
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
    public MongoTemplate mongoTemplate() {
        MappingMongoConverter converter =  new MappingMongoConverter(new DefaultDbRefResolver(mongoDbFactory), mongoMappingContext);
        converter.setTypeMapper(new DefaultMongoTypeMapper(null));
        MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory, converter);
        return mongoTemplate;
    }

    @Bean
    public GridFsTemplate exportGridFsTemplate() {
        MappingMongoConverter converter = new MappingMongoConverter(new DefaultDbRefResolver(mongoDbFactory), mongoMappingContext);
        converter.setTypeMapper(new DefaultMongoTypeMapper(null));
        GridFsTemplate gridFsTemplate = new GridFsTemplate(mongoDbFactory, converter);
        return gridFsTemplate;
    }


    @Bean
    public GridFsTemplate storageGridFsTemplate() {
        MappingMongoConverter converter = new MappingMongoConverter(new DefaultDbRefResolver(mongoDbFactory), mongoMappingContext);
        converter.setTypeMapper(new DefaultMongoTypeMapper(null));
        GridFsTemplate gridFsTemplate = new GridFsTemplate(mongoDbFactory, converter,"storage");
        return gridFsTemplate;
    }

    @Bean
    public GridFsTemplate previewGridFsTemplate() {
        MappingMongoConverter converter = new MappingMongoConverter(new DefaultDbRefResolver(mongoDbFactory), mongoMappingContext);
        converter.setTypeMapper(new DefaultMongoTypeMapper(null));
        GridFsTemplate gridFsTemplate = new GridFsTemplate(mongoDbFactory, converter,"preview");
        return gridFsTemplate;
    }
}
