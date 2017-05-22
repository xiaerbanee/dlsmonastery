package net.myspring.util.excel;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.function.BiFunction;

/**
 * Created by liuj on 2017/2/16.
 */
public class SimpleExcelColumn {

    private String fieldName;
    private Integer width;
    private String label;
    private CellStyle headerStyle;
    private CellStyle cellStyle;
    private BiFunction colValueGetter;


    public SimpleExcelColumn(String fieldName,String label) {
        this.fieldName = fieldName;
        this.label = label;
    }


    public SimpleExcelColumn(Workbook workbook , String fieldName, String label) {

        this.fieldName = fieldName;
        this.label = label;
        this.headerStyle = ExcelUtils.getCellStyleMap(workbook).get(ExcelCellStyle.HEADER.name());
        this.cellStyle =  ExcelUtils.getCellStyleMap(workbook).get(ExcelCellStyle.DATA.name());
    }

    public SimpleExcelColumn(Workbook workbook , BiFunction<? extends Object, Integer, Object> colValueGetter, String label) {
        this.colValueGetter = colValueGetter;

        this.label = label;
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

    public BiFunction getColValueGetter() {
        return colValueGetter;
    }

    public void setColValueGetter(BiFunction colValueGetter) {
        this.colValueGetter = colValueGetter;
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
