package com.zt.entity;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
    private boolean lombokState;
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

    public boolean isLombokState() {
        return lombokState;
    }

    public void setLombokState(boolean lombokState) {
        this.lombokState = lombokState;
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


    public static void main(String[] args) {
        ExecutorService threadPool = Executors.newFixedThreadPool(6);
        threadPool.submit(() -> {
            for (int i = 0; i < 10; i++) {
                System.out.println("dddddddi = " + i);
            }
            for (int i = 0; i < 10; i++) {
                System.out.println("ffffffi = " + i);

            }
            return 0;
        });

    }
}

