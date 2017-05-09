package net.myspring.cloud.modules.input.web.query;

import net.myspring.cloud.common.enums.KingdeeBillTypeEnum;

/**
 * Created by lihx on 2017/4/25.
 */
public class BatchBillQuery {

    private KingdeeBillTypeEnum[] typeList;


    public KingdeeBillTypeEnum[] getTypeList() {
        return typeList;
    }

    public void setTypeList(KingdeeBillTypeEnum[] typeList) {
        this.typeList = typeList;
    }

}
