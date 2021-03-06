package net.myspring.future.modules.third.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.myspring.basic.common.util.CompanyConfigUtil;
import net.myspring.common.constant.CharConstant;
import net.myspring.common.enums.CompanyConfigCodeEnum;
import net.myspring.common.enums.CompanyNameEnum;
import net.myspring.future.common.datasource.DbContextHolder;
import net.myspring.future.modules.crm.domain.ProductIme;
import net.myspring.future.modules.crm.repository.ProductImeRepository;
import net.myspring.future.modules.third.client.VivoClient;
import net.myspring.future.modules.third.dto.VivoPlantElectronicsn;
import net.myspring.future.modules.third.dto.VivoPlantSendimei;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by guolm on 2017/5/23.
 */
@Service
@Transactional(readOnly = true)
public class VivoService {

    @Autowired
    private VivoClient vivoClient;
    @Autowired
    private ProductImeRepository productImeRepository;
    @Autowired
    private RedisTemplate redisTemplate;

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Transactional
    public String pullFactoryData(String date) {
        if (StringUtils.isBlank(date)) {
            date = LocalDateUtils.formatLocalDate(LocalDate.now(),LocalDateUtils.FORMATTER);
        }
        String companyName = DbContextHolder.get().getCompanyName();
        String agentCode = CompanyConfigUtil.findByCode(redisTemplate,CompanyConfigCodeEnum.FACTORY_AGENT_CODES.name()).getValue();
        String lxAgentCode = CompanyConfigUtil.findByCode(redisTemplate,CompanyConfigCodeEnum.LX_FACTORY_AGENT_CODES.name()).getValue();
        List<String> lxAgentCodes = Lists.newArrayList();
        if (StringUtils.isNotBlank(lxAgentCode)) {
            lxAgentCodes = StringUtils.getSplitList(lxAgentCode, CharConstant.COMMA);
        }
        List<String> agentCodes = Lists.newArrayList();
        if (StringUtils.isNotBlank(agentCode)) {
            agentCodes = StringUtils.getSplitList(agentCode, CharConstant.COMMA);
        }
        String goodStoreProduct = "";
        if (!CompanyNameEnum.IDVIVO.equals(companyName)) {
             goodStoreProduct = "49";
        }
        String defaultStoreId = CompanyConfigUtil.findByCode(redisTemplate,CompanyConfigCodeEnum.DEFAULT_STORE_ID.name()).getValue();
        String goodStoreId = CompanyConfigUtil.findByCode(redisTemplate,CompanyConfigCodeEnum.GOOD_STORE_ID.name()).getValue();
        String lxDefaultStoreId = CompanyConfigUtil.findByCode(redisTemplate,CompanyConfigCodeEnum.LX_DEFAULT_STORE_ID.name()).getValue();
        List<VivoPlantSendimei> vivoPlantSendimeis = vivoClient.getSendImeList(companyName,date, agentCode);
        List<ProductIme> productImes=Lists.newArrayList();
        List<ProductIme> productImeList=Lists.newArrayList();
        //判断绑定货品是否为空
        Set<String> itemNumberSet = Sets.newHashSet();
        if(CollectionUtil.isNotEmpty(vivoPlantSendimeis)){
            for (VivoPlantSendimei vivoPlantSendimei : vivoPlantSendimeis) {
                if (CollectionUtil.isNotEmpty(lxAgentCodes) && lxAgentCodes.contains(vivoPlantSendimei.getCompanyId())) {
                    if (StringUtils.isBlank(vivoPlantSendimei.getLxProductId())) {
                        itemNumberSet.add(vivoPlantSendimei.getProductId());
                    }
                } else {
                    if (CollectionUtil.isNotEmpty(agentCodes) && agentCodes.contains(vivoPlantSendimei.getCompanyId())) {
                        if (StringUtils.isEmpty(vivoPlantSendimei.getDefaultProductId())) {
                            itemNumberSet.add(vivoPlantSendimei.getProductId());
                        }
                    }
                }
            }
            if (CollectionUtil.isNotEmpty(itemNumberSet)) {
                return "物料编码" + itemNumberSet.toString() + "没有绑定货品，请绑定后重新同步";
            }
            if (CollectionUtil.isNotEmpty(lxAgentCodes) && StringUtils.isBlank(lxDefaultStoreId)) {
                return "联信没有绑定默认仓库";
            }
            //同步串码
            List<String> imeList = CollectionUtil.extractToList(vivoPlantSendimeis, "imei");
            for(List<String> imes:CollectionUtil.splitList(imeList,1000)){
                productImeList.addAll(productImeRepository.findByEnabledIsTrueAndImeIn(imes));
            }
            List<String>  localImeList=CollectionUtil.extractToList(productImeList, "ime");
            for (VivoPlantSendimei vivoPlantSendimei : vivoPlantSendimeis) {
                String imei = vivoPlantSendimei.getImei();
                if (!localImeList.contains(imei)) {
                    ProductIme productIme = new ProductIme();
                    if (CollectionUtil.isNotEmpty(lxAgentCodes) && lxAgentCodes.contains(vivoPlantSendimei.getCompanyId())) {
                        productIme.setDepotId(lxDefaultStoreId);
                        productIme.setRetailShopId(lxDefaultStoreId);
                        productIme.setProductId(vivoPlantSendimei.getLxProductId());
                    } else {
                        productIme.setDepotId(defaultStoreId);
                        productIme.setRetailShopId(defaultStoreId);
                        productIme.setProductId(vivoPlantSendimei.getDefaultProductId());
                    }
                    if (StringUtils.isNotBlank(goodStoreProduct)) {
                        if (StringUtils.isNotBlank(vivoPlantSendimei.getProductId()) && vivoPlantSendimei.getProductId().startsWith(goodStoreProduct)) {
                            productIme.setDepotId(goodStoreId);
                            productIme.setRetailShopId(goodStoreId);
                        }
                    }
                    productIme.setIme(vivoPlantSendimei.getImei());
                    productIme.setCreatedTime(vivoPlantSendimei.getCreatedTime());
                    productIme.setIme2(vivoPlantSendimei.getImei2());
                    productIme.setItemNumber(vivoPlantSendimei.getProductId());
                    productIme.setColorId(vivoPlantSendimei.getColorId());
                    productIme.setMeid(vivoPlantSendimei.getMeid());
                    productIme.setBoxIme(vivoPlantSendimei.getRemark());
                    productIme.setInputType("工厂入库");
                    productIme.setBillId(vivoPlantSendimei.getBillId());
                    productIme.setImeReverse(StringUtils.reverse(vivoPlantSendimei.getImei()));
                    productIme.setCreatedBy("1");
                    productIme.setCreatedDate(LocalDateTime.now());
                    productIme.setLastModifiedBy("1");
                    productIme.setLastModifiedDate(LocalDateTime.now());
                    productIme.setVersion(0);
                    productIme.setLocked(false);
                    productIme.setEnabled(true);
                    productImes.add(productIme);
                }
            }
            if(CollectionUtil.isNotEmpty(productImes)){
                productImeRepository.batchSave(productImes);
            }
        }
        //同步电子保卡
        List<ProductIme> localProductImeList=Lists.newArrayList();
        List<VivoPlantElectronicsn>  vivoPlantElectronicsns=vivoClient.getItemelectronSelList(companyName,date,agentCode);
        if(CollectionUtil.isNotEmpty(vivoPlantElectronicsns)){
            Map<String,VivoPlantElectronicsn> vivoPlantElectronicsnsMap=Maps.newHashMap();
            for(VivoPlantElectronicsn vivoPlantElectronicsn:vivoPlantElectronicsns){
                vivoPlantElectronicsnsMap.put(vivoPlantElectronicsn.getSnImei(),vivoPlantElectronicsn);
            }
            List<String> productNoList= CollectionUtil.extractToList(vivoPlantElectronicsns, "snImei");
            for(List<String> imes:CollectionUtil.splitList(productNoList,1000)){
                localProductImeList.addAll(productImeRepository.findByImeInAndRetailDate(imes));
            }
            for(ProductIme productIme:localProductImeList){
                productIme.setRetailDate(vivoPlantElectronicsnsMap.get(productIme.getIme()).getRetailDate());
            }
        }
        if(CollectionUtil.isNotEmpty(localProductImeList)){
            productImeRepository.save(localProductImeList);
        }
        return "串码同步成功，同步"+productImes.size()+"条串码,同步"+localProductImeList.size()+"条电子保卡";
    }
}
