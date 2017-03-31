package net.myspring.util.time;

import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Created by liuj on 2017/3/31.
 */
public class LocalDateUtils {

    public static final String FORMATTER = "yyyy-MM-dd";

    public static String format(LocalDate localDate) {
        return format(localDate,FORMATTER);
    }

    public static String format(LocalDate localDate,String formatter) {
        String result = "";
        if(localDate != null) {
            result = localDate.format(DateTimeFormatter.ofPattern(formatter));
        }
        return result;
    }

    public static LocalDate parse(String localDateStr) {
        return parse(localDateStr,FORMATTER);
    }

    public static LocalDate parse(String localDateStr,String formatter) {
        LocalDate localDate = null;
        if(StringUtils.isNotBlank(localDateStr)) {
            localDate =LocalDate.parse(localDateStr,DateTimeFormatter.ofPattern(formatter));
        }
        return localDate;
    }

}
