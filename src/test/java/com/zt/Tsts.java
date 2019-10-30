package com.zt;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFStyles;
import org.junit.Test;

import java.io.FileOutputStream;
import java.io.IOException;

public class Tsts {

    @Test
    public void name() throws IOException {
        // 新建的word文档对象
        XWPFDocument doc = new XWPFDocument();
        // 获取新建文档对象的样式
        XWPFStyles newStyles = doc.createStyles();
        // 关键行// 修改设置文档样式为静态块中读取到的样式
//        newStyles.setStyles(wordStyles);

        // 开始内容输入
        // 标题1，1级大纲
        XWPFParagraph para1 = doc.createParagraph();
        // 关键行// 1级大纲
        para1.setStyle("1");
        XWPFRun run1 = para1.createRun();
        // 标题内容
        run1.setText("标题 1");

        // 标题2
        XWPFParagraph para2 = doc.createParagraph();
        // 关键行// 2级大纲
        para2.setStyle("2");
        XWPFRun run2 = para2.createRun();
        // 标题内容
        run2.setText("标题 2");

        // 正文
        XWPFParagraph paraX = doc.createParagraph();
        XWPFRun runX = paraX.createRun();
        // 正文内容
        runX.setText("正文");

        // word写入到文件
        FileOutputStream fos = new FileOutputStream("D:/myDoc.docx");
        doc.write(fos);
        fos.close();

    }
}
