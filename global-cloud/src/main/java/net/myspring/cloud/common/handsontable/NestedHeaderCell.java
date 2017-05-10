package net.myspring.cloud.common.handsontable;

/**
 * Created by lihx on 2016/11/7.
 */
public class NestedHeaderCell {
    String label;
    Integer colspan;

    public NestedHeaderCell(){
    };

    public NestedHeaderCell(String label, Integer colSpan) {
        this.label = label;
        this.colspan = colSpan;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Integer getColspan() {
        return colspan;
    }

    public void setColspan(Integer colspan) {
        this.colspan = colspan;
    }
}
