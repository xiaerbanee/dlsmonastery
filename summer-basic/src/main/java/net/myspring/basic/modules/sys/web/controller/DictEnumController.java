package net.myspring.basic.modules.sys.web.controller;

import com.google.common.collect.HashBiMap;
import com.google.common.collect.Maps;
import net.myspring.basic.modules.sys.dto.DictEnumDto;
import net.myspring.basic.modules.sys.service.DictEnumService;
import net.myspring.basic.modules.sys.web.form.DictEnumForm;
import net.myspring.basic.modules.sys.web.query.DictEnumQuery;
import net.myspring.common.enums.DictEnumCategoryEnum;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.util.collection.CollectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "sys/dictEnum")
public class DictEnumController {

    @Autowired
    private DictEnumService dictEnumService;

    @RequestMapping(method = RequestMethod.GET)
    public Page<DictEnumDto>  list(Pageable pageable, DictEnumQuery dictEnumQuery){
        Page<DictEnumDto> page = dictEnumService.findPage(pageable,dictEnumQuery);
        return page;
    }

    @RequestMapping(value = "delete")
    public RestResponse delete(String id) {
        dictEnumService.logicDelete(id);
        RestResponse restResponse =new RestResponse("删除成功",ResponseCodeEnum.removed.name());
        return restResponse;
    }

    @RequestMapping(value = "save")
    public RestResponse save(DictEnumForm dictEnumForm) {
        dictEnumService.save(dictEnumForm);
        return new RestResponse("保存成功", ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "findOne")
    public DictEnumDto findOne(String id){
        return dictEnumService.findOne(id);
    }

    @RequestMapping(value = "getForm")
    public DictEnumForm getForm(DictEnumForm dictEnumForm){
        dictEnumForm.getExtra().put("categoryList",dictEnumService.findDistinctCategory());
        return dictEnumForm;
    }


    @RequestMapping(value="getQuery")
    public  DictEnumQuery getQuery(DictEnumQuery dictEnumQuery){
        dictEnumQuery.setCategoryList(dictEnumService.findDistinctCategory());
        return dictEnumQuery;
    }

    @RequestMapping(value="findByCategory")
    public List<DictEnumDto> findByCategory(String category){
        return dictEnumService.findByCategory(category);
    }

    @RequestMapping(value="findByCategoryList")
    public Map<String,List<DictEnumDto>> findByCategoryList(@RequestParam(value = "categoryList[]") String[] categoryList){
        List<String> categorys= Arrays.asList(categoryList);
        List<DictEnumDto> list = dictEnumService.findByCategoryList(categorys);
        Map<String, List<DictEnumDto>> categoryMap = Maps.newHashMap();
        if(CollectionUtil.isNotEmpty(list)){
            HashBiMap<String,String> biMap= DictEnumCategoryEnum.getMap();
            Map<String, List<DictEnumDto>> map= CollectionUtil.extractToMapList(list, "category");
            for(String key:map.keySet()){
                categoryMap.put(biMap.inverse().get(key),map.get(key));
            }
        }
        return categoryMap;
    }

    @RequestMapping(value = "findByValue")
    public String findByValue(String value){
        return dictEnumService.findByValue(value);
    }
}
