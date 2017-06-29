package net.myspring.future.modules.crm.web.controller;


import net.myspring.common.exception.ServiceException;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.future.common.enums.ShipTypeEnum;
import net.myspring.future.common.enums.StoreAllotStatusEnum;
import net.myspring.future.common.enums.StoreAllotTypeEnum;
import net.myspring.future.modules.basic.service.ExpressCompanyService;
import net.myspring.future.modules.crm.dto.StoreAllotDetailSimpleDto;
import net.myspring.future.modules.crm.dto.StoreAllotDetailDto;
import net.myspring.future.modules.crm.dto.StoreAllotDto;
import net.myspring.future.modules.crm.service.StoreAllotDetailService;
import net.myspring.future.modules.crm.service.StoreAllotService;
import net.myspring.future.modules.crm.web.form.StoreAllotForm;
import net.myspring.future.modules.crm.web.query.StoreAllotQuery;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.excel.ExcelView;
import net.myspring.util.excel.SimpleExcelBook;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "crm/storeAllot")
public class StoreAllotController {

    @Autowired
    private StoreAllotService storeAllotService;
    @Autowired
    private ExpressCompanyService expressCompanyService;
    @Autowired
    private StoreAllotDetailService storeAllotDetailService;

    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasPermission(null,'crm:storeAllot:view')")
    public Page<StoreAllotDto> list(Pageable pageable, StoreAllotQuery storeAllotQuery){
        return storeAllotService.findPage(pageable, storeAllotQuery);

    }

    @RequestMapping(value = "save")
    @PreAuthorize("hasPermission(null,'crm:storeAllot:edit')")
    public RestResponse save(StoreAllotForm storeAllotForm) {

        //TODO 需要完善金蝶的控制
//        if(AccountUtils.getAccount().getOutId()==null) {
//            return new Message("message_store_allot_no_bind_finance",Message.Type.danger);
//        }
        if(!storeAllotForm.isCreate()){
            throw new ServiceException("大库调拨不可修改");
        }

        if(CollectionUtil.isEmpty(storeAllotForm.getStoreAllotDetailList())){
            throw new ServiceException("请录入大库店调拨明细");
        }

        storeAllotService.save(storeAllotForm);
        return new RestResponse("保存成功", ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "getForm")
    @PreAuthorize("hasPermission(null,'crm:storeAllot:view')")
    public StoreAllotForm getForm(StoreAllotForm storeAllotForm) {

        storeAllotForm.getExtra().put("allotTypeList",StoreAllotTypeEnum.getList());
        storeAllotForm.getExtra().put("shipTypeList",ShipTypeEnum.getList());
        storeAllotForm.getExtra().put("showAllotType", storeAllotService.getShowAllotType());

        return storeAllotForm;
    }

    @RequestMapping(value = "findForView")
    @PreAuthorize("hasPermission(null,'crm:storeAllot:view')")
    public StoreAllotDto findForView(String id) {
        return storeAllotService.findStoreAllotDtoById(id);
    }

    @RequestMapping(value = "findDetailList")
    @PreAuthorize("hasPermission(null,'crm:storeAllot:view')")
    public List<StoreAllotDetailDto> findDetailList(String storeAllotId) {
        return storeAllotDetailService.findByStoreAllotId(storeAllotId);
    }

    @RequestMapping(value = "findDetailListForCommonAllot")
    @PreAuthorize("hasPermission(null,'crm:storeAllot:view')")
    public List<StoreAllotDetailSimpleDto> findDetailListForCommonAllot(String fromStoreId) {
        return storeAllotService.findDetailListForCommonAllot(fromStoreId);
    }

    @RequestMapping(value = "findDetailListForFastAllot")
    @PreAuthorize("hasPermission(null,'crm:storeAllot:view')")
    public List<StoreAllotDetailSimpleDto> findDetailListForFastAllot() {

        return storeAllotService.findDetailListForFastAllot();

    }

    @RequestMapping(value = "findDto")
    @PreAuthorize("hasPermission(null,'crm:storeAllot:view')")
    public StoreAllotDto findDto(String id) {

        if(StringUtils.isBlank(id)){
            return new StoreAllotDto();
        }

        return storeAllotService.findDto(id);

    }

    @RequestMapping(value = "print")
    @PreAuthorize("hasPermission(null,'crm:storeAllot:print')")
    public StoreAllotDto print(String id) {

        if(StringUtils.isBlank(id)){
            throw new ServiceException("不存在对应的大库调拨记录");
        }
        return storeAllotService.print(id);
    }

    @RequestMapping(value = "shipPrint")
    public StoreAllotDto shipPrint(String id) {
        if(StringUtils.isBlank(id)){
            throw new ServiceException("不存在对应的大库调拨记录");
        }
        return storeAllotService.shipPrint(id);
    }

    @RequestMapping(value = "findStoreAllotForFastAllot")
    public StoreAllotDto findStoreAllotForFastAllot() {

        StoreAllotDto storeAllotDto = new StoreAllotDto();

        List<String> storeIds = storeAllotService.getMergeStoreIds();
        storeAllotDto.setFromStoreId(storeIds.get(0));
        storeAllotDto.setToStoreId(storeIds.get(1));
        storeAllotDto.setExpressCompanyId(expressCompanyService.getDefaultExpressCompanyId());
        storeAllotDto.setShipType(ShipTypeEnum.总部自提.name());

        return storeAllotDto;
    }

    @RequestMapping(value = "getStoreAllotData")
    @PreAuthorize("hasPermission(null,'crm:storeAllot:view')")
    public String getStoreAllotData() {
        return null;
    }

    @RequestMapping(value = "getCloudQty")
    @PreAuthorize("hasPermission(null,'crm:storeAllot:view')")
    public String getCloudQty() {
        return null;
    }

    @RequestMapping(value="getQuery")
    @PreAuthorize("hasPermission(null,'crm:storeAllot:view')")
    public StoreAllotQuery getQuery(StoreAllotQuery storeAllotQuery) {
        storeAllotQuery.getExtra().put("statusList",StoreAllotStatusEnum.getList());
        return storeAllotQuery;
    }

    @RequestMapping(value = "ship", method=RequestMethod.GET)
    @PreAuthorize("hasPermission(null,'crm:storeAllot:ship')")
    public String shipForm() {
        return null;
    }

    @RequestMapping(value = "shipBoxAndIme", method = RequestMethod.GET)
    @PreAuthorize("hasPermission(null,'crm:storeAllot:ship')")
    public Map<String, Object> shipBoxAndIme(String id, String boxImeStr, String imeStr) {
        return storeAllotService.shipBoxAndIme(id, boxImeStr, imeStr);
    }

    @RequestMapping(value = "ship", method=RequestMethod.POST)
    @PreAuthorize("hasPermission(null,'crm:storeAllot:ship')")
    public RestResponse ship() {
        return null;
    }

    @RequestMapping(value = "delete")
    @PreAuthorize("hasPermission(null,'crm:storeAllot:delete')")
    public RestResponse delete(String id) {
        storeAllotService.delete(id);
        return new RestResponse("删除成功",ResponseCodeEnum.removed.name());
    }

    @RequestMapping(value="export",method = RequestMethod.GET)
    @PreAuthorize("hasPermission(null,'crm:storeAllot:view')")
    public ModelAndView export(StoreAllotQuery storeAllotQuery) throws IOException {
        SimpleExcelBook simpleExcelBook = storeAllotService.export(storeAllotQuery);
        ExcelView excelView = new ExcelView();
        return new ModelAndView(excelView,"simpleExcelBook",simpleExcelBook);
    }

}
