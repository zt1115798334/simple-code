package com.zt.service;

import com.google.common.collect.Lists;
import com.zt.entity.Column;
import com.zt.entity.Table;
import com.zt.utils.CamelCaseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/8/11 0:11
 * description:
 */
@Component
public class ColumnService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Table> findColumnAll(List<String> tableNames) throws SQLException {

        List<Table> tables = Lists.newArrayList();
        for (String tableName : tableNames) {
            String tableNameTrans = CamelCaseUtils.underlineToHump(tableName);
            Connection connection = jdbcTemplate.getDataSource().getConnection();
            DatabaseMetaData metaData = connection.getMetaData();

            List<Column> columns = Lists.newArrayList();
            // 根据表名提前表里面信息：
            ResultSet colRet = metaData.getColumns(null, "%", tableName,
                    "%");
            while (colRet.next()) {
                System.out.println("colRet = " + colRet);
                String columnName = colRet.getString("COLUMN_NAME");
                String columnNameTrans = CamelCaseUtils.underlineToHump(columnName);
                String columnType = colRet.getString("TYPE_NAME");
                String columnTypeTrans = CamelCaseUtils.typeTrans(columnType);
                int dataSize = colRet.getInt("COLUMN_SIZE");
                int digits = colRet.getInt("DECIMAL_DIGITS");
                int nullable = colRet.getInt("NULLABLE");
                String remarks = colRet.getString("REMARKS");

                Column c = new Column(columnName, columnNameTrans, columnType, columnTypeTrans, dataSize, digits, nullable, remarks);
                columns.add(c);
            }
            Table table = new Table();
            table.setColumns(columns);
            table.setTableName(tableName);
            table.setTableNameTrans(tableNameTrans);
            tables.add(table);
        }
        return tables;
    }
}
