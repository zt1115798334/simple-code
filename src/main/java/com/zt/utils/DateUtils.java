package com.zt.utils;

import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/7/16 9:48
 * description:时间工具类
 */
public class DateUtils {

    public static final String DATE_TIME_FORMAT = "yyyy/MM/dd HH:mm";

    /**
     * 获取最新时间
     *
     */
    public static LocalDateTime currentDateTime() {
        return LocalDateTime.now();
    }

    /**
     * 时间格式字符串转换为时间
     *
     */
    public static LocalDateTime parseDateTime(String dateTime, String dateFormat) {
        LocalDateTime result = null;
        if (StringUtils.isNotEmpty(dateTime)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
            result = LocalDateTime.parse(dateTime, formatter);
        }
        return result;
    }


    /**
     * 格式化时间为 yyyy-MM-dd HH:mm:ss 格式
     *
     */
    public static String formatDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }
        return formatDate(dateTime, DATE_TIME_FORMAT);
    }

    /**
     * 格式化时间
     *
     */
    public static String formatDate(LocalDateTime dateTime, String dateFormat) {
        String result = null;
        if (dateTime != null && StringUtils.isNotEmpty(dateFormat)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
            result = formatter.format(dateTime);
        }
        return result;
    }


    public static void main(String[] args) {
        LocalDateTime localDateTime = currentDateTime();
        System.out.println("localDateTime = " + formatDateTime(localDateTime));
    }

}
