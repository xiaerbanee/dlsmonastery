package net.myspring.util.excel;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by liuj on 2017/2/16.
 */
public class SimpleExcelSheet {
    private String sheetName;
    private List<Object> dataList;
    private boolean isExcelColumn;
    private List<SimpleExcelColumn> simpleExcelColumnList = Lists.newArrayList();

    private List<List<SimpleExcelColumn>> excelColumnList = Lists.newArrayList();

    public SimpleExcelSheet(String sheetName,List dataList,List<SimpleExcelColumn> simpleExcelColumnList) {
        this.sheetName = sheetName;
        this.dataList = dataList;
        this.simpleExcelColumnList = simpleExcelColumnList;
    }

    public SimpleExcelSheet(String sheetName,List<List<SimpleExcelColumn>> excelColumnList) {
        this.sheetName = sheetName;
        this.excelColumnList = excelColumnList;
        isExcelColumn=true;
    }

    public boolean getIsExcelColumn() {
        return isExcelColumn;
    }

    public void setExcelColumn(boolean excelColumn) {
        isExcelColumn = excelColumn;
    }

    public List<List<SimpleExcelColumn>> getExcelColumnList() {
        return excelColumnList;
    }

    public void setExcelColumnList(List<List<SimpleExcelColumn>> excelColumnList) {
        this.excelColumnList = excelColumnList;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public List<Object> getDataList() {
        return dataList;
    }

    public void setDataList(List<Object> dataList) {
        this.dataList = dataList;
    }

    public List<SimpleExcelColumn> getSimpleExcelColumnList() {
        return simpleExcelColumnList;
    }

    public void setSimpleExcelColumnList(List<SimpleExcelColumn> simpleExcelColumnList) {
        this.simpleExcelColumnList = simpleExcelColumnList;
    }
}
