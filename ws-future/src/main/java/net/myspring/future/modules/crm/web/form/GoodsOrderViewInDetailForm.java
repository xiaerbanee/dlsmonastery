package net.myspring.future.modules.crm.web.form;

import net.myspring.common.form.BaseForm;
import net.myspring.future.modules.basic.dto.DepotDto;
import net.myspring.future.modules.crm.domain.GoodsOrder;
import net.myspring.future.modules.crm.dto.ExpressOrderDto;
import net.myspring.future.modules.crm.dto.GoodsOrderDetailDto;
import net.myspring.future.modules.crm.dto.GoodsOrderDto;
import net.myspring.future.modules.crm.dto.GoodsOrderImeDto;

import java.util.ArrayList;
import java.util.List;

public class GoodsOrderViewInDetailForm extends BaseForm<GoodsOrder> {

    private DepotDto shopDto;
    private DepotDto storeDto;

    private GoodsOrderDto goodsOrderDto;
    private ExpressOrderDto expressOrderDto;
    private List<GoodsOrderDetailDto> goodsOrderDetailDtoList = new ArrayList<>();
    private List<GoodsOrderImeDto> goodsOrderImeDtoList = new ArrayList<>();

    public List<GoodsOrderImeDto> getGoodsOrderImeDtoList() {
        return goodsOrderImeDtoList;
    }

    public void setGoodsOrderImeDtoList(List<GoodsOrderImeDto> goodsOrderImeDtoList) {
        this.goodsOrderImeDtoList = goodsOrderImeDtoList;
    }

    public List<GoodsOrderDetailDto> getGoodsOrderDetailDtoList() {
        return goodsOrderDetailDtoList;
    }

    public void setGoodsOrderDetailDtoList(List<GoodsOrderDetailDto> goodsOrderDetailDtoList) {
        this.goodsOrderDetailDtoList = goodsOrderDetailDtoList;
    }

    public ExpressOrderDto getExpressOrderDto() {
        return expressOrderDto;
    }

    public void setExpressOrderDto(ExpressOrderDto expressOrderDto) {
        this.expressOrderDto = expressOrderDto;
    }

    public DepotDto getShopDto() {
        return shopDto;
    }

    public void setShopDto(DepotDto shopDto) {
        this.shopDto = shopDto;
    }

    public DepotDto getStoreDto() {
        return storeDto;
    }

    public void setStoreDto(DepotDto storeDto) {
        this.storeDto = storeDto;
    }

    public GoodsOrderDto getGoodsOrderDto() {
        return goodsOrderDto;
    }

    public void setGoodsOrderDto(GoodsOrderDto goodsOrderDto) {
        this.goodsOrderDto = goodsOrderDto;
    }

}
