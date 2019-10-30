package com.zt.utils;

import com.alibaba.fastjson.JSONObject;
import com.zt.entity.Column;
import com.zt.entity.Table;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.*;

import java.io.FileOutputStream;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CreateDBWord {

    public static void main(String[] args) throws Exception {
        XWPFDocument document = initDoc(getJSONData(getData()));
        FileOutputStream out = new FileOutputStream("d:\\部门通讯录2.docx");
        document.write(out);
        out.close();
        System.out.println("导出成功!!!!!!!!");
    }

    private static List<Table> getData() {
        return IntStream.range(0, 5)
                .boxed()
                .map(i -> {
                    Table table = new Table();
                    List<Column> collect = IntStream.range(0, 10)
                            .boxed()
                            .map(j -> {
                                Column column = new Column();
                                column.setColumnName("名称" + j);
                                column.setIsAutoincrement("ON");
                                column.setTypeName("varchar" + j);
                                column.setColumnSize("columnSize" + j);
                                column.setDecimalDigits("2 + j");
                                column.setIsNullable("ON");
                                column.setColumnDef("");
                                column.setRemarks("注释" + j);
                                return column;
                            }).collect(Collectors.toList());
                    table.setTableName("表明" + i);
                    table.setTableNameTrans("表别名" + i);
                    table.setColumns(collect);
                    return table;
                }).collect(Collectors.toList());

    }

    public static List<JSONObject> getJSONData(List<Table> tableList) {
        return tableList.stream()
                .map(table -> {
                    List<JSONObject> cJSONList = table.getColumns().stream()
                            .map(column -> {
                                JSONObject cJO = new JSONObject();
                                cJO.put("zdm", Optional.ofNullable(column.getColumnName()).orElse(StringUtils.EMPTY));
                                cJO.put("bs", "");
                                cJO.put("zj", Optional.ofNullable(column.getIsAutoincrement()).orElse(StringUtils.EMPTY));
                                cJO.put("lx", Optional.ofNullable(column.getTypeName()).orElse(StringUtils.EMPTY));
                                cJO.put("cd", Optional.ofNullable(column.getColumnSize()).orElse(StringUtils.EMPTY));
                                cJO.put("jd", "");
                                cJO.put("xs", Optional.ofNullable(column.getDecimalDigits()).orElse(StringUtils.EMPTY));
                                cJO.put("null", Optional.ofNullable(column.getIsNullable()).orElse(StringUtils.EMPTY));
                                cJO.put("default", Optional.ofNullable(column.getColumnDef()).orElse(StringUtils.EMPTY));
                                cJO.put("sm", Optional.ofNullable(column.getRemarks()).orElse(StringUtils.EMPTY));
                                return cJO;
                            }).collect(Collectors.toList());
                    JSONObject tJSON = new JSONObject();
                    tJSON.put("cJSONList", cJSONList);
                    tJSON.put("tableName", table.getTableName());
                    tJSON.put("tableNameTrans", table.getTableNameTrans());
                    return tJSON;
                }).collect(Collectors.toList());
    }

    public static XWPFDocument initDoc(List<JSONObject> tableList) {

        XWPFDocument document = new XWPFDocument();

        CTSectPr sectPr = document.getDocument().getBody().addNewSectPr();
        CTPageMar pageMar = sectPr.addNewPgMar();
        pageMar.setLeft(BigInteger.valueOf(1200L));
        pageMar.setTop(BigInteger.valueOf(1500L));
        pageMar.setRight(BigInteger.valueOf(1200L));
        pageMar.setBottom(BigInteger.valueOf(1500L));

        addCustomHeadingStyle(document, "t", 1);
        XWPFParagraph tParagraph = document.createParagraph();
        //设置段落居中
        tParagraph.setAlignment(ParagraphAlignment.LEFT);
        XWPFRun tParagraphRun = tParagraph.createRun();
        tParagraphRun.setText("数据字典");
        tParagraphRun.setBold(true);//加粗
        tParagraphRun.setFontSize(30);//字体大小
        tParagraphRun.setFontFamily("宋体");
        tParagraph.setStyle("t");
        //============添加标题====================
        IntStream.range(0, tableList.size())
                .boxed()
                .forEach(i -> {
                    JSONObject table = tableList.get(i);
                    String tableName = table.getString("tableName");
                    String tableNameTrans = table.getString("tableNameTrans");
                    /*XWPFParagraph firstParagraph = document.createParagraph();
                    firstParagraph.setAlignment(ParagraphAlignment.RIGHT);//设置段落内容靠右
                    firstParagraph.setIndentationRight(500);//末尾缩进300*/

                    addCustomHeadingStyle(document, i.toString(), 2);
                    //============添加标题====================
                    XWPFParagraph titleParagraph = document.createParagraph();
                    //设置段落居中
                    titleParagraph.setAlignment(ParagraphAlignment.LEFT);
                    XWPFRun titleParagraphRun = titleParagraph.createRun();
                    titleParagraphRun.setText(tableName);
                    titleParagraphRun.setBold(true);//加粗
                    titleParagraphRun.setFontSize(15);//字体大小
                    titleParagraphRun.setFontFamily("宋体");
                    titleParagraphRun.addBreak();//添加一个回车空行
                /*     XWPFRun titleSubParagraphRun = titleParagraph.createRun();
                    titleSubParagraphRun.setText(tableName);
                    titleSubParagraphRun.setBold(true);//加粗
                    titleSubParagraphRun.setFontSize(22);//字体大小
                    titleSubParagraphRun.setFontFamily("宋体");
                    titleSubParagraphRun.addBreak();//添加一个回车空行*/
                    titleParagraph.setStyle(i.toString());
                    createTable(document, table.getJSONArray("cJSONList").toJavaList(JSONObject.class));
                    document.createParagraph().createRun().addBreak();
                });

        return document;
    }

    private static void addCustomHeadingStyle(XWPFDocument document, String strStyleId, int headingLevel) {

        CTStyle ctStyle = CTStyle.Factory.newInstance();
        ctStyle.setStyleId(strStyleId);

        CTString styleName = CTString.Factory.newInstance();
        styleName.setVal(strStyleId);
        ctStyle.setName(styleName);

        CTDecimalNumber indentNumber = CTDecimalNumber.Factory.newInstance();
        indentNumber.setVal(BigInteger.valueOf(headingLevel));

        // lower number > style is more prominent in the formats bar
        ctStyle.setUiPriority(indentNumber);

        CTOnOff ctOnOff = CTOnOff.Factory.newInstance();
        ctStyle.setUnhideWhenUsed(ctOnOff);

        // style shows up in the formats bar
        ctStyle.setQFormat(ctOnOff);

        // style defines a heading of the given level
        CTPPr ppr = CTPPr.Factory.newInstance();
        ppr.setOutlineLvl(indentNumber);
        ctStyle.setPPr(ppr);

        XWPFStyle style = new XWPFStyle(ctStyle);

        // is a null op if already defined
        XWPFStyles styles = document.createStyles();

        style.setType(STStyleType.PARAGRAPH);
        styles.addStyle(style);
    }


    private static void createTable(XWPFDocument document, List<JSONObject> columnList) {
        //==============添加表格数据===========
        //数据表格
        XWPFTable comTable = document.createTable();//默认创建一个一行一列的表格

        //===========表格表头行===============
        titleStyle(comTable);//设置表头行样式和内容

        //将数据封装到表格中
        for (JSONObject columnJSON : columnList) {
            //=================================遍历具体的数据===============================
            XWPFTableRow dataRow = comTable.createRow();//创建一个新的行
            dataRow.setHeight(500);//设置行高
            for (Map.Entry<String, Object> entry : columnJSON.entrySet()) {
                List<XWPFTableCell> dataCellList = dataRow.getTableCells();//获取创建行的所有的列
                int ci = cellIndex(entry.getKey());
                XWPFTableCell tc = dataCellList.get(ci);
                dataCellData(tc, entry.getValue().toString());
            }
        }


    }

    private static int cellIndex(String string) {
        switch (string) {
            case "zdm":
                return 0;
            case "bs":
                return 1;
            case "zj":
                return 2;
            case "lx":
                return 3;
            case "cd":
                return 4;
            case "jd":
                return 5;
            case "xs":
                return 6;
            case "null":
                return 7;
            case "default":
                return 8;
            case "sm":
                return 9;
        }
        return 1;
    }

    //设置表头行样式和内容
    private static void titleStyle(XWPFTable comTable) {
        XWPFTableRow titleRow = comTable.getRow(0);//创建的的一行一列的表格，获取第一行
        titleRow.setHeight(500);//设置当前行行高
        tableTitleCellData(titleRow.getCell(0), "字段名", "2700");

        XWPFTableCell bsCell = titleRow.addNewTableCell();//在当前行继续创建新列
        tableTitleCellData(bsCell, "标识", "900");

        XWPFTableCell zjCell = titleRow.addNewTableCell();
        tableTitleCellData(zjCell, "主键", "900");

        XWPFTableCell lxCell = titleRow.addNewTableCell();
        tableTitleCellData(lxCell, "类型", "1500");

        XWPFTableCell cdCell = titleRow.addNewTableCell();
        tableTitleCellData(cdCell, "长度", "1500");

        XWPFTableCell jdCell = titleRow.addNewTableCell();
        tableTitleCellData(jdCell, "精度", "900");

        XWPFTableCell xsCell = titleRow.addNewTableCell();
        tableTitleCellData(xsCell, "小数位数", "1500");

        XWPFTableCell nullCell = titleRow.addNewTableCell();
        tableTitleCellData(nullCell, "允许空", "900");

        XWPFTableCell defaultCell = titleRow.addNewTableCell();
        tableTitleCellData(defaultCell, "默认值", "900");

        XWPFTableCell smCell = titleRow.addNewTableCell();
        tableTitleCellData(smCell, "字段说明", "2700");
    }

    //设置数据列样式
    private static void CellDataCss(XWPFTableCell cell, String width) {
        CTTc cttc = cell.getCTTc();
        CTTcPr ctPr = cttc.addNewTcPr();
        ctPr.addNewVAlign().setVal(STVerticalJc.CENTER);//上下居中
        cttc.getPList().get(0).addNewPPr().addNewJc().setVal(STJc.CENTER);//左右居中
        CTTblWidth tblWidth = ctPr.isSetTcW() ? ctPr.getTcW() : ctPr.addNewTcW();
        tblWidth.setW(new BigInteger(width));//设置列宽度
        tblWidth.setType(STTblWidth.DXA);

    }

    //设置内容信息样式
    private static void dataCellData(XWPFTableCell cell, String txt) {
        //给当前列中添加段落，就是给列添加内容
        XWPFParagraph p = cell.getParagraphs().get(0);
        XWPFRun headRun0 = p.createRun();
        headRun0.setText(txt);//设置内容
        headRun0.setFontSize(11);//设置大小
        // headRun0.setBold(true);//是否粗体
        headRun0.setFontFamily("仿宋_GB2312");
        //给列中的内容设置样式
        CTTc cttc = cell.getCTTc();
        CTTcPr ctPr = cttc.addNewTcPr();
        ctPr.addNewVAlign().setVal(STVerticalJc.CENTER);//上下居中
        cttc.getPList().get(0).addNewPPr().addNewJc().setVal(STJc.CENTER);//左右居中
        CTTblWidth tblWidth = ctPr.isSetTcW() ? ctPr.getTcW() : ctPr.addNewTcW();
        tblWidth.setType(STTblWidth.DXA);
    }

    //设置表头和单位信息样式
    private static void tableTitleCellData(XWPFTableCell cell, String txt, String with) {
        //给当前列中添加段落，就是给列添加内容
        cell.setColor("CCFFFF");
        XWPFParagraph p = cell.getParagraphs().get(0);
        XWPFRun headRun0 = p.createRun();
        headRun0.setText(txt);//设置内容
        headRun0.setFontSize(10);//设置大小
        headRun0.setBold(true);//是否粗体
        headRun0.setFontFamily("宋体");
        //给列中的内容设置样式
        CTTc cttc = cell.getCTTc();
        CTTcPr ctPr = cttc.addNewTcPr();
        ctPr.addNewVAlign().setVal(STVerticalJc.CENTER);//上下居中
        cttc.getPList().get(0).addNewPPr().addNewJc().setVal(STJc.CENTER);//左右居中
        CTTblWidth tblWidth = ctPr.isSetTcW() ? ctPr.getTcW() : ctPr.addNewTcW();
        tblWidth.setW(new BigInteger(with));//设置列宽度
        tblWidth.setType(STTblWidth.DXA);
    }

}
