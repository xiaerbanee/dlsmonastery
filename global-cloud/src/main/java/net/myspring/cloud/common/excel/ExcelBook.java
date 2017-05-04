package net.myspring.cloud.common.excel;

import com.google.common.collect.Lists;

import java.util.List;

public class ExcelBook {
    private String name;
    private List<ExcelSheet> excelSheets = Lists.newArrayList();

    public ExcelBook() {
    }

    public ExcelBook(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ExcelSheet> getExcelSheets() {
        return excelSheets;
    }

    public void setExcelSheets(List<ExcelSheet> excelSheets) {
        this.excelSheets = excelSheets;
    }

}
