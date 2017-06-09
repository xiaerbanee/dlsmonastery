package net.myspring.future.modules.basic.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.future.common.enums.OutTypeEnum;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.basic.client.OfficeClient;
import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.basic.domain.DepotShop;
import net.myspring.future.modules.basic.dto.DepotReportDetailDto;
import net.myspring.future.modules.basic.dto.DepotReportDto;
import net.myspring.future.modules.basic.dto.DepotShopDto;
import net.myspring.future.modules.basic.manager.DepotManager;
import net.myspring.future.modules.basic.repository.DepotRepository;
import net.myspring.future.modules.basic.repository.DepotShopRepository;
import net.myspring.future.modules.basic.web.form.DepotForm;
import net.myspring.future.modules.basic.web.form.DepotShopForm;
import net.myspring.future.modules.basic.web.query.DepotQuery;
import net.myspring.future.modules.basic.web.query.DepotReportQuery;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.reflect.ReflectionUtil;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by liuj on 2017/5/12.
 */
@Service
public class DepotShopService {
    @Autowired
    private DepotShopRepository depotShopRepository;
    @Autowired
    private DepotManager depotManager;
    @Autowired
    private DepotRepository depotRepository;
    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private OfficeClient officeClient;

    public Page<DepotShopDto> findPage(Pageable pageable, DepotQuery depotQuery){
        depotQuery.setOfficeIdList(officeClient.getOfficeFilterIds(RequestUtils.getRequestEntity().getOfficeId()));
        depotQuery.setDepotIdList(depotManager.filterDepotIds());
        if(StringUtils.isNotBlank(depotQuery.getOfficeId())){
            depotQuery.getOfficeIdList().addAll(officeClient.getChildOfficeIds(depotQuery.getOfficeId()));
        }
        Page<DepotShopDto> page=depotShopRepository.findPage(pageable,depotQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    public DepotShopForm getForm(DepotShopForm depotShopForm) {
        if(!depotShopForm.isCreate()) {
            DepotShop depotShop =depotShopRepository.findOne(depotShopForm.getId());
            depotShopForm= BeanUtil.map(depotShop,DepotShopForm.class);
            cacheUtils.initCacheInput(depotShopForm);
        }
        return depotShopForm;
    }

    public DepotForm findDepotForm(DepotForm depotForm) {
        if(!depotForm.isCreate()) {
            Depot depot =depotRepository.findOne(depotForm.getId());
            depotForm= BeanUtil.map(depot,DepotForm.class);
            cacheUtils.initCacheInput(depotForm);
        }
        return depotForm;
    }

    public DepotShop save(DepotShopForm depotShopForm) {
        DepotShop depotShop;
        if(depotShopForm.isCreate()) {
            depotShop = BeanUtil.map(depotShopForm,DepotShop.class);
            depotShopRepository.save(depotShop);
            Depot depot=new Depot();
            depot.setName(depotShopForm.getDepotName());
            depot.setNamePinyin(StringUtils.getFirstSpell(depotShopForm.getDepotName()));
            depot.setDepotShopId(depotShop.getId());
            depotRepository.save(depot);
            depotShop.setDepotId(depot.getId());
            depotShopRepository.save(depotShop);
        } else {
            depotShop = depotShopRepository.findOne(depotShopForm.getId());
            ReflectionUtil.copyProperties(depotShopForm,depotShop);
            depotShopRepository.save(depotShop);
        }
        return depotShop;
    }

    public Depot saveDepot(DepotForm depotForm){
        Depot depot;
        depotForm.setNamePinyin(StringUtils.getFirstSpell(depotForm.getName()));
        if(depotForm.isCreate()){
            depot=BeanUtil.map(depotForm,Depot.class);
            depotRepository.save(depot);
            DepotShop depotShop=new DepotShop();
            depotShop.setDepotId(depot.getId());
            depotShopRepository.save(depotShop);
            depot.setDepotShopId(depotShop.getId());
            depotRepository.save(depot);
        }else {
            depot=depotRepository.findOne(depotForm.getId());
            ReflectionUtil.copyProperties(depotForm,depot);
            depotRepository.save(depot);
        }
        return depot;
    }


    public void logicDelete(String id) {
        depotShopRepository.logicDelete(id);
    }

    public List<DepotShopDto> setReportData(List<DepotShopDto>  depotList,DepotReportQuery depotReportQuery){
        depotReportQuery.setShopIdList(CollectionUtil.extractToList(depotList,"depotId"));
        List<DepotReportDto> depotReportList=getProductImeReportList(depotReportQuery);
        Map<String,DepotReportDto> map= CollectionUtil.extractToMap(depotReportList,"depotId");
        for(DepotShopDto depotShopDto:depotList){
            DepotReportDto depotReportDto=map.get(depotShopDto.getDepotId());
            if(depotReportDto!=null){
                depotShopDto.setQty(depotReportDto.getQty());
            }
        }
        setPercentage(depotList);
        return depotList;
    }

    public DepotReportDetailDto getReportDataDetail(DepotReportQuery depotReportQuery){
        DepotReportDetailDto depotReportDetail=new DepotReportDetailDto();
        List<DepotReportDto> depotReportList=getProductImeReportList(depotReportQuery);
        depotReportDetail.setDepotReportList(depotReportList);
        Map<String,Integer> map= Maps.newHashMap();
        for(DepotReportDto depotReportDto:depotReportList){
            if(!map.containsKey(depotReportDto.getProductName())){
                map.put(depotReportDto.getProductName(),1);
            }else {
                map.put(depotReportDto.getProductName(),map.get(depotReportDto.getProductName())+1);
            }
        }
        depotReportDetail.setProductQtyMap(map);
        return depotReportDetail;
    }

    private List<DepotReportDto> getProductImeReportList(DepotReportQuery depotReportQuery){
        List<DepotReportDto> depotReportList= Lists.newArrayList();
        if(OutTypeEnum.电子报卡.name().equals(depotReportQuery.getOutType())){
            if("销售报表".equals(depotReportQuery.getType())){
                depotReportList=depotShopRepository.findBaokaSaleReport(depotReportQuery);
            }else if("库存报表".equals(depotReportQuery.getType())){
                depotReportList=depotShopRepository.findBaokaStoreReport(depotReportQuery);
            }
        }else {
            if("销售报表".equals(depotReportQuery.getType())){
                depotReportList=depotShopRepository.findSaleReport(depotReportQuery);
            }else if("库存报表".equals(depotReportQuery.getType())){
                depotReportList=depotShopRepository.findBaokaStoreReport(depotReportQuery);
            }
        }
        return depotReportList;
    }

    private void setPercentage(List<DepotShopDto> depotShopList) {
        Integer sum = 0;
        for (DepotShopDto depotShopDto : depotShopList) {
            sum= sum + depotShopDto.getQty();
        }
        for (DepotShopDto depotShopDto : depotShopList) {
            depotShopDto.setPercent(division(sum,depotShopDto.getQty()));
        }
    }

    private String division(Integer totalQty, Integer qty) {
        if (qty == 0 || totalQty == 0) {
            return "0.00";
        }
        BigDecimal percent = new BigDecimal(qty).multiply(new BigDecimal(100)).divide(new BigDecimal(totalQty),2, BigDecimal.ROUND_HALF_UP);
        return percent.toString();
    }

}
