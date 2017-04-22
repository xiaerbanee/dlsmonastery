package net.myspring.basic.modules.sys.dto;

import net.myspring.basic.common.dto.DataDto;
import net.myspring.basic.common.enums.DictMapCategoryEnum;
import net.myspring.basic.common.utils.Global;
import net.myspring.basic.modules.sys.domain.Office;
import net.myspring.util.cahe.annotation.CacheInput;
import net.myspring.util.text.StringUtils;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Created by admin on 2017/4/5.
 */
public class OfficeDto extends DataDto<Office> {

    private String name;
    private BigDecimal point;
    private String parentId;
    private boolean locked;
    private boolean enabled;
    private String type;
    private BigDecimal taskPoint;

    @CacheInput(inputKey = "offices",inputInstance = "parentId",outputInstance = "name")
    private String parentName;

    public BigDecimal getTaskPoint() {
        return taskPoint;
    }

    public void setTaskPoint(BigDecimal taskPoint) {
        this.taskPoint = taskPoint;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPoint() {
        return point;
    }

    public void setPoint(BigDecimal point) {
        this.point = point;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getTypeLabel(){
        if(StringUtils.isNotBlank(type)){
            Map<String,String> map= Global.getDictMapList(DictMapCategoryEnum.机构分类.name());
            return map.get(type);
        }
        return "";
    }
}
