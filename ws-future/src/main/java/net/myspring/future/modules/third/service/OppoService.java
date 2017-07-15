package net.myspring.future.modules.third.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.myspring.common.constant.CharConstant;
import net.myspring.common.enums.CompanyConfigCodeEnum;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.basic.client.CompanyConfigClient;
import net.myspring.future.modules.crm.domain.ProductIme;
import net.myspring.future.modules.crm.repository.ProductImeRepository;
import net.myspring.future.modules.third.client.OppoClient;
import net.myspring.future.modules.third.dto.OppoPlantProductItemelectronSel;
import net.myspring.future.modules.third.dto.OppoPlantSendImeiPpsel;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.time.LocalDateUtils;
import net.myspring.util.text.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
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
@Transactional(readOnly = false)
public class OppoService {

    @Autowired
    private OppoClient oppoClient;
    @Autowired
    private CompanyConfigClient companyConfigClient;
    @Autowired
    private ProductImeRepository productImeRepository;

    protected Logger logger = LoggerFactory.getLogger(getClass());


    @Scheduled(cron = "0 0 0/1 * * ?")
    public void syn(){
        logger.info("开始同步串码："+LocalDateTime.now());
        String date=LocalDateUtils.format(LocalDate.now());
        synOppo(date);
        logger.info("串码同步结束："+LocalDateTime.now());
    }

    public String synOppo(String date) {
        if (StringUtils.isBlank(date)) {
            date = LocalDateUtils.formatLocalDate(LocalDate.now(), "yyyy-MM-dd");
        }
        String agentCode = companyConfigClient.getValueByCode(CompanyConfigCodeEnum.FACTORY_AGENT_CODES.name()).replace("\"", "");
        String lxAgentCode = companyConfigClient.getValueByCode(CompanyConfigCodeEnum.LX_FACTORY_AGENT_CODES.name()).replace("\"", "");
        List<String> lxAgentCodes = Lists.newArrayList();
        if (StringUtils.isNotBlank(lxAgentCode)) {
            lxAgentCodes = StringUtils.getSplitList(lxAgentCode, CharConstant.COMMA);
        }
        List<String> agentCodes = Lists.newArrayList();
        if (StringUtils.isNotBlank(agentCode)) {
            agentCodes = StringUtils.getSplitList(agentCode, CharConstant.COMMA);
        }
        String goodStoreProduct = "";
        String companyName = companyConfigClient.getValueByCode(CompanyConfigCodeEnum.COMPANY_NAME.name()).replace("\"", "");
        if (!"WZOPPO".equals(companyName)) {
            goodStoreProduct = "7070";
        }
        String defaultStoreId = companyConfigClient.getValueByCode(CompanyConfigCodeEnum.DEFAULT_STORE_ID.name()).replace("\"", "");
        String goodStoreId = companyConfigClient.getValueByCode(CompanyConfigCodeEnum.GOOD_STORE_ID.name()).replace("\"", "");
        String lxDefaultStoreId = companyConfigClient.getValueByCode(CompanyConfigCodeEnum.LX_DEFAULT_STORE_ID.name()).replace("\"", "");
        List<OppoPlantSendImeiPpsel> oppoPlantSendImeiPpsels = oppoClient.findSynImeList(date, agentCode);
        List<ProductIme> productImes=Lists.newArrayList();
        List<ProductIme> productImeList=Lists.newArrayList();
        //判断绑定货品是否为空
        Set<String> itemNumberSet = Sets.newHashSet();
        if(CollectionUtil.isNotEmpty(oppoPlantSendImeiPpsels)){
            for (OppoPlantSendImeiPpsel oppoPlantSendImeiPpsel : oppoPlantSendImeiPpsels) {
                if (CollectionUtil.isNotEmpty(lxAgentCodes) && lxAgentCodes.contains(oppoPlantSendImeiPpsel.getCompanyId())) {
                    if (StringUtils.isEmpty(oppoPlantSendImeiPpsel.getLxProductId())) {
                        itemNumberSet.add(oppoPlantSendImeiPpsel.getDlsProductId());
                    }
                } else {
                    if (CollectionUtil.isNotEmpty(agentCodes) && agentCodes.contains(oppoPlantSendImeiPpsel.getCompanyId())) {
                        if (StringUtils.isEmpty(oppoPlantSendImeiPpsel.getProductId())) {
                            itemNumberSet.add(oppoPlantSendImeiPpsel.getDlsProductId());
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
            List<String> imeList = CollectionUtil.extractToList(oppoPlantSendImeiPpsels, "imei");
            for(List<String> imes:CollectionUtil.splitList(imeList,1000)){
                productImeList.addAll(productImeRepository.findByEnabledIsTrueAndImeIn(imes));
            }
            List<String>  localImeList=CollectionUtil.extractToList(productImeList, "ime");
            for (OppoPlantSendImeiPpsel oppoPlantSendImeiPpsel : oppoPlantSendImeiPpsels) {
                String imei = oppoPlantSendImeiPpsel.getImei();
                if (!localImeList.contains(imei)) {
                    ProductIme productIme = new ProductIme();
                    if (CollectionUtil.isNotEmpty(lxAgentCodes) && lxAgentCodes.contains(oppoPlantSendImeiPpsel.getCompanyId())) {
                        productIme.setDepotId(lxDefaultStoreId);
                        productIme.setRetailShopId(lxDefaultStoreId);
                        productIme.setProductId(oppoPlantSendImeiPpsel.getLxProductId());
                    } else {
                        productIme.setDepotId(defaultStoreId);
                        productIme.setRetailShopId(defaultStoreId);
                        productIme.setProductId(oppoPlantSendImeiPpsel.getProductId());
                    }
                    if (StringUtils.isNotBlank(goodStoreProduct)) {
                        if (StringUtils.isNotBlank(oppoPlantSendImeiPpsel.getDlsProductId()) && oppoPlantSendImeiPpsel.getDlsProductId().startsWith(goodStoreProduct)) {
                            productIme.setDepotId(goodStoreId);
                            productIme.setRetailShopId(goodStoreId);
                        }
                    }
                    productIme.setIme(oppoPlantSendImeiPpsel.getImei());
                    productIme.setCreatedTime(oppoPlantSendImeiPpsel.getCreatedTime());
                    productIme.setIme2(oppoPlantSendImeiPpsel.getImei2());
                    productIme.setItemNumber(oppoPlantSendImeiPpsel.getDlsProductId());
                    productIme.setColorId(oppoPlantSendImeiPpsel.getColorId());
                    productIme.setMeid(oppoPlantSendImeiPpsel.getMeid());
                    productIme.setBoxIme(oppoPlantSendImeiPpsel.getRemark());
                    productIme.setInputType("工厂入库");
                    productIme.setBillId(oppoPlantSendImeiPpsel.getBillId());
                    productIme.setImeReverse(StringUtils.reverse(oppoPlantSendImeiPpsel.getImei()));
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
        List<OppoPlantProductItemelectronSel>  oppoPlantProductItemelectronSels=oppoClient.synProductItemelectronSel(date,agentCode);
        if(CollectionUtil.isNotEmpty(oppoPlantProductItemelectronSels)){
            Map<String,OppoPlantProductItemelectronSel> productItemelectronSelMap=Maps.newHashMap();
            for(OppoPlantProductItemelectronSel oppoPlantProductItemelectronSel:oppoPlantProductItemelectronSels){
                productItemelectronSelMap.put(oppoPlantProductItemelectronSel.getProductNo(),oppoPlantProductItemelectronSel);
            }
            List<String> productNoList= CollectionUtil.extractToList(oppoPlantProductItemelectronSels, "productNo");
            for(List<String> imes:CollectionUtil.splitList(productNoList,1000)){
                localProductImeList.addAll(productImeRepository.findByImeInAndRetailDate(imes));
            }
            for(ProductIme productIme:localProductImeList){
                productIme.setRetailDate(productItemelectronSelMap.get(productIme.getIme()).getDateTime());
            }
        }
        logger.info("localProductImeList=="+localProductImeList.toString());
        if(CollectionUtil.isNotEmpty(localProductImeList)){
            productImeRepository.save(localProductImeList);
        }
        return "串码同步成功，同步"+productImes.size()+"条串码,同步"+localProductImeList.size()+"条电子保卡";
    }
}
