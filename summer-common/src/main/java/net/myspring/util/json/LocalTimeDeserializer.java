package net.myspring.util.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import net.myspring.util.time.LocalTimeUtils;

import java.io.IOException;
import java.time.LocalTime;

/**
 * Created by liuj on 2016-09-10.
 */
public class LocalTimeDeserializer extends JsonDeserializer<LocalTime> {
    @Override
    public LocalTime deserialize(JsonParser p, DeserializationContext ctx) throws IOException {
        LocalTime localTime = LocalTimeUtils.parse(p.getText());
        return localTime;
    }
}
