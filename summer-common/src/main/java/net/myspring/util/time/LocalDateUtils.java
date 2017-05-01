package net.myspring.util.time;

import com.google.common.collect.Lists;
import net.myspring.util.text.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

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

    public static List<LocalDate> getDateList(LocalDate dateStart, LocalDate dateEnd) {
        List<LocalDate> dateList = Lists.newArrayList();
        if (dateEnd.isBefore(dateStart)) {
            return dateList;
        }
        Long step = dateEnd.toEpochDay() - dateStart.toEpochDay();
        if (step >= 1) {
            for (int i = 0; i <= step; i++) {
                dateList.add(dateStart.plusDays(i));
            }
        }
        return dateList;
    }

    public static String getDayOfWeek(LocalDate localDate) {
        if(localDate!=null){
            Locale defaultLocal = Locale.getDefault();
            return localDate.getDayOfWeek().getDisplayName(TextStyle.FULL, defaultLocal);
        }else {
            return null;
        }
    }

}
