package net.myspring.future.modules.basic.manager;

import com.google.common.collect.HashBiMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.future.common.enums.*;
import net.myspring.future.common.utils.Const;
import net.myspring.future.common.utils.SecurityUtils;
import net.myspring.future.modules.basic.client.CompanyConfigClient;
import net.myspring.future.modules.basic.client.OfficeClient;
import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.basic.mapper.DepotMapper;
import net.myspring.future.modules.basic.web.form.DepotForm;
import net.myspring.future.modules.basic.web.query.DepotQuery;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by lihx on 2017/4/20.
 */
@Component
@CacheConfig(cacheNames = "depots")
public class DepotManager {
    @Autowired
    private DepotMapper depotMapper;
    @Autowired
    private OfficeClient officeClient;
    @Autowired
    private CompanyConfigClient companyConfigClient;

    @Cacheable(key="#p0")
    public Depot findOne(String id) {
        return depotMapper.findOne(id);
    }

    @Cacheable(key="#p0.id")
    public Depot save(Depot depot){
        depotMapper.save(depot);
        return  depot;
    }

    @CachePut(key="#p0.id")
    public Depot update(Depot depot){
        depotMapper.update(depot);
        return  depotMapper.findOne(depot.getId());
    }

    @CachePut(key="#p0.id")
    public Depot updateForm(DepotForm depotForm){
        depotMapper.updateForm(depotForm);
        return  depotMapper.findOne(depotForm.getId());
    }

    @CacheEvict(key="#p0")
    public int deleteById(String id) {
        return depotMapper.deleteById(id);
    }

    public List<String> getDepotIds(String accountId) {
        List<Depot> depotList=depotMapper.findByAccountId(accountId);
        return CollectionUtil.extractToList(depotList,"id");
    }

    public void deleteByIds(List<String> ids) {
        for(String id:ids){
            deleteById(id);
        }
    }

    //根据类型获取depotType
    public  HashBiMap<String,Integer> getTypeMapByCategory(String depotCategory) {
        HashBiMap<String,Integer> depotMap = HashBiMap.create();
        if(DepotCategoryEnum.STORE.name().equals(depotCategory)) {
            depotMap.put(DepotTypeEnum.大库_代理.name(),DepotTypeEnum.大库_代理.getValue());
            depotMap.put(DepotTypeEnum.大库_办事处.name(),DepotTypeEnum.大库_办事处.getValue());
            depotMap.put(DepotTypeEnum.大库_总部.name(),DepotTypeEnum.大库_总部.getValue());
        }
        if(DepotCategoryEnum.SECOND_STORE.name().equals(depotCategory)) {
            depotMap.put(DepotTypeEnum.大库_代理.name(),DepotTypeEnum.大库_代理.getValue());
        }
        if(DepotCategoryEnum.SHOP_PROXY_STORE.name().equals(depotCategory)) {
            depotMap.put(DepotTypeEnum.门店_直营.name(),DepotTypeEnum.门店_直营.getValue());
            depotMap.put(DepotTypeEnum.门店_直营_分店.name(),DepotTypeEnum.门店_直营_分店.getValue());
            depotMap.put(DepotTypeEnum.大库_代理.name(),DepotTypeEnum.大库_代理.getValue());
            depotMap.put(DepotTypeEnum.门店_寄售.name(),DepotTypeEnum.门店_寄售.getValue());
            depotMap.put(DepotTypeEnum.门店_代理.name(),DepotTypeEnum.门店_代理.getValue());
        }
        if(DepotCategoryEnum.SHOP.name().equals(depotCategory)) {
            depotMap.put(DepotTypeEnum.门店_直营.name(),DepotTypeEnum.门店_直营.getValue());
            depotMap.put(DepotTypeEnum.门店_直营_分店.name(),DepotTypeEnum.门店_直营_分店.getValue());
            depotMap.put(DepotTypeEnum.门店_寄售.name(),DepotTypeEnum.门店_寄售.getValue());
            depotMap.put(DepotTypeEnum.门店_代理.name(),DepotTypeEnum.门店_代理.getValue());
        }
        if(DepotCategoryEnum.AD_SHOP.name().equals(depotCategory)) {
            depotMap.put(DepotTypeEnum.门店_直营.name(),DepotTypeEnum.门店_直营.getValue());
            depotMap.put(DepotTypeEnum.门店_直营_分店.name(),DepotTypeEnum.门店_直营_分店.getValue());
            depotMap.put(DepotTypeEnum.大库_代理.name(),DepotTypeEnum.大库_代理.getValue());
            depotMap.put(DepotTypeEnum.门店_寄售.name(),DepotTypeEnum.门店_寄售.getValue());
        }
        if(DepotCategoryEnum.DIRECT_SHOP.name().equals(depotCategory)) {
            depotMap.put(DepotTypeEnum.门店_直营.name(),DepotTypeEnum.门店_直营.getValue());
            depotMap.put(DepotTypeEnum.大库_代理.name(),DepotTypeEnum.大库_代理.getValue());
        }
        if(DepotCategoryEnum.DIRECT_AND_SUB_SHOP.name().equals(depotCategory)) {
            depotMap.put(DepotTypeEnum.门店_直营.name(),DepotTypeEnum.门店_直营.getValue());
            depotMap.put(DepotTypeEnum.门店_直营_分店.name(),DepotTypeEnum.门店_直营_分店.getValue());
            depotMap.put(DepotTypeEnum.大库_代理.name(),DepotTypeEnum.大库_代理.getValue());
            depotMap.put(DepotTypeEnum.门店_寄售.name(),DepotTypeEnum.门店_寄售.getValue());
        }
        if(DepotCategoryEnum.DELEGATE_SHOP.name().equals(depotCategory)) {
            depotMap.put(DepotTypeEnum.门店_寄售.name(),DepotTypeEnum.门店_寄售.getValue());
        }
        if(DepotCategoryEnum.DELEGATE_STORE.name().equals(depotCategory)) {
            depotMap.put(DepotTypeEnum.大库_寄售.name(),DepotTypeEnum.大库_寄售.getValue());
        }
        if(DepotCategoryEnum.PROXY_SHOP.name().equals(depotCategory)) {
            depotMap.put(DepotTypeEnum.门店_代理.name(),DepotTypeEnum.门店_代理.getValue());
        }
        return depotMap;
    }

    public  List<String> getTypeNameByCategory(String depotCategory) {
        HashBiMap<String,Integer> hashBiMap=getTypeMapByCategory(depotCategory);
        return Lists.newArrayList(hashBiMap.keySet());
    }

    public  List<Integer> getTypeValueByCategory(String depotCategory) {
        HashBiMap<String,Integer> hashBiMap=getTypeMapByCategory(depotCategory);
        return Lists.newArrayList(hashBiMap.values());
    }

    //根据级别获取
    public  HashBiMap<String,Integer> getTypesByLevel(String levelType) {
        HashBiMap<String,Integer> depotMap = HashBiMap.create();
        if(DepotLevelEnum.FIRST.name().equals(levelType)) {
            depotMap.put(DepotTypeEnum.大库_总部.name(), DepotTypeEnum.大库_总部.getValue());
        }
        if(DepotLevelEnum.SECOND.name().equals(levelType)) {
            depotMap.put(DepotTypeEnum.大库_办事处.name(), DepotTypeEnum.大库_办事处.getValue());
            depotMap.put(DepotTypeEnum.大库_代理.name(), DepotTypeEnum.大库_代理.getValue());
        }
        if(DepotLevelEnum.THIRD.name().equals(levelType)) {
            depotMap.put(DepotTypeEnum.门店_代理.name(), DepotTypeEnum.门店_代理.getValue());
            depotMap.put(DepotTypeEnum.门店_直营.name(), DepotTypeEnum.门店_直营.getValue());
            depotMap.put(DepotTypeEnum.大库_寄售.name(), DepotTypeEnum.大库_寄售.getValue());
            depotMap.put(DepotTypeEnum.门店_寄售.name(), DepotTypeEnum.门店_寄售.getValue());
            depotMap.put(DepotTypeEnum.门店_直营_分店.name(), DepotTypeEnum.门店_直营_分店.getValue());
        }
        return depotMap;
    }

    public  Boolean isShop(Depot depot) {
        return getTypeMapByCategory(DepotCategoryEnum.SHOP_PROXY_STORE.name()).containsKey(depot.getType());
    }

    public  boolean isAccess(Depot depot, Boolean checkChain) {
        Map<String,Object> filterMap=filterDepotIds();
        List<String> depotIdList= (List<String>) filterMap.get("depotIdList");
        List<String> officeIdList= (List<String>) filterMap.get("officeIdList");
        if(CollectionUtil.isNotEmpty(depotIdList)) {
            if(depotIdList.contains(depot.getId())) {
                return true;
            }
        } else {
            if(officeIdList.contains(depot.getOfficeId())) {
                return true;
            }
        }
        if(checkChain && StringUtils.isNotBlank(depot.getChainId())) {
            DepotQuery depotQuery=new DepotQuery();
            depotQuery.setOfficeIdList(officeIdList);
            depotQuery.setDepotIdList(depotIdList);
            List<String> chainIds = depotMapper.findChainIds(depotQuery);
            if(CollectionUtil.isNotEmpty(chainIds) && chainIds.contains(depot.getChainId())) {
                return true;
            }
        }
        return false;
    }

    public  Integer getDepotType(Depot depot) {
        Integer type = depot.getType()==null?DepotTypeEnum.门店_代理.getValue():depot.getType();
        List<String> shopDelegateGroupIds = StringUtils.getSplitList(companyConfigClient.findByCode(CompanyConfigCodeEnum.SHOP_DELEGATE_GROUP_IDS.getCode()).getValue(), Const.CHAR_COMMA);
        List<String> storeDelegateGroupIds= StringUtils.getSplitList(companyConfigClient.findByCode(CompanyConfigCodeEnum.STORE_DELEGATE_GROUP_IDS.getCode()).getValue(), Const.CHAR_COMMA);
        Depot parent=findOne(depot.getParentId());
        //如果财务类型为空
        if(StringUtils.isBlank(depot.getOutType())) {
            //如果总店为空
            if(StringUtils.isBlank(depot.getParentId())) {
                type = DepotTypeEnum.门店_代理.getValue();
            } else {
                if(depot.getType()>=600){
                    type = DepotTypeEnum.门店_直营_分店.getValue();
                }else{
                    if(depot.getParentId()!=null&& DepotOutTypeEnum.大库.equals(parent.getOutType())){
                        type=DepotTypeEnum.大库_办事处.getValue();
                    }
                }
            }
        } else {
            if(DepotOutTypeEnum.门店.name().equals(depot.getOutType())) {
                if(shopDelegateGroupIds.contains(depot.getOutGroupId().toString())) {
                    type = DepotTypeEnum.门店_寄售.getValue();
                } else {
                    if (!type.equals(DepotTypeEnum.大库_代理.getValue()) && !type.equals(DepotTypeEnum.门店_直营.getValue()) && !type.equals(DepotTypeEnum.门店_专卖店.getValue())) {
                        type = DepotTypeEnum.门店_直营.getValue();
                    }
                }
            } else {
                if(storeDelegateGroupIds.contains(depot.getOutGroupId().toString())) {
                    type = DepotTypeEnum.大库_寄售.getValue();
                } else {
                    if (!type.equals(DepotTypeEnum.大库_总部.getValue()) && !type.equals(DepotTypeEnum.大库_办事处.getValue())) {
                        type = DepotTypeEnum.大库_总部.getValue();
                    }
                }
            }
        }
        if(type.equals(DepotTypeEnum.门店_直营.getValue())) {
            if(depot.getDelegateDepotId() !=null) {
                type = DepotTypeEnum.门店_寄售.getValue();
            }
            if(depot.getParentId() !=null) {
                type = DepotTypeEnum.门店_直营_分店.getValue();
            }
        }
        if(type.equals(DepotTypeEnum.门店_代理.getValue())&&depot.getDelegateDepotId() !=null){
            type = DepotTypeEnum.门店_寄售.getValue();
        }
        return type;
    }

    public Map<String,Object> filterDepotIds(){
        Map<String,Object> filterMap= Maps.newLinkedHashMap();
        List<Depot> depotList=depotMapper.findByAccountId(SecurityUtils.getAccountId());
        filterMap.put("depotIdList",CollectionUtil.extractToList(depotList,"id"));
        List<String> officeIdList=officeClient.getOfficeFilterIds(SecurityUtils.getAccountId());
        filterMap.put("officeIdList",officeIdList);
        return filterMap;
    }
}
