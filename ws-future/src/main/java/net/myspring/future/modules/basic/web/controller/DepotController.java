package net.myspring.future.modules.basic.web.controller;


import com.google.common.collect.HashBiMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.common.response.RestResponse;
import net.myspring.future.common.utils.Const;
import net.myspring.future.common.utils.DepotUtils;
import net.myspring.future.modules.basic.client.OfficeClient;
import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.basic.dto.DepotDto;
import net.myspring.future.modules.basic.service.DepotService;
import net.myspring.future.modules.basic.web.validator.DepotValidator;
import net.myspring.future.modules.basic.web.form.DepotForm;
import net.myspring.future.modules.basic.web.query.DepotQuery;
import net.myspring.future.modules.layout.service.ShopAttributeService;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "basic/depot")
public class DepotController {

    @Autowired
    private DepotService depotService;
    @Autowired
    private ShopAttributeService shopAttributeService;
    @Autowired
    private DepotValidator depotValidator;
    @Autowired
    private OfficeClient officeClient;

    @RequestMapping(method = RequestMethod.GET)
    public Page<DepotDto>  list(Pageable pageable, DepotQuery depotQuery){
        Page<DepotDto> page = depotService.findPage(pageable, depotQuery);
        return page;
    }

    @RequestMapping(value = "getQuery")
    public DepotQuery getQuery(DepotQuery depotQuery) {
        depotQuery = depotService.getQueryProperty(depotQuery);
        depotQuery.setBasicOfficeDtoList(officeClient.findSortList());
        LocalDate dateStart = LocalDate.from(LocalDate.now().plusDays(-70));
        LocalDate dateEnd = LocalDate.from(LocalDate.now().plusMonths(1));
        String dateRange = LocalDateUtils.format(dateStart) + " - " + LocalDateUtils.format(dateEnd);
        depotQuery.setDateRange(dateRange);
        return depotQuery;
    }

    @RequestMapping(value = "getFormProperty")
    public DepotForm getFormProperty(DepotForm depotForm) {
        depotService.getFormProperty(depotForm);
        return depotForm;
    }


    @RequestMapping(value = "detail")
    public DepotForm detail(DepotForm depotForm) {
        depotForm.setShopAttributeList(shopAttributeService.findByShopId(depotForm.getId()));
        return depotForm;
    }


    @RequestMapping(value = "save")
    public RestResponse save(DepotForm depotForm) {
        RestResponse restResponse = new RestResponse("保存成功",null);
        depotService.save(depotForm);
        return restResponse;
    }

    @RequestMapping(value = "syn")
    public RestResponse syn(){
        depotService.synStore();
        depotService.synShop();
        return new RestResponse("同步成功",null);
    }


    @RequestMapping(value = "findForm")
    public DepotDto findOne(String depotId){
        DepotDto depotDto = depotService.findOne(depotId);
        return depotDto;
    }

    @RequestMapping(value = "searchShop")
    public  List<DepotDto> searchShop(String name){
        String category = "SHOP";
        List<Depot> depots = search(name,category);
        List<DepotDto> depotDtos = new ArrayList<>();
        for(Depot depot:depots){
            depotDtos.add(BeanUtil.map(depot,DepotDto.class));
        }
        return  depotDtos;
    }

    @RequestMapping(value = "search")
    public List<Depot> search(String name,String category) {
        List<Depot> depotList = Lists.newArrayList();
//        Map<String,Object> filter = FilterUtils.getDepotFilter(AccountUtils.getAccountId());
        DepotQuery depotQuery = new DepotQuery();
        if(/*depotQuery.getDepotIdList().size()>0 || */StringUtils.isNotBlank(name)) {
            depotQuery.setName(name);
            if (StringUtils.isNotBlank(category)) {
                HashBiMap<String, Integer> typeMap = DepotUtils.getTypeMapByCategory(category);
                depotQuery.setTypeList(CollectionUtil.extractToList(typeMap.entrySet(),"value"));
            }
            depotList = depotService.findByFilter(depotQuery);
        }
        return depotList;
    }

    @RequestMapping(value = "proxyShop")
    public List<Map<String, String>> adShop(String key) {
        List<Map<String, String>> list = Lists.newArrayList();
        if(StringUtils.isNotBlank(key)) {
            DepotQuery depotQuery = new DepotQuery();
            depotQuery.setDepotName(key);
            depotQuery.setType(key);
            List<DepotDto> depots = depotService.findProxyShop(depotQuery);
            for (DepotDto depot : depots) {
                Map<String,String> map = Maps.newHashMap();
                map.put("id", depot.getId());
                map.put("fullName", depot.getAreaName() + Const.CHAR_SLASH_LINE+depot.getName());
                map.put("name", depot.getAreaName() + Const.CHAR_SLASH_LINE+depot.getName());
                list.add(map);
            }
        }
        return list;
    }

    @RequestMapping(value = "adShop")
    public List<Map<String, String>> adShop(String adShopName,String billType){
        List<Map<String, String>> list = Lists.newArrayList();
        if(StringUtils.isNotBlank(adShopName)) {
            List<DepotDto> depots = depotService.findAdDepot(adShopName,billType);
            for (DepotDto depotDto : depots) {
                Map<String, String> map = Maps.newHashMap();
                map.put("id", depotDto.getId());
                map.put("typeLabel",depotDto.getTypeLabel());
                map.put("name", depotDto.getAreaName() + Const.CHAR_SLASH_LINE + depotDto.getName());
                list.add(map);
            }
        }
        return list;
    }

    @RequestMapping(value = "adShopBsc")
    public List<Map<String, String>> adShopBsc(String key) {
        List<Map<String, String>> list = Lists.newArrayList();
        List<DepotDto> depots;
        if(StringUtils.isNotBlank(key)) {
            depots = depotService.findAdShopBsc(key);
            for (DepotDto depot : depots) {
                Map<String, String> map = Maps.newHashMap();
                map.put("id", depot.getId());
                map.put("fullName", depot.getAreaName() + Const.CHAR_SLASH_LINE + depot.getName());
                map.put("name", depot.getAreaName() + Const.CHAR_SLASH_LINE + depot.getName());
                list.add(map);
            }
        }
        return list;
    }

    @RequestMapping(value = "shop")
    public List<Depot> shop(String name) {
        List<Depot> shopList = Lists.newArrayList();
//        Map<String,Object> filter = FilterUtils.getDepotFilter(AccountUtils.getAccountId());
        DepotQuery depotQuery = new DepotQuery();
        if(StringUtils.isNotBlank(name)|| depotQuery.getDepotIdList().size() > 0) {
            depotQuery.setName(name);
            shopList = depotService.findByFilter(depotQuery);
        }
        return shopList;
    }

}
