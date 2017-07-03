package net.myspring.future.modules.crm.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.basic.common.util.CompanyConfigUtil;
import net.myspring.common.enums.CompanyConfigCodeEnum;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.basic.domain.Product;
import net.myspring.future.modules.basic.repository.DepotRepository;
import net.myspring.future.modules.basic.repository.ProductRepository;
import net.myspring.future.modules.crm.domain.AfterSale;
import net.myspring.future.modules.crm.domain.AfterSaleDetail;
import net.myspring.future.modules.crm.domain.AfterSaleImeAllot;
import net.myspring.future.modules.crm.domain.ProductIme;
import net.myspring.future.modules.crm.dto.AfterSaleDto;
import net.myspring.future.modules.crm.dto.ProductImeDto;
import net.myspring.future.modules.crm.repository.AfterSaleImeAllotRepository;
import net.myspring.future.modules.crm.repository.AfterSaleRepository;
import net.myspring.future.modules.crm.repository.ProductImeRepository;
import net.myspring.future.modules.crm.web.query.AfterSaleQuery;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.text.IdUtils;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class AfterSaleService {

    @Autowired
    private AfterSaleRepository afterSaleRepository;
    @Autowired
    private DepotRepository depotRepository;
    @Autowired
    private ProductImeRepository productImeRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private AfterSaleImeAllotRepository afterSaleImeAllotRepository;
    @Autowired
    private CacheUtils cacheUtils;

    public Page<AfterSaleDto> findPage(Pageable pageable, AfterSaleQuery afterSaleQuery){
        Page<AfterSaleDto> afterSaleDtoPage=afterSaleRepository.findPage(pageable,afterSaleQuery);
        cacheUtils.initCacheInput(afterSaleDtoPage.getContent());
        return afterSaleDtoPage;
    }


    public List<ProductImeDto> findFormData(List<String> imeList) {
        List<ProductImeDto> productImeList = productImeRepository.findDtoListByImeList(imeList, RequestUtils.getCompanyId());
        return productImeList;
    }

    public Map<String, AfterSale> findAfterSale(List<String> imeList) {
        Map<String, AfterSale> map = Maps.newHashMap();
        List<AfterSale> afterSales = afterSaleRepository.findByBadProductImeIn(imeList);
        if(CollectionUtil.isNotEmpty(afterSales)){
            Map<String,ProductIme> productImeMap=productImeRepository.findMap(CollectionUtil.extractToList(afterSales,"badProductImeId"));
            for (AfterSale afterSale : afterSales) {
                map.put(productImeMap.get(afterSale.getBadProductImeId()).getIme(), afterSale);
            }
        }
        return map;
    }

    //单据录入
    public void save(List<List<String>> datas, LocalDate toStoreDate) {
        String companyId = RequestUtils.getCompanyId();
        Depot badStore = depotRepository.findOne(CompanyConfigUtil.findByCode(redisTemplate, companyId, CompanyConfigCodeEnum.BAD_STORE_ID.name()).getValue());
        List<String> imeList = Lists.newArrayList();
        List<String> depotNameList = Lists.newArrayList();
        for (List<String> row : datas) {
            listAddTrim(imeList, row.get(0));
            listAddTrim(imeList, row.get(3));
            listAddTrim(depotNameList, row.get(4));
        }
        List<ProductIme> productImeList = productImeRepository.findByEnabledIsTrueAndCompanyIdAndImeIn(companyId, imeList);
        List<Depot> depotList = depotRepository.findByNameList(depotNameList);
        Map<String, ProductIme> productImeMap = CollectionUtil.extractToMap(productImeList, "ime");
        Map<String, Depot> depotMap = CollectionUtil.extractToMap(depotList, "name");
        for (List<String> row : datas) {
            if (CollectionUtil.isNotEmpty(row)) {
                AfterSale afterSale = new AfterSale();
                ProductIme badProductIme = productImeMap.get(row.get(0).trim());
                ProductIme toAreaProductIme = productImeMap.get(row.get(3).trim());
                Depot toAreaDepot = depotMap.get(row.get(4).trim());
                for (int i = 0; i < row.size(); i++) {
                    String value = StringUtils.toString(row.get(i)).trim();
                    switch (i) {
                        case 0:
                            if (badProductIme != null) {
                                afterSale.setBadProductImeId(badProductIme.getId());
                                afterSale.setBadProductImeId(badProductIme.getId());
                                afterSale.setBadDepotId(badProductIme.getDepotId());
                            }
                            break;
                        case 3:
                            if (StringUtils.isNotBlank(value)) {
                                afterSale.setToAreaProductImeId(toAreaProductIme.getId());
                                afterSale.setToAreaProductImeId(toAreaProductIme.getId());
                            }
                            break;
                        case 4:
                            if (StringUtils.isNotBlank(value)) {
                                afterSale.setAreaDepotId(toAreaDepot.getId());
                                afterSale.setAreaDepotId(toAreaDepot.getId());
                            }
                            break;
                        case 5:
                            afterSale.setPackageStatus(value);
                            break;
                        case 6:
                            afterSale.setToStoreType(value);
                            break;
                        case 7:
                            afterSale.setMemory(value);
                            break;
                        case 8:
                            afterSale.setToStoreRemarks(value);
                            break;
                        default:
                            break;
                    }
                }
                String maxBusinessId = afterSaleRepository.findMaxBusinessId(LocalDate.now());
                afterSale.setBusinessId(IdUtils.getNextBusinessId(maxBusinessId));
                afterSale.setToStoreDate(toStoreDate);
                afterSaleRepository.save(afterSale);
                AfterSaleImeAllot afterSaleImeAllot = new AfterSaleImeAllot(afterSale.getId(), badProductIme.getId(), badProductIme.getDepotId(), badStore.getId(), "坏机录入");
                afterSaleImeAllotRepository.save(afterSaleImeAllot);
                //调拨到坏机库
                badProductIme.setDepotId(badStore.getId());
                productImeRepository.save(badProductIme);

                if (StringUtils.isNotBlank(afterSale.getToAreaProductImeId())) {
                    afterSaleImeAllot = new AfterSaleImeAllot(afterSale.getId(), toAreaProductIme.getId(), toAreaProductIme.getDepotId(), toAreaDepot.getId(), "替换机录入");
                    afterSaleImeAllotRepository.save(afterSaleImeAllot);
                    toAreaProductIme.setDepotId(afterSale.getAreaDepotId());
                    productImeRepository.save(toAreaProductIme);
                }
            }
        }
    }


    //单据修改
    @Transactional
    public void update(List<List<String>> datas) {
        String companyId = RequestUtils.getCompanyId();

        List<String> badImeList = Lists.newArrayList();
        List<String> imeList = Lists.newArrayList();
        List<String> depotNameList = Lists.newArrayList();
        List<String> productNameList = Lists.newArrayList();
        for (List<String> row : datas) {
            listAddTrim(badImeList, row.get(0).trim());
            listAddTrim(imeList, row.get(3).trim());
            listAddTrim(depotNameList, row.get(4).trim());
            listAddTrim(productNameList, row.get(4).trim());
        }
        Map<String, AfterSale> afterSaleMap = findAfterSale(badImeList);
        if (afterSaleMap.size() > 0) {
            String goodStoreId = CompanyConfigUtil.findByCode(redisTemplate, companyId, CompanyConfigCodeEnum.GOOD_STORE_ID.name()).getValue();
            List<ProductIme> productImeList = productImeRepository.findByEnabledIsTrueAndCompanyIdAndImeIn(companyId, imeList);
            List<Depot> depotList = depotRepository.findByNameList(depotNameList);
            List<Product> productList = productRepository.findByNameIn(productNameList);
            Map<String, Product> productMap = CollectionUtil.extractToMap(productList, "name");
            Map<String, Depot> depotMap = CollectionUtil.extractToMap(depotList, "name");
            Map<String, ProductIme> productImeMap = CollectionUtil.extractToMap(productImeList, "ime");
            Map<String, ProductIme> toAreaProductImeMap = productImeRepository.findMap(CollectionUtil.extractToList(afterSaleMap.values(), "toAreaProductImeId"));
            for (List<String> row : datas) {
                String badIme = StringUtils.toString(row.get(0)).trim();
                AfterSale afterSale = afterSaleMap.get(badIme);
                for (int i = 0; i < row.size(); i++) {
                    String value = StringUtils.toString(row.get(i)).trim();
                    switch (i) {
                        case 3:
                            //更改替换机
                            if (afterSale.getToAreaToFinance() == null || !afterSale.getToAreaToFinance()) {
                                ProductIme toAreaProductIme = productImeMap.get(value);
                                boolean isUpdate = false;
                                if (toAreaProductIme == null && StringUtils.isNotBlank(afterSale.getToAreaProductImeId())) {
                                    if (StringUtils.isNotBlank(afterSale.getToAreaProductImeId())) {
                                        isUpdate = true;
                                    }
                                } else {
                                    if (StringUtils.isNotBlank(afterSale.getToAreaProductImeId()) || !afterSale.getToAreaProductImeId().equals(toAreaProductIme.getId())) {
                                        isUpdate = true;
                                    }
                                }
                                if (isUpdate) {
                                    if (StringUtils.isNotBlank(afterSale.getToAreaProductImeId())) {
                                        AfterSaleImeAllot afterSaleImeAllot = new AfterSaleImeAllot(afterSale.getId(), afterSale.getToAreaProductImeId(), toAreaProductImeMap.get(afterSale.getToAreaProductImeId()).getDepotId(), goodStoreId, "替换机退回");
                                        afterSaleImeAllotRepository.save(afterSaleImeAllot);
                                        toAreaProductIme.setDepotId(goodStoreId);
                                        productImeRepository.save(toAreaProductIme);
                                    }
                                    if (toAreaProductIme != null) {
                                        AfterSaleImeAllot afterSaleImeAllot = new AfterSaleImeAllot(afterSale.getId(), toAreaProductIme.getId(), toAreaProductIme.getDepotId(), afterSale.getAreaDepotId(), "替换机录入");
                                        afterSaleImeAllotRepository.save(afterSaleImeAllot);
                                        toAreaProductIme.setDepotId(afterSale.getAreaDepotId());
                                        productImeRepository.save(toAreaProductIme);
                                    }
                                    afterSale.setToAreaProductImeId(toAreaProductIme.getId());
                                }
                            }
                            break;
                        case 4:
                            Depot areaDepot = depotMap.get(value);
                            afterSale.setAreaDepotId(areaDepot.getId());
                            break;
                        case 5:
                            afterSale.setPackageStatus(value);
                            break;
                        case 6:
                            afterSale.setToStoreType(value);
                            break;
                        case 7:
                            afterSale.setMemory(value);
                            break;
                        case 8:
                            //更改工厂返回
                            if (afterSale.getToAreaToFinance() == null || !afterSale.getFromCompanyToFinance() && StringUtils.isNotBlank(afterSale.getFromCompanyProductId())) {
                                Product fromCompanyProduct = productMap.get(value);
                                afterSale.setFromCompanyProductId(fromCompanyProduct.getId());
                            }
                            break;
                        case 9:
                            afterSale.setToStoreRemarks(value);
                            break;
                        default:
                            break;
                    }
                }
                afterSaleRepository.save(afterSale);
            }
        }
    }


    //坏机返厂
    @Transactional
    public void toCompany(List<String> badImes, LocalDate toCompanyDate, String toCompanyRemarks) {
        Map<String, AfterSale> afterSaleMap = findAfterSale(badImes);
        for (int i = 0; i < badImes.size(); i++) {
            String badIme = badImes.get(i);
            AfterSale afterSale = afterSaleMap.get(badIme);
            //坏机已录单，并且没有同步到金蝶
            if (afterSale != null && !afterSale.getToCompanyToFinance()) {
                afterSale.setToCompanyDate(toCompanyDate);
                afterSale.setToCompanyRemarks(toCompanyRemarks);
                afterSaleRepository.save(afterSale);
            }
        }
    }


    //工厂返回
    @Transactional
    public void fromCompany(List<List<String>> datas, LocalDate fromCompanyDate) {
        List<String> badImes = Lists.newArrayList();
        List<String> productNames = Lists.newArrayList();
        for (List<String> row : datas) {
            badImes.add(row.get(0));
            productNames.add(row.get(1));
        }
        List<Product> productList = productRepository.findByNameIn(productNames);
        Map<String, Product> productMap = CollectionUtil.extractToMap(productList, "name");
        Map<String, AfterSale> afterSaleMap = findAfterSale(badImes);
        for (int i = 0; i < badImes.size(); i++) {
            String badIme = badImes.get(i);
            AfterSale afterSale = afterSaleMap.get(badIme);
            //坏机已录单，并且没有同步到速达
            if (afterSale != null && !afterSale.getFromCompanyToFinance() && StringUtils.isNotBlank(productNames.get(i))) {
                Product fromCompanyProduct = productMap.get(productNames.get(i));
                afterSale.setFromCompanyProductId(fromCompanyProduct.getId());
                afterSale.setFromCompanyDate(fromCompanyDate);
                afterSaleRepository.save(afterSale);
            }
        }
    }


    public void delete(String id) {
        AfterSale afterSale = afterSaleRepository.findOne(id);
        if (!(afterSale.getFromAreaToFinance() || afterSale.getToAreaToFinance() || afterSale.getFromCompanyToFinance() || afterSale.getToCompanyToFinance())) {
            List<AfterSaleImeAllot> afterSaleImeAllots = afterSaleImeAllotRepository.findByAfterSaleId(afterSale.getId());
            if (CollectionUtil.isNotEmpty(afterSaleImeAllots)) {
                Map<String, ProductIme> productImeMap = productImeRepository.findMap(CollectionUtil.extractToList(afterSaleImeAllots, "productImeId"));
                for (AfterSaleImeAllot afterSaleImeAllot : afterSaleImeAllots) {
                    ProductIme productIme = productImeMap.get(afterSaleImeAllot.getProductImeId());
                    if (productIme.getDepotId().equals(afterSaleImeAllot.getToDepotId())) {
                        productIme.setDepotId(afterSaleImeAllot.getFromDepotId());
                        productImeRepository.save(productIme);
                    }
                    afterSaleImeAllotRepository.logicDelete(afterSaleImeAllot.getId());
                }
            }
            afterSaleRepository.logicDelete(afterSale.getId());
        }
    }

    private void listAddTrim(List<String> list, String item) {
        if (StringUtils.isNotBlank(item)) {
            list.add(item.trim());
        }
    }
}
