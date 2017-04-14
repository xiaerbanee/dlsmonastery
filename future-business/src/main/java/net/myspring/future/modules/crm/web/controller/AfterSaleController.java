package net.myspring.future.modules.crm.web.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.common.domain.RestResponse;
import net.myspring.common.domain.SearchEntity;
import net.myspring.common.enums.MessageTypeEnum;
import net.myspring.common.utils.Collections3;
import net.myspring.common.utils.DateUtils;
import net.myspring.common.utils.ObjectMapperUtils;
import net.myspring.common.utils.StringUtils;
import net.myspring.future.common.enums.CompanyConfigCodeEnum;
import net.myspring.future.common.enums.DictEnumCategoryEnum;
import net.myspring.future.common.service.AccountUtils;
import net.myspring.future.common.service.CacheUtils;
import net.myspring.future.common.service.FilterUtils;
import net.myspring.future.common.utils.Const;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.common.utils.SecurityUtils;
import net.myspring.future.modules.crm.domain.AfterSale;
import net.myspring.future.modules.crm.domain.ProductIme;
import net.myspring.future.modules.crm.service.AfterSaleService;
import net.myspring.future.modules.crm.service.ProductImeService;
import net.myspring.future.modules.crm.service.ProductService;
import net.myspring.future.modules.sys.service.CompanyConfigService;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "crm/afterSale")
public class AfterSaleController {

    @Autowired
    private AfterSaleService afterSaleService;
    @Autowired
    private CompanyConfigService companyConfigService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductImeService productImeService;

    @ModelAttribute
    public AfterSale get(@RequestParam(required = false) String id) {
        return StringUtils.isBlank(id) ? new AfterSale() : afterSaleService.findOne(id);
    }


    @RequestMapping(method = RequestMethod.GET)
    public String list(HttpServletRequest request) {
        SearchEntity searchEntity = RequestUtils.getSearchEntity(request);
        searchEntity.getParams().putAll(FilterUtils.getDepotFilter(AccountUtils.getAccountId()));
        Page<AfterSale> page = afterSaleService.findPage(searchEntity.getPageable(), searchEntity.getParams());
        for(AfterSale afterSale : page.getContent()){
            afterSale.setActionList(getActionList(afterSale));
        }
        return ObjectMapperUtils.writeValueAsString(page);
    }

    @RequestMapping(value = "getFromCompanyData",method = RequestMethod.GET)
    public String getFromCompanyData(HttpServletRequest request) {
        SearchEntity searchEntity = RequestUtils.getSearchEntity(request);
        searchEntity.getParams().putAll(FilterUtils.getDepotFilter(AccountUtils.getAccountId()));
        List<AfterSale> page = afterSaleService.findFilter(searchEntity.getParams());
        return ObjectMapperUtils.writeValueAsString(page);
    }

    @RequestMapping(value="getFormProperty")
    public String getListProperty(){
        Map<String,Object> map= Maps.newHashMap();
        map.put("toStoreDate", LocalDate.now());
        map.put("packageStatus",CacheUtils.findDictEnumByCategory(DictEnumCategoryEnum.PACKAGES_STATUS.getValue()));
        map.put("toStoreType", CacheUtils.findDictEnumByCategory(DictEnumCategoryEnum.TOS_TORE_TYPE.getValue()));
        map.put("memory",CacheUtils.findDictEnumByCategory(DictEnumCategoryEnum.MEMORY.getValue()));
        map.put("goodStoreId",companyConfigService.findByCode(CompanyConfigCodeEnum.GOOD_STORE_ID.getCode()).getValue());
        return ObjectMapperUtils.writeValueAsString(map);
    }

    @RequestMapping(value = "formData", method = RequestMethod.GET)
    public String formData(String imeStr) {
        List<ProductIme> list = Lists.newArrayList();
        StringBuilder stringBuilder=new StringBuilder();
        if(StringUtils.isNotBlank(imeStr)) {
            List<String> imeList = StringUtils.getSplitList(imeStr, Const.CHAR_ENTER);
            List<ProductIme> productImeList=productImeService.findByImeList(imeList);
            Map<String,ProductIme> productImeMap=Collections3.extractToMap(productImeList,"ime");
            Map<String, AfterSale> afterSaleMap = afterSaleService.findAfterSale(imeList);
            for(String ime:imeList){
                if(!productImeMap.containsKey(ime)){
                    stringBuilder.append("串码" + ime + "在系统中不存在\n");
                }else if(afterSaleMap.containsKey(ime)) {
                    stringBuilder.append("串码" + ime + "已经做了售后单据\n");
                } else  {
                    list.add(productImeMap.get(ime));
                }
            }
        }
        Map<String,Object> map=Maps.newHashMap();
        map.put("list",list);
        map.put("message",stringBuilder);
        return ObjectMapperUtils.writeValueAsString(map);
    }

    @RequestMapping(value = "editFormData", method = RequestMethod.GET)
    public String editFormData(String imeStr) {
        List<AfterSale> list = Lists.newArrayList();
        StringBuilder stringBuilder=new StringBuilder();
        if(StringUtils.isNotBlank(imeStr)) {
            List<String> imeList = StringUtils.getSplitList(imeStr, Const.CHAR_ENTER);
            List<ProductIme> productImeList=productImeService.findByImeList(imeList);
            Map<String,ProductIme> productImeMap=Collections3.extractToMap(productImeList,"ime");
            Map<String, AfterSale> afterSaleMap = afterSaleService.findAfterSale(imeList);
            for(String ime:imeList){
                if(!productImeMap.containsKey(ime)){
                    stringBuilder.append("串码" + ime + "在系统中不存在\n");
                }else if(!afterSaleMap.containsKey(ime)) {
                    stringBuilder.append("串码" + ime + "没有找到售后单据\n");
                } else  {
                    list.add(afterSaleMap.get(ime));
                }
            }
        }
        Map<String,Object> map=Maps.newHashMap();
        map.put("list",list);
        map.put("message",stringBuilder);
        return ObjectMapperUtils.writeValueAsString(map);
    }

    @RequestMapping(value="searchImeMap" ,method=RequestMethod.GET)
    public String searchImeMap(String imeStr){
        Map<String,AfterSale> list = Maps.newHashMap();
        if(StringUtils.isNotBlank(imeStr)) {
            List<String> imeList=StringUtils.getSplitList(imeStr,Const.CHAR_ENTER);
            list=afterSaleService.findAfterSale(imeList);
        }
        return ObjectMapperUtils.writeValueAsString(list);
    }


    @RequestMapping(value = "save", method = RequestMethod.POST)
    public RestResponse save(String data,String toStoreDate) {
        List<List<String>> datas = ObjectMapperUtils.readValue(HtmlUtils.htmlUnescape(data), ArrayList.class);
        if(Collections3.isEmpty(datas)) {
            return new RestResponse(false,"保存失败", MessageTypeEnum.error.name());
        }
        afterSaleService.save(datas, DateUtils.parseLocalDate(toStoreDate));
        return new RestResponse("保存成功");
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    public RestResponse update(String data) {
        List<List<String>> datas = ObjectMapperUtils.readValue(HtmlUtils.htmlUnescape(data), ArrayList.class);
        if(Collections3.isEmpty(datas)) {
            return new RestResponse(false,"保存失败", MessageTypeEnum.error.name());
        }
        afterSaleService.update(datas);
        return new RestResponse("保存成功");
    }

    @RequestMapping(value = "fromCompany", method = RequestMethod.POST)
    public RestResponse fromCompany(String data,String fromCompanyDate) {
        List<List<String>> datas = ObjectMapperUtils.readValue(HtmlUtils.htmlUnescape(data), ArrayList.class);
        if(Collections3.isEmpty(datas)) {
            return new RestResponse(false,"保存失败", MessageTypeEnum.error.name());
        }
        afterSaleService.fromCompany(datas,DateUtils.parseLocalDate(fromCompanyDate));
        return new RestResponse("保存成功");
    }

    @RequestMapping(value = "toCompany", method = RequestMethod.POST)
    public RestResponse toCompany(String imeStr,AfterSale afterSale) {
        List<String> imes=StringUtils.getSplitList(imeStr,Const.CHAR_ENTER);
        if(Collections3.isEmpty(imes)) {
            return new RestResponse(false,"保存失败", MessageTypeEnum.error.name());
        }
        afterSaleService.toCompany(imes,afterSale.getToCompanyDate(),afterSale.getToCompanyRemarks());
        return new RestResponse("保存成功");
    }

    @RequestMapping(value = "toCompanyForm")
    public String toCompanyForm(String imeStr) {
        Map<String,Object> map=Maps.newHashMap();
        StringBuilder stringBuilder=new StringBuilder();
        if(StringUtils.isNotBlank(imeStr)) {
            final List<String> imeList = StringUtils.getSplitList(imeStr, Const.CHAR_ENTER);
            Map<String,AfterSale> afterSaleMap =afterSaleService.findAfterSale(imeList);
            List<AfterSale> afterSales = Lists.newArrayList();
            for(String ime:imeList) {
                if(!afterSaleMap.containsKey(ime)) {
                    stringBuilder.append("串码" + ime + "没有找到售后单据\n");
                } else if(afterSaleMap.get(ime).getToCompanyToFinance()) {
                    stringBuilder.append("串码" + ime + "已经返回工厂并且数据已同步到财务系统\n");
                } else {
                    afterSales.add(afterSaleMap.get(ime));
                }
            }
            //显示串码数量
            if(Collections3.isNotEmpty(afterSales)) {
                List<String> productImes = Lists.newArrayList();
                for(AfterSale afterSale:afterSales) {
                    productImes.add(afterSale.getBadProductIme().getIme());
                }
                Map<String,Integer> qtyMap = productService.getproductNumber(productImes);
                map.put("qtyMap",qtyMap);
            }
            map.put("message",stringBuilder);
            map.put("list",afterSales);
            map.put("toCompanyDate",LocalDate.now());
        }
        return ObjectMapperUtils.writeValueAsString(map);
    }


    @RequestMapping(value = "synToFinance")
    public String synToFinance() {
        afterSaleService.synToFinance();
        RestResponse restResponse=new RestResponse("同步成功");
        return ObjectMapperUtils.writeValueAsString(restResponse);
    }

    @RequestMapping(value = "delete")
    public String logicDelete(String id) {
        afterSaleService.logicDelete(id);
        RestResponse restResponse=new RestResponse("删除成功");
        return ObjectMapperUtils.writeValueAsString(restResponse);
    }

    public List<String> getActionList(AfterSale afterSale){
        List<String> actionList = Lists.newArrayList();
        if(SecurityUtils.getAuthorityList().contains("crm:afterSale:delete")){
            actionList.add(Const.ITEM_ACTION_DELETE);
        }
        return actionList;
    }

}
