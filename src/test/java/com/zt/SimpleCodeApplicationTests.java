package com.zt;

import com.zt.entity.Table;
import com.zt.service.ColumnService;
import com.zt.service.TableService;
import com.zt.utils.CreateDBWord;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import static com.zt.utils.CreateDBWord.getJSONData;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/8/10 21:54
 * description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SimpleCodeApplicationTests {

    @Autowired
    private TableService tableService;

    @Autowired
    private ColumnService columnService;
    @Test
    public void tableAll() throws Exception {
        List<Table> tableAll = tableService.findTableAll();
        List<String> tableNames = tableAll.stream().map(Table::getTableName).collect(Collectors.toList());
        List<Table> tableList = columnService.findColumnAll(tableNames);
        XWPFDocument document = CreateDBWord.initDoc(getJSONData(tableList));
        FileOutputStream out = new FileOutputStream("d:\\部门通讯录2.docx");
        document.write(out);
        out.close();
        System.out.println("导出成功!!!!!!!!");
    }
}
