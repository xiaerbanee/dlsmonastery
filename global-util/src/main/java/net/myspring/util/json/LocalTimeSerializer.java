package net.myspring.util.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by liuj on 2016-08-20.
 */
public class LocalTimeSerializer extends JsonSerializer<LocalTime> {
    @Override
    public void serialize(LocalTime value, JsonGenerator gen, SerializerProvider provider) throws IOException, JsonProcessingException {
        String string = value==null?"":value.format(DateTimeFormatter.ofPattern("HH:mm"));
        gen.writeString(string);
    }
}
