package net.myspring.cloud.modules.input.dto;


/**
 * Created by liuj on 2016-06-20.
 */
public abstract class KingdeeSynExtendDto extends KingdeeSynDto {

    private String nextFormId;

    public KingdeeSynExtendDto(String formId, String content, String nextFormId) {
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
