package net.myspring.future.common.service;

import com.google.common.collect.HashBiMap;
import com.google.common.collect.Lists;
import net.myspring.future.WsFutureApplication;
import net.myspring.future.common.enums.*;
import net.myspring.future.common.utils.Const;
import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.basic.web.form.DepotForm;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.text.StringUtils;

import java.util.List;

/**
 * Created by liuj on 2016/12/30.
 */
public class DepotUtils {

    //根据类型获取depotType
    public static HashBiMap<String,Integer> getTypeMapByCategory(String depotCategory) {
        HashBiMap<String,Integer> depotMap = HashBiMap.create();
        if(DepotCategoryEnum.STORE.name().equals(depotCategory)) {
            depotMap.put(DepotTypeEnum.大库_代理.name(), DepotTypeEnum.大库_代理.getValue());
            depotMap.put(DepotTypeEnum.大库_办事处.name(), DepotTypeEnum.大库_办事处.getValue());
            depotMap.put(DepotTypeEnum.大库_总部.name(), DepotTypeEnum.大库_总部.getValue());
        }
        if(DepotCategoryEnum.SECOND_STORE.name().equals(depotCategory)) {
            depotMap.put(DepotTypeEnum.大库_代理.name(), DepotTypeEnum.大库_代理.getValue());
        }
        if(DepotCategoryEnum.SHOP_PROXY_STORE.name().equals(depotCategory)) {
            depotMap.put(DepotTypeEnum.门店_直营.name(), DepotTypeEnum.门店_直营.getValue());
            depotMap.put(DepotTypeEnum.门店_直营_分店.name(), DepotTypeEnum.门店_直营_分店.getValue());
            depotMap.put(DepotTypeEnum.大库_代理.name(), DepotTypeEnum.大库_代理.getValue());
            depotMap.put(DepotTypeEnum.门店_寄售.name(), DepotTypeEnum.门店_寄售.getValue());
            depotMap.put(DepotTypeEnum.门店_代理.name(), DepotTypeEnum.门店_代理.getValue());
        }
        if(DepotCategoryEnum.SHOP.name().equals(depotCategory)) {
            depotMap.put(DepotTypeEnum.门店_直营.name(), DepotTypeEnum.门店_直营.getValue());
            depotMap.put(DepotTypeEnum.门店_直营_分店.name(), DepotTypeEnum.门店_直营_分店.getValue());
            depotMap.put(DepotTypeEnum.门店_寄售.name(), DepotTypeEnum.门店_寄售.getValue());
            depotMap.put(DepotTypeEnum.门店_代理.name(), DepotTypeEnum.门店_代理.getValue());
        }
        if(DepotCategoryEnum.AD_SHOP.name().equals(depotCategory)) {
            depotMap.put(DepotTypeEnum.门店_直营.name(), DepotTypeEnum.门店_直营.getValue());
            depotMap.put(DepotTypeEnum.门店_直营_分店.name(), DepotTypeEnum.门店_直营_分店.getValue());
            depotMap.put(DepotTypeEnum.大库_代理.name(), DepotTypeEnum.大库_代理.getValue());
            depotMap.put(DepotTypeEnum.门店_寄售.name(), DepotTypeEnum.门店_寄售.getValue());
        }
        if(DepotCategoryEnum.DIRECT_SHOP.name().equals(depotCategory)) {
            depotMap.put(DepotTypeEnum.门店_直营.name(), DepotTypeEnum.门店_直营.getValue());
            depotMap.put(DepotTypeEnum.大库_代理.name(), DepotTypeEnum.大库_代理.getValue());
        }
        if(DepotCategoryEnum.DIRECT_AND_SUB_SHOP.name().equals(depotCategory)) {
            depotMap.put(DepotTypeEnum.门店_直营.name(), DepotTypeEnum.门店_直营.getValue());
            depotMap.put(DepotTypeEnum.门店_直营_分店.name(), DepotTypeEnum.门店_直营_分店.getValue());
            depotMap.put(DepotTypeEnum.大库_代理.name(), DepotTypeEnum.大库_代理.getValue());
            depotMap.put(DepotTypeEnum.门店_寄售.name(), DepotTypeEnum.门店_寄售.getValue());
        }
        if(DepotCategoryEnum.DELEGATE_SHOP.name().equals(depotCategory)) {
            depotMap.put(DepotTypeEnum.门店_寄售.name(), DepotTypeEnum.门店_寄售.getValue());
        }
        if(DepotCategoryEnum.DELEGATE_STORE.name().equals(depotCategory)) {
            depotMap.put(DepotTypeEnum.大库_寄售.name(), DepotTypeEnum.大库_寄售.getValue());
        }
        if(DepotCategoryEnum.PROXY_SHOP.name().equals(depotCategory)) {
            depotMap.put(DepotTypeEnum.门店_代理.name(), DepotTypeEnum.门店_代理.getValue());
        }
        return depotMap;
    }

    public static List<String> getTypeNameByCategory(String depotCategory) {
        HashBiMap<String,Integer> hashBiMap=getTypeMapByCategory(depotCategory);
        return Lists.newArrayList(hashBiMap.keySet());
    }

    public static List<Integer> getTypeValueByCategory(String depotCategory) {
        HashBiMap<String,Integer> hashBiMap=getTypeMapByCategory(depotCategory);
        return Lists.newArrayList(hashBiMap.values());
    }


    //根据级别获取
    public static HashBiMap<String,Integer> getTypesByLevel(String levelType) {
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

    public static Boolean isShop(Depot depot) {
        return getTypeMapByCategory(DepotCategoryEnum.SHOP_PROXY_STORE.name()).containsKey(depot.getType());
    }

    public static boolean isAccess(Depot depot, Boolean checkChain) {
//        Integer dataScope = AccountUtils.getPosition().getDataScope();
//        if(DataScopeEnum.ALL.getValue().equals(dataScope)) {
//            return true;
//        }
//        String accountId = AccountUtils.getAccountId();
//        List<String> officeIds = FilterUtils.getOfficeIds(accountId);
//        List<String> depotIds = FilterUtils.getDepotIds(accountId);
//        if(CollectionUtil.isNotEmpty(depotIds)) {
//            if(depotIds.contains(depot.getId())) {
//                return true;
//            }
//        } else {
//            if(officeIds.contains(depot.getOfficeId())) {
//                return true;
//            }
//        }
//        if(checkChain && StringUtils.isNotBlank(depot.getChainId())) {
//            List<String> chainIds = FilterUtils.getChainIds(accountId);
//            if(CollectionUtil.isNotEmpty(chainIds) && chainIds.contains(depot.getChainId())) {
//                return true;
//            }
//        }
        return false;
    }

    public static Integer getDepotType(DepotForm depot) {
//        CompanyConfigService companyConfigService = WsFutureApplication.getBean(CompanyConfigService.class);
//        Integer type = depot.getType()==null?DepotTypeEnum.门店_代理.getValue():depot.getType();
//        List<String> shopDelegateGroupIds = StringUtils.getSplitList(companyConfigService.findByCode(CompanyConfigCodeEnum.SHOP_DELEGATE_GROUP_IDS.getCode()).getValue(), Const.CHAR_COMMA);
//        List<String> storeDelegateGroupIds= StringUtils.getSplitList(companyConfigService.findByCode(CompanyConfigCodeEnum.STORE_DELEGATE_GROUP_IDS.getCode()).getValue(), Const.CHAR_COMMA);
//        //如果财务类型为空
//        if(StringUtils.isBlank(depot.getOutType())) {
//            //如果总店为空
//            if(StringUtils.isBlank(depot.getParentId())) {
//                type = DepotTypeEnum.门店_代理.getValue();
//            } else {
//                if(depot.getType()>=600){
//                    type = DepotTypeEnum.门店_直营_分店.getValue();
//                }else{
//                    if(depot.getParentId()!=null&& DepotOutTypeEnum.大库.equals(depot.getParent().getOutType())){
//                        type=DepotTypeEnum.大库_办事处.getValue();
//                    }
//                }
//            }
//        } else {
//            if(DepotOutTypeEnum.门店.name().equals(depot.getOutType())) {
//                if(shopDelegateGroupIds.contains(depot.getOutGroupId().toString())) {
//                    type = DepotTypeEnum.门店_寄售.getValue();
//                } else {
//                    if (!type.equals(DepotTypeEnum.大库_代理.getValue()) && !type.equals(DepotTypeEnum.门店_直营.getValue()) && !type.equals(DepotTypeEnum.门店_专卖店.getValue())) {
//                        type = DepotTypeEnum.门店_直营.getValue();
//                    }
//                }
//            } else {
//                if(storeDelegateGroupIds.contains(depot.getOutGroupId().toString())) {
//                    type = DepotTypeEnum.大库_寄售.getValue();
//                } else {
//                    if (!type.equals(DepotTypeEnum.大库_总部.getValue()) && !type.equals(DepotTypeEnum.大库_办事处.getValue())) {
//                        type = DepotTypeEnum.大库_总部.getValue();
//                    }
//                }
//            }
//        }
//        if(type.equals(DepotTypeEnum.门店_直营.getValue())) {
//            if(depot.getDelegateDepot() !=null) {
//                type = DepotTypeEnum.门店_寄售.getValue();
//            }
//            if(depot.getParent() !=null) {
//                type = DepotTypeEnum.门店_直营_分店.getValue();
//            }
//        }
//        if(type.equals(DepotTypeEnum.门店_代理.getValue()) && depot.getDelegateDepot() !=null){
//            type = DepotTypeEnum.门店_寄售.getValue();
//        }
        return null;
    }
}
