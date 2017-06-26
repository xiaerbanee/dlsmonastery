package net.myspring.uaa.dto;

public class OfficeRuleDto {
    private String id;
    private String name;
    private String code;
    private Boolean hasPoint;
    private Integer level;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Boolean getHasPoint() {
        return hasPoint;
    }

    public void setHasPoint(Boolean hasPoint) {
        this.hasPoint = hasPoint;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }
}
