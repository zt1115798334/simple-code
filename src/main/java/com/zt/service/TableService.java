package com.zt.service;

import com.google.common.collect.Lists;
import com.zt.entity.Table;
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
 * date: 2018/8/10 22:00
 * description:
 */
@Component
public class TableService {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    public List<Table> findTableAll() throws SQLException {
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet rs = metaData.getTables(null, "", null,
                new String[]{"TABLE", "VIEW"});
        List<Table> tables = Lists.newArrayList();
        while (rs.next()) {
            Table table = new Table();
            if (rs.getString(4) != null
                    && (rs.getString(4).equalsIgnoreCase("TABLE") || rs
                    .getString(4).equalsIgnoreCase("VIEW"))) {
                String tableName = rs.getString(3).toLowerCase();
                System.out.print(tableName + "\t");
                table.setTableName(tableName);
            }
            tables.add(table);
        }
        return tables;
    }
}
