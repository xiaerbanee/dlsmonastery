package net.myspring.cloud.common.excel;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

public class ExcelSheet {
    private String name;
    private List<List<ExcelCell>> excelCells = Lists.newArrayList();
    private List<List<Object>> datas = Lists.newArrayList();
    private Map<Integer, Integer> columnWidthMap = Maps.newHashMap();
    private Integer defaultColumnWidth = 12;
    private Boolean autoWidth = true;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<List<ExcelCell>> getExcelCells() {
        return excelCells;
    }

    public void setExcelCells(List<List<ExcelCell>> excelCells) {
        this.excelCells = excelCells;
    }

    public List<List<Object>> getDatas() {
        return datas;
    }

    public void setDatas(List<List<Object>> datas) {
        this.datas = datas;
    }

    public Map<Integer, Integer> getColumnWidthMap() {
        return columnWidthMap;
    }

    public void setColumnWidthMap(Map<Integer, Integer> columnWidthMap) {
        this.columnWidthMap = columnWidthMap;
    }

    public Integer getDefaultColumnWidth() {
        return defaultColumnWidth;
    }

    public void setDefaultColumnWidth(Integer defaultColumnWidth) {
        this.defaultColumnWidth = defaultColumnWidth;
    }

    public Boolean getAutoWidth() {
        return autoWidth;
    }

    public void setAutoWidth(Boolean autoWidth) {
        this.autoWidth = autoWidth;
    }

}
