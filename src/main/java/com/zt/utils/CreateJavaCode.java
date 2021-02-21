package com.zt.utils;

import com.zt.entity.ColumnTrans;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/8/13 10:07
 * description:
 */
public class CreateJavaCode {

    public static String createTab(int number) {
        return IntStream.range(0, number).boxed().map(i -> "\t").collect(Collectors.joining());
    }

    public static String createEndBrackets() {
        return "}";
    }

    public static String toUpperCaseFirstOne(String s) {
        if (Character.isUpperCase(s.charAt(0)))
            return s;
        else
            return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }

    //首字母转小写
    public static String toLowerCaseFirstOne(String s) {
        if (Character.isLowerCase(s.charAt(0)))
            return s;
        else
            return Character.toLowerCase(s.charAt(0)) + s.substring(1);

    }

    public static String createRemarks(String remarks) {
        return createTab(1) + "/**\n" +
                createTab(1) + "* " + remarks + "\n" +
                createTab(1) + "*/";
    }

    public static String createField(String columnType, String columnName) {
        return createTab(1) + "private " + columnType + " " + columnName + ";";
    }

    public static String createGetFun(String columnType, String columnName) {
        String columnNameUpperCase = toUpperCaseFirstOne(columnName);
        return createTab(1) + "public " + columnType + " get" + columnNameUpperCase + "() {\n" +
                createTab(2) + "return " + columnName + ";\n" +
                createTab(1) + "}";
    }

    public static String createSetFun(String columnType, String columnName) {
        String columnNameUpperCase = toUpperCaseFirstOne(columnName);
        return createTab(1) + "public void set" + columnNameUpperCase + "(" + columnType + " " + columnName + ") {\n" +
                createTab(2) + "this." + columnName + " = " + columnName + ";\n" +
                createTab(1) + "}";
    }

    public static String createGet(String columnName) {
        return ".get" + toUpperCaseFirstOne(columnName) + "()";
    }

    public static String createSet(String columnName, String value) {
        return ".get" + toUpperCaseFirstOne(columnName) + "(" + value + ")";
    }

    public static String createChangeLine() {
        return "\n";
    }

    public static String createSaveLogic(String entityNameStatement, String entityNameStatementDb, ColumnTrans columnTran) {
        return createTab(3) + entityNameStatementDb + ".set" + toUpperCaseFirstOne(columnTran.getColumnName()) + "(" + entityNameStatement + ".get" + toUpperCaseFirstOne(columnTran.getColumnName()) + "());";
    }

    public static String createBuilder(String entityName) {
        return createTab(2) + "return " + entityName + ".builder()\n";
    }

    public static String createBuild() {
        return createTab(3) + ".build();\n";
    }


    public static String createDtoChangeEntity(String entityDtoName, String entityDtoNameStatement, String entityName) {
        return createTab(1) + "public static " + entityName + " dtoChangeEntity(" + entityDtoName + " " + entityDtoNameStatement + ") {\n";
    }

    public static String createEntityChangeDto(String entityDtoName, String entityName, String entityNameStatement) {
        return createTab(1) + "public static " + entityDtoName + " entityChangeDto(" + entityName + " " + entityNameStatement + ") {\n";
    }

    public static String createEntityChangeListDto(String entityDtoName, String entityName, String entityNameListStatement) {
        return createTab(1) + "public static List<" + entityDtoName + "> entityChangeListDto(List<" + entityName + "> " + entityNameListStatement + ") {\n" +
                createTab(2) + "return " + entityNameListStatement + ".stream().map(e -> " + entityDtoName + ".entityChangeDto(e, subjectList)).collect(Collectors.toList());\n" +
                createTab(1) + "}";
    }

    public static void main(String[] args) {
        String columnType = "String";
        String columnName = "name";
        String get = createSetFun(columnType, columnName);
        System.out.println(get);
    }
}
