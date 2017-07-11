package net.myspring.future.modules.crm.web.controller;


import com.google.common.collect.Lists;
import net.myspring.basic.common.util.CompanyConfigUtil;
import net.myspring.basic.modules.sys.dto.CompanyConfigCacheDto;
import net.myspring.common.enums.CompanyConfigCodeEnum;
import net.myspring.common.exception.ServiceException;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.future.common.enums.GoodsOrderPullStatusEnum;
import net.myspring.future.common.enums.GoodsOrderStatusEnum;
import net.myspring.future.common.enums.NetTypeEnum;
import net.myspring.future.common.enums.ShipTypeEnum;
import net.myspring.future.modules.api.service.CarrierOrderService;
import net.myspring.future.modules.api.web.form.CarrierOrderFrom;
import net.myspring.future.modules.basic.dto.DepotAccountDto;
import net.myspring.future.modules.basic.service.DepotService;
import net.myspring.future.modules.basic.service.ExpressCompanyService;
import net.myspring.future.modules.basic.web.query.DepotQuery;
import net.myspring.future.modules.crm.domain.GoodsOrder;
import net.myspring.future.modules.crm.dto.GoodsOrderDetailDto;
import net.myspring.future.modules.crm.dto.GoodsOrderDto;
import net.myspring.future.modules.crm.service.GoodsOrderService;
import net.myspring.future.modules.crm.web.form.*;
import net.myspring.future.modules.crm.web.query.GoodsOrderQuery;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.excel.ExcelView;
import net.myspring.util.json.ObjectMapperUtils;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "crm/goodsOrder")
public class GoodsOrderController {

    @Autowired
    private GoodsOrderService goodsOrderService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ExpressCompanyService expressCompanyService;
    @Autowired
    private CarrierOrderService carrierOrderService;
    @Autowired
    private DepotService depotService;

    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasPermission(null,'crm:goodsOrder:view')")
    public Page<GoodsOrderDto> list(Pageable pageable, GoodsOrderQuery goodsOrderQuery){
        return goodsOrderService.findAll(pageable, goodsOrderQuery);
    }

    @RequestMapping(value = "detail")
    @PreAuthorize("hasPermission(null,'crm:goodsOrder:view')")
    public GoodsOrderDto detail(String id) {
        return goodsOrderService.findDetail(id);
    }

    @RequestMapping(value = "getQuery")
    @PreAuthorize("hasPermission(null,'crm:goodsOrder:view')")
    public GoodsOrderQuery getQuery(GoodsOrderQuery goodsOrderQuery) {
        goodsOrderQuery.getExtra().put("netTypeList",NetTypeEnum.getList());
        goodsOrderQuery.getExtra().put("shipTypeList",ShipTypeEnum.getList());
        goodsOrderQuery.getExtra().put("statusList",GoodsOrderStatusEnum.getList());
        goodsOrderQuery.getExtra().put("pullStatusList", GoodsOrderPullStatusEnum.getList());
        return goodsOrderQuery;
    }

    @RequestMapping(value = "getForm")
    @PreAuthorize("hasPermission(null,'crm:goodsOrder:edit')")
    public GoodsOrderForm getForm(GoodsOrderForm goodsOrderForm){
        goodsOrderForm.getExtra().put("netTypeList", Lists.newArrayList(NetTypeEnum.移动.name(), NetTypeEnum.联信.name()));
        goodsOrderForm.getExtra().put("shipTypeList",ShipTypeEnum.getList());
        return goodsOrderForm;
    }

    @RequestMapping(value = "updatePullStatus")
    public RestResponse updatePullStatus(String id, String pullStatus,String expressOrderExpressCodes) {
        goodsOrderService.updatePullStatus(id, pullStatus,expressOrderExpressCodes);
        return new RestResponse("状态修改成功",ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "findDetailList")
    @PreAuthorize("hasPermission(null,'crm:goodsOrder:view')")
    public List<GoodsOrderDetailDto> findDetailList(String id, String shopId, String netType, String shipType) {
        return goodsOrderService.findDetailList(id,shopId,netType,shipType);
    }

    @RequestMapping(value = "validateShop")
    public RestResponse validateShop(String shopId) {
        return goodsOrderService.validateShop(shopId);
    }

    @RequestMapping(value = "save")
    @PreAuthorize("hasPermission(null,'crm:goodsOrder:edit')")
    public RestResponse save(GoodsOrderForm goodsOrderForm) {
        GoodsOrder goodsOrder = goodsOrderService.save(goodsOrderForm);
        if(StringUtils.isNotBlank(goodsOrderForm.getDetailJson())){
            CarrierOrderFrom carrierOrderFrom= BeanUtil.map(goodsOrderForm, CarrierOrderFrom.class);
            carrierOrderFrom.setGoodsOrderId(goodsOrder.getId());
            carrierOrderService.save(carrierOrderFrom);
        }
        return new RestResponse("保存成功", ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "updateCarrierOrderDetail")
    @PreAuthorize("hasPermission(null,'crm:goodsOrder:edit')")
    public RestResponse updateCarrierOrderDetail(GoodsOrderForm goodsOrderForm) {
        RestResponse restResponse=new RestResponse("保存成功", ResponseCodeEnum.saved.name());
        if(StringUtils.isNotBlank(goodsOrderForm.getDetailJson())){
            CarrierOrderFrom carrierOrderFrom= BeanUtil.map(goodsOrderForm, CarrierOrderFrom.class);
            Map<String, Object> map =carrierOrderService.checkDetailJsons(carrierOrderFrom);
            if (StringUtils.isNotBlank(map.get("message").toString())) {
                String message = map.get("message").toString();
                restResponse=new RestResponse(message,null,false);
            } else {
                carrierOrderFrom.setGoodsOrderId(goodsOrderForm.getId());
                carrierOrderService.save(carrierOrderFrom);
            }
        }else {
            restResponse=new RestResponse("输入商城信息",null,false);
        }
        return restResponse;
    }

    @RequestMapping(value = "getBillForm")
    @PreAuthorize("hasPermission(null,'crm:goodsOrder:bill')")
    public GoodsOrderBillForm getBillForm(GoodsOrderBillForm goodsOrderBillForm){
        //设置仓库
        GoodsOrderDto goodsOrderDto = goodsOrderService.findOne(goodsOrderBillForm.getId());
        DepotQuery depotQuery = new DepotQuery();
        depotQuery.setShipType(goodsOrderDto.getShipType());
        goodsOrderBillForm.getExtra().put("storeList",depotService.findStoreList(depotQuery));
        goodsOrderBillForm.getExtra().put("expressCompanyList",expressCompanyService.findAll());
        goodsOrderBillForm.getExtra().put("expressProductId", CompanyConfigUtil.findByCode(redisTemplate, CompanyConfigCodeEnum.EXPRESS_PRODUCT_ID.name()).getValue());
        CompanyConfigCacheDto companyConfigCacheDto = CompanyConfigUtil.findByCode(redisTemplate, CompanyConfigCodeEnum.EXPRESS_SHOULD_GET_RULE.name());
        if(companyConfigCacheDto !=null && StringUtils.isNotBlank(companyConfigCacheDto.getValue())){
            goodsOrderBillForm.getExtra().put("expressRuleList", ObjectMapperUtils.readValue(companyConfigCacheDto.getValue(), List.class));
        }
        return goodsOrderBillForm;
    }

    @RequestMapping(value = "findShopAccountByGoodsOrderId")
    @PreAuthorize("hasPermission(null,'crm:goodsOrder:view')")
    public DepotAccountDto findShopAccountByGoodsOrderId(String goodsOrderId){
        if(StringUtils.isBlank(goodsOrderId)){
            return new DepotAccountDto();
        }
        return goodsOrderService.findShopAccountByGoodsOrderId(goodsOrderId);
    }

    @RequestMapping(value = "getBill")
    @PreAuthorize("hasPermission(null,'crm:goodsOrder:bill')")
    public GoodsOrderDto getBill(String id) {
        return goodsOrderService.getBill(id);
    }

    @RequestMapping(value = "bill")
    @PreAuthorize("hasPermission(null,'crm:goodsOrder:bill')")
    public RestResponse bill(GoodsOrderBillForm goodsOrderBillForm) {
        if(CollectionUtil.isEmpty(goodsOrderBillForm.getGoodsOrderBillDetailFormList())){
            throw new ServiceException("开单明细不能为空");
        }
        Integer totalBillQty = 0;
        for(GoodsOrderBillDetailForm goodsOrderBillDetailForm : goodsOrderBillForm.getGoodsOrderBillDetailFormList()){
            if(goodsOrderBillDetailForm.getPrice()==null || goodsOrderBillDetailForm.getPrice().compareTo(BigDecimal.ZERO)<0){
                throw new ServiceException("开单明细里的单价必填且不能小于0");
            }
            if(goodsOrderBillDetailForm.getBillQty()!=null && goodsOrderBillDetailForm.getBillQty()<0){
                throw new ServiceException("开单明细里的数量不能小于0");
            }
            if(goodsOrderBillDetailForm.getBillQty() !=null){
                totalBillQty = totalBillQty + goodsOrderBillDetailForm.getBillQty();
            }
        }
        if(totalBillQty == 0){
            throw new ServiceException("总开单数量必须大于0");
        }
        goodsOrderService.bill(goodsOrderBillForm);
        return new RestResponse("开单成功", ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "findOne")
    @PreAuthorize("hasPermission(null,'crm:goodsOrder:view')")
    public GoodsOrderDto findOne(String id) {
        return goodsOrderService.findOne(id);
    }

    @RequestMapping(value="delete")
    @PreAuthorize("hasPermission(null,'crm:goodsOrder:delete')")
    public RestResponse delete(String id) {
        goodsOrderService.delete(id);
        return new RestResponse("删除成功", ResponseCodeEnum.removed.name());
    }

    @RequestMapping(value = "getBatchAddForm")
    @PreAuthorize("hasPermission(null,'crm:goodsOrder:batchAdd')")
    public GoodsOrderBatchAddForm getBatchAddForm(GoodsOrderBatchAddForm goodsOrderBatchAddForm){
        goodsOrderBatchAddForm.getExtra().put("netTypeList", Lists.newArrayList(NetTypeEnum.移动.name(), NetTypeEnum.联信.name()));
        goodsOrderBatchAddForm.getExtra().put("shipTypeList",ShipTypeEnum.getList());
        return goodsOrderBatchAddForm;
    }

    @RequestMapping(value = "batchAdd")
    @PreAuthorize("hasPermission(null,'crm:goodsOrder:edit')")
    public RestResponse batchAdd(GoodsOrderBatchAddForm goodsOrderBatchAddForm){
        if(CollectionUtil.isEmpty(goodsOrderBatchAddForm.getGoodsOrderBatchAddDetailFormList())){
            throw new ServiceException("请录入需要添加的订货信息");
        }

        for(GoodsOrderBatchAddDetailForm goodsOrderBatchAddDetailForm : goodsOrderBatchAddForm.getGoodsOrderBatchAddDetailFormList()){
            if(StringUtils.isBlank(goodsOrderBatchAddDetailForm.getShopName())){
                throw new ServiceException("门店不可以为空");
            }
            if(StringUtils.isBlank(goodsOrderBatchAddDetailForm.getNetType())){
                throw new ServiceException("网络制式不可以为空");
            }
            if(!NetTypeEnum.getList().contains(goodsOrderBatchAddDetailForm.getNetType())){
                throw new ServiceException("网络制式不合法");
            }
            if(StringUtils.isBlank(goodsOrderBatchAddDetailForm.getProductName())){
                throw new ServiceException("型号不可以为空");
            }
            if(goodsOrderBatchAddDetailForm.getQty() == null || goodsOrderBatchAddDetailForm.getQty()<0){
                throw new ServiceException("数量不可以小于0");
            }
            if(goodsOrderBatchAddDetailForm.getPrice() == null || goodsOrderBatchAddDetailForm.getPrice().compareTo(BigDecimal.ZERO)<0){
                throw new ServiceException("单价不可以小于0");
            }
            if(StringUtils.isBlank(goodsOrderBatchAddDetailForm.getShipType())){
                throw new ServiceException("发货类型不可以为空");
            }
            if(!ShipTypeEnum.getList().contains(goodsOrderBatchAddDetailForm.getShipType())){
                throw new ServiceException("发货类型不合法");
            }

        }

        goodsOrderService.batchAdd(goodsOrderBatchAddForm);

        return new RestResponse("保存成功", ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value="export")
    @PreAuthorize("hasPermission(null,'crm:goodsOrder:view')")
    public ModelAndView export(GoodsOrderQuery goodsOrderQuery) {
        return new ModelAndView(new ExcelView(), "simpleExcelBook", goodsOrderService.export(goodsOrderQuery));
    }
}
