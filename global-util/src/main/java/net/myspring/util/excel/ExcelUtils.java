package net.myspring.util.excel;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.util.reflect.ReflectionUtil;
import net.myspring.util.time.LocalDateTimeUtils;
import net.myspring.util.time.LocalDateUtils;
import net.myspring.util.time.LocalTimeUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by liuj on 2017/2/16.
 */
public class ExcelUtils {
    public static void doWrite(Workbook workbook,Collection<SimpleExcelSheet> simpleExcelSheets) {
        if(CollectionUtils.isNotEmpty(simpleExcelSheets)) {
            for(SimpleExcelSheet simpleExcelSheet:simpleExcelSheets) {
                Sheet sheet = workbook.createSheet(simpleExcelSheet.getSheetName());
                doWriteSheet(sheet,simpleExcelSheet);
            }
        }
    }

    public static void doWrite(Workbook workbook,SimpleExcelSheet simpleExcelSheet) {
        Sheet sheet = workbook.createSheet(simpleExcelSheet.getSheetName());
        doWriteSheet(sheet,simpleExcelSheet);
    }

    public static Workbook getWorkbook(File file) {
        Workbook workbook = null;
        String fileName = file.getName();
        InputStream inputStream;
        try {
            inputStream = new FileInputStream(file);
            if (inputStream != null) {
                if (StringUtils.isBlank(fileName)) {
                    throw new RuntimeException("文件不可为空");
                } else if (fileName.toLowerCase().endsWith("xls")) {
                    workbook = new HSSFWorkbook(inputStream);
                } else if (fileName.toLowerCase().endsWith("xlsx")) {
                    workbook = new XSSFWorkbook(inputStream);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return workbook;
    }

    public static Workbook getWorkbook(MultipartFile multipartFile) {
        Workbook workbook = null;
        String fileName = multipartFile.getOriginalFilename();
        InputStream inputStream;
        try {
            inputStream = multipartFile.getInputStream();
            if (inputStream != null) {
                if (StringUtils.isBlank(fileName)) {
                    throw new RuntimeException("文件不可为空");
                } else if (fileName.toLowerCase().endsWith("xls")) {
                    workbook = new HSSFWorkbook(inputStream);
                } else if (fileName.toLowerCase().endsWith("xlsx")) {
                    workbook = new XSSFWorkbook(inputStream);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return workbook;
    }

    public static <T> List<T> doRead(Sheet sheet,List<SimpleExcelColumn> columnList,Class<T> clazz) {
        return doRead(sheet,columnList,clazz,0,1);
    }

    public  static <T> List<T> doRead(Sheet sheet,List<SimpleExcelColumn> columnList,Class<T> clazz,Integer headerRowNumber,Integer startRowNumber) {
        Map<String,Integer> headerLabelMap = Maps.newHashMap();
        Row headerRow = sheet.getRow(headerRowNumber);
        for (int i = 0; i < headerRow.getLastCellNum(); i++) {
            Cell cell = headerRow.getCell(i);
            String label = String.valueOf(getCellValue(cell));
            if(StringUtils.isNotBlank(label)) {
                headerLabelMap.put(label,i);
            }
        }
        List<T> list = Lists.newArrayList();
        for (int i = startRowNumber; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if(row != null) {
                T instance = null;
                try {
                    instance = clazz.newInstance();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                for (int j = 0; j < columnList.size(); j++) {
                    String label=columnList.get(j).getLabel();
                    String fieldName = columnList.get(j).getFieldName();
                    Integer columnIndex = headerLabelMap.get(label);
                    if(columnIndex != null) {
                        Cell cell = row.getCell(columnIndex);
                        Object value=getCellValue(cell);
                        ReflectionUtil.setFieldValue(instance,fieldName,value);
                    }
                }
                list.add(instance);
            }
        }
        return list;
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
        map.put(ExcelCellStyle.HEADER.name(), cellStyleHeader);

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
        map.put(ExcelCellStyle.DATA.name(), cellStyleData);

        CellStyle cellStyleYellow = workbook.createCellStyle();
        cellStyleYellow.cloneStyleFrom(cellStyleData);
        cellStyleYellow.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
        cellStyleYellow.setFillPattern(CellStyle.SOLID_FOREGROUND);
        map.put(ExcelCellStyle.YELLOW.name(), cellStyleYellow);

        CellStyle cellStyleLightGreen = workbook.createCellStyle();
        cellStyleLightGreen.cloneStyleFrom(cellStyleData);
        cellStyleLightGreen.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        cellStyleLightGreen.setFillPattern(CellStyle.SOLID_FOREGROUND);
        map.put(ExcelCellStyle.LIGHT_GREEN.name(), cellStyleLightGreen);

        CellStyle cellStyleRed = workbook.createCellStyle();
        cellStyleRed.cloneStyleFrom(cellStyleData);
        cellStyleRed.setFillForegroundColor(IndexedColors.RED.getIndex());
        cellStyleRed.setFillPattern(CellStyle.SOLID_FOREGROUND);
        map.put(ExcelCellStyle.RED.name(), cellStyleRed);

        CellStyle cellStyleLightBlue=workbook.createCellStyle();
        cellStyleLightBlue.cloneStyleFrom(cellStyleData);
        cellStyleLightBlue.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
        cellStyleLightBlue.setFillPattern(CellStyle.SOLID_FOREGROUND);
        map.put(ExcelCellStyle.LIGHT_BLUE.name(),cellStyleLightBlue);
        return map;
    }


    private static void doWriteSheet(Sheet sheet,SimpleExcelSheet simpleExcelSheet) {
        if(CollectionUtils.isNotEmpty(simpleExcelSheet.getDataList())) {
            int rowIndex = 0;
            Row row = sheet.createRow(rowIndex);
            for(int i=0;i<simpleExcelSheet.getSimpleExcelColumnList().size();i++) {
                SimpleExcelColumn simpleExcelColumn = simpleExcelSheet.getSimpleExcelColumnList().get(i);
                if(simpleExcelColumn.getWidth() != null) {
                    if(simpleExcelColumn.getWidth()>0) {
                        sheet.setColumnWidth(i,simpleExcelColumn.getWidth());
                    } else {
                        sheet.autoSizeColumn(i);
                    }
                }
                Cell cell = row.createCell(i);
                cell.setCellStyle(simpleExcelColumn.getHeaderStyle());
                setCellValue(cell,simpleExcelColumn.getLabel());
            }
            for(Object rowValue:simpleExcelSheet.getDataList()) {
                rowIndex = rowIndex + 1;
                row = sheet.createRow(rowIndex);
                for(int i=0;i<simpleExcelSheet.getSimpleExcelColumnList().size();i++) {
                    SimpleExcelColumn simpleExcelColumn = simpleExcelSheet.getSimpleExcelColumnList().get(i);
                    Object value=null;
                    if(StringUtils.isNotBlank(simpleExcelColumn.getFieldName())){
                        value = ReflectionUtil.getFieldValue(rowValue,simpleExcelColumn.getFieldName());
                    }
                    Cell cell = row.createCell(i);
                    cell.setCellStyle(simpleExcelColumn.getCellStyle());
                    setCellValue(cell,value);
                }
            }
        }
    }

    private static Object getCellValue(Cell cell) {
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

    private static void setCellValue(Cell cell,Object value) {
        if (value == null) {
            cell.setCellValue("");
        } else if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Long) {
            cell.setCellValue((Long) value);
        } else if (value instanceof Double) {
            cell.setCellValue((Double) value);
        } else if (value instanceof BigDecimal) {
            cell.setCellValue(((BigDecimal) value).doubleValue());
        } else if (value instanceof Float) {
            cell.setCellValue((Float) value);
        }else if (value instanceof LocalDate){
            cell.setCellValue(LocalDateUtils.format((LocalDate) value));
        } else if (value instanceof LocalDateTime){
            cell.setCellValue(LocalDateTimeUtils.format((LocalDateTime) value));
        } else if (value instanceof LocalTime){
            cell.setCellValue(LocalTimeUtils.format((LocalTime) value));
        }else if (value instanceof String) {
            String itemValue = (String)value;
            if (itemValue.startsWith("=")) {
                cell.setCellType(Cell.CELL_TYPE_FORMULA);
                cell.setCellFormula(itemValue);
            } else {
                cell.setCellValue(itemValue);
            }
        } else {
            cell.setCellValue(String.valueOf(value));
        }
    }
}
