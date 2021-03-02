package com.zt.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/8/10 19:36
 * description:
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Table implements Serializable {

    private String code;

    private String tableName;

    private String tableNameTrans;

    private List<Column> columns;

}
