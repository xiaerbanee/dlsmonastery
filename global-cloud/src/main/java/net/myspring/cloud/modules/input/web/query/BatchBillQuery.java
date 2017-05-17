package net.myspring.cloud.modules.input.web.query;

import net.myspring.cloud.common.enums.SalOutStockBillTypeEnum;
import net.myspring.cloud.common.enums.SalReturnStockBillTypeEnum;
import net.myspring.cloud.modules.kingdee.domain.BdCustomer;
import net.myspring.cloud.modules.kingdee.domain.BdMaterial;

import java.util.List;

/**
 * Created by lihx on 2017/5/12.
 */
public class BatchBillQuery {
    private List<String> bdCustomerNameList;
    private List<String> bdMaterialNameList;
    private SalOutStockBillTypeEnum[] outStockBillTypeEnums;
    private SalReturnStockBillTypeEnum[] returnStockBillTypeEnums;

    public List<String> getBdCustomerNameList() {
        return bdCustomerNameList;
    }

    public void setBdCustomerNameList(List<String> bdCustomerNameList) {
        this.bdCustomerNameList = bdCustomerNameList;
    }

    public List<String> getBdMaterialNameList() {
        return bdMaterialNameList;
    }

    public void setBdMaterialNameList(List<String> bdMaterialNameList) {
        this.bdMaterialNameList = bdMaterialNameList;
    }

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
