package net.myspring.future.modules.crm.web.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.common.domain.RestResponse;
import net.myspring.common.domain.SearchEntity;
import net.myspring.common.enums.MessageTypeEnum;
import net.myspring.common.utils.ObjectMapperUtils;
import net.myspring.common.utils.StringUtils;
import net.myspring.future.common.enums.CompanyConfigCodeEnum;
import net.myspring.future.common.enums.FolderDefaultEnum;
import net.myspring.future.common.service.AccountUtils;
import net.myspring.future.common.service.FilterUtils;
import net.myspring.future.common.utils.Const;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.common.utils.SecurityUtils;
import net.myspring.future.modules.crm.domain.ShopImage;
import net.myspring.future.modules.crm.service.ShopImageService;
import net.myspring.future.modules.crm.validator.ShopImageValidator;
import net.myspring.future.modules.hr.domain.Account;
import net.myspring.future.modules.hr.service.OfficeService;
import net.myspring.future.modules.sys.service.CompanyConfigService;
import net.myspring.future.modules.sys.service.FolderService;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "crm/shopImage")
public class ShopImageController {

    @Autowired
    private ShopImageService shopImageService;
    @Autowired
    private OfficeService officeService;
    @Autowired
    private FolderService folderService;
    @Autowired
    private CompanyConfigService companyConfigService;
    @Autowired
    private ShopImageValidator shopImageValidator;

    @ModelAttribute
    public ShopImage get(@RequestParam(required = false) String id) {
        return StringUtils.isBlank(id) ? new ShopImage() : shopImageService.findOne(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    public String list(HttpServletRequest request) {
        SearchEntity searchEntity = RequestUtils.getSearchEntity(request);
        searchEntity.getParams().putAll(FilterUtils.getDepotFilter(AccountUtils.getAccountId()));
        Page<ShopImage> page = shopImageService.findPage(searchEntity.getPageable(), searchEntity.getParams());
        for(ShopImage shopImage : page.getContent()){
            shopImage.setActionList(getActionList(shopImage));
        }
        return ObjectMapperUtils.writeValueAsString(page);
    }

    @RequestMapping(value = "detail", method = RequestMethod.GET)
    public String detail(ShopImage shopImage) {
        return ObjectMapperUtils.writeValueAsString(shopImage);
    }

    @RequestMapping(value = "getFormProperty", method = RequestMethod.GET)
    public String getFormProperty(ShopImage shopImage) {
        Map<String,Object> map=Maps.newHashMap();
        Account account = AccountUtils.getAccount();
        map.put("folder", folderService.getAccountFolder(account, FolderDefaultEnum.SHOP_IMAGE.toString()));
        String shopImageType = companyConfigService.getCompanyConfig( CompanyConfigCodeEnum.SHOP_IMAGE_TYPE.getCode());
        map.put("shopImageTypes", Arrays.asList(shopImageType.split(Const.CHAR_COMMA)));
        map.put("shopImage",shopImage);
        map.put("areaList", officeService.findByType(Const.OFFICE_TYPE_AREA));
        return ObjectMapperUtils.writeValueAsString(map);
    }
    @RequestMapping(value = "getListProperty", method = RequestMethod.GET)
    public String getListProperty(ShopImage shopImage) {
        Map<String,Object> map=Maps.newHashMap();
        map.put("areaList", officeService.findByType(Const.OFFICE_TYPE_AREA));
        return ObjectMapperUtils.writeValueAsString(map);
    }

    @RequestMapping(value = "save")
    @ResponseBody
    public String save(ShopImage shopImage, BindingResult bindingResult) {
        shopImageValidator.validate(shopImage,bindingResult);
        RestResponse restResponse=new RestResponse("保存成功");
        if(bindingResult.hasErrors()){
            restResponse = new RestResponse(false,bindingResult, MessageTypeEnum.error.name());
        }else{
            shopImageService.save(shopImage);
        }
        return ObjectMapperUtils.writeValueAsString(restResponse);
    }

    @RequestMapping(value = "delete", method = RequestMethod.GET)
    public String logicDelete(String id) {
        shopImageService.logicDelete(id);
        RestResponse restResponse=new RestResponse("删除成功");
        return ObjectMapperUtils.writeValueAsString(restResponse);
    }

    public List<String> getActionList(ShopImage shopImage){
        List<String> actionList = Lists.newArrayList();
        if(shopImage.getCreatedBy().equals(AccountUtils.getAccountId())){
            for(String authoritie : SecurityUtils.getAuthorityList()){
                switch (authoritie){
                    case "crm:shopImage:delete" : actionList.add(Const.ITEM_ACTION_DELETE); break;
                    case "crm:shopImage:edit" : actionList.add(Const.ITEM_ACTION_EDIT); break;
                    default : break;
                }
            }
        }
        return actionList;
    }

}
