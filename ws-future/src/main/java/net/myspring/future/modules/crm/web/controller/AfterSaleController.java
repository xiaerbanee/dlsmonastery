package net.myspring.future.modules.crm.web.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.common.constant.CharConstant;
import net.myspring.common.enums.CompanyConfigCodeEnum;
import net.myspring.common.enums.DictEnumCategoryEnum;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestErrorField;
import net.myspring.common.response.RestResponse;
import net.myspring.future.common.enums.AfterSaleDetailTypeEnum;
import net.myspring.future.common.enums.AfterSaleTypeEnum;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.basic.client.DictEnumClient;
import net.myspring.future.modules.basic.domain.Product;
import net.myspring.future.modules.basic.dto.ProductDto;
import net.myspring.future.modules.basic.service.ProductService;
import net.myspring.future.modules.crm.domain.AfterSale;
import net.myspring.future.modules.crm.domain.ProductIme;
import net.myspring.future.modules.crm.dto.AfterSaleInputDto;
import net.myspring.future.modules.crm.dto.AfterSaleDto;
import net.myspring.future.modules.crm.dto.AfterSaleCompanyDto;
import net.myspring.future.modules.crm.dto.ProductImeDto;
import net.myspring.future.modules.crm.service.AfterSaleService;
import net.myspring.future.modules.crm.service.ProductImeService;
import net.myspring.future.modules.crm.web.form.AfterSaleForm;
import net.myspring.future.modules.crm.web.form.AfterSaleToCompanyForm;
import net.myspring.future.modules.crm.web.query.AfterSaleQuery;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.json.ObjectMapperUtils;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.reflect.ReflectionUtil;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateUtils;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private ProductService productService;
    @Autowired
    private ProductImeService productImeService;
    @Autowired
    private DictEnumClient dictEnumClient;

    @RequestMapping(method = RequestMethod.GET)
    public Page<AfterSaleDto> list(Pageable pageable,AfterSaleQuery afterSaleQuery) {
        Page<AfterSaleDto> page = afterSaleService.findPage(pageable, afterSaleQuery);
        return page;
    }

    @RequestMapping(value="getQuery")
    public  AfterSaleQuery getQuery(AfterSaleQuery afterSaleQuery){
        return afterSaleQuery;
    }

    @RequestMapping(value="getForm")
    public AfterSaleForm getForm(AfterSaleForm afterSaleForm){
        return afterSaleForm;
    }

    @RequestMapping(value = "formData", method = RequestMethod.GET)
    public Map<String, Object> formData(String imeStr) {
        List<ProductIme> list = Lists.newArrayList();
        StringBuilder stringBuilder=new StringBuilder();
        if(StringUtils.isNotBlank(imeStr)) {
            List<String> imeList = StringUtils.getSplitList(imeStr, CharConstant.ENTER);
            List<ProductImeDto> productImeList=productImeService.findByImeList(imeList);
            Map<String,ProductIme> productImeMap=CollectionUtil.extractToMap(productImeList,"ime");
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
        return map;
    }

    @RequestMapping(value = "editFormData", method = RequestMethod.GET)
    public Map<String, Object> editFormData(String imeStr) {
        List<AfterSale> list = Lists.newArrayList();
        StringBuilder stringBuilder=new StringBuilder();
        if(StringUtils.isNotBlank(imeStr)) {
            List<String> imeList = StringUtils.getSplitList(imeStr, CharConstant.ENTER);
            List<ProductImeDto> productImeList=productImeService.findByImeList(imeList);
            Map<String,ProductIme> productImeMap=CollectionUtil.extractToMap(productImeList,"ime");
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
        return map;
    }

    @RequestMapping(value="searchImeMap" ,method=RequestMethod.GET)
    public String searchImeMap(String imeStr){
        Map<String,AfterSale> list = Maps.newHashMap();
        if(StringUtils.isNotBlank(imeStr)) {
            List<String> imeList=StringUtils.getSplitList(imeStr,CharConstant.ENTER);
            list=afterSaleService.findAfterSale(imeList);
        }
        return ObjectMapperUtils.writeValueAsString(list);
    }


    @RequestMapping(value = "save", method = RequestMethod.POST)
    public RestResponse save(String data,String toStoreDate) {
        List<List<String>> datas = ObjectMapperUtils.readValue(HtmlUtils.htmlUnescape(data), ArrayList.class);
        if(CollectionUtil.isEmpty(datas)) {
            return new RestResponse("保存失败", null,false);
        }
        afterSaleService.save(datas, LocalDateUtils.parse(toStoreDate));
        return new RestResponse("保存成功",null);
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    public RestResponse update(String data) {
        List<List<String>> datas = ObjectMapperUtils.readValue(HtmlUtils.htmlUnescape(data), ArrayList.class);
        if(CollectionUtil.isEmpty(datas)) {
            return new RestResponse("保存失败", null,false);
        }
        afterSaleService.update(datas);
        return new RestResponse("保存成功",null);
    }

    @RequestMapping(value = "fromCompany", method = RequestMethod.POST)
    public RestResponse fromCompany(String data,String fromCompanyDate) {
        List<List<String>> datas = ObjectMapperUtils.readValue(HtmlUtils.htmlUnescape(data), ArrayList.class);
        if(CollectionUtil.isEmpty(datas)) {
            return new RestResponse("保存失败", null,false);
        }
        afterSaleService.fromCompany(datas,LocalDateUtils.parse(fromCompanyDate));
        return new RestResponse("保存成功",null);
    }

    @RequestMapping(value = "toCompany", method = RequestMethod.POST)
    public RestResponse toCompany(String imeStr,AfterSale afterSale) {
        List<String> imes=StringUtils.getSplitList(imeStr,CharConstant.ENTER);
        if(CollectionUtil.isEmpty(imes)) {
            return new RestResponse("保存失败", null,false);
        }
        afterSaleService.toCompany(imes,afterSale.getToCompanyDate(),afterSale.getToCompanyRemarks());
        return new RestResponse("保存成功",null);
    }

    @RequestMapping(value = "toCompanyForm")
    public Map<String, Object> toCompanyForm(String imeStr) {
        Map<String,Object> map=Maps.newHashMap();
        StringBuilder stringBuilder=new StringBuilder();
        if(StringUtils.isNotBlank(imeStr)) {
            final List<String> imeList = StringUtils.getSplitList(imeStr, CharConstant.ENTER);
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
            if(CollectionUtil.isNotEmpty(afterSales)) {
                List<String> productImeIdList = Lists.newArrayList();
                for(AfterSale afterSale:afterSales) {
                    productImeIdList.add(afterSale.getBadProductImeId());
                }
                List<ProductIme> productImeList=productImeService.findByIds(productImeIdList);
                Map<String,Integer> qtyMap = productImeService.findProductImeSearchResult(CollectionUtil.extractToList(productImeList,"ime")).getProductQtyMap();
                map.put("qtyMap",qtyMap);
            }
            map.put("message",stringBuilder);
            map.put("list",afterSales);
            map.put("toCompanyDate",LocalDate.now());
        }
        return map;
    }


    @RequestMapping(value = "synToFinance")
    public RestResponse synToFinance() {
        return new RestResponse("同步成功",null);
    }

    @RequestMapping(value = "delete")
    public RestResponse logicDelete(String id) {
        afterSaleService.delete(id);
        return new RestResponse("删除成功",null);
    }
}
