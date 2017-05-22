package net.myspring.util.excel;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.function.Function;

/**
 * Created by liuj on 2017/2/16.
 */
public class SimpleExcelSheet {
    private String sheetName;
    private List<Object> dataList;
    private Function<Object, List> rowExpander;

    private List<SimpleExcelColumn> simpleExcelColumnList = Lists.newArrayList();

    public SimpleExcelSheet(String sheetName,List dataList,List<SimpleExcelColumn> simpleExcelColumnList) {
        this.sheetName = sheetName;
        this.dataList = dataList;
        this.simpleExcelColumnList = simpleExcelColumnList;
    }

    public SimpleExcelSheet(String sheetName,List dataList,List<SimpleExcelColumn> simpleExcelColumnList, Function<Object, List> rowExpander) {
        this.sheetName = sheetName;
        this.dataList = dataList;
        this.simpleExcelColumnList = simpleExcelColumnList;
        this.rowExpander = rowExpander;
    }

    public Function<Object, List> getRowExpander() {
        return rowExpander;
    }

    public void setRowExpander(Function<Object, List> rowExpander) {
        this.rowExpander = rowExpander;
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
