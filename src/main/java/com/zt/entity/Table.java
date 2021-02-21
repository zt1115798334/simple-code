package com.zt.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/8/10 19:36
 * description:
 */
@Data
public class Table {
    private String tableName;

    private String tableNameTrans;

    private List<Column> columns;

}
