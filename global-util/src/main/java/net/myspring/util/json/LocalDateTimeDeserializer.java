package net.myspring.util.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import net.myspring.util.text.TextValidator;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

/**
 * Created by liuj on 2016-09-10.
 */
public class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {
    private static Pattern PATTERN_REGEX_MINUTE =Pattern.compile("^\\d4-\\d2-\\d2 \\d2:\\d2$");
    private static Pattern PATTERN_REGEX_SECOND =Pattern.compile("^\\d4-\\d2-\\d2 \\d2:\\d2:\\d2$");
    private static Pattern PATTERN_REGEX_MS =Pattern.compile("^\\d4-\\d2-\\d2 \\d2:\\d2\\.\\d3$");
    @Override
    public LocalDateTime deserialize(JsonParser p, DeserializationContext ctx) throws IOException, JsonProcessingException {
        LocalDateTime localDateTime = null;
        if(StringUtils.isNotBlank(p.getText())) {
            String pattern = "";
            if(TextValidator.isMatch(PATTERN_REGEX_SECOND,p.getText())) {
                pattern = "yyyy-MM-dd HH:mm:ss";
            } else if(TextValidator.isMatch(PATTERN_REGEX_MINUTE,p.getText())) {
                pattern = "yyyy-MM-dd HH:mm";
            } else if(TextValidator.isMatch(PATTERN_REGEX_MS,p.getText())) {
                pattern = "yyyy-MM-dd HH:mm:ss.SSS";
            }
            if(StringUtils.isNotBlank(pattern)) {
                DateTimeFormatter formatter =  DateTimeFormatter.ofPattern(pattern);
                localDateTime = LocalDateTime.parse(p.getText(),formatter);
            }
        }
        return localDateTime;
    }
}
