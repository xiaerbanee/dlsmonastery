package net.myspring.future.modules.crm.web.form;

import com.ctc.wstx.util.StringUtil;
import net.myspring.common.constant.CharConstant;
import net.myspring.common.form.DataForm;
import net.myspring.future.modules.basic.dto.DepotDto;
import net.myspring.future.modules.basic.dto.ExpressCompanyDto;
import net.myspring.future.modules.crm.domain.GoodsOrder;
import net.myspring.future.modules.crm.dto.GoodsOrderDto;
import net.myspring.util.text.StringUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class GoodsOrderShipForm extends DataForm<GoodsOrder> {
    private String goodsOrderId;
    private String boxImeStr;
    private String imeStr;
    private String expressStr;
    private String shipRemarks;

    public String getGoodsOrderId() {
        return goodsOrderId;
    }

    public void setGoodsOrderId(String goodsOrderId) {
        this.goodsOrderId = goodsOrderId;
    }

    public String getBoxImeStr() {
        return boxImeStr;
    }

    public void setBoxImeStr(String boxImeStr) {
        this.boxImeStr = boxImeStr;
    }

    public String getImeStr() {
        return imeStr;
    }

    public void setImeStr(String imeStr) {
        this.imeStr = imeStr;
    }

    public String getExpressStr() {
        return expressStr;
    }

    public void setExpressStr(String expressStr) {
        this.expressStr = expressStr;
    }

    public String getShipRemarks() {
        return shipRemarks;
    }

    public void setShipRemarks(String shipRemarks) {
        this.shipRemarks = shipRemarks;
    }

    public List<String> getImeList() {
        return StringUtils.getSplitList(imeStr, CharConstant.ENTER);
    }

    public List<String> getBoxImeList() {
        return StringUtils.getSplitList(boxImeStr, CharConstant.ENTER);
    }

    public List<String> getExpressList() {
        return StringUtils.getSplitList(expressStr, CharConstant.ENTER);
    }
}
