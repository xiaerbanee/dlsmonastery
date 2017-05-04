package net.myspring.cloud.common.excel;

public class ExcelCell {
    private String cellStyleType;
    private Object value;
    private String comment;

    public ExcelCell() {

    }

    public ExcelCell(Object value, String cellStyleType) {
        this.value = value;
        this.cellStyleType = cellStyleType;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getCellStyleType() {
        return cellStyleType;
    }

    public void setCellStyleType(String cellStyleType) {
        this.cellStyleType = cellStyleType;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
