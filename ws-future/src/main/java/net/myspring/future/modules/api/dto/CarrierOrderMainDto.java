package net.myspring.future.modules.api.dto;

import com.google.common.collect.Lists;

import java.util.List;

public class CarrierOrderMainDto {
    private String id;
    private String mallOrderId;
    private String md5;
    private List<CarrierOrderDetailDto> detail = Lists.newArrayList();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMallOrderId() {
        return mallOrderId;
    }

    
    public void setMallOrderId(String mallOrderId) {
        this.mallOrderId = mallOrderId;
    }


    public List<CarrierOrderDetailDto> getDetail() {
        return detail;
    }

    public void setDetail(List<CarrierOrderDetailDto> detail) {
        this.detail = detail;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }
}
