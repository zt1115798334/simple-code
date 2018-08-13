package com.zt.entity;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/8/10 19:36
 * description:
 */
public class Table {
    private String tableName;

    private String tableNameTrans;

    private List<Column> columns;


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

    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    @Override
    public String toString() {
        return "Table{" +
                "tableName='" + tableName + '\'' +
                '}';
    }
}
