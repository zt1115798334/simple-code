package com.zt.entity;

import lombok.Data;

import java.util.List;

@Data
public class DatabaseTrans {
    List<TableTrans> tableTrans;
    private String projectName;
    private boolean createBasics;
}
