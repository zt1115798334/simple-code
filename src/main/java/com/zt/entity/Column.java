package com.zt.entity;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/8/10 19:36
 * description:
 */
public class Column {

    private String columnName;

    private String columnNameTrans;

    private String columnType;

    private String columnTypeTrans;

    private Integer dataSize;

    private Integer digits;

    private Integer nullable;

    private String remarks;

    public Column() {
    }

    public Column(String columnName, String columnNameTrans, String columnType, String columnTypeTrans, Integer dataSize, Integer digits, Integer nullable, String remarks) {
        this.columnName = columnName;
        this.columnNameTrans = columnNameTrans;
        this.columnType = columnType;
        this.columnTypeTrans = columnTypeTrans;
        this.dataSize = dataSize;
        this.digits = digits;
        this.nullable = nullable;
        this.remarks = remarks;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnNameTrans() {
        return columnNameTrans;
    }

    public void setColumnNameTrans(String columnNameTrans) {
        this.columnNameTrans = columnNameTrans;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public String getColumnTypeTrans() {
        return columnTypeTrans;
    }

    public void setColumnTypeTrans(String columnTypeTrans) {
        this.columnTypeTrans = columnTypeTrans;
    }

    public Integer getDataSize() {
        return dataSize;
    }

    public void setDataSize(Integer dataSize) {
        this.dataSize = dataSize;
    }

    public Integer getDigits() {
        return digits;
    }

    public void setDigits(Integer digits) {
        this.digits = digits;
    }

    public Integer getNullable() {
        return nullable;
    }

    public void setNullable(Integer nullable) {
        this.nullable = nullable;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Override
    public String toString() {
        return "Column{" +
                "columnName='" + columnName + '\'' +
                ", columnNameTrans='" + columnNameTrans + '\'' +
                ", columnType='" + columnType + '\'' +
                ", columnTypeTrans='" + columnTypeTrans + '\'' +
                ", dataSize=" + dataSize +
                ", digits=" + digits +
                ", nullable=" + nullable +
                ", remarks='" + remarks + '\'' +
                '}';
    }
}
