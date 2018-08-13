package com.zt.entity;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/8/13 10:06
 * description:
 */
public class ColumnTrans {
    private String columnName;
    private String columnType;
    private String columnRemarks;

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public String getColumnRemarks() {
        return columnRemarks;
    }

    public void setColumnRemarks(String columnRemarks) {
        this.columnRemarks = columnRemarks;
    }

    @Override
    public String toString() {
        return "ColumnTrans{" +
                "columnName='" + columnName + '\'' +
                ", columnType='" + columnType + '\'' +
                ", columnRemarks='" + columnRemarks + '\'' +
                '}';
    }
}
