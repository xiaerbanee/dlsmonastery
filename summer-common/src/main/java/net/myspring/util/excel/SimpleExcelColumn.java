package net.myspring.util.excel;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;

/**
 * Created by liuj on 2017/2/16.
 */
public class SimpleExcelColumn {
    private String fieldName;
    private Integer width;
    private String label;
    private CellStyle headerStyle;
    private CellStyle cellStyle;
    private String fieldKey;
    private Object value;

    public SimpleExcelColumn(String fieldName,String label) {
        this.fieldName = fieldName;
        this.label = label;
    }

    public SimpleExcelColumn(CellStyle cellStyle,Object value) {
        this.cellStyle = cellStyle;
        this.value = value;
    }

    public SimpleExcelColumn(Workbook workbook,String fieldName, String label) {
        this.fieldName = fieldName;
        this.label = label;
        this.headerStyle = ExcelUtils.getCellStyleMap(workbook).get(ExcelCellStyle.HEADER.name());
        this.cellStyle =  ExcelUtils.getCellStyleMap(workbook).get(ExcelCellStyle.DATA.name());
    }

    public SimpleExcelColumn(Workbook workbook,String fieldName,String label,String fieldKey) {
        this.fieldName = fieldName;
        this.label = label;
        this.fieldKey = fieldKey;
        this.headerStyle = ExcelUtils.getCellStyleMap(workbook).get(ExcelCellStyle.HEADER.name());
        this.cellStyle =  ExcelUtils.getCellStyleMap(workbook).get(ExcelCellStyle.DATA.name());
    }

    public SimpleExcelColumn(Workbook workbook,String fieldName, String label,Integer width) {
        this.fieldName = fieldName;
        this.label = label;
        this.width = width;
        this.headerStyle = ExcelUtils.getCellStyleMap(workbook).get(ExcelCellStyle.HEADER.name());
        this.cellStyle =  ExcelUtils.getCellStyleMap(workbook).get(ExcelCellStyle.DATA.name());
    }

    public SimpleExcelColumn(String fieldName,Integer width,String label,CellStyle headerStyle,CellStyle cellStyle) {
        this.fieldName = fieldName;
        this.width = width;
        this.label = label;
        this.headerStyle = headerStyle;
        this.cellStyle = cellStyle;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getFieldKey() {
        return fieldKey;
    }

    public void setFieldKey(String fieldKey) {
        this.fieldKey = fieldKey;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public CellStyle getHeaderStyle() {
        return headerStyle;
    }

    public void setHeaderStyle(CellStyle headerStyle) {
        this.headerStyle = headerStyle;
    }

    public CellStyle getCellStyle() {
        return cellStyle;
    }

    public void setCellStyle(CellStyle cellStyle) {
        this.cellStyle = cellStyle;
    }
}
