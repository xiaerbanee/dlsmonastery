package net.myspring.cloud.modules.report.dto;

/**
 *handsonTable-nestedHerder
 * Created by lihx on 2017/7/6.
 */
public class NestedHeaderCellDto {
    String label;
    Integer colspan;

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
    public NestedHeaderCellDto(String label, Integer colSpan) {
        this.label = label;
        this.colspan = colSpan;
    }
    public NestedHeaderCellDto(){};
}
