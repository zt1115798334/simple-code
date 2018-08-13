package com.zt.utils;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/8/10 22:43
 * description:
 */
public class CamelCaseUtils {

    public static String underlineToHump(String para) {
        StringBuilder result = new StringBuilder();
        String a[] = para.split("_");
        for (String s : a) {
            if (result.length() == 0) {
                result.append(s.toLowerCase());
            } else {
                result.append(s.substring(0, 1).toUpperCase());
                result.append(s.substring(1).toLowerCase());
            }
        }
        return result.toString();
    }


    public static String typeTrans(String type) {
        String result = "";
        switch (type) {
            case "BIGINT":
                result = "Long";
                break;
            case "VARCHAR":
                result = "String";
                break;
            case "LONGTEXT":
                result = "String";
                break;
            case "INT":
                result = "Integer";
                break;
            case "CHAR":
                result = "String";
                break;
            case "DATETIME":
                result = "LocalDateTime";
                break;
            case "TINYINT":
                result = "Integer";
                break;
        }
        return result;
    }

    public static void main(String[] args) {
        String tableName = "t_name";
        System.out.println("tableName = " + underlineToHump(tableName));
    }
}
