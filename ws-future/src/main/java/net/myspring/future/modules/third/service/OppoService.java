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
import net.myspring.future.modules.third.client.OppoClient;
import net.myspring.future.modules.third.dto.OppoPlantProductItemelectronSel;
import net.myspring.future.modules.third.dto.OppoPlantSendImeiPpsel;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.time.LocalDateUtils;
import net.myspring.util.text.StringUtils;
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
@Transactional
public class OppoService {

    @Autowired
    private OppoClient oppoClient;
    @Autowired
    private ProductImeRepository productImeRepository;
    @Autowired
    private RedisTemplate redisTemplate;


    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Transactional
    public String pullFactoryData(String date) {
        String companyName = DbContextHolder.get().getCompanyName();
        if (StringUtils.isBlank(date)) {
            date = LocalDateUtils.formatLocalDate(LocalDate.now(), "yyyy-MM-dd");
        }
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
        if (!CompanyNameEnum.WZOPPO.name().equals(companyName)) {
            goodStoreProduct = "7070";
        }
        String defaultStoreId = CompanyConfigUtil.findByCode(redisTemplate,CompanyConfigCodeEnum.DEFAULT_STORE_ID.name()).getValue();
        String goodStoreId = CompanyConfigUtil.findByCode(redisTemplate,CompanyConfigCodeEnum.GOOD_STORE_ID.name()).getValue();
        String lxDefaultStoreId = CompanyConfigUtil.findByCode(redisTemplate,CompanyConfigCodeEnum.LX_DEFAULT_STORE_ID.name()).getValue();
        List<OppoPlantSendImeiPpsel> oppoPlantSendImeiPpsels = oppoClient.getSendImeList(companyName,date, agentCode);
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
        List<OppoPlantProductItemelectronSel>  oppoPlantProductItemelectronSels=oppoClient.getItemelectronSelList(companyName,date,agentCode);
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
        if(CollectionUtil.isNotEmpty(localProductImeList)){
            productImeRepository.save(localProductImeList);
        }
        if (productImes.size() == 0 && localProductImeList.size() == 0){
            return "串码已同步";
        }
        return "串码同步成功，同步"+productImes.size()+"条串码,同步"+localProductImeList.size()+"条电子保卡";
    }
}
