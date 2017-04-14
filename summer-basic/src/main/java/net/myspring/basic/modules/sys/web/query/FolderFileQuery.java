package net.myspring.basic.modules.sys.web.query;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Created by lihx on 2017/4/7.
 */
public class FolderFileQuery {
    private LocalDate createdDate;

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdDate) {
        this.createdDate = createdDate;
    }
}
