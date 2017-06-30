package net.myspring.future.modules.third.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.myspring.common.constant.CharConstant;
import net.myspring.common.enums.CompanyConfigCodeEnum;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.basic.client.CompanyConfigClient;
import net.myspring.future.modules.crm.domain.ProductIme;
import net.myspring.future.modules.crm.repository.ProductImeRepository;
import net.myspring.future.modules.third.client.OppoClient;
import net.myspring.future.modules.third.domain.*;
import net.myspring.future.modules.third.repository.*;
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
import java.util.ArrayList;
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
    private CacheUtils cacheUtils;
    @Autowired
    private ProductImeRepository productImeRepository;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private CompanyConfigClient companyConfigClient;
    @Autowired
    private OppoCustomerAllotRepository oppoCustomerAllotRepository;
    @Autowired
    private OppoCustomerStockRepository oppoCustomerStockRepository;
    @Autowired
    private OppoCustomerImeiStockRepository oppoCustomerImeiStockRepository;
    @Autowired
    private OppoCustomerSaleRepository oppoCustomerSaleRepository;
    @Autowired
    OppoCustomerSaleImeiRepository oppoCustomerSaleImeiRepository;
    @Autowired
    private OppoCustomerSaleCountRepository oppoCustomerSaleCountRepository;
    @Autowired
    private OppoCustomerAfterSaleImeiRepository oppoCustomerAfterSaleImeiRepository;
    @Autowired
    private OppoCustomerDemoPhoneRepository oppoCustomerDemoPhoneRepository;

    protected Logger logger = LoggerFactory.getLogger(getClass());

    public String synIme(String date){
        if(StringUtils.isBlank(date)){
            date= LocalDateUtils.formatLocalDate(LocalDate.now(),"yyyy-MM-dd");
        }
        String agentCode= companyConfigClient.getValueByCode(CompanyConfigCodeEnum.FACTORY_AGENT_CODES.name()).replace("\"","");
        String lxAgentCode=companyConfigClient.getValueByCode(CompanyConfigCodeEnum.LX_FACTORY_AGENT_CODES.name()).replace("\"","");
        List<String> lxAgentCodes=Lists.newArrayList();
        if(StringUtils.isNotBlank(lxAgentCode)){
            lxAgentCodes=StringUtils.getSplitList(lxAgentCode,CharConstant.COMMA);
        }
        List<String> agentCodes=Lists.newArrayList();
        if(StringUtils.isNotBlank(agentCode)){
            agentCodes=StringUtils.getSplitList(agentCode,CharConstant.COMMA);
        }
        String goodStoreProduct = "";
        String companyName=companyConfigClient.getValueByCode(CompanyConfigCodeEnum.COMPANY_NAME.name()).replace("\"","");
        if(!"WZOPPO".equals(companyName)){
            goodStoreProduct = "7070";
        }
        String defaultStoreId = companyConfigClient.getValueByCode(CompanyConfigCodeEnum.DEFAULT_STORE_ID.name()).replace("\"","");
        String goodStoreId = companyConfigClient.getValueByCode(CompanyConfigCodeEnum.GOOD_STORE_ID.name()).replace("\"","");
        String lxDefaultStoreId=companyConfigClient.getValueByCode(CompanyConfigCodeEnum.LX_DEFAULT_STORE_ID.name()).replace("\"","");
        List<OppoPlantSendImeiPpsel> oppoPlantSendImeiPpsels=oppoClient.findSynImeList(date,agentCode);
        if(CollectionUtil.isEmpty(oppoPlantSendImeiPpsels)){
            return "同步成功,同步"+oppoPlantSendImeiPpsels.size()+"条串码";
        }
        List<String> imeList= CollectionUtil.extractToList(oppoPlantSendImeiPpsels, "imei");
        Map<String,ProductIme> productImeMap= Maps.newHashMap();
        List<ProductIme> productImeList=productImeRepository.findByImeList(imeList);
        if(CollectionUtil.isNotEmpty(productImeList)){
            for(ProductIme pro:productImeList){
                productImeMap.put(pro.getIme(),pro);
            }
        }
        Set<String> itemNumberSet= Sets.newHashSet();
        for(OppoPlantSendImeiPpsel oppoPlantSendImeiPpsel:oppoPlantSendImeiPpsels){
            if(CollectionUtil.isNotEmpty(lxAgentCodes)&&lxAgentCodes.contains(oppoPlantSendImeiPpsel.getCompanyId())){
                if(StringUtils.isBlank(oppoPlantSendImeiPpsel.getLxProductId())){
                    itemNumberSet.add(oppoPlantSendImeiPpsel.getDlsProductId());
                }
            }else{
                if(CollectionUtil.isNotEmpty(agentCodes)&&agentCodes.contains(oppoPlantSendImeiPpsel.getCompanyId())){
                    if(StringUtils.isBlank(oppoPlantSendImeiPpsel.getProductId())){
                        itemNumberSet.add(oppoPlantSendImeiPpsel.getDlsProductId());
                    }
                }
            }
        }
        if(CollectionUtil.isNotEmpty(itemNumberSet)){
            return "物料编码"+itemNumberSet.toString()+"没有绑定货品，请绑定后重新同步";
        }
        if(CollectionUtil.isNotEmpty(lxAgentCodes)&&StringUtils.isBlank(lxDefaultStoreId)){
            return "联信没有绑定默认仓库";
        }
        for(OppoPlantSendImeiPpsel oppoPlantSendImeiPpsel:oppoPlantSendImeiPpsels){
            String imei=oppoPlantSendImeiPpsel.getImei();
            if(!productImeMap.containsKey(imei)){
                ProductIme productIme = new ProductIme();
                if(CollectionUtil.isNotEmpty(lxAgentCodes)&&lxAgentCodes.contains(oppoPlantSendImeiPpsel.getCompanyId())){
                    productIme.setDepotId(lxDefaultStoreId);
                    productIme.setRetailShopId(lxDefaultStoreId);
                    productIme.setProductId(oppoPlantSendImeiPpsel.getLxProductId());
                }else{
                    productIme.setDepotId(defaultStoreId);
                    productIme.setRetailShopId(defaultStoreId);
                    productIme.setProductId(oppoPlantSendImeiPpsel.getProductId());
                }
                if(StringUtils.isNotBlank(goodStoreProduct)){
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
                productIme.setCompanyId(RequestUtils.getCompanyId());
                productIme.setBillId(oppoPlantSendImeiPpsel.getBillId());
                productIme.setImeReverse(StringUtils.reverse(oppoPlantSendImeiPpsel.getImei()));
                productImeMap.put(imei,productIme);
            }else{
                productImeMap.get(imei).setColorId(oppoPlantSendImeiPpsel.getColorId());
                productImeMap.get(imei).setItemNumber(oppoPlantSendImeiPpsel.getDlsProductId());
                if(CollectionUtil.isNotEmpty(lxAgentCodes)&&lxAgentCodes.contains(oppoPlantSendImeiPpsel.getCompanyId())){
                    productImeMap.get(imei).setProductId(oppoPlantSendImeiPpsel.getLxProductId());
                }else{
                    productImeMap.get(imei).setProductId(oppoPlantSendImeiPpsel.getProductId());
                }
            }
        }
        List<ProductIme> productImes=new ArrayList<ProductIme>(productImeMap.values());
        productImeRepository.save(productImes);

        //同步电子保卡
        List<OppoPlantProductItemelectronSel>  oppoPlantProductItemelectronSels=oppoClient.synProductItemelectronSel(date,agentCode);
        List<ProductIme> localProductImeList=Lists.newArrayList();
        if(CollectionUtil.isNotEmpty(oppoPlantProductItemelectronSels)){
            Map<String,OppoPlantProductItemelectronSel> productItemelectronSelMap=Maps.newHashMap();
            for(OppoPlantProductItemelectronSel oppoPlantProductItemelectronSel:oppoPlantProductItemelectronSels){
                productItemelectronSelMap.put(oppoPlantProductItemelectronSel.getProductNo(),oppoPlantProductItemelectronSel);
            }
            List<String> productNoList= CollectionUtil.extractToList(oppoPlantProductItemelectronSels, "productNo");
            localProductImeList=productImeRepository.findByImeList(productNoList);
            for(ProductIme productIme:localProductImeList){
                productIme.setRetailDate(productItemelectronSelMap.get(productIme.getIme()).getDateTime());
            }
        }
        if(CollectionUtil.isNotEmpty(localProductImeList)){
            productImeRepository.save(localProductImeList);
        }
        return "串码同步成功，同步"+productImes.size()+"条串码,同步"+localProductImeList.size()+"条电子保卡";
    }


    public List<OppoCustomerAllot> findOppoCustomerAllots(LocalDate dateStart, LocalDate dateEnd, String companyId){
        List<OppoCustomerAllot> oppoCustomerAllots=oppoCustomerAllotRepository.findAll(LocalDateUtils.format(dateStart),LocalDateUtils.format(dateEnd),companyId);
        return oppoCustomerAllots;
    }

    public List<OppoCustomerStock> findOppoCustomerStocks(LocalDate dateStart, LocalDate dateEnd, String companyId){
        List<OppoCustomerStock> oppoCustomerStocks=oppoCustomerStockRepository.findAll(LocalDateUtils.format(dateStart),LocalDateUtils.format(dateEnd),companyId);
        return oppoCustomerStocks;
    }

    public List<OppoCustomerImeiStock> findOppoCustomerImeiStocks(LocalDate dateStart, LocalDate dateEnd, String companyId){
        List<OppoCustomerImeiStock> oppoCustomerImeiStocks=oppoCustomerImeiStockRepository.findAll(LocalDateUtils.format(dateStart),LocalDateUtils.format(dateEnd),companyId);
        return oppoCustomerImeiStocks;
    }

    public List<OppoCustomerSale> findOppoCustomerSales(LocalDate dateStart, LocalDate dateEnd, String companyId){
        List<OppoCustomerSale> oppoCustomerSales=oppoCustomerSaleRepository.findAll(LocalDateUtils.format(dateStart),LocalDateUtils.format(dateEnd),companyId);
        return oppoCustomerSales;
    }

    public List<OppoCustomerSaleImei> findOppoCustomerSaleImeis(LocalDate dateStart, LocalDate dateEnd, String companyId){
        List<OppoCustomerSaleImei> oppoCustomerSaleImeis=oppoCustomerSaleImeiRepository.findAll(LocalDateUtils.format(dateStart),LocalDateUtils.format(dateEnd),companyId);
        return oppoCustomerSaleImeis;
    }

    public List<OppoCustomerSaleCount>  findOppoCustomerSaleCounts(LocalDate dateStart, LocalDate dateEnd, String companyId){
        List<OppoCustomerSaleCount> oppoCustomerSaleCounts=oppoCustomerSaleCountRepository.findAll(LocalDateUtils.format(dateStart),LocalDateUtils.format(dateEnd),companyId);
        return oppoCustomerSaleCounts;
    }

    public List<OppoCustomerAfterSaleImei> findOppoCustomerAfterSaleImeis(LocalDate dateStart, LocalDate dateEnd, String companyId){
        List<OppoCustomerAfterSaleImei> oppoCustomerAfterSaleImeis=oppoCustomerAfterSaleImeiRepository.findAll(LocalDateUtils.format(dateStart),LocalDateUtils.format(dateEnd),companyId);
        return oppoCustomerAfterSaleImeis;
    }

    public List<OppoCustomerDemoPhone> findOppoCustomerDemoPhones(LocalDate dateStart, LocalDate dateEnd, String companyId){
        List<OppoCustomerDemoPhone> oppoCustomerDemoPhones=oppoCustomerDemoPhoneRepository.findAll(LocalDateUtils.format(dateStart),LocalDateUtils.format(dateEnd),companyId);
        return oppoCustomerDemoPhones;
    }

}
