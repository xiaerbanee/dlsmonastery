package net.myspring.cloud.modules.input.dto;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by lihx on 2017/5/11.
 */
public class RootDto {
    private String creator;
    private List<String> needUpDateFields = Lists.newArrayList();
    private List<String> needReturnFields = Lists.newArrayList();
    private Boolean isDeleteEntry = true;
    private String subSystemId;
    private ModelDto modelDto;

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public List<String> getNeedUpDateFields() {
        return needUpDateFields;
    }

    public void setNeedUpDateFields(List<String> needUpDateFields) {
        this.needUpDateFields = needUpDateFields;
    }

    public List<String> getNeedReturnFields() {
        return needReturnFields;
    }

    public void setNeedReturnFields(List<String> needReturnFields) {
        this.needReturnFields = needReturnFields;
    }

    public Boolean getDeleteEntry() {
        return isDeleteEntry;
    }

    public void setDeleteEntry(Boolean deleteEntry) {
        isDeleteEntry = deleteEntry;
    }

    public String getSubSystemId() {
        return subSystemId;
    }

    public void setSubSystemId(String subSystemId) {
        this.subSystemId = subSystemId;
    }

    public ModelDto getModelDto() {
        return modelDto;
    }

    public void setModelDto(ModelDto modelDto) {
        this.modelDto = modelDto;
    }
}
