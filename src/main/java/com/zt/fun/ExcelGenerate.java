package com.zt.fun;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.*;

import java.io.*;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.time.temporal.TemporalAdjusters;
import java.util.Locale;

public class ExcelGenerate {
    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("D:\\demo.xlsx");
        FileOutputStream outputStream = new FileOutputStream(file);
        ExcelGenerate exportExcel = new ExcelGenerate();
        exportExcel.exportXSExcelByProject(outputStream);

    }

    public void exportXSExcelByProject(OutputStream out) {
        // 声明一个工作薄
        XSSFWorkbook workbook = new XSSFWorkbook();
        // 生成一个表格
        XSSFSheet sheet0 = workbook.createSheet();
        XSSFCellStyle style = workbook.createCellStyle();
        // 设置这些样式
        setCellStyle(style);
        // 生成一个字体
        XSSFFont font = workbook.createFont();
        font.setColor(IndexedColors.BLACK.getIndex());
        font.setFontHeightInPoints((short) 12);
        // 把字体应用到当前的样式
        style.setFont(font);
        createSheet0(sheet0, style, workbook);
        WorkBookWrite(out, workbook);
    }

    public void createSheet0(XSSFSheet sheet, XSSFCellStyle style, XSSFWorkbook workbook) {

        LocalDate date = LocalDate.now();
        XSSFRow xssfRow_0 = sheet.createRow(0);
        XSSFRow xssfRow_1 = sheet.createRow(1);
        for (int i = 1; i <= 12; i++) {
            LocalDate month = date.withMonth(i);
            int firstDayOfMonth = month.with(TemporalAdjusters.firstDayOfMonth()).getDayOfYear();
            int lastDayOfMonth = month.with(TemporalAdjusters.lastDayOfMonth()).getDayOfYear();
            CellRangeAddress cellRangeAddress = new CellRangeAddress(0, 0, firstDayOfMonth, lastDayOfMonth);
            sheet.addMergedRegion(cellRangeAddress);
            String name = month.getMonth().getDisplayName(TextStyle.SHORT, Locale.CHINA);
            createCell(xssfRow_0, firstDayOfMonth, name, style);
            for (int j = 0; j < month.lengthOfMonth(); j++) {
                createCell(xssfRow_1, firstDayOfMonth + j, String.valueOf(j+1), style);
            }
        }
    }


    public void setRegionBorder(CellRangeAddress cellRangeAddress, XSSFSheet sheet, XSSFWorkbook workbook) {
        RegionUtil.setBorderBottom(BorderStyle.THIN, cellRangeAddress, sheet); // 下边框
        RegionUtil.setBorderLeft(BorderStyle.THIN, cellRangeAddress, sheet); // 左边框
        RegionUtil.setBorderRight(BorderStyle.THIN, cellRangeAddress, sheet); // 有边框
        RegionUtil.setBorderTop(BorderStyle.THIN, cellRangeAddress, sheet); // 上边框
    }

    public void createCell(XSSFRow xssfRow, int cell, String txt, CellStyle style) {
        XSSFCell xssfCell = xssfRow.createCell(cell);
        xssfCell.setCellStyle(style);
        xssfCell.setCellValue(new XSSFRichTextString(txt));
    }

    private void setCellStyle(CellStyle style2) {
        style2.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        style2.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style2.setBorderBottom(BorderStyle.THIN);
        style2.setBorderLeft(BorderStyle.THIN);
        style2.setBorderRight(BorderStyle.THIN);
        style2.setBorderTop(BorderStyle.THIN);
        style2.setAlignment(HorizontalAlignment.CENTER);
        style2.setVerticalAlignment(VerticalAlignment.CENTER);

    }

    private void WorkBookWrite(OutputStream out, Workbook workbook) {
        try {
            workbook.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(workbook);
            IOUtils.closeQuietly(out);
        }
    }
}
