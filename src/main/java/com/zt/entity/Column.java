package com.zt.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/8/10 19:36
 * description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Column  implements Serializable {

    private String tableCat;
    private String tableSchem;
    private String tableName;
    private String columnName;
    private String columnNameTrans;
    private String dataType;
    private String typeName;
    private String typeNameTrans;
    private String columnSize;
    private String bufferLength;
    private String decimalDigits;
    private String numPrecRadix;
    private String nullable;
    private String remarks;
    private String columnDef;
    private String sqlDataType;
    private String sqlDatetimeSub;
    private String charOctetLength;
    private String ordinalPosition;
    private String isNullable;
    private String scopeCatalog;
    private String scopeSchema;
    private String scopeTable;
    private String sourceDataType;
    private String isAutoincrement;
    private String isGeneratedColumn;

}




