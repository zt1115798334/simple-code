package com.zt.entity;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/8/11 11:29
 * description:
 */
public class TableTrans {

    private String tableName;
    private String tableNameTrans;
    private String packagePath;
    private List<ColumnTrans> columnTrans;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableNameTrans() {
        return tableNameTrans;
    }

    public void setTableNameTrans(String tableNameTrans) {
        this.tableNameTrans = tableNameTrans;
    }

    public String getPackagePath() {
        return packagePath;
    }

    public void setPackagePath(String packagePath) {
        this.packagePath = packagePath;
    }

    public List<ColumnTrans> getColumnTrans() {
        return columnTrans;
    }

    public void setColumnTrans(List<ColumnTrans> columnTrans) {
        this.columnTrans = columnTrans;
    }

    @Override
    public String toString() {
        return "TableTrans{" +
                "tableName='" + tableName + '\'' +
                ", tableNameTrans='" + tableNameTrans + '\'' +
                ", packagePath='" + packagePath + '\'' +
                ", columnTrans=" + columnTrans +
                '}';
    }
}

