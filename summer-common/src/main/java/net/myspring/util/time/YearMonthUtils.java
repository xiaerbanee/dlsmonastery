package net.myspring.util.time;

import com.google.common.collect.Lists;
import net.myspring.util.text.StringUtils;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

/**
 * Created by liuj on 2017/3/31.
 */
public class YearMonthUtils {

    public static final String FORMATTER_FULL = "yyyy-MM";
    public static final String FORMATTER_SINGLE = "yyyy.M";

    public static String format(YearMonth yearMonth) {
        return format(yearMonth,FORMATTER_FULL);
    }

    public static String format(YearMonth yearMonth,String formatter) {
        String result = "";
        if(yearMonth != null) {
            result = yearMonth.format(DateTimeFormatter.ofPattern(formatter));
        }
        return result;
    }
}
