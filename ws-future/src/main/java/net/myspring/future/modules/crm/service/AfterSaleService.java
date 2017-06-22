package net.myspring.future.modules.crm.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.future.common.enums.AfterSaleDetailTypeEnum;
import net.myspring.future.common.enums.AfterSaleTypeEnum;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.basic.domain.Product;
import net.myspring.future.modules.basic.repository.DepotRepository;
import net.myspring.future.modules.basic.repository.ProductRepository;
import net.myspring.future.modules.crm.domain.AfterSale;
import net.myspring.future.modules.crm.domain.AfterSaleDetail;
import net.myspring.future.modules.crm.domain.AfterSaleFlee;
import net.myspring.future.modules.crm.domain.ProductIme;
import net.myspring.future.modules.crm.dto.*;
import net.myspring.future.modules.crm.repository.AfterSaleDetailRepository;
import net.myspring.future.modules.crm.repository.AfterSaleFleeRepository;
import net.myspring.future.modules.crm.repository.AfterSaleRepository;
import net.myspring.future.modules.crm.repository.ProductImeRepository;
import net.myspring.future.modules.crm.web.form.AfterSaleToCompanyForm;
import net.myspring.future.modules.crm.web.query.AfterSaleQuery;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.reflect.ReflectionUtil;
import net.myspring.util.text.StringUtils;
import org.aspectj.lang.annotation.After;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
    private AfterSaleDetailRepository afterSaleDetailRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private AfterSaleFleeRepository afterSaleFleeRepository;
    @Autowired
    private CacheUtils cacheUtils;

    public List<AfterSale> findByBadProductImeIdList(List<String> badProductImeIdList){
        return afterSaleRepository.findByBadProductImeIdIn(badProductImeIdList);
    }

    public List<AfterSaleDto> findDtoByImeListAndType(List<String> imeList, String type) {
        List<AfterSaleDto> afterSaleDtoList=Lists.newArrayList();
        List<AfterSaleDto> afterSales = afterSaleRepository.findDtoByBadProductImeInAndType(imeList, type);
        if (CollectionUtil.isNotEmpty(afterSales)) {
            List<AfterSaleDetailDto>afterSaleDetailList  = afterSaleDetailRepository.findDtoByAfterSaleIdInAndType(CollectionUtil.extractToList(afterSales, "id"), AfterSaleDetailTypeEnum.总部录入.name());
            Map<String, AfterSaleDto> afterSaleMap = CollectionUtil.extractToMap(afterSales, "id");
            for (AfterSaleDetailDto detail : afterSaleDetailList) {
                AfterSaleDto afterSale = afterSaleMap.get(detail.getAfterSaleId());
                ReflectionUtil.copyProperties(detail,afterSale);
                afterSaleDtoList.add(afterSale);
            }
        }
        return afterSales;
    }

    public List<AfterSaleInputDto> inputUpdateData(List<String> imeList, String type, String inputType) {
        List<AfterSaleInputDto> afterSaleInputList = Lists.newArrayList();
        Map<String, AfterSaleFlee> afterSaleFleeMap = Maps.newHashMap();
        List<AfterSaleDto> afterSaleList = afterSaleRepository.findDtoByBadProductImeInAndType(imeList, type);
        if (CollectionUtil.isNotEmpty(afterSaleList)) {
            List<AfterSaleDetailDto> afterSaleDetailList = afterSaleDetailRepository.findDtoByAfterSaleIdInAndType(CollectionUtil.extractToList(afterSaleList, "id"), inputType);
            Map<String, AfterSaleDto> afterSaleMap = CollectionUtil.extractToMap(afterSaleList, "id");
            if (AfterSaleTypeEnum.窜货机.name().equals(type)) {
                List<AfterSaleFlee> afterSaleFleeList = afterSaleFleeRepository.findByEnabledIsTrueAndImeIn(imeList);
                afterSaleFleeMap = CollectionUtil.extractToMap(afterSaleFleeList, "afterSaleId");
            }
            for (AfterSaleDetailDto detail : afterSaleDetailList) {
                AfterSaleInputDto afterSaleInputDto = new AfterSaleInputDto();
                AfterSaleDto afterSale = afterSaleMap.get(detail.getAfterSaleId());
                ReflectionUtil.copyProperties(afterSale, afterSaleInputDto);
                if (AfterSaleTypeEnum.窜货机.name().equals(type)) {
                    AfterSaleFlee afterSaleFlee = afterSaleFleeMap.get(afterSale.getId());
                    ReflectionUtil.copyProperties(afterSaleFlee, afterSaleInputDto);
                    afterSaleInputDto.setBadProductIme(afterSaleFlee.getIme());
                }
                ReflectionUtil.copyProperties(detail, afterSaleInputDto);
                afterSaleInputList.add(afterSaleInputDto);
            }
        }
        return afterSaleInputList;
    }

    public List<AfterSaleCompanyDto> getFromCompanyData(AfterSaleQuery afterSaleQuery) {
        List<AfterSaleCompanyDto> afterSaleCompanyList = Lists.newArrayList();
        afterSaleQuery.setInputType(AfterSaleDetailTypeEnum.工厂录入.name());
        List<AfterSaleDto> afterSaleList = afterSaleRepository.findFilter(afterSaleQuery);
        List<AfterSaleDetailDto> afterSaleDetailList = afterSaleDetailRepository.findDtoByAfterSaleIdInAndType(CollectionUtil.extractToList(afterSaleList, "id"), AfterSaleDetailTypeEnum.工厂录入.name());
        Map<String, AfterSaleDetailDto> afterSaleDetailMap = CollectionUtil.extractToMap(afterSaleDetailList, "afterSaleId");
        for (AfterSaleDto afterSale : afterSaleList) {
            AfterSaleCompanyDto afterSaleCompanyDto = BeanUtil.map(afterSale, AfterSaleCompanyDto.class);
            AfterSaleDetailDto afterSaleDetail = afterSaleDetailMap.get(afterSale.getId());
            ReflectionUtil.copyProperties(afterSaleDetail,afterSaleCompanyDto);
            if(AfterSaleTypeEnum.窜货机.name().equals(afterSaleQuery.getType())){
                afterSaleCompanyDto.setBadProductIme(afterSale.getIme());
            }
            afterSaleCompanyList.add(afterSaleCompanyDto);
        }
        return afterSaleCompanyList;
    }

    //地区录入
    public void save(List<List<String>> datas, String type, String action) {
        List<String> imeList = Lists.newArrayList();
        List<String> productNameList = Lists.newArrayList();
        List<String> depotNameList = Lists.newArrayList();
        for (List<String> row : datas) {
            listAddTrim(imeList, StringUtils.toString(row.get(0)).trim());
            listAddTrim(imeList, StringUtils.toString(row.get(8)).trim());
            listAddTrim(productNameList, StringUtils.toString(row.get(1)).trim());
            listAddTrim(productNameList, StringUtils.toString(row.get(9)).trim());
            listAddTrim(depotNameList, StringUtils.toString(row.get(2)).trim());
            listAddTrim(depotNameList, StringUtils.toString(row.get(6)).trim());
            listAddTrim(depotNameList, StringUtils.toString(row.get(7)).trim());
        }
        List<ProductIme> productImeList = productImeRepository.findByImeList(imeList);
        List<Product> productList = productRepository.findByNameIn(productNameList);
        List<Depot> depotList = depotRepository.findByEnabledIsTrueAndNameIn(depotNameList);
        Map<String, ProductIme> productImeMap = CollectionUtil.extractToMap(productImeList, "ime");
        Map<String, Product> productMap = CollectionUtil.extractToMap(productList, "name");
        Map<String, Depot> depotMap = CollectionUtil.extractToMap(depotList, "name");
        Map<String, AfterSale> afterSaleMap = Maps.newHashMap();
        Map<String, AfterSaleFlee> afterSaleFleeMap = Maps.newHashMap();
        Map<String, AfterSaleDetail> afterSaleDetailMap = Maps.newHashMap();
        if ("update".equals(action)) {
            List<AfterSale> afterSaleList = Lists.newArrayList();
            if (AfterSaleTypeEnum.窜货机.name().equals(type)) {
                List<AfterSaleFlee> afterSaleFleeList = afterSaleFleeRepository.findByEnabledIsTrueAndImeIn(imeList);
                afterSaleFleeMap = CollectionUtil.extractToMap(afterSaleFleeList, "ime");
                afterSaleList = afterSaleRepository.findAll(CollectionUtil.extractToList(afterSaleFleeList, "afterSaleId"));
                afterSaleMap = CollectionUtil.extractToMap(afterSaleList, "id");
            } else {
                afterSaleList = afterSaleRepository.findByBadProductImeIn(imeList);
                afterSaleMap = CollectionUtil.extractToMap(afterSaleList, "badProductImeId");
            }
            List<AfterSaleDetail> afterSaleDetailList = afterSaleDetailRepository.findByAfterSaleIdInAndType(CollectionUtil.extractToList(afterSaleList, "id"), AfterSaleDetailTypeEnum.地区录入.name());
            afterSaleDetailMap = CollectionUtil.extractToMap(afterSaleDetailList, "afterSaleId");
        }
        for (List<String> row : datas) {
            AfterSale afterSale = new AfterSale();
            AfterSaleFlee afterSaleFlee = new AfterSaleFlee();
            AfterSaleDetail afterSaleDetail = new AfterSaleDetail();
            if ("update".equals(action)) {
                if (AfterSaleTypeEnum.窜货机.name().equals(type)) {
                    afterSaleFlee = afterSaleFleeMap.get(StringUtils.toString(row.get(0)).trim());
                    afterSale = afterSaleMap.get(afterSaleFlee.getAfterSaleId());
                } else {
                    ProductIme productIme = productImeMap.get(StringUtils.toString(row.get(0)).trim());
                    afterSale = afterSaleMap.get(productIme.getId());
                }
                afterSaleDetail = afterSaleDetailMap.get(afterSale.getId());
            }
            afterSale.setType(type);
            afterSaleDetail.setInputDate(LocalDate.now());
            afterSaleDetail.setType(AfterSaleDetailTypeEnum.地区录入.name());
            String remarks = StringUtils.toString(row.get(row.size() - 1)).trim();
            afterSaleDetail.setRemarks(remarks);
            for (int i = 0; i < row.size()-1; i++) {
                String value = StringUtils.toString(row.get(i)).trim();
                switch (i) {
                    case 0:
                        if (StringUtils.isNotBlank(value)) {
                            if (AfterSaleTypeEnum.窜货机.name().equals(type)) {
                                afterSaleFlee.setIme(value);
                            } else {
                                ProductIme productIme = productImeMap.get(StringUtils.toString(row.get(0)).trim());
                                afterSale.setBadProductImeId(productIme.getId());
                                afterSale.setBadProductId(productIme.getProductId());
                                afterSale.setBadDepotId(productIme.getDepotId());
                            }
                        }
                        break;
                    case 1:
                        if (StringUtils.isNotBlank(value)) {
                            Product product = productMap.get(value);
                            afterSale.setBadProductId(product.getId());
                        }
                        break;
                    case 2:
                        if (StringUtils.isNotBlank(value)) {
                            Depot badDepot = depotMap.get(value);
                            afterSale.setBadDepotId(badDepot.getId());
                            if (AfterSaleTypeEnum.窜货机.name().equals(type)) {
                                afterSaleFlee.setFleeShopName(badDepot.getName());
                            }
                        }
                        break;
                    case 3:
                        afterSale.setBadType(value);
                        break;
                    case 4:
                        afterSale.setPackageStatus(value);
                        break;
                    case 5:
                        afterSale.setMemory(value);
                        break;
                    case 6:
                        if (StringUtils.isNotBlank(value)) {
                            Depot fromDepot = depotMap.get(value);
                            afterSaleDetail.setFromDepotId(fromDepot.getId());
                        }
                        break;
                    case 7:
                        if (StringUtils.isNotBlank(value)) {
                            Depot toDepot = depotMap.get(value);
                            afterSaleDetail.setToDepotId(toDepot.getId());
                        }
                        break;
                    case 8:
                        if (StringUtils.isNotBlank(value)) {
                            ProductIme replaceProductIme = productImeMap.get(value);
                            afterSaleDetail.setReplaceProductImeId(replaceProductIme.getId());
                            afterSaleDetail.setReplaceProductId(replaceProductIme.getProductId());
                        }
                        break;
                    case 9:
                        if (StringUtils.isNotBlank(value)) {
                            Product product = productMap.get(value);
                            afterSaleDetail.setReplaceProductId(product.getId());
                        }
                        break;
                    case 10:
                        if (StringUtils.isNotBlank(value)) {
                            afterSaleDetail.setReplaceAmount(new BigDecimal(value));
                        }
                        break;
                    case 11:
                        afterSaleFlee.setContact(value);
                        break;
                    case 12:
                        afterSaleFlee.setMobilePhone(value);
                        break;
                    case 13:
                        afterSaleFlee.setAddress(value);
                        break;
                    case 14:
                        afterSaleFlee.setBuyAmount(new BigDecimal(value));
                        break;
                    default:
                        break;
                }
            }
            afterSaleRepository.save(afterSale);
            afterSaleDetail.setAfterSaleId(afterSale.getId());
            afterSaleDetailRepository.save(afterSaleDetail);
            if (AfterSaleTypeEnum.窜货机.name().equals(type)) {
                afterSaleFlee.setAfterSaleId(afterSale.getId());
                afterSaleFleeRepository.save(afterSaleFlee);
            }
        }
    }

    //总部录入
    public void saveHead(List<List<String>> datas, String type, String action) {
        List<String> imeList = Lists.newArrayList();
        List<String> productNameList = Lists.newArrayList();
        List<String> depotNameList = Lists.newArrayList();
        for (List<String> row : datas) {
            listAddTrim(imeList, StringUtils.toString(row.get(0)).trim());
            listAddTrim(imeList, StringUtils.toString(row.get(8)).trim());
            listAddTrim(productNameList, StringUtils.toString(row.get(1)).trim());
            listAddTrim(productNameList, StringUtils.toString(row.get(9)).trim());
            listAddTrim(depotNameList, StringUtils.toString(row.get(2)).trim());
            listAddTrim(depotNameList, StringUtils.toString(row.get(6)).trim());
            listAddTrim(depotNameList, StringUtils.toString(row.get(7)).trim());
        }
        List<ProductIme> productImeList = productImeRepository.findByImeList(imeList);
        List<Product> productList = productRepository.findByNameIn(productNameList);
        List<Depot> depotList = depotRepository.findByEnabledIsTrueAndNameIn(depotNameList);
        Map<String, ProductIme> productImeMap = CollectionUtil.extractToMap(productImeList, "ime");
        Map<String, Product> productMap = CollectionUtil.extractToMap(productList, "name");
        Map<String, Depot> depotMap = CollectionUtil.extractToMap(depotList, "name");
        Map<String, AfterSale> afterSaleMap = Maps.newHashMap();
        Map<String, AfterSaleFlee> afterSaleFleeMap = Maps.newHashMap();
        Map<String, AfterSaleDetail> afterSaleDetailMap = Maps.newHashMap();
        List<AfterSale> afterSaleList = Lists.newArrayList();
        if (AfterSaleTypeEnum.窜货机.name().equals(type)) {
            List<AfterSaleFlee> afterSaleFleeList = afterSaleFleeRepository.findByEnabledIsTrueAndImeIn(imeList);
            afterSaleFleeMap = CollectionUtil.extractToMap(afterSaleFleeList, "ime");
            afterSaleList = afterSaleRepository.findAll(CollectionUtil.extractToList(afterSaleFleeList, "afterSaleId"));
            afterSaleMap = CollectionUtil.extractToMap(afterSaleList, "id");
        } else {
            afterSaleList = afterSaleRepository.findByBadProductImeIn(imeList);
            afterSaleMap = CollectionUtil.extractToMap(afterSaleList, "badProductImeId");
        }
        if ("update".equals(action)) {
            List<AfterSaleDetail> afterSaleDetailList = afterSaleDetailRepository.findByAfterSaleIdInAndType(CollectionUtil.extractToList(afterSaleList, "id"), AfterSaleDetailTypeEnum.总部录入.name());
            afterSaleDetailMap = CollectionUtil.extractToMap(afterSaleDetailList, "afterSaleId");
        }
        for (List<String> row : datas) {
            AfterSaleFlee afterSaleFlee = afterSaleFleeMap.get(StringUtils.toString(row.get(0)).trim());
            AfterSale afterSale;
            if (AfterSaleTypeEnum.窜货机.name().equals(type)) {
                if (afterSaleFlee != null) {
                    afterSale = afterSaleMap.get(afterSaleFlee.getAfterSaleId());
                }else {
                    afterSale=new AfterSale();
                    afterSaleFlee=new AfterSaleFlee();
                }
            } else {
                ProductIme productIme = productImeMap.get(StringUtils.toString(row.get(0)).trim());
                afterSale = afterSaleMap.get(productIme.getId());
                if (afterSale == null) {
                    afterSale = new AfterSale();
                }
            }
            AfterSaleDetail afterSaleDetail = afterSaleDetailMap.get(afterSale.getId());
            if (afterSaleDetail == null) {
                afterSaleDetail = new AfterSaleDetail();
            }
            afterSale.setType(type);
            afterSaleDetail.setInputDate(LocalDate.now());
            afterSaleDetail.setType(AfterSaleDetailTypeEnum.总部录入.name());
            String remarks = StringUtils.toString(row.get(row.size() - 1)).trim();
            afterSaleDetail.setRemarks(remarks);
            for (int i = 0; i < row.size()-1; i++) {
                String value = StringUtils.toString(row.get(i)).trim();
                switch (i) {
                    case 0:
                        if (StringUtils.isNotBlank(value)) {
                            if (AfterSaleTypeEnum.窜货机.name().equals(type)) {
                                afterSaleFlee.setIme(value);
                            } else {
                                ProductIme productIme = productImeMap.get(StringUtils.toString(row.get(0)).trim());
                                afterSale.setBadProductImeId(productIme.getId());
                                afterSale.setBadProductId(productIme.getProductId());
                                afterSale.setBadDepotId(productIme.getDepotId());
                            }
                        }
                        break;
                    case 1:
                        if (StringUtils.isNotBlank(value)) {
                            Product product = productMap.get(value);
                            afterSale.setBadProductId(product.getId());
                        }
                        break;
                    case 2:
                        if (StringUtils.isNotBlank(value)) {
                            Depot badDepot = depotMap.get(value);
                            afterSale.setBadDepotId(badDepot.getId());
                            if (AfterSaleTypeEnum.窜货机.name().equals(type)) {
                                afterSaleFlee.setFleeShopName(badDepot.getName());
                            }
                        }
                        break;
                    case 3:
                        afterSale.setBadType(value);
                        break;
                    case 4:
                        afterSale.setPackageStatus(value);
                        break;
                    case 5:
                        afterSale.setMemory(value);
                        break;
                    case 6:
                        if (StringUtils.isNotBlank(value)) {
                            Depot fromDepot = depotMap.get(value);
                            afterSaleDetail.setFromDepotId(fromDepot.getId());
                        }
                        break;
                    case 7:
                        if (StringUtils.isNotBlank(value)) {
                            Depot toDepot = depotMap.get(value);
                            afterSaleDetail.setToDepotId(toDepot.getId());
                        }
                        break;
                    case 8:
                        if (StringUtils.isNotBlank(value)) {
                            ProductIme replaceProductIme = productImeMap.get(value);
                            afterSaleDetail.setReplaceProductImeId(replaceProductIme.getId());
                            afterSaleDetail.setReplaceProductId(replaceProductIme.getProductId());
                        }
                        break;
                    case 9:
                        if (StringUtils.isNotBlank(value)) {
                            Product product = productMap.get(value);
                            afterSaleDetail.setReplaceProductId(product.getId());
                        }
                        break;
                    case 10:
                        if (StringUtils.isNotBlank(value)) {
                            afterSaleDetail.setReplaceAmount(new BigDecimal(value));
                        }
                        break;
                    case 11:
                        afterSaleFlee.setContact(value);
                        break;
                    case 12:
                        afterSaleFlee.setMobilePhone(value);
                        break;
                    case 13:
                        afterSaleFlee.setAddress(value);
                        break;
                    case 14:
                        afterSaleFlee.setBuyAmount(new BigDecimal(value));
                        break;
                    default:
                        break;
                }
            }
            afterSaleRepository.save(afterSale);
            afterSaleDetail.setAfterSaleId(afterSale.getId());
            afterSaleDetailRepository.save(afterSaleDetail);
            if (AfterSaleTypeEnum.窜货机.name().equals(type)) {
                afterSaleFlee.setAfterSaleId(afterSale.getId());
                afterSaleFleeRepository.save(afterSaleFlee);
            }
        }
    }


    //坏机返厂
    @Transactional
    public void toCompany(AfterSaleToCompanyForm afterSaleToCompanyForm) {
        List<AfterSaleDto> afterSaleList = afterSaleRepository.findDtoByBadProductImeInAndType(afterSaleToCompanyForm.getImeList(),afterSaleToCompanyForm.getType());
        List<AfterSaleDetail> afterSaleDetails = afterSaleDetailRepository.findByAfterSaleIdIn(CollectionUtil.extractToList(afterSaleList, "id"));
        Map<String,AfterSaleDetail> afterSaleDetailMap=CollectionUtil.extractToMap(afterSaleDetails,"afterSaleId");
        List<AfterSaleDetail> afterSaleDetailList = Lists.newArrayList();
        for (AfterSaleDto afterSale : afterSaleList) {
            AfterSaleDetail detail = afterSaleDetailMap.get(afterSale.getId());
            AfterSaleDetail afterSaleDetail = BeanUtil.map(detail, AfterSaleDetail.class);
            afterSaleDetail.setType(AfterSaleDetailTypeEnum.工厂录入.name());
            afterSaleDetail.setInputDate(afterSaleToCompanyForm.getToCompanyDate());
            afterSaleDetail.setRemarks(afterSaleToCompanyForm.getToCompanyRemarks());
            afterSaleDetail.setId(null);
            afterSaleDetailList.add(afterSaleDetail);
        }
        if (CollectionUtil.isNotEmpty(afterSaleDetailList)) {
            afterSaleDetailRepository.save(afterSaleDetailList);
        }
    }


    //工厂返回
    @Transactional
    public void fromCompany(List<List<String>> datas, LocalDate fromCompanyDate) {
        List<String> imeList = Lists.newArrayList();
        List<String> productNameList = Lists.newArrayList();
        for (List<String> row : datas) {
            listAddTrim(imeList, row.get(5));
            listAddTrim(imeList, row.get(2));
            listAddTrim(productNameList, row.get(1));
        }
        List<AfterSale> afterSaleList = afterSaleRepository.findByBadProductImeIn(imeList);
        List<ProductIme> productImeList = productImeRepository.findByImeList(imeList);
        List<AfterSaleDetail> afterSaleDetailList = afterSaleDetailRepository.findByAfterSaleIdInAndType(CollectionUtil.extractToList(afterSaleList, "id"), "工厂录入");
        List<Product> productList = productRepository.findByNameIn(productNameList);
        Map<String, AfterSale> afterSaleMap = CollectionUtil.extractToMap(afterSaleList, "badProductImeId");
        Map<String, ProductIme> productImeMap = CollectionUtil.extractToMap(productImeList, "ime");
        Map<String, AfterSaleDetail> afterSaleDetailMap = CollectionUtil.extractToMap(afterSaleDetailList, "afterSaleId");
        Map<String, Product> productMap = CollectionUtil.extractToMap(productList, "name");
        for (List<String> row : datas) {
            ProductIme productIme = productImeMap.get(StringUtils.toString(row.get(5)).trim());
            AfterSale afterSale = afterSaleMap.get(productIme.getId());
            AfterSaleDetail afterSaleDetail = afterSaleDetailMap.get(afterSale.getId());
            afterSaleDetail.setReplaceDate(fromCompanyDate);
            for (int i = 0; i < row.size(); i++) {
                String value = StringUtils.toString(row.get(i)).trim();
                switch (i) {
                    case 1:
                        if (StringUtils.isNotBlank(value)) {
                            Product product = productMap.get(value);
                            afterSaleDetail.setReplaceProductId(product.getId());
                        }
                        break;
                    case 2:
                        if (StringUtils.isNotBlank(value)) {
                            ProductIme replaceProductIme = productImeMap.get(value);
                            afterSaleDetail.setReplaceProductImeId(replaceProductIme.getId());
                        }
                        break;
                    case 3:
                        if (StringUtils.isNotBlank(value)) {
                            afterSaleDetail.setReplaceAmount(new BigDecimal(value));
                        }
                        break;
                    default:
                        break;
                }
                afterSaleDetailRepository.save(afterSaleDetail);
            }
        }
    }

    public Page findPage(Pageable pageable, AfterSaleQuery afterSaleQuery) {
        Page<AfterSaleDto> page = afterSaleRepository.findPage(pageable, afterSaleQuery);
        return page;
    }

    public void delete(String detailId){
        AfterSaleDetail afterSaleDetail=afterSaleDetailRepository.getOne(detailId);
        afterSaleDetail.setEnabled(false);
        afterSaleDetailRepository.save(afterSaleDetail);
        List<AfterSaleDetail> afterSaleDetailList=afterSaleDetailRepository.findByEnabledIsTrueAndAfterSaleId(afterSaleDetail.getAfterSaleId());
        if(CollectionUtil.isEmpty(afterSaleDetailList)){
            AfterSale afterSale=afterSaleRepository.getOne(afterSaleDetail.getAfterSaleId());
            afterSale.setEnabled(false);
            afterSaleRepository.save(afterSale);
            AfterSaleFlee afterSaleFlee=afterSaleFleeRepository.findByEnabledIsTrueAndAfterSaleId(afterSaleDetail.getAfterSaleId());
            if(afterSaleFlee!=null){
                afterSaleFlee.setEnabled(false);
                afterSaleFleeRepository.save(afterSaleFlee);
            }
        }
    }

    private void listAddTrim(List<String> list, String item) {
        if (StringUtils.isNotBlank(item)) {
            list.add(item.trim());
        }
    }
}
