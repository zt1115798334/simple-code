package com.zt.utils;

import java.util.Random;

public class CodeUtils {

    private static final String TABLE = "T";
    private static final String COLUMN = "C";

    /**
     * 随即编码
     */

    private static final int[] r = new int[]{7, 9, 6, 2, 8, 1, 3, 0, 5, 4};
    /**
     * 用户id和随机数总长度
     */

    private static final int maxLength = 14;

    /**
     * 根据id进行加密+加随机数组成固定长度编码
     */
    private static String toCode(Long userId) {
        String idStr = userId.toString();
        StringBuilder sb = new StringBuilder();
        for (int i = idStr.length() - 1; i >= 0; i--) {
            sb.append(r[idStr.charAt(i) - '0']);
        }
        return sb.append(getRandom(maxLength - idStr.length())).toString();
    }

    /**
     * 生成时间戳
     */
    private static String getDateTime() {
        return DateUtils.formatDateTime(DateUtils.currentDateTime(), DateUtils.DATE_TIME_FORMAT_4);
    }

    /**
     * 生成固定长度随机码
     *
     * @param n 长度
     */

    private static long getRandom(long n) {
        long min = 1, max = 9;
        for (int i = 1; i < n; i++) {
            min *= 10;
            max *= 10;
        }
        return (((long) (new Random().nextDouble() * (max - min)))) + min;
    }


    /**
     * 生成不带类别标头的编码
     *
     * @param userId
     */
    private static synchronized String getCode(Long userId) {
        userId = userId == null ? 10000 : userId;
        return getDateTime() + toCode(userId);
    }


    public static String getTableCode() {
        return TABLE + getCode(0L);
    }

    public static String getColumnCode() {
        return COLUMN + getCode(0L);
    }

}
