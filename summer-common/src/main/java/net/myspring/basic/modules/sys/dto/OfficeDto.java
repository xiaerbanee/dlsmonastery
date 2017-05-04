package net.myspring.basic.modules.sys.dto;

import java.math.BigDecimal;

/**
 * Created by wangzm on 2017/5/4.
 */
public class OfficeDto {
    private String id;
    private String name;
    private BigDecimal taskPoint;

    public BigDecimal getTaskPoint() {
        return taskPoint;
    }

    public void setTaskPoint(BigDecimal taskPoint) {
        this.taskPoint = taskPoint;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
