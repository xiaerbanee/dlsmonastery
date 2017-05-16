package net.myspring.cloud.modules.input.web.query;

import net.myspring.cloud.common.enums.SalOutStockBillTypeEnum;
import net.myspring.cloud.common.enums.SalReturnStockBillTypeEnum;

/**
 * Created by lihx on 2017/5/12.
 */
public class BatchBillQuery {
    private SalOutStockBillTypeEnum[] outStockBillTypeEnums;
    private SalReturnStockBillTypeEnum[] returnStockBillTypeEnums;

    public SalOutStockBillTypeEnum[] getOutStockBillTypeEnums() {
        return outStockBillTypeEnums;
    }

    public void setOutStockBillTypeEnums(SalOutStockBillTypeEnum[] outStockBillTypeEnums) {
        this.outStockBillTypeEnums = outStockBillTypeEnums;
    }

    public SalReturnStockBillTypeEnum[] getReturnStockBillTypeEnums() {
        return returnStockBillTypeEnums;
    }

    public void setReturnStockBillTypeEnums(SalReturnStockBillTypeEnum[] returnStockBillTypeEnums) {
        this.returnStockBillTypeEnums = returnStockBillTypeEnums;
    }
}
