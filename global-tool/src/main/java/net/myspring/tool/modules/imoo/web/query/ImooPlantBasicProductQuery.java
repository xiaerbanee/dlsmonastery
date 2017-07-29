package net.myspring.tool.modules.imoo.web.query;

import java.util.List;

public class ImooPlantBasicProductQuery {
    private String description;
    private String segment1;
    private List<String> segment1List;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSegment1() {
        return segment1;
    }

    public void setSegment1(String segment1) {
        this.segment1 = segment1;
    }

    public List<String> getSegment1List() {
        return segment1List;
    }

    public void setSegment1List(List<String> segment1List) {
        this.segment1List = segment1List;
    }
}
