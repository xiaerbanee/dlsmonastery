package net.myspring.cloud.modules.input.dto;


import net.myspring.cloud.modules.sys.domain.KingdeeBook;

/**
 * Created by liuj on 2016-06-20.
 */
public abstract class KingdeeSynExtendDto extends KingdeeSynDto {

    private String nextFormId;

    public KingdeeSynExtendDto(String extendId,String extendType,String formId, String content, KingdeeBook kingdeeBook, String nextFormId) {
        super(extendId,extendType,formId, content,kingdeeBook);
        this.nextFormId = nextFormId;
    }

    public KingdeeSynExtendDto(Boolean success, String result) {
        super(success,result);
    }

    public abstract String getNextBillNo();

    public String getNextFormId() {
        return nextFormId;
    }

    public void setNextFormId(String nextFormId) {
        this.nextFormId = nextFormId;
    }

}
