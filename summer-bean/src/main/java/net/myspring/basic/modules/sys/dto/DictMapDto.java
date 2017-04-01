package net.myspring.basic.modules.sys.dto;

import com.google.common.collect.Lists;
import net.myspring.basic.modules.sys.domain.DictMap;
import net.myspring.common.dto.DataDto;
import net.myspring.util.cahe.annotation.CacheInput;

import java.util.List;

/**
 * Created by admin on 2017/4/1.
 */
public class DictMapDto extends DataDto<DictMap>{
    private String category;
    private String name;
    private String value;

    @CacheInput(inputKey = "account",inputInstance = "createdBy",outputInstance = "loginName")
    private String createdByName;

    private List<String> createdByList;

    @CacheInput(inputKey = "account",inputInstance = "createdByList",outputInstance = "loginName")
    private List<String> createdByNameList;

    private List<String> lastModifiedByList;

    @CacheInput(inputKey = "account",inputInstance = "lastModifiedByList",outputInstance = "loginName")
    private List<String> lastModifiedByNameList;

    public List<String> getLastModifiedByList() {
        return lastModifiedByList;
    }

    public void setLastModifiedByList(List<String> lastModifiedByList) {
        this.lastModifiedByList = lastModifiedByList;
    }

    public List<String> getLastModifiedByNameList() {
        return lastModifiedByNameList;
    }

    public void setLastModifiedByNameList(List<String> lastModifiedByNameList) {
        this.lastModifiedByNameList = lastModifiedByNameList;
    }

    public List<String> getCreatedByList() {
        return this.createdByList;
    }

    public void setCreatedByList(List<String> createdByList) {
        this.createdByList = createdByList;
    }

    public List<String> getCreatedByNameList() {
        return createdByNameList;
    }

    public void setCreatedByNameList(List<String> createdByNameList) {
        this.createdByNameList = createdByNameList;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
