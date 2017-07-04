package net.myspring.future.modules.api.dto;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by 23950 on 2016/11/25.
 */
public class B2bOrderDto {

    private String mallOrderId;
    private String mallOrderCode;
    private String deliveryNo;
    private String deliveryCompany;
    private List<B2bOrderDetailDto> b2bOrderDetailList = Lists.newArrayList();
    private boolean status=false;

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMallOrderId() {
        return mallOrderId;
    }

    public void setMallOrderId(String mallOrderId) {
        this.mallOrderId = mallOrderId;
    }

    public List<B2bOrderDetailDto> getB2bOrderDetailList() {
        return b2bOrderDetailList;
    }

    public void setB2bOrderDetailList(List<B2bOrderDetailDto> b2bOrderDetailList) {
        this.b2bOrderDetailList = b2bOrderDetailList;
    }

    public String getDeliveryNo() {
        return deliveryNo;
    }

    public void setDeliveryNo(String deliveryNo) {
        this.deliveryNo = deliveryNo;
    }

    public String getDeliveryCompany() {
        return deliveryCompany;
    }

    public void setDeliveryCompany(String deliveryCompany) {
        this.deliveryCompany = deliveryCompany;
    }

    public String getMallOrderCode() {
        return mallOrderCode;
    }

    public void setMallOrderCode(String mallOrderCode) {
        this.mallOrderCode = mallOrderCode;
    }
}
