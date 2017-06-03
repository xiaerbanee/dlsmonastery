package net.myspring.future.modules.crm.web.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.common.constant.CharConstant;
import net.myspring.common.response.RestResponse;
import net.myspring.future.modules.basic.service.ProductService;
import net.myspring.future.modules.crm.domain.AfterSale;
import net.myspring.future.modules.crm.domain.AfterSaleDetail;
import net.myspring.future.modules.crm.domain.ProductIme;
import net.myspring.future.modules.crm.dto.AfterSaleInputDto;
import net.myspring.future.modules.crm.dto.AfterSaleDto;
import net.myspring.future.modules.crm.dto.AfterSaleCompanyDto;
import net.myspring.future.modules.crm.dto.ProductImeDto;
import net.myspring.future.modules.crm.service.AfterSaleService;
import net.myspring.future.modules.crm.service.ProductImeService;
import net.myspring.future.modules.crm.web.query.AfterSaleQuery;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.json.ObjectMapperUtils;
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

    @RequestMapping(method = RequestMethod.GET)
    public Page<AfterSaleDto> list(Pageable pageable, AfterSaleQuery afterSaleQuery) {
        Page<AfterSaleDto> page = afterSaleService.findPage(pageable, afterSaleQuery);
        return page;
    }

    @RequestMapping(value = "areaInputData", method = RequestMethod.GET)
    public List<AfterSaleInputDto> formData(String imeStr) {
        List<AfterSaleInputDto> afterSaleInputList=Lists.newArrayList();
        if(StringUtils.isNotBlank(imeStr)){
            List<String> imeList = StringUtils.getSplitList(imeStr, CharConstant.ENTER);
            List<ProductImeDto> productImeList=productImeService.findByImeList(imeList);
            for(ProductImeDto productIme:productImeList){
                AfterSaleInputDto afterSaleInputDto=new AfterSaleInputDto();
                afterSaleInputDto.setProductName(productIme.getProductName());
                afterSaleInputDto.setIme(productIme.getIme());
                afterSaleInputDto.setDepotName(productIme.getDepotName());
                afterSaleInputList.add(afterSaleInputDto);
            }
        }
        return afterSaleInputList;
    }

    @RequestMapping(value = "areaInputUpdateData", method = RequestMethod.GET)
    public List<AfterSaleInputDto> areaInputUpdateData(String imeStr) {
        List<AfterSaleInputDto> afterSaleInputList=Lists.newArrayList();
        if(StringUtils.isNotBlank(imeStr)){
            List<String> imeList = StringUtils.getSplitList(imeStr, CharConstant.ENTER);
            afterSaleInputList=afterSaleService.areaInputUpdateData(imeList);
        }
        return afterSaleInputList;
    }

    @RequestMapping(value = "headInputData", method = RequestMethod.GET)
    public List<AfterSaleInputDto> editFormData(String imeStr) {
        List<AfterSaleInputDto> afterSaleInputList=Lists.newArrayList();
        if(StringUtils.isNotBlank(imeStr)){
            List<String> imeList = StringUtils.getSplitList(imeStr, CharConstant.ENTER);
            List<ProductImeDto> productImeList=productImeService.findByImeList(imeList);
            List<AfterSale> afterSaleList=afterSaleService.findByImeList(imeList);
            Map<String,AfterSale> afterSaleMap= CollectionUtil.extractToMap(afterSaleList,"badProductImeId");
            for(ProductImeDto productIme:productImeList){
                AfterSaleInputDto afterSaleInputDto=new AfterSaleInputDto();
                AfterSale afterSale=afterSaleMap.get(productIme.getId());
                if(afterSale!=null){
                    afterSaleInputDto.setMemory(afterSale.getMemory());
                    afterSaleInputDto.setBadType(afterSale.getBadType());
                    afterSaleInputDto.setPackageStatus(afterSale.getPackageStatus());
                }
                afterSaleInputDto.setProductName(productIme.getProductName());
                afterSaleInputDto.setIme(productIme.getIme());
                afterSaleInputDto.setDepotName(productIme.getDepotName());
                afterSaleInputList.add(afterSaleInputDto);
            }
        }
        return afterSaleInputList;
    }

    @RequestMapping(value="searchImeMap" ,method=RequestMethod.GET)
    public Map<String,Object> searchImeMap(String imeStr){
        Map<String,Object> map=Maps.newLinkedHashMap();
        if(StringUtils.isNotBlank(imeStr)){
            List<String> imeList = StringUtils.getSplitList(imeStr, CharConstant.ENTER);
            List<AfterSale> afterSaleList=afterSaleService.findByImeList(imeList);
            map.put("afterSaleList",afterSaleList);
            Map<String,Integer> productQtyMap=productImeService.findQtyMap(imeList);
            map.put("productQtyMap",productQtyMap);
        }
        return map;
    }


    @RequestMapping(value = "getFromCompanyData",method = RequestMethod.GET)
    public List<AfterSaleCompanyDto> getFromCompanyData(String imeStr) {
        List<AfterSaleCompanyDto> afterSaleToCompanyList=Lists.newArrayList();
        if(StringUtils.isNotBlank(imeStr)){
            List<String> imeList = StringUtils.getSplitList(imeStr, CharConstant.ENTER);
            afterSaleService.getFromCompanyData(imeList);
        }
        return afterSaleToCompanyList;
    }

    @RequestMapping(value = "save", method = RequestMethod.POST)
    public RestResponse save(String data, String type) {
        List<List<String>> datas = ObjectMapperUtils.readValue(HtmlUtils.htmlUnescape(data), ArrayList.class);
        if(CollectionUtil.isEmpty(datas)) {
            return new RestResponse("保存失败",null,false);
        }
        afterSaleService.save(datas,type);
        return new RestResponse("保存成功",null);
    }

    @RequestMapping(value = "saveHead", method = RequestMethod.POST)
    public RestResponse saveHead(String data, String type) {
        List<List<String>> datas = ObjectMapperUtils.readValue(HtmlUtils.htmlUnescape(data), ArrayList.class);
        if(CollectionUtil.isEmpty(datas)) {
            return new RestResponse("保存失败",null,false);
        }
        afterSaleService.saveHead(datas,type);
        return new RestResponse("保存成功",null);
    }

    @RequestMapping(value = "toCompany", method = RequestMethod.POST)
    public RestResponse toCompany(List<String> badImes, LocalDate toCompanyDate, String toCompanyRemarks) {
        if(CollectionUtil.isEmpty(badImes)) {
            return new RestResponse("保存失败",null,false);
        }
        afterSaleService.toCompany(badImes,toCompanyDate,toCompanyRemarks);
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

    @RequestMapping(value = "synToFinance")
    public String synToFinance() {
        return null;
    }

    @RequestMapping(value = "delete")
    public String logicDelete(String id) {
        return null;
    }

}
