package net.myspring.util.excel;

import com.google.common.collect.Lists;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.Collection;

/**
 * Created by liuj on 2017/2/16.
 */
public class SimpleExcelBook {
    private Workbook workbook;
    private String name;
    private Collection<SimpleExcelSheet> simpleExcelSheets;

    public SimpleExcelBook (Workbook workbook,String name,SimpleExcelSheet simpleExcelSheet) {
        this.workbook = workbook;
        this.name = name;
        this.simpleExcelSheets = Lists.newArrayList(simpleExcelSheet);
    }

    public SimpleExcelBook (Workbook workbook,String name,Collection<SimpleExcelSheet> simpleExcelSheets) {
        this.workbook = workbook;
        this.name = name;
        this.simpleExcelSheets = simpleExcelSheets;
    }

    public Workbook getWorkbook() {
        return workbook;
    }

    public void setWorkbook(Workbook workbook) {
        this.workbook = workbook;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<SimpleExcelSheet> getSimpleExcelSheets() {
        return simpleExcelSheets;
    }

    public void setSimpleExcelSheets(Collection<SimpleExcelSheet> simpleExcelSheets) {
        this.simpleExcelSheets = simpleExcelSheets;
    }
}
