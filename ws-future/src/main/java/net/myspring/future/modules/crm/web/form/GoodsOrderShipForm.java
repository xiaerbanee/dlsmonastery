package net.myspring.future.modules.crm.web.form;

import com.google.common.collect.Lists;
import net.myspring.common.constant.CharConstant;
import net.myspring.common.form.BaseForm;
import net.myspring.future.modules.crm.domain.GoodsOrder;
import net.myspring.future.modules.crm.dto.GoodsOrderDetailDto;
import net.myspring.util.text.IdUtils;
import net.myspring.util.text.StringUtils;

import java.util.List;

public class GoodsOrderShipForm extends BaseForm<GoodsOrder> {
    private String boxImeStr;
    private String imeStr;
    private String expressStr;
    private String shipRemarks;

    //显示数据
    private String storeName;
    private String shopName;

    //明细数据
    private List<GoodsOrderDetailDto> goodsOrderDetailList = Lists.newArrayList();

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
        List<String> list =  StringUtils.getSplitList(imeStr, CharConstant.ENTER);
        List<String> imeList = Lists.newArrayList();
        for(String ime:list) {
            if (ime.startsWith("86")) {
                imeList.add(StringUtils.getNumberStr(ime));
            } else {
                imeList.add(ime);
            }
        }
        return imeList;
    }

    public List<String> getBoxImeList() {
        return StringUtils.getSplitList(boxImeStr, CharConstant.ENTER);
    }

    public List<String> getExpressList() {
        return StringUtils.getSplitList(expressStr, CharConstant.ENTER);
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public List<GoodsOrderDetailDto> getGoodsOrderDetailList() {
        return goodsOrderDetailList;
    }

    public void setGoodsOrderDetailList(List<GoodsOrderDetailDto> goodsOrderDetailList) {
        this.goodsOrderDetailList = goodsOrderDetailList;
    }

    public String getFormatId() {
        return IdUtils.getFormatId(getId(),"XK");
    }
}
