package net.myspring.uaa.dto;

public class OfficeBusinessDto {
    private String id;
    private String officeId;
    private String businessOfficeId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    public String getBusinessOfficeId() {
        return businessOfficeId;
    }

    public void setBusinessOfficeId(String businessOfficeId) {
        this.businessOfficeId = businessOfficeId;
    }
}
