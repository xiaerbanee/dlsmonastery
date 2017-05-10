package net.myspring.future.modules.crm.web.controller;


import net.myspring.common.exception.ServiceException;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.future.common.enums.ShipTypeEnum;
import net.myspring.future.common.enums.StoreAllotStatusEnum;
import net.myspring.future.common.enums.StoreAllotTypeEnum;
import net.myspring.future.modules.basic.service.ExpressCompanyService;
import net.myspring.future.modules.crm.domain.ExpressOrder;
import net.myspring.future.modules.crm.domain.StoreAllot;
import net.myspring.future.modules.crm.dto.StoreAllotDto;
import net.myspring.future.modules.crm.service.*;
import net.myspring.future.modules.crm.web.form.StoreAllotForm;
import net.myspring.future.modules.crm.web.query.StoreAllotQuery;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "crm/storeAllot")
public class StoreAllotController {

    @Autowired
    private StoreAllotService storeAllotService;

    @Autowired
    private ExpressCompanyService expressCompanyService;

    @Autowired
    private StoreAllotDetailService storeAllotDetailService;

    @Autowired
    private StoreAllotImeService storeAllotImeService;
    @Autowired
    private ExpressOrderService expressOrderService;

    @Autowired
    private GoodsOrderService goodsOrderService;

    @RequestMapping(method = RequestMethod.GET)
    public Page<StoreAllotDto> list(Pageable pageable, StoreAllotQuery storeAllotQuery){
        Page<StoreAllotDto> page = storeAllotService.findPage(pageable, storeAllotQuery);
        return page;
    }

    @RequestMapping(value = "save")
    public RestResponse save(StoreAllotForm storeAllotForm) {

        //TODO 需要完善金蝶的控制
//        if(AccountUtils.getAccount().getOutId()==null) {
//            return new Message("message_store_allot_no_bind_finance",Message.Type.danger);
//        }
        if(!storeAllotForm.isCreate()){
            throw new ServiceException("error.storeAllot.cantEdit");
        }

        StoreAllot storeAllot = storeAllotService.saveForm(storeAllotForm);
        if(storeAllotForm.getSyn()){
//            k3cloudSynService.syn(store.getId(), K3CloudSynEntity.ExtendType.大库调拨.name());
//            if(store.getExpressOrder()!=null){
//                ExpressOrder expressOrder = store.getExpressOrder();
//                expressOrder.setOutCode(k3cloudSynService.getOutCode(store.getId(), K3CloudSynEntity.ExtendType.大库调拨.name()));
//                expressOrderService.save(expressOrder);
//            }
        }
        return new RestResponse("保存成功", ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "findForm")
    public StoreAllotForm findForm(StoreAllotForm storeAllotForm) {

        if(!storeAllotForm.isCreate()){
           throw new ServiceException("error.storeAllot.cantEdit");
        }
        StoreAllotForm result;

        if(StoreAllotTypeEnum.快速调拨.name().equals(storeAllotForm.getAllotType())){

            result = new StoreAllotForm();

            LocalDate billDate = LocalDateUtils.parse(LocalDateUtils.format(LocalDate.now()));
            String mergeStoreIds ="3489,9873";//TODO 需要调用配置参数
            List<String> storeIds = StringUtils.getSplitList(mergeStoreIds, ",");
            String fromStoreId =storeIds.get(0);
            String toStoreId =storeIds.get(1);

            result.setExpressCompanyId( expressCompanyService.getDefaultExpressCompanyId());
            result.setShipType(ShipTypeEnum.总部自提.name());
            result.setStoreAllotDetailFormList(storeAllotDetailService.findStoreAllotDetailsForFastAllot(billDate, toStoreId, "待发货"));
            result.setFromStoreId(fromStoreId);
            result.setToStoreId(toStoreId);
            result.setSyn(Boolean.FALSE);
            result.setAllotType(StoreAllotTypeEnum.快速调拨.name());

        }else{
            result = new StoreAllotForm();
            result.setSyn(Boolean.TRUE);
            result.setStoreAllotDetailFormList(new ArrayList<>());
        }

        result.setAllotTypeList(StoreAllotTypeEnum.getList());
        result.setShipTypeList(ShipTypeEnum.getList());
        result.setShowAllotType(Boolean.TRUE);

        return result;


    }

    @RequestMapping(value = "findForOverview")
    public StoreAllotDto findForOverview(String id) {
        StoreAllotDto result = storeAllotService.findStoreAllotDtoById(id);
        if(result.getExpressOrderId() != null){
            ExpressOrder expressOrder = expressOrderService.findOne(result.getExpressOrderId());
            result.setExpressOrderCodes(expressOrder.getExpressCodes());
        }
        result.setStoreAllotDetailDtoList(storeAllotDetailService.findByStoreAllotId(id));
        result.setStoreAllotImeDtoList(storeAllotImeService.findByStoreAllotId(id));
        return result;
    }

    @RequestMapping(value = "getStoreAllotData")
    public String getStoreAllotData() {
        return null;
    }

    @RequestMapping(value = "getCloudQty")
    public String getCloudQty() {
        return null;
    }

    @RequestMapping(value="getFormProperty")
    public String getFormProperty() {
        return null;
    }

    @RequestMapping(value="getQuery")
    public StoreAllotQuery getQuery(StoreAllotQuery storeAllotQuery) {
        storeAllotQuery.setStatusList(StoreAllotStatusEnum.getList());
        return storeAllotQuery;
    }

    @RequestMapping(value = "export", method = RequestMethod.GET)
    public ModelAndView export() {
        return null;
    }

    @RequestMapping(value = "ship", method=RequestMethod.GET)
    public String shipForm() {
        return null;
    }

    @RequestMapping(value = "shipBoxAndIme", method = RequestMethod.GET)
    public String shipBoxAndIme() {
        return null;
    }

    @RequestMapping(value = "ship", method=RequestMethod.POST)
    public RestResponse ship() {
        return null;
    }

    @RequestMapping(value = "delete")
    public RestResponse delete() {
        return null;
    }


}
