package net.myspring.cloud.common.excel;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.cloud.common.enums.DateFormat;
import net.myspring.common.exception.ServiceException;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.text.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class ExcelUtils {
    public static final String CELL_STYLE_HEADER = "cellStyleHeader";
    public static final String CELL_STYLE_DATA = "cellStyleData";
    public static final String CELL_STYLE_YELLOW = "cellStyleYellow";
    public static final String CELL_STYLE_LIGHT_GREEN = "cellStyleLightGreen";
    public static final String CELL_STYLE_RED = "cellStyleRed";


    public static String getSheetName(String name) {
        if (StringUtils.isBlank(name)) {
            return null;
        } else {
            return name.trim().replace("/", "").replace("\\", "").replace("?", "").replace("[", "").replace("]", "").replace("*", "");
        }
    }

    public static Map<String, CellStyle> getCellStyleMap(Workbook workbook) {
        Map<String, CellStyle> map = Maps.newHashMap();
        CellStyle cellStyleHeader = workbook.createCellStyle();
        cellStyleHeader.setAlignment(CellStyle.ALIGN_LEFT);
        cellStyleHeader.setBorderRight(CellStyle.BORDER_THIN);
        cellStyleHeader.setRightBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        cellStyleHeader.setBorderLeft(CellStyle.BORDER_THIN);
        cellStyleHeader.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        cellStyleHeader.setBorderTop(CellStyle.BORDER_THIN);
        cellStyleHeader.setTopBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        cellStyleHeader.setBorderBottom(CellStyle.BORDER_THIN);
        cellStyleHeader.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
        cellStyleHeader.setFillPattern(CellStyle.SOLID_FOREGROUND);
        Font headerFont = workbook.createFont();
        headerFont.setFontName("微软雅黑");
        headerFont.setFontHeightInPoints((short) 10);
        headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        headerFont.setColor(IndexedColors.WHITE.getIndex());
        cellStyleHeader.setFont(headerFont);
        map.put(CELL_STYLE_HEADER, cellStyleHeader);

        CellStyle cellStyleData = workbook.createCellStyle();
        cellStyleData.setAlignment(CellStyle.ALIGN_LEFT);
        cellStyleData.setBorderRight(CellStyle.BORDER_THIN);
        cellStyleData.setRightBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        cellStyleData.setBorderLeft(CellStyle.BORDER_THIN);
        cellStyleData.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        cellStyleData.setBorderTop(CellStyle.BORDER_THIN);
        cellStyleData.setTopBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        cellStyleData.setBorderBottom(CellStyle.BORDER_THIN);
        cellStyleData.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        Font dataFont = workbook.createFont();
        dataFont.setFontName("微软雅黑");
        dataFont.setFontHeightInPoints((short) 10);
        cellStyleData.setFont(dataFont);
        map.put(CELL_STYLE_DATA, cellStyleData);

        CellStyle cellStyleYellow = workbook.createCellStyle();
        cellStyleYellow.cloneStyleFrom(cellStyleData);
        cellStyleYellow.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
        cellStyleYellow.setFillPattern(CellStyle.SOLID_FOREGROUND);
        map.put(CELL_STYLE_YELLOW, cellStyleYellow);

        CellStyle cellStyleLightGreen = workbook.createCellStyle();
        cellStyleLightGreen.cloneStyleFrom(cellStyleData);
        cellStyleLightGreen.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        cellStyleLightGreen.setFillPattern(CellStyle.SOLID_FOREGROUND);
        map.put(CELL_STYLE_LIGHT_GREEN, cellStyleLightGreen);

        CellStyle cellStyleRed = workbook.createCellStyle();
        cellStyleRed.cloneStyleFrom(cellStyleData);
        cellStyleRed.setFillForegroundColor(IndexedColors.RED.getIndex());
        cellStyleRed.setFillPattern(CellStyle.SOLID_FOREGROUND);
        map.put(CELL_STYLE_RED, cellStyleRed);
        return map;
    }

    public static ExcelBook getExcelBook(String name, List<List<Object>> datas) {
        ExcelBook excelBook = new ExcelBook(name);
        ExcelSheet excelSheet = new ExcelSheet();
        excelSheet.setName("导出数据");
        List<List<ExcelCell>> excelCellList = Lists.newArrayList();
        for (int i = 0; i < datas.size(); i++) {
            List<Object> objectRow = datas.get(i);
            List<ExcelCell> cellRow = Lists.newArrayList();
            for (Object obj : objectRow) {
                ExcelCell excelCell = new ExcelCell();
                excelCell.setValue(obj);
                if (i == 0) {
                    excelCell.setCellStyleType(CELL_STYLE_HEADER);
                } else {
                    excelCell.setCellStyleType(CELL_STYLE_DATA);
                }
                cellRow.add(excelCell);
            }
            excelCellList.add(cellRow);
        }
        excelSheet.setExcelCells(excelCellList);
        excelBook.getExcelSheets().add(excelSheet);
        return excelBook;
    }

    public static ExcelBook getExcelBook(String name, Map<String, List<List<Object>>> dataMap) {
        ExcelBook excelBook = new ExcelBook(name);
        for (String sheetName : dataMap.keySet()) {
            List<List<Object>> datas = dataMap.get(sheetName);
            ExcelSheet excelSheet = new ExcelSheet();
            excelSheet.setName(sheetName);
            List<List<ExcelCell>> excelCellList = Lists.newArrayList();
            for (int i = 0; i < datas.size(); i++) {
                List<Object> objectRow = datas.get(i);
                List<ExcelCell> cellRow = Lists.newArrayList();
                for (Object obj : objectRow) {
                    ExcelCell excelCell = new ExcelCell();
                    excelCell.setValue(obj);
                    if (i == 0) {
                        excelCell.setCellStyleType(CELL_STYLE_HEADER);
                    } else {
                        excelCell.setCellStyleType(CELL_STYLE_DATA);
                    }
                    cellRow.add(excelCell);
                }
                excelCellList.add(cellRow);
            }
            excelSheet.setExcelCells(excelCellList);
            excelBook.getExcelSheets().add(excelSheet);
        }
        return excelBook;
    }

    public static Workbook getWorkbook(Workbook workbook, ExcelBook excelBook) {
        Map<String, CellStyle> cellStyleMap = getCellStyleMap(workbook);
        for (ExcelSheet excelSheet : excelBook.getExcelSheets()) {
            Sheet sheet = workbook.createSheet(getSheetName(excelSheet.getName()));
            sheet.setDefaultColumnWidth(excelSheet.getDefaultColumnWidth());
            if (excelSheet.getColumnWidthMap().size() > 0) {
                for (Integer columnIndex : excelSheet.getColumnWidthMap().keySet()) {
                    sheet.setColumnWidth(columnIndex, excelSheet.getColumnWidthMap().get(columnIndex));
                }
            }
            if (CollectionUtil.isNotEmpty(excelSheet.getExcelCells())) {
                for (int i = 0; i < excelSheet.getExcelCells().size(); i++) {
                    List<ExcelCell> excelCellList = excelSheet.getExcelCells().get(i);
                    Row row = sheet.createRow(i);
                    for (int j = 0; j < excelCellList.size(); j++) {
                        ExcelCell excelCell = excelCellList.get(j);
                        Cell cell = row.createCell(j);
                        cell.setCellStyle(cellStyleMap.get(excelCell.getCellStyleType()));
                        Object obj = excelCell.getValue();
                        if (obj == null) {
                            cell.setCellValue("");
                        } else if (obj instanceof Integer) {
                            cell.setCellValue((Integer) obj);
                        } else if (obj instanceof Long) {
                            cell.setCellValue((Long) obj);
                        } else if (obj instanceof Double) {
                            cell.setCellValue((Double) obj);
                        } else if (obj instanceof BigDecimal) {
                            cell.setCellValue(((BigDecimal) obj).doubleValue());
                        } else if (obj instanceof Float) {
                            cell.setCellValue((Float) obj);
                        } else if (obj instanceof LocalDateTime) {
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DateFormat.DATE_TIME.getValue());
                            cell.setCellValue(((LocalDateTime) obj).format(formatter));
                        } else if (obj instanceof String) {
                            if (((String) obj).startsWith("=")) {
                                cell.setCellFormula(String.valueOf(obj));
                                cell.setCellType(Cell.CELL_TYPE_FORMULA);
                            } else {
                                cell.setCellValue(String.valueOf(obj));
                            }
                        }
                    }
                }
                if (excelSheet.getAutoWidth()) {
                    for (int i = 0; i < excelSheet.getExcelCells().get(0).size(); i++) {
                        sheet.autoSizeColumn(i);
                    }
                }
            }
        }
        return workbook;
    }


    public static ExcelBook getExcelBook(MultipartFile multipartFile) {
        ExcelBook excelBook = new ExcelBook();
        Workbook workbook = null;
        String fileName = multipartFile.getOriginalFilename();
        InputStream inputStream = null;
        try {
            inputStream = multipartFile.getInputStream();
            if (inputStream != null) {
                if (StringUtils.isBlank(fileName)) {
                    throw new ServiceException("文件不可为空");
                } else if (fileName.toLowerCase().endsWith("xls")) {
                    workbook = new HSSFWorkbook(inputStream);
                } else if (fileName.toLowerCase().endsWith("xlsx")) {
                    workbook = new XSSFWorkbook(inputStream);
                } else {
                    throw new ServiceException("文档格式不正确，请确认文件后缀是以xls或xlsx");
                }
            }
        } catch (IOException e) {
            throw new ServiceException("文件导入失败");
        }
        if (workbook != null) {
            excelBook.setName(fileName);
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                Sheet sheet = workbook.getSheetAt(i);
                if (sheet != null) {
                    ExcelSheet excelSheet = new ExcelSheet();
                    // 循环行Row
                    for (int rownum = 0; rownum <= sheet.getLastRowNum(); rownum++) {
                        Row row = sheet.getRow(rownum);
                        if (row != null) {
                            List<Object> rowData = Lists.newArrayList();
                            for (int cellnum = 0; cellnum < row.getLastCellNum(); cellnum++) {
                                Cell cell = row.getCell(cellnum);
                                rowData.add(getCellValue(cell));
                            }
                            excelSheet.getDatas().add(rowData);
                        }
                    }
                    excelBook.getExcelSheets().add(excelSheet);
                }
            }
        }
        return excelBook;
    }

    public static Object getCellValue(Cell cell) {
        Object cellValue = "";
        if (cell != null) {
            if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
                if (HSSFDateUtil.isCellDateFormatted(cell)) {
                    cellValue = cell.getDateCellValue();
                } else {
                    DecimalFormat df = new DecimalFormat("0");
                    cellValue = df.format(cell.getNumericCellValue());
                }
            } else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
                cellValue = cell.getStringCellValue().trim();
            } else if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
                cellValue = cell.getCellFormula();
            } else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
                cellValue = cell.getBooleanCellValue();
            } else if (cell.getCellType() == Cell.CELL_TYPE_ERROR) {
                cellValue = cell.getErrorCellValue();
            }
        }
        return cellValue;
    }

    public static String getStringCellValue(Cell cell) {
        return String.valueOf(getCellValue(cell));
    }

}
