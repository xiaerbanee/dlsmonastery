package net.myspring.util.excel;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by liuj on 2017/2/16.
 */
public class SimpleExcelSheet {
    private String sheetName;
    private List<Object> dataList;
    private List<SimpleExcelColumn> simpleExcelColumnList = Lists.newArrayList();

    public SimpleExcelSheet(String sheetName,List dataList,List<SimpleExcelColumn> simpleExcelColumnList) {
        this.sheetName = sheetName;
        this.dataList = dataList;
        this.simpleExcelColumnList = simpleExcelColumnList;
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
