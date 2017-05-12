package net.myspring.cloud.modules.input.web.query;

import net.myspring.cloud.common.enums.SalOutStockBillTypeEnum;

/**
 * Created by lihx on 2017/5/12.
 */
public class BatchBillQuery {
    private SalOutStockBillTypeEnum[] billTypeEnums;

    public SalOutStockBillTypeEnum[] getBillTypeEnums() {
        return billTypeEnums;
    }

    public void setBillTypeEnums(SalOutStockBillTypeEnum[] billTypeEnums) {
        this.billTypeEnums = billTypeEnums;
    }
}
