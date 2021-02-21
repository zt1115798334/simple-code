package com.zt.service;

import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Lists;
import com.zt.entity.Column;
import com.zt.entity.Table;
import com.zt.utils.CamelCaseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

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
            Connection connection = Objects.requireNonNull(jdbcTemplate.getDataSource()).getConnection();
            DatabaseMetaData metaData = connection.getMetaData();

            List<Column> columns = Lists.newArrayList();
            // 根据表名提前表里面信息：
            ResultSet colRet = metaData.getColumns(null, "%", tableName,
                    "%");
            while (colRet.next()) {

                String columnName = colRet.getString("COLUMN_NAME");
                String columnNameTrans = CamelCaseUtils.underlineToHump(columnName);
                String typeName = colRet.getString("TYPE_NAME");
                String typeNameTrans = CamelCaseUtils.typeTrans(typeName);
                Column c = new Column(
                        colRet.getString("TABLE_CAT"),
                        colRet.getString("TABLE_SCHEM"),
                        colRet.getString("TABLE_NAME"),
                        columnName,
                        columnNameTrans,
                        colRet.getString("DATA_TYPE"),
                        typeName,
                        typeNameTrans,
                        colRet.getString("COLUMN_SIZE"),
                        colRet.getString("BUFFER_LENGTH"),
                        colRet.getString("DECIMAL_DIGITS"),
                        colRet.getString("NUM_PREC_RADIX"),
                        colRet.getString("NULLABLE"),
                        colRet.getString("REMARKS"),
                        colRet.getString("COLUMN_DEF"),
                        colRet.getString("SQL_DATA_TYPE"),
                        colRet.getString("SQL_DATETIME_SUB"),
                        colRet.getString("CHAR_OCTET_LENGTH"),
                        colRet.getString("ORDINAL_POSITION"),
                        colRet.getString("IS_NULLABLE"),
                        colRet.getString("SCOPE_CATALOG"),
                        colRet.getString("SCOPE_SCHEMA"),
                        colRet.getString("SCOPE_TABLE"),
                        colRet.getString("SOURCE_DATA_TYPE"),
                        colRet.getString("IS_AUTOINCREMENT"),
                        colRet.getString("IS_GENERATEDCOLUMN"));
                columns.add(c);
            }

            Table table = new Table();
            table.setColumns(columns);
            table.setTableName(tableName);
            table.setTableNameTrans(tableNameTrans);
            tables.add(table);
            connection.close();
        }
        return tables;
    }
}
