package net.myspring.future.modules.layout.web.controller;

import net.myspring.common.constant.CharConstant;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.future.common.enums.OutBillTypeEnum;
import net.myspring.future.common.enums.ShopGoodsDepositStatusEnum;
import net.myspring.future.modules.layout.domain.ShopGoodsDeposit;
import net.myspring.future.modules.layout.dto.ShopDepositDto;
import net.myspring.future.modules.layout.dto.ShopGoodsDepositDto;
import net.myspring.future.modules.layout.service.ShopGoodsDepositService;
import net.myspring.future.modules.layout.web.form.ShopGoodsDepositForm;
import net.myspring.future.modules.layout.web.query.ShopGoodsDepositQuery;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping(value = "crm/shopGoodsDeposit")
public class ShopGoodsDepositController {

    @Autowired
    private ShopGoodsDepositService shopGoodsDepositService;


    @RequestMapping(method = RequestMethod.GET)
    public Page<ShopGoodsDepositDto> list(Pageable pageable, ShopGoodsDepositQuery shopGoodsDepositQuery){
        return shopGoodsDepositService.findPage(pageable, shopGoodsDepositQuery);
    }

    @RequestMapping(value="getQuery")
    public ShopGoodsDepositQuery getQuery(ShopGoodsDepositQuery shopGoodsDepositQuery) {
        shopGoodsDepositQuery.setOutBillTypeList(OutBillTypeEnum.getList());
        shopGoodsDepositQuery.setStatusList(ShopGoodsDepositStatusEnum.getList());

        return shopGoodsDepositQuery;
    }

    @RequestMapping(value = "save")
    public RestResponse save(ShopGoodsDepositForm shopGoodsDepositForm) {
        shopGoodsDepositService.save(shopGoodsDepositForm);
        return new RestResponse("保存成功", ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "batchAudit")
    public RestResponse batchAudit(@RequestParam(value = "ids[]")  String[] ids) {

        if(ids==null || ids.length == 0){
            return new RestResponse("请选择需要批量审批的记录", ResponseCodeEnum.invalid.name(), false);
        }

        List<String> errMsgList = new ArrayList<>();

        //在事务外循环，可以保证当方法内部调用接口报错时，不会全部回滚记录
        for(String id : ids){
            try{
                shopGoodsDepositService.audit(id);
            }catch(Exception e){
                errMsgList.add(e.getMessage());
            }
        }

        if(errMsgList.isEmpty()){
            return new RestResponse(String.format("批量审批全部成功，共成功%d条", ids.length), ResponseCodeEnum.audited.name());
        }else{
            return new RestResponse(String.format("批量审批部分失败，共失败%d条，失败原因为%s", errMsgList.size(), StringUtils.join(errMsgList, CharConstant.ENTER)), ResponseCodeEnum.invalid.name(), false);
        }

    }

    @RequestMapping(value = "delete")
    public RestResponse delete(String id) {
        shopGoodsDepositService.delete(id);
        return new RestResponse("删除成功", ResponseCodeEnum.removed.name());

    }

    @RequestMapping(value = "getForm")
    public ShopGoodsDepositForm getForm(ShopGoodsDepositForm shopGoodsDepositForm) {

        //TODO 需要修改获取departMentList的方法
        shopGoodsDepositForm.setDepartMentList(new ArrayList<>());

        return shopGoodsDepositForm;
    }

    @RequestMapping(value = "findDefaultDepartMent")
    public String findDefaultDepartMent(String shopId) {

        if(StringUtils.isBlank(shopId)){
            return "";
        }
        //TODO 需要从金蝶获取shopId对应的默认departMent

        return "defaultDepartMent";
    }

    @RequestMapping(value="export")
    public String export(ShopGoodsDepositQuery shopGoodsDepositQuery) {
        return shopGoodsDepositService.export(shopGoodsDepositQuery);
    }

    @RequestMapping(value = "findDto")
    public ShopGoodsDepositDto findDto(String id) {

        if(StringUtils.isBlank(id)){
            return new ShopGoodsDepositDto();
        }
        return shopGoodsDepositService.findDto(id);

    }

}
