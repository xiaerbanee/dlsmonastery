package net.myspring.cloud.modules.input.utils;


import net.myspring.cloud.modules.input.dto.K3CloudSaveDto;

/**
 * Created by liuj on 2016-06-20.
 */
public abstract class K3CloudSaveExtend extends K3CloudSaveDto {

    private String nextFormId;

    public K3CloudSaveExtend(String formId, String content, String nextFormId) {
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
