package net.myspring.util.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import net.myspring.util.text.TextValidator;
import net.myspring.util.time.LocalTimeUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

/**
 * Created by liuj on 2016-09-10.
 */
public class LocalTimeDeserializer extends JsonDeserializer<LocalTime> {
    @Override
    public LocalTime deserialize(JsonParser p, DeserializationContext ctx) throws IOException, JsonProcessingException {
        LocalTime localTime = LocalTimeUtils.parse(p.getText());
        return localTime;
    }
}
