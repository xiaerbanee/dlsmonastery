package net.myspring.basic.modules.sys.web.query;

import java.time.LocalDateTime;

/**
 * Created by lihx on 2017/4/7.
 */
public class DictEnumQuery {
    private String category;
    private String value;
    private LocalDateTime createdDateStart;
    private LocalDateTime createdDateEnd;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public LocalDateTime getCreatedDateStart() {
        return createdDateStart;
    }

    public void setCreatedDateStart(LocalDateTime createdDateStart) {
        this.createdDateStart = createdDateStart;
    }

    public LocalDateTime getCreatedDateEnd() {
        return createdDateEnd;
    }

    public void setCreatedDateEnd(LocalDateTime createdDateEnd) {
        this.createdDateEnd = createdDateEnd;
    }
}
