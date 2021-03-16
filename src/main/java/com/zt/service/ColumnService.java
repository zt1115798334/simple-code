package com.zt.service;

import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Lists;
import com.zt.entity.Column;
import com.zt.entity.Table;
import com.zt.utils.CamelCaseUtils;
import com.zt.utils.CodeUtils;
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
                Column c = Column.builder()
                        .code(CodeUtils.getColumnCode())
                        .tableCat(colRet.getString("TABLE_CAT"))
                        .tableSchema(colRet.getString("TABLE_SCHEM"))
                        .tableName(colRet.getString("TABLE_NAME"))
                        .columnName(columnName)
                        .columnNameTrans(columnNameTrans)
                        .columnType(colRet.getString("DATA_TYPE"))
                        .typeName(typeName)
                        .typeNameTrans(typeNameTrans)
                        .columnSize(colRet.getString("COLUMN_SIZE"))
                        .bufferLength(colRet.getString("BUFFER_LENGTH"))
                        .decimalDigits(colRet.getString("DECIMAL_DIGITS"))
                        .numPrecedingRadix(colRet.getString("NUM_PREC_RADIX"))
                        .nullable(colRet.getString("NULLABLE"))
                        .remarks(colRet.getString("REMARKS"))
                        .columnDef(colRet.getString("COLUMN_DEF"))
                        .sqlDataType(colRet.getString("SQL_DATA_TYPE"))
                        .sqlDatetimeSub(colRet.getString("SQL_DATETIME_SUB"))
                        .charOctetLength(colRet.getString("CHAR_OCTET_LENGTH"))
                        .ordinalPosition(colRet.getString("ORDINAL_POSITION"))
                        .isNullable(colRet.getString("IS_NULLABLE"))
                        .scopeCatalog(colRet.getString("SCOPE_CATALOG"))
                        .scopeSchema(colRet.getString("SCOPE_SCHEMA"))
                        .scopeTable(colRet.getString("SCOPE_TABLE"))
                        .sourceDataType(colRet.getString("SOURCE_DATA_TYPE"))
                        .isAutoincrement(colRet.getString("IS_AUTOINCREMENT"))
                        .isGeneratedColumn(colRet.getString("IS_GENERATEDCOLUMN"))
                        .columnRangeSearch(true)
                        .columnEqualSearch(true).build();
                columns.add(c);
            }

            Table table = new Table();
            table.setColumns(columns);
            table.setTableName(tableName);
            table.setTableNameTrans(tableNameTrans.substring(1));
            tables.add(table);
            connection.close();
        }
        return tables;
    }

}
