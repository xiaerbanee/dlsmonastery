package net.myspring.basic.common.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.myspring.util.json.ObjectMapperUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Created by liuj on 2017/4/7.
 */
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(new MappingJackson2HttpMessageConverter(ObjectMapperUtils.getObjectMapper()));
        converters.add(new StringHttpMessageConverter(StandardCharsets.UTF_8));
    }
}