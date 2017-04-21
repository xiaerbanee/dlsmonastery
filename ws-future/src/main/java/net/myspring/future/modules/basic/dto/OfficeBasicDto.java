package net.myspring.future.modules.basic.dto;

import net.myspring.common.domain.DataEntity;
import net.myspring.future.common.dto.DataDto;

import java.math.BigDecimal;

/**
 * Created by wangzm on 2017/4/21.
 */
public class OfficeBasicDto {

    private String id;
    private BigDecimal taskPoint;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public BigDecimal getTaskPoint() {
        return taskPoint;
    }

    public void setTaskPoint(BigDecimal taskPoint) {
        this.taskPoint = taskPoint;
    }
}
