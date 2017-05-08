package net.myspring.cloud.modules.input.web.query;

import net.myspring.cloud.common.enums.K3CloudBillTypeEnum;

/**
 * Created by lihx on 2017/4/25.
 */
public class BatchBillQuery {

    private K3CloudBillTypeEnum[] typeList;


    public K3CloudBillTypeEnum[] getTypeList() {
        return typeList;
    }

    public void setTypeList(K3CloudBillTypeEnum[] typeList) {
        this.typeList = typeList;
    }

}
