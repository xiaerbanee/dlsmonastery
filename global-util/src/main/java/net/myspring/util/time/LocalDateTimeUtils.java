package net.myspring.util.time;

import net.myspring.util.text.TextValidator;
import org.apache.commons.lang3.StringUtils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Created by liuj on 2017/3/31.
 */
public class LocalDateTimeUtils {
    public static final String FORMATTER_SECOND = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMATTER_MINUTE = "yyyy-MM-dd HH:mm";
    public static final String FORMATTER_MILLISECOND = "yyyy-MM-dd HH:mm:ss.SSS";

    private static Pattern PATTERN_REGEX_MINUTE = Pattern.compile("^\\d4-\\d2-\\d2 \\d2:\\d2$");
    private static Pattern PATTERN_REGEX_SECOND = Pattern.compile("^\\d4-\\d2-\\d2 \\d2:\\d2:\\d2$");
    private static Pattern PATTERN_REGEX_MILLISECOND = Pattern.compile("^\\d4-\\d2-\\d2 \\d2:\\d2\\.\\d3$");


    public static String format(LocalDateTime localDateTime) {
        return format(localDateTime, FORMATTER_SECOND);
    }

    public static String format(LocalDateTime localDateTime, String formatter) {
        String result = "";
        if (localDateTime != null) {
            result = localDateTime.format(DateTimeFormatter.ofPattern(formatter));
        }
        return result;
    }

    public static LocalDateTime parse(String localDateTimeStr) {
        String formatter = "";
        if (TextValidator.isMatch(PATTERN_REGEX_SECOND, localDateTimeStr)) {
            formatter = FORMATTER_SECOND;
        } else if (TextValidator.isMatch(PATTERN_REGEX_MINUTE, localDateTimeStr)) {
            formatter = FORMATTER_MINUTE;
        } else if (TextValidator.isMatch(PATTERN_REGEX_MILLISECOND, localDateTimeStr)) {
            formatter = FORMATTER_MILLISECOND;
        }
        if (StringUtils.isNotBlank(formatter)) {
            return parse(localDateTimeStr, formatter);
        } else {
            return null;
        }
    }

    public static LocalDateTime parse(String localDateTimeStr, String formatter) {
        LocalDateTime localDateTime = null;
        if (StringUtils.isNotBlank(localDateTimeStr)) {
            localDateTime = LocalDateTime.parse(localDateTimeStr, DateTimeFormatter.ofPattern(formatter));
        }
        return localDateTime;
    }

    public static LocalDateTime getFirstDayOfMonth(LocalDateTime localDateTime) {
        return localDateTime.withDayOfMonth(1);
    }

    public static LocalDateTime getLastDayOfMonth(LocalDateTime localDateTime) {
        return localDateTime.withDayOfMonth(localDateTime.toLocalDate().lengthOfMonth());
    }
}
