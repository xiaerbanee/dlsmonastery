package net.myspring.cloud.modules.input.dto;


/**
 * Created by liuj on 2016-06-20.
 */
public abstract class K3CloudSaveExtendDto extends K3CloudSaveDto {

    private String nextFormId;

    public K3CloudSaveExtendDto(String formId, String content, String nextFormId) {
        super(formId, content);
        this.nextFormId = nextFormId;
    }

    public abstract String getNextBillNo();

    public String getNextFormId() {
        return nextFormId;
    }

    public void setNextFormId(String nextFormId) {
        this.nextFormId = nextFormId;
    }
}
