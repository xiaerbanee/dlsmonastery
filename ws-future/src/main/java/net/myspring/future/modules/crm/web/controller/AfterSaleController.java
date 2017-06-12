package net.myspring.future.modules.crm.web.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.common.constant.CharConstant;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestErrorField;
import net.myspring.common.response.RestResponse;
import net.myspring.future.common.enums.AfterSaleDetailTypeEnum;
import net.myspring.future.common.enums.AfterSaleTypeEnum;
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
import net.myspring.future.modules.crm.web.form.AfterSaleToCompanyForm;
import net.myspring.future.modules.crm.web.query.AfterSaleQuery;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.json.ObjectMapperUtils;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.reflect.ReflectionUtil;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateUtils;
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
    private ProductImeService productImeService;
    @Autowired
    private ProductService productService;

    @RequestMapping(method = RequestMethod.GET)
    public Page<AfterSaleDto> areaList(Pageable pageable, AfterSaleQuery afterSaleQuery) {
        Page<AfterSaleDto> page = afterSaleService.findPage(pageable, afterSaleQuery);
        return page;
    }

    @RequestMapping(value = "areaInputData", method = RequestMethod.GET)
    public Map<String,Object> formData(String imeStr,String action,String type) {
        Map<String,Object> map=Maps.newHashMap();
        if(StringUtils.isNotBlank(imeStr)){
            List<AfterSaleInputDto> afterSaleInputList=Lists.newArrayList();
            RestResponse restResponse =  new RestResponse("valid", ResponseCodeEnum.valid.name());
            List<String> imeList = StringUtils.getSplitList(imeStr, CharConstant.ENTER);
            List<String>  inputImeList=Lists.newArrayList();
            if("update".equals(action)){
                afterSaleInputList=afterSaleService.inputUpdateData(imeList,type, AfterSaleDetailTypeEnum.地区录入.name());
            }else {
                List<ProductImeDto> productImeList=productImeService.findByImeList(imeList);
                if(CollectionUtil.isNotEmpty(productImeList)){
                    List<AfterSale> afterSaleList=afterSaleService.findByBadProductImeIdList(CollectionUtil.extractToList(productImeList,"id"));
                    List<String> badProductImeIdList=CollectionUtil.extractToList(afterSaleList,"badProductImeId");
                    for(ProductImeDto productIme:productImeList){
                        if(!badProductImeIdList.contains(productIme.getId())){
                            AfterSaleInputDto afterSaleInputDto=new AfterSaleInputDto();
                            afterSaleInputDto.setBadProductName(productIme.getProductName());
                            afterSaleInputDto.setBadProductIme(productIme.getIme());
                            afterSaleInputDto.setBadDepotName(productIme.getDepotName());
                            afterSaleInputList.add(afterSaleInputDto);
                        }else {
                            inputImeList.add(productIme.getIme());
                            restResponse.getErrors().add(new RestErrorField("串码"+productIme.getIme()+"已录入","bad_product_ime_error","badProductIme"));
                        }
                    }
                }
            }
            List<String> list=CollectionUtil.extractToList(afterSaleInputList,"badProductIme");
            for(String ime:imeList){
                if(!list.contains(ime)&&!inputImeList.contains(ime)){
                    restResponse.getErrors().add(new RestErrorField("串码"+ime+"未录入","bad_product_ime_error","badProductIme"));
                }
            }
            map.put("restResponse",restResponse);
            map.put("afterSaleInputList",afterSaleInputList);
        }
        return map;
    }

    @RequestMapping(value = "getQuery")
    public AfterSaleQuery getQuery(AfterSaleQuery afterSaleQuery){
        return afterSaleQuery;
    }

    @RequestMapping(value = "headInputData", method = RequestMethod.GET)
    public Map<String,Object>  editFormData(String imeStr,String type,String action) {
        Map<String,Object> map=Maps.newHashMap();
        if(StringUtils.isNotBlank(imeStr)){
            List<String> imeList = StringUtils.getSplitList(imeStr, CharConstant.ENTER);
            RestResponse restResponse =  new RestResponse("valid", ResponseCodeEnum.valid.name());
            List<AfterSaleInputDto> afterSaleInputList=Lists.newArrayList();
            if("update".equals(action)){
                afterSaleInputList=afterSaleService.inputUpdateData(imeList,type, AfterSaleDetailTypeEnum.总部录入.name());
            }else {
                List<ProductImeDto> productImeList=productImeService.findByImeList(imeList);
                List<AfterSaleDto> afterSaleList=afterSaleService.findDtoByImeListAndType(imeList,type);
                Map<String,AfterSaleDto> afterSaleMap= CollectionUtil.extractToMap(afterSaleList,"badProductImeId");
                if(AfterSaleTypeEnum.窜货机.name().equals(type)){
                    for(AfterSaleDto afterSale:afterSaleList){
                        AfterSaleInputDto afterSaleInputDto= BeanUtil.map(afterSale,AfterSaleInputDto.class);
                        afterSaleInputDto.setBadProductIme(afterSale.getIme());
                        afterSaleInputList.add(afterSaleInputDto);
                    }
                }
                for(ProductImeDto productIme:productImeList){
                    AfterSaleInputDto afterSaleInputDto=new AfterSaleInputDto();
                    AfterSaleDto afterSale=afterSaleMap.get(productIme.getId());
                    afterSaleInputDto.setBadProductName(productIme.getProductName());
                    afterSaleInputDto.setBadProductIme(productIme.getIme());
                    afterSaleInputDto.setBadDepotName(productIme.getDepotName());
                    if(afterSale!=null){
                        ReflectionUtil.copyProperties(afterSale,afterSaleInputDto);
                    }
                    afterSaleInputList.add(afterSaleInputDto);
                }
            }
            if(AfterSaleTypeEnum.售后机.name().equals(type)){
                List<String> list=CollectionUtil.extractToList(afterSaleInputList,"badProductIme");
                for(String ime:imeList){
                    if(!list.contains(ime)){
                        restResponse.getErrors().add(new RestErrorField("串码"+ime+"未录入","bad_product_ime_error","badProductIme"));
                    }
                }
            }
            map.put("restResponse",restResponse);
            map.put("afterSaleInputList",afterSaleInputList);
        }
        return map;
    }

    @RequestMapping(value="searchImeMap" ,method=RequestMethod.GET)
    public Map<String,Object> searchImeMap(String imeStr,String type){
        Map<String,Object> map=Maps.newLinkedHashMap();
        if(StringUtils.isNotBlank(imeStr)){
            List<String> imeList = StringUtils.getSplitList(imeStr, CharConstant.ENTER);
            List<AfterSaleDto> afterSaleList=afterSaleService.findDtoByImeListAndType(imeList,type);
            map.put("afterSaleList",afterSaleList);
            Map<String,Integer> productQtyMap=Maps.newHashMap();
            if(CollectionUtil.isNotEmpty(afterSaleList)){
                if(AfterSaleTypeEnum.窜货机.name().equals(type)){
                    List<ProductDto> productList=productService.findByIds(CollectionUtil.extractToList(afterSaleList,"badProductId"));
                    for(ProductDto product:productList){
                        if(!productQtyMap.containsKey(product.getName())){
                            productQtyMap.put(product.getName(),0);
                        }
                        productQtyMap.put(product.getName(),productQtyMap.get(product.getName())+1);
                    }
                }else {
                    imeList=CollectionUtil.extractToList(afterSaleList,"badProductIme");
                    productQtyMap=productImeService.findQtyMap(imeList);
                }
            }
            map.put("productQtyMap",productQtyMap);
        }
        return map;
    }

    @RequestMapping(value = "getToCompanyForm",method = RequestMethod.GET)
    public AfterSaleToCompanyForm getToCompanyForm(AfterSaleToCompanyForm afterSaleToCompanyForm) {
        afterSaleToCompanyForm.setToCompanyDate(LocalDate.now());
        afterSaleToCompanyForm.setType(AfterSaleTypeEnum.售后机.name());
        afterSaleToCompanyForm.getExtra().put("typeList",AfterSaleTypeEnum.getList());
        return afterSaleToCompanyForm;
    }

    @RequestMapping(value = "getFromCompanyData",method = RequestMethod.GET)
    public List<AfterSaleCompanyDto> getFromCompanyData(AfterSaleQuery afterSaleQuery) {
        List<AfterSaleCompanyDto> afterSaleToCompanyList=afterSaleService.getFromCompanyData(afterSaleQuery);
        return afterSaleToCompanyList;
    }

    @RequestMapping(value = "save", method = RequestMethod.POST)
    public RestResponse save(String data, String type,String action) {
        List<List<String>> datas = ObjectMapperUtils.readValue(HtmlUtils.htmlUnescape(data), ArrayList.class);
        if(CollectionUtil.isEmpty(datas)) {
            return new RestResponse("保存失败",null,false);
        }
        afterSaleService.save(datas,type,action);
        return new RestResponse("保存成功",null);
    }

    @RequestMapping(value = "saveHead", method = RequestMethod.POST)
    public RestResponse saveHead(String data, String type,String action) {
        List<List<String>> datas = ObjectMapperUtils.readValue(HtmlUtils.htmlUnescape(data), ArrayList.class);
        if(CollectionUtil.isEmpty(datas)) {
            return new RestResponse("保存失败",null,false);
        }
        afterSaleService.saveHead(datas,type,action);
        return new RestResponse("保存成功",null);
    }

    @RequestMapping(value = "toCompany", method = RequestMethod.POST)
    public RestResponse toCompany(AfterSaleToCompanyForm afterSaleToCompanyForm) {
        if(CollectionUtil.isEmpty(afterSaleToCompanyForm.getImeList())) {
            return new RestResponse("保存失败",null,false);
        }
        afterSaleService.toCompany(afterSaleToCompanyForm);
        return new RestResponse("保存成功",null);
    }

    @RequestMapping(value = "fromCompany", method = RequestMethod.POST)
    public RestResponse fromCompany(String data,String fromCompanyDate) {
        List<List<String>> datas = ObjectMapperUtils.readValue(HtmlUtils.htmlUnescape(data), ArrayList.class);
        if(CollectionUtil.isEmpty(datas)) {
            return new RestResponse("保存失败",null,false);
        }
        afterSaleService.fromCompany(datas, LocalDateUtils.parse(fromCompanyDate));
        return new RestResponse("保存成功",null);
    }

    @RequestMapping(value = "delete")
    public RestResponse fromCompany(String detailId) {
        afterSaleService.delete(detailId);
        return new RestResponse("删除成功",null);
    }

}
