package com.zt.entity;

import lombok.Data;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/8/11 11:29
 * description:
 */
@Data
public class TableTrans {

    private String tableName;
    private String tableNameTrans;
    private String packagePath;
    private List<ColumnTrans> columnTrans;

}

