package net.myspring.future.modules.api.web.form;

import net.myspring.common.form.BaseForm;
import net.myspring.future.modules.api.domain.CarrierOrder;
import net.myspring.util.text.IdUtils;
import net.myspring.util.text.StringUtils;
import org.springframework.web.util.HtmlUtils;

public class CarrierOrderFrom extends BaseForm<CarrierOrder>{
    private String detailJson;
    private String businessId;
    private String formatId;
    private String carrierShopId;
    private String goodsOrderId;
    private boolean checkColor=false;

    public String getGoodsOrderId() {
        return goodsOrderId;
    }

    public void setGoodsOrderId(String goodsOrderId) {
        this.goodsOrderId = goodsOrderId;
    }

    public String getCarrierShopId() {
        return carrierShopId;
    }

    public void setCarrierShopId(String carrierShopId) {
        this.carrierShopId = carrierShopId;
    }

    public boolean getCheckColor() {
        return checkColor;
    }

    public void setCheckColor(boolean checkColor) {
        this.checkColor = checkColor;
    }

    public String getFormatId() {
        if(StringUtils.isBlank(formatId)&&StringUtils.isNotBlank(businessId)){
            this.formatId= IdUtils.getFormatId(businessId,"XK");
        }
        return formatId;
    }

    public void setFormatId(String formatId) {
        this.formatId = formatId;
    }


    public String getBusinessId() {
        if(StringUtils.isBlank(businessId)&&StringUtils.isNotBlank(formatId)){
            return formatId.replace("XK","" );
        }
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getDetailJson() {
        if(StringUtils.isNotBlank(detailJson)){
            this.detailJson= HtmlUtils.htmlUnescape(detailJson);
        }
        return detailJson;
    }

    public void setDetailJson(String detailJson) {
        this.detailJson = detailJson;
    }
}
