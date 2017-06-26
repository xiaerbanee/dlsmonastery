package net.myspring.future.modules.third.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.myspring.basic.common.util.CompanyConfigUtil;
import net.myspring.basic.modules.sys.dto.CompanyConfigCacheDto;
import net.myspring.common.constant.CharConstant;
import net.myspring.common.enums.CompanyConfigCodeEnum;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.crm.domain.ProductIme;
import net.myspring.future.modules.crm.repository.ProductImeRepository;
import net.myspring.future.modules.third.client.OppoClient;
import net.myspring.future.modules.third.domain.*;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.json.ObjectMapperUtils;
import net.myspring.util.time.LocalDateUtils;
import net.myspring.util.text.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.reflect.misc.ConstructorUtil;

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

    protected Logger logger = LoggerFactory.getLogger(getClass());

//    @Autowired
//    private OppoCustomerAllotRepository oppoCustomerAllotRepository;
//    @Autowired
//    private OppoCustomerStockRepository oppoCustomerStockRepository;
//    @Autowired
//    private OppoCustomerImeiStockRepository oppoCustomerImeiStockRepository;
//    @Autowired
//    private OppoCustomerSaleRepository oppoCustomerSaleRepository;
//    @Autowired
//    private OppoCustomerSaleImeiRepository oppoCustomerSaleImeiRepository;
//    @Autowired
//    private OppoCustomerSaleCountRepository oppoCustomerSaleCountRepository;
//    @Autowired
//    private OppoCustomerAfterSaleImeiRepository oppoCustomerAfterSaleImeiRepository;
//    @Autowired
//    private OppoCustomerDemoPhoneRepository oppoCustomerDemoPhoneRepository;
//
    public String synIme(String date){
        if(StringUtils.isBlank(date)){
            date= LocalDateUtils.formatLocalDate(LocalDate.now(),"yyyy-MM-dd");
        }
        String agentCode= CompanyConfigUtil.findByCode(redisTemplate, RequestUtils.getCompanyId(), CompanyConfigCodeEnum.FACTORY_AGENT_CODES.name()).getValue();
        if(StringUtils.isEmpty(agentCode)){
            agentCode="M13AMB,M13CMB,M13DMB,M13EMB,M13FMB,M13GMB,M13HMB,M13JMB,M13KMB";
        }
        List<String> lxAgentCodes=null;
        CompanyConfigCacheDto companyLxAgentcode = CompanyConfigUtil.findByCode( redisTemplate, RequestUtils.getCompanyId(), CompanyConfigCodeEnum.LX_FACTORY_AGENT_CODES.name());
        if(StringUtils.isNotBlank(companyLxAgentcode.getValue())){
            lxAgentCodes=StringUtils.getSplitList(companyLxAgentcode.getValue(),CharConstant.COMMA);
        }
        String goodStoreProduct = "";
        String lxDefaultStoreId = null;
        CompanyConfigCacheDto companyCacheDto= CompanyConfigUtil.findByCode( redisTemplate, RequestUtils.getCompanyId(), CompanyConfigCodeEnum.PRODUCT_NAME.name());
        if(companyCacheDto!=null&&!"WZOPPO".equals(companyCacheDto.getValue())){
            goodStoreProduct = "7070";
        }
        String defaultStoreId = CompanyConfigUtil.findByCode( redisTemplate, RequestUtils.getCompanyId(), CompanyConfigCodeEnum.DEFAULT_STORE_ID.name()).getValue();
        String goodStoreId ="";
        CompanyConfigCacheDto goodStoreIdCacheDto= CompanyConfigUtil.findByCode( redisTemplate, RequestUtils.getCompanyId(), CompanyConfigCodeEnum.GOOD_STORE_ID.name());
        if(goodStoreIdCacheDto!=null&&StringUtils.isNotBlank(goodStoreIdCacheDto.getValue())){
            goodStoreId=goodStoreIdCacheDto.getValue();
        }
        logger.info("agentCode=="+agentCode+"\tlxAgentCodes==="+lxAgentCodes);
        String imeStr=oppoClient.findSynImeList(date,agentCode);
        logger.info("imeStr=="+imeStr);
        if(StringUtils.isBlank(imeStr)){
            return "同步成功";
        }
        List<OppoPlantSendImeiPpsel> oppoPlantSendImeiPpsels=ObjectMapperUtils.readValue(imeStr, ArrayList.class);
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
                if(StringUtils.isBlank(oppoPlantSendImeiPpsel.getProductId())){
                    itemNumberSet.add(oppoPlantSendImeiPpsel.getDlsProductId());
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
                if(lxAgentCodes.contains(oppoPlantSendImeiPpsel.getCompanyId())){
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
                if(lxAgentCodes.contains(oppoPlantSendImeiPpsel.getCompanyId())){
                    productImeMap.get(imei).setProductId(oppoPlantSendImeiPpsel.getLxProductId());
                }else{
                    productImeMap.get(imei).setProductId(oppoPlantSendImeiPpsel.getProductId());
                }
            }
        }
        List<ProductIme> productImes=new ArrayList<ProductIme>(productImeMap.values());
        productImeRepository.save(productImes);
        return "串码同步成功";
    }
//
//
    public List<OppoCustomerAllot> findOppoCustomerAllots(LocalDate dateStart, LocalDate dateEnd, String companyId){
//        List<OppoCustomerAllot> oppoCustomerAllots=oppoCustomerAllotRepository.findAll(dateStart,dateEnd,companyId);
//        return oppoCustomerAllots;
        return null;
    }
//
    public List<OppoCustomerStock> findOppoCustomerStocks(LocalDate dateStart, LocalDate dateEnd, String companyId){
//        List<OppoCustomerStock> oppoCustomerStocks=oppoCustomerStockRepository.findAll(dateStart,dateEnd,companyId);
//        return oppoCustomerStocks;
        return null;
    }
//
    public List<OppoCustomerImeiStock> findOppoCustomerImeiStocks(LocalDate dateStart, LocalDate dateEnd, String companyId){
//        List<OppoCustomerImeiStock> oppoCustomerStocks=oppoCustomerImeiStockRepository.findAll(dateStart,dateEnd,companyId);
//        return oppoCustomerStocks;
        return null;
    }
//
    public List<OppoCustomerSale> findOppoCustomerSales(LocalDate dateStart, LocalDate dateEnd, String companyId){
//        List<OppoCustomerSale> oppoCustomerSales=oppoCustomerSaleRepository.findAll(dateStart,dateEnd,companyId);
//        return oppoCustomerSales;
        return null;
    }
//
    public List<OppoCustomerSaleImei> findOppoCustomerSaleImeis(LocalDate dateStart, LocalDate dateEnd, String companyId){
//        List<OppoCustomerSaleImei> oppoCustomerSaleImeis=oppoCustomerSaleImeiRepository.findAll(dateStart,dateEnd,companyId);
//        cacheUtils.initCacheInput(oppoCustomerSaleImeis);
//        return oppoCustomerSaleImeis;
        return null;
    }
//
    public List<OppoCustomerSaleCount>  findOppoCustomerSaleCounts(LocalDate dateStart, LocalDate dateEnd, String companyId){
//        List<OppoCustomerSaleCount> oppoCustomerSaleCounts=oppoCustomerSaleCountRepository.findAll(dateStart,dateEnd,companyId);
//        return oppoCustomerSaleCounts;
        return null;
    }
//
    public List<OppoCustomerAfterSaleImei> findOppoCustomerAfterSaleImeis(LocalDate dateStart, LocalDate dateEnd, String companyId){
//        List<OppoCustomerAfterSaleImei> oppoCustomerAfterSaleImeis=oppoCustomerAfterSaleImeiRepository.findAll(dateStart,dateEnd,companyId);
//        return oppoCustomerAfterSaleImeis;
        return null;
    }
//
    public List<OppoCustomerDemoPhone> findOppoCustomerDemoPhones(LocalDate dateStart, LocalDate dateEnd, String companyId){
//        List<OppoCustomerDemoPhone> oppoCustomerDemoPhones=oppoCustomerDemoPhoneRepository.findAll(dateStart,dateEnd,companyId);
//        return oppoCustomerDemoPhones;
        return null;
    }

}
