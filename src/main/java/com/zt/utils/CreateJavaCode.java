package com.zt.utils;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/8/13 10:07
 * description:
 */
public class CreateJavaCode {

    public static String toUpperCaseFirstOne(String s) {
        if (Character.isUpperCase(s.charAt(0)))
            return s;
        else
            return (new StringBuilder()).append(Character.toUpperCase(s.charAt(0))).append(s.substring(1)).toString();
    }

    //首字母转小写
    public static String toLowerCaseFirstOne(String s) {
        if (Character.isLowerCase(s.charAt(0)))
            return s;
        else
            return (new StringBuilder()).append(Character.toLowerCase(s.charAt(0))).append(s.substring(1)).toString();

    }

    public static String createField(String columnType, String columnName) {
        return "\tprivate " + columnType + " " + columnName;
    }

    public static String createGet(String columnType, String columnName) {
        columnName = toUpperCaseFirstOne(columnName);
        return "\tpublic " + columnType + " get" + columnName + "() {\n" +
                "\t\treturn name;\n" +
                "\t}";
    }

    public static String createSet(String columnType, String columnName) {
        String columnNameUpperCase = toUpperCaseFirstOne(columnName);
        return "\tpublic void set" + columnNameUpperCase + "(" + columnType + " " + columnName + ") {\n" +
                "\t\tthis." + columnName + " = " + columnName + ";\n" +
                "\t}";
    }

    public static String createChangeLine() {
        return "\n";
    }

    public static void main(String[] args) {
        String columnType = "String";
        String columnName = "name";
        String get = createSet(columnType, columnName);
        System.out.println(get);
    }
}
