package net.myspring.future.modules.third.service;

import com.google.common.collect.Lists;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.crm.domain.ProductIme;
import net.myspring.future.modules.crm.repository.ProductImeRepository;
import net.myspring.future.modules.third.client.OppoClient;
import net.myspring.future.modules.third.domain.*;
import net.myspring.tool.modules.oppo.repository.*;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.json.ObjectMapperUtils;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
    private OppoCustomerAllotRepository oppoCustomerAllotRepository;
    @Autowired
    private OppoCustomerStockRepository oppoCustomerStockRepository;
    @Autowired
    private OppoCustomerImeiStockRepository oppoCustomerImeiStockRepository;
    @Autowired
    private OppoCustomerSaleRepository oppoCustomerSaleRepository;
    @Autowired
    private OppoCustomerSaleImeiRepository oppoCustomerSaleImeiRepository;
    @Autowired
    private OppoCustomerSaleCountRepository oppoCustomerSaleCountRepository;
    @Autowired
    private OppoCustomerAfterSaleImeiRepository oppoCustomerAfterSaleImeiRepository;
    @Autowired
    private OppoCustomerDemoPhoneRepository oppoCustomerDemoPhoneRepository;

    public String synIme(String date){
        String imeStr=oppoClient.findSynImeList("2017-05-16");
        List<OppoPlantSendImeiPpsel> oppoPlantSendImeiPpsels=ObjectMapperUtils.readValue(imeStr, ArrayList.class);
        List<String> imeList=CollectionUtil.extractToList(oppoPlantSendImeiPpsels, "imei");
        List<String> localImeList=CollectionUtil.extractToList( productImeRepository.findByImeList(imeList), "ime");
        List<String> uploadProductIds=Lists.newArrayList();
        List<ProductIme> productImes=Lists.newArrayList();
        String defaultStoreId = "1";
        String goodStoreId ="2";
        String lxDefaultStoreId = null;
        String goodStoreProduct = "";
        List<String> lxAgentCodes= Lists.newArrayList();
        for(OppoPlantSendImeiPpsel oppoPlantSendImeiPpsel:oppoPlantSendImeiPpsels){
            String imei=oppoPlantSendImeiPpsel.getImei();
            String productId=oppoPlantSendImeiPpsel.getProductId();
            String lxProductId=oppoPlantSendImeiPpsel.getLxProductId();
            if(!localImeList.contains(imei)){
                ProductIme productIme=new ProductIme();
                if(CollectionUtil.isNotEmpty(lxAgentCodes)&&lxAgentCodes.contains(oppoPlantSendImeiPpsel.getCompanyId())){
                    if(lxDefaultStoreId==null){
                        return  ("联信没有移动货品");
                    }else{
                        if(lxProductId==null){
                            return ("物料编码"+oppoPlantSendImeiPpsel.getDlsProductId()+"没有绑定联信货品");
                        }else{
                            productIme.setDepotId(lxDefaultStoreId);
                            productIme.setRetailShopId(lxDefaultStoreId);
                            productIme.setProductId(lxProductId);
                            uploadProductIds.add(lxProductId);
                        }
                    }
                }else{
                    if(productId==null){
                        return ("物料编码"+oppoPlantSendImeiPpsel.getDlsProductId()+"没有绑定移动货品");
                    }else{
                        productIme.setDepotId(defaultStoreId);
                        productIme.setRetailShopId(defaultStoreId);
                        productIme.setProductId(productId);
                        uploadProductIds.add(productId);
                    }
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
                productIme.setCompanyId(RequestUtils.getCompanyId());
                productIme.setBillId(oppoPlantSendImeiPpsel.getBillId());
                productIme.setImeReverse(StringUtils.reverse(oppoPlantSendImeiPpsel.getImei()));
                productImes.add(productIme);
            }
        }
//        if(Collections3.isNotEmpty(productImes)){
//            productImeMapper.batchSave(productImes);
//        }
//        if(Collections3.isNotEmpty(updateId)){
//            commonMapper.updateProduct(updateId);
//        }
        return "串码同步成功";
    }


    public List<OppoCustomerAllot> findOppoCustomerAllots(LocalDate dateStart, LocalDate dateEnd, String companyId){
        List<OppoCustomerAllot> oppoCustomerAllots=oppoCustomerAllotRepository.findAll(dateStart,dateEnd,companyId);
        return oppoCustomerAllots;
    }

    public List<OppoCustomerStock> findOppoCustomerStocks(LocalDate dateStart,LocalDate dateEnd,String companyId){
        List<OppoCustomerStock> oppoCustomerStocks=oppoCustomerStockRepository.findAll(dateStart,dateEnd,companyId);
        return oppoCustomerStocks;
    }

    public List<OppoCustomerImeiStock> findOppoCustomerImeiStocks(LocalDate dateStart, LocalDate dateEnd, String companyId){
        List<OppoCustomerImeiStock> oppoCustomerStocks=oppoCustomerImeiStockRepository.findAll(dateStart,dateEnd,companyId);
        return oppoCustomerStocks;
    }

    public List<OppoCustomerSale> findOppoCustomerSales(LocalDate dateStart, LocalDate dateEnd, String companyId){
        List<OppoCustomerSale> oppoCustomerSales=oppoCustomerSaleRepository.findAll(dateStart,dateEnd,companyId);
        return oppoCustomerSales;
    }

    public List<OppoCustomerSaleImei> findOppoCustomerSaleImeis(LocalDate dateStart, LocalDate dateEnd, String companyId){
        List<OppoCustomerSaleImei> oppoCustomerSaleImeis=oppoCustomerSaleImeiRepository.findAll(dateStart,dateEnd,companyId);
        cacheUtils.initCacheInput(oppoCustomerSaleImeis);
        return oppoCustomerSaleImeis;
    }

    public List<OppoCustomerSaleCount>  findOppoCustomerSaleCounts(LocalDate dateStart, LocalDate dateEnd, String companyId){
        List<OppoCustomerSaleCount> oppoCustomerSaleCounts=oppoCustomerSaleCountRepository.findAll(dateStart,dateEnd,companyId);
        return oppoCustomerSaleCounts;
    }

    public List<OppoCustomerAfterSaleImei> findOppoCustomerAfterSaleImeis(LocalDate dateStart, LocalDate dateEnd, String companyId){
        List<OppoCustomerAfterSaleImei> oppoCustomerAfterSaleImeis=oppoCustomerAfterSaleImeiRepository.findAll(dateStart,dateEnd,companyId);
        return oppoCustomerAfterSaleImeis;
    }

    public List<OppoCustomerDemoPhone> findOppoCustomerDemoPhones(LocalDate dateStart, LocalDate dateEnd, String companyId){
        List<OppoCustomerDemoPhone> oppoCustomerDemoPhones=oppoCustomerDemoPhoneRepository.findAll(dateStart,dateEnd,companyId);
        return oppoCustomerDemoPhones;
    }

}
