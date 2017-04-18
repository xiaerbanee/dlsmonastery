package net.myspring.basic.modules.hr.dto;

import net.myspring.basic.common.dto.DataDto;
import net.myspring.basic.common.enums.DataScopeEnum;
import net.myspring.basic.modules.hr.domain.Position;
import net.myspring.util.cahe.annotation.CacheInput;
import net.myspring.util.collection.CollectionUtil;

import java.util.Map;

/**
 * Created by admin on 2017/4/5.
 */
public class PositionDto extends DataDto<Position> {
    private String name;
    private String permission;
    private Integer dataScope;
    @CacheInput(inputKey = "jobs",inputInstance = "jobId",outputInstance = "name")
    private String jobName;
    private boolean locked;
    private String jobId;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public Integer getDataScope() {
        return dataScope;
    }

    public void setDataScope(Integer dataScope) {
        this.dataScope = dataScope;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getDataScopeLabel(){
        Map<Integer, String> map = DataScopeEnum.getMap();
        if(CollectionUtil.isNotEmpty(map)){
            return map.get(dataScope);
        }
        return "";
    }
}
