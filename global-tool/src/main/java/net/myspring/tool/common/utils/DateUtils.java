package net.myspring.tool.common.utils;

import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Created by liuj on 2016/11/10.
 */
public class DateUtils {

    public static LocalDate parseLocalDate(String localDateStr) {
        if(StringUtils.isNotBlank(localDateStr)){
            String format="yyyy-MM-dd";
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            if(format.length()!=localDateStr.length()){
                localDateStr=localDateStr.substring(0,format.length());
            }
            LocalDate localDate = LocalDate.parse(localDateStr, formatter);
            return localDate;
        }
        return null;
    }

    public static LocalDateTime parseLocalDateTime(String localDateTimeStr) {
        String format = "yyyy-MM-dd HH:mm";
        if(localDateTimeStr.length() != format.length()) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        LocalDateTime localDateTime = LocalDateTime.parse(localDateTimeStr, formatter);
        return localDateTime;
    }

    public static LocalTime parseLocalTime(String localTimeStr){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalTime localTime = LocalTime.parse(localTimeStr,formatter);
        return localTime;
    }

    public static String formatLocalDate(LocalDate localDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return localDate.format(formatter);
    }

    public static String formatLocalDate(LocalDate localDate,String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return localDate.format(formatter);
    }

    public static String formatLocalDateTime(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return localDateTime.format(formatter);
    }

    public static String formatLocalTime(LocalTime localTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return localTime.format(formatter);
    }

    public static LocalDate getFirstDayOfLastMonth(LocalDate date){
        LocalDate initial = date.minusMonths(1);
        return initial.withDayOfMonth(1);
    }

    public static LocalDateTime getFirstDayOfThisMonth(LocalDateTime localDateTime){
        return localDateTime.withDayOfMonth(1);
    }

    public static LocalDateTime getLastDayOfThisMonth(LocalDateTime localDateTime){
        return localDateTime.withDayOfMonth(localDateTime.toLocalDate().lengthOfMonth());
    }

    public static LocalDateTime getLocalDateTimeStart(LocalDateTime localDateTime){
        LocalDateTime dateTime=LocalDateTime.of(localDateTime.toLocalDate(),LocalTime.MIN);
        return dateTime;
    }

    public static LocalDateTime getLocalDateTimeEnd(LocalDateTime localDateTime){
        LocalDateTime dateTime=LocalDateTime.of(localDateTime.toLocalDate(),LocalTime.MAX);
        return dateTime;
    }

    public static LocalDate getFirstDayOfThisMonth(LocalDate localDate){
        return localDate.withDayOfMonth(1);
    }

    public static LocalDate getLastDayOfThisMonth(LocalDate date){
        LocalDate initial = date.minusMonths(1);
        return initial.withDayOfMonth(initial.lengthOfMonth());
    }

    public static LocalDate getFirstDayOfLastWeek(LocalDate date){
        LocalDate lastMonday = date.with(DayOfWeek.MONDAY).minusWeeks(1) ;
        return lastMonday;
    }

    public static LocalDate getLastDayOfLastWeek(LocalDate date){
        LocalDate lastMonday = date.with(DayOfWeek.SUNDAY).minusWeeks(1);
        return lastMonday;
    }

    // 时间是否交叉
    public static boolean isCross(LocalTime from1, LocalTime to1, LocalTime from2, LocalTime to2) {
        if (from1 == null || to1 == null || from2 == null || to2 == null) {
            return false;
        }
        LocalTime minTime = from1;
        LocalTime maxTime= to1;
        if (from2.isBefore(from1)) {
            minTime = from2;
        }
        if (to2.isAfter(to1)) {
            maxTime = to2;
        }
        Long maxDiff = ChronoUnit.MILLIS.between(minTime,maxTime);
        Long diff =ChronoUnit.MILLIS.between(from1,to1) + ChronoUnit.MILLIS.between(from2,to2);
        if (maxDiff >= diff) {
            return false;
        } else {
            return true;
        }
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

    public static Boolean isAm(LocalTime time) {
        return time.getHour() < 12 ? true : false;
    }
}
