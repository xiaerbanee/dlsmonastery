package net.myspring.util.time;

import net.myspring.util.text.TextValidator;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

/**
 * Created by liuj on 2017/3/31.
 */
public class LocalTimeUtils {
    public static final String FORMATTER_SECOND= "HH:mm:ss";
    public static final String FORMATTER_MINUTE = "HH:mm";
    public static final String FORMATTER_MILLISECOND= "HH:mm:ss.SSS";

    private static Pattern PATTERN_REGEX_MINUTE =Pattern.compile("^\\d{2}:\\d{2}$");
    private static Pattern PATTERN_REGEX_SECOND =Pattern.compile("^\\d{2}:\\d{2}:\\d{2}$");
    private static Pattern PATTERN_REGEX_MILLISECOND =Pattern.compile("^\\d{2}:\\d{2}\\.\\d{3}$");


    public static String format(LocalTime localTime) {
        return format(localTime,FORMATTER_SECOND);
    }

    public static String format(LocalTime localTime, String formatter) {
        String result = "";
        if(localTime != null) {
            result = localTime.format(DateTimeFormatter.ofPattern(formatter));
        }
        return result;
    }

    public static LocalTime parse(String localTimeStr) {
        String formatter = "";
        if(TextValidator.isMatch(PATTERN_REGEX_SECOND,localTimeStr)) {
            formatter = FORMATTER_SECOND;
        } else if(TextValidator.isMatch(PATTERN_REGEX_MINUTE,localTimeStr)) {
            formatter = FORMATTER_MINUTE;
        } else if(TextValidator.isMatch(PATTERN_REGEX_MILLISECOND,localTimeStr)) {
            formatter = FORMATTER_MILLISECOND;
        }
        if(StringUtils.isNotBlank(formatter)) {
            return parse(localTimeStr,formatter);
        } else {
            return null;
        }
    }

    public static LocalTime parse(String localTimeStr,String formatter) {
        LocalTime localTime = null;
        if(StringUtils.isNotBlank(localTimeStr)) {
            localTime =LocalTime.parse(localTimeStr,DateTimeFormatter.ofPattern(formatter));
        }
        return localTime;
    }
}
