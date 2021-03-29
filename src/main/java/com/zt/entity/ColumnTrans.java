package com.zt.entity;

import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/8/13 10:06
 * description:
 */
@Data
public class ColumnTrans {
    private String columnNameTrans;
    private String typeNameTrans;
    private String columnRemarks;
    private Boolean columnRangeSearch;
    private Boolean columnEqualSearch;

}
