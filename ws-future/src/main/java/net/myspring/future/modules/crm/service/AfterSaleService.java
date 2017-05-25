package net.myspring.future.modules.crm.service;

import com.google.common.collect.Lists;
import net.myspring.future.common.enums.AfterSaleTypeEnum;
import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.basic.domain.Product;
import net.myspring.future.modules.basic.repository.DepotRepository;
import net.myspring.future.modules.basic.repository.ProductRepository;
import net.myspring.future.modules.crm.domain.AfterSale;
import net.myspring.future.modules.crm.domain.AfterSaleDetail;
import net.myspring.future.modules.crm.domain.AfterSaleFlee;
import net.myspring.future.modules.crm.domain.ProductIme;
import net.myspring.future.modules.crm.dto.AfterSaleCompanyDto;
import net.myspring.future.modules.crm.repository.AfterSaleDetailRepository;
import net.myspring.future.modules.crm.repository.AfterSaleFleeRepository;
import net.myspring.future.modules.crm.repository.AfterSaleRepository;
import net.myspring.future.modules.crm.repository.ProductImeRepository;
import net.myspring.future.modules.crm.web.query.AfterSaleQuery;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.text.StringUtils;
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

    public List<AfterSale> findByImeList(List<String> imeList) {
        List<AfterSale> afterSales = afterSaleRepository.findByBadProductImeIn(imeList);
        return afterSales;
    }

    public List<AfterSaleCompanyDto> getFromCompanyData(List<String> imeList){
        List<AfterSaleCompanyDto> afterSaleCompanyList=Lists.newArrayList();
        List<AfterSale> afterSaleList=afterSaleRepository.findByBadProductImeIn(imeList);
        List<ProductIme> productImeList=productImeRepository.findAll(CollectionUtil.extractToList(afterSaleList,"badProductImeId"));
        List<Product> productList=productRepository.findAll(CollectionUtil.extractToList(afterSaleList,"badProductId"));
        List<AfterSaleDetail> afterSaleDetailList=afterSaleDetailRepository.findByAfterSaleIdInAndType(CollectionUtil.extractToList(afterSaleList,"id"),"工厂录入");
        Map<String,AfterSaleDetail> afterSaleDetailMap=CollectionUtil.extractToMap(afterSaleDetailList,"afterSaleId");
        Map<String,ProductIme> productImeMap=CollectionUtil.extractToMap(productImeList,"id");
        Map<String,Product> productMap=CollectionUtil.extractToMap(productList,"id");
        for(AfterSale afterSale:afterSaleList){
            AfterSaleCompanyDto afterSaleCompanyDto=new AfterSaleCompanyDto();
            AfterSaleDetail afterSaleDetail=afterSaleDetailMap.get(afterSale.getId());
            ProductIme productIme=productImeMap.get(afterSale.getBadProductImeId());
            Product product=productMap.get(afterSale.getBadProductId());
            afterSaleCompanyDto.setBadIme(productIme.getIme());
            afterSaleCompanyDto.setBadProductName(product.getName());
            afterSaleCompanyDto.setInputDate(afterSaleDetail.getInputDate());
            afterSaleCompanyDto.setBadType(afterSale.getBadType());
            afterSaleCompanyDto.setPackageStatus(afterSale.getPackageStatus());
            afterSaleCompanyDto.setMemory(afterSale.getMemory());
            afterSaleCompanyList.add(afterSaleCompanyDto);
        }
        return afterSaleCompanyList;
    }

    //地区录入
    public void save(List<List<String>> datas, String type) {
        List<String> imeList=Lists.newArrayList();
        List<String> productNameList=Lists.newArrayList();
        List<String> depotNameList=Lists.newArrayList();
        for (List<String> row : datas) {
            imeList.add( StringUtils.toString(row.get(0)).trim());
            productNameList.add( StringUtils.toString(row.get(1)).trim());
            depotNameList.add( StringUtils.toString(row.get(2)).trim());
        }
        List<ProductIme> productImeList=productImeRepository.findByImeList(imeList);
        List<Product> productList=productRepository.findByNameList(productNameList);
        List<Depot> depotList=depotRepository.findByNameList(depotNameList);
        Map<String,ProductIme> productImeMap=CollectionUtil.extractToMap(productImeList,"ime");
        Map<String,Product> productMap=CollectionUtil.extractToMap(productList,"name");
        Map<String,Depot> depotMap=CollectionUtil.extractToMap(depotList,"name");
        for (List<String> row : datas) {
            AfterSale afterSale=new AfterSale();
            AfterSaleDetail afterSaleDetail=new AfterSaleDetail();
            AfterSaleFlee afterSaleFlee=new AfterSaleFlee();
            afterSale.setType(type);
            afterSaleDetail.setInputDate(LocalDate.now());
            afterSaleDetail.setType("地区录入");
            for (int i = 0; i < row.size(); i++) {
                String value = StringUtils.toString(row.get(i)).trim();
                switch (i) {
                    case 0:
                        if(StringUtils.isNotBlank(value)){
                            ProductIme productIme =productImeMap.get(value);
                            afterSale.setBadProductImeId(productIme.getId());
                            afterSale.setBadProductId(productIme.getProductId());
                            afterSale.setBadDepotId(productIme.getDepotId());
                        }
                        break;
                    case 1:
                        if(StringUtils.isNotBlank(value)){
                            Product product =productMap.get(value);
                            afterSale.setBadProductId(product.getId());
                        }
                        break;
                    case 2:
                        if(StringUtils.isNotBlank(value)){
                            Depot badDepot =depotMap.get(value);
                            afterSale.setBadDepotId(badDepot.getId());
                            if(AfterSaleTypeEnum.窜货机.name().equals(type)){
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
                        afterSale.setMemory(value);
                        break;
                    case 7:
                        if(StringUtils.isNotBlank(value)){
                            Depot fromDepot=depotRepository.findByName(value);
                            afterSaleDetail.setFromDepotId(fromDepot.getId());
                        }
                        break;
                    case 8:
                        if(StringUtils.isNotBlank(value)){
                            Depot toDepot=depotRepository.findByName(value);
                            afterSaleDetail.setToDepotId(toDepot.getId());
                        }
                        break;
                    case 9:
                        if(StringUtils.isNotBlank(value)){
                            ProductIme replaceProductIme=productImeRepository.findByIme(value);
                            afterSaleDetail.setReplaceProductImeId(replaceProductIme.getId());
                            afterSaleDetail.setReplaceProductId(replaceProductIme.getProductId());
                        }
                        break;
                    case 10:
                        afterSaleFlee.setIme(value);
                        break;
                    case 11:
                        afterSaleFlee.setAddress(value);
                        break;
                    case 12:
                        afterSaleFlee.setBuyAmount(new BigDecimal(value));
                        break;
                    case 13:
                        afterSaleFlee.setContact(value);
                        break;
                    case 14:
                        afterSaleFlee.setMobilePhone(value);
                        break;
                    default:
                        break;
                }
            }
            afterSaleRepository.save(afterSale);
            afterSaleDetail.setAfterSaleId(afterSale.getId());
            afterSaleDetailRepository.save(afterSaleDetail);
            if(AfterSaleTypeEnum.窜货机.name().equals(type)){
                afterSaleFlee.setAfterSaleId(afterSale.getId());
                afterSaleFleeRepository.save(afterSaleFlee);
            }
        }
    }

    //总部录入
    public void saveHead(List<List<String>> datas,String type) {
        List<String> imeList=Lists.newArrayList();
        List<String> productNameList=Lists.newArrayList();
        List<String> depotNameList=Lists.newArrayList();
        for (List<String> row : datas) {
            imeList.add( StringUtils.toString(row.get(0)).trim());
            productNameList.add( StringUtils.toString(row.get(1)).trim());
            depotNameList.add( StringUtils.toString(row.get(2)).trim());
        }
        List<AfterSale> afterSaleList=afterSaleRepository.findByBadProductImeIn(imeList);
        List<AfterSaleFlee> afterSaleFleeList=afterSaleFleeRepository.findByImeList(imeList);
        List<ProductIme> productImeList=productImeRepository.findByImeList(imeList);
        List<Product> productList=productRepository.findByNameList(productNameList);
        List<Depot> depotList=depotRepository.findByNameList(depotNameList);
        Map<String,AfterSale> afterSaleMap=CollectionUtil.extractToMap(afterSaleList,"badProductImeId");
        Map<String,AfterSaleFlee> afterSaleFleeMap=CollectionUtil.extractToMap(afterSaleFleeList,"ime");
        Map<String,ProductIme> productImeMap=CollectionUtil.extractToMap(productImeList,"ime");
        Map<String,Product> productMap=CollectionUtil.extractToMap(productList,"name");
        Map<String,Depot> depotMap=CollectionUtil.extractToMap(depotList,"name");
        for (List<String> row : datas) {
            ProductIme productIme=productImeMap.get(StringUtils.toString(row.get(0)).trim());
            AfterSale afterSale=afterSaleMap.get(productIme.getIme());
            if(afterSale==null){
                afterSale=new AfterSale();
            }
            AfterSaleFlee afterSaleFlee=afterSaleFleeMap.get(StringUtils.toString(row.get(0)).trim());
            if(afterSaleFlee==null){
                afterSaleFlee=new AfterSaleFlee();
            }
            AfterSaleDetail afterSaleDetail=new AfterSaleDetail();
            afterSale.setType(type);
            afterSaleDetail.setInputDate(LocalDate.now());
            afterSaleDetail.setType("总部录入");
            for (int i = 0; i < row.size(); i++) {
                String value = StringUtils.toString(row.get(i)).trim();
                switch (i) {
                    case 0:
                        if(StringUtils.isNotBlank(value)){
                            afterSale.setBadProductImeId(productIme.getId());
                            afterSale.setBadProductId(productIme.getProductId());
                            afterSale.setBadDepotId(productIme.getDepotId());
                        }
                        break;
                    case 1:
                        if(StringUtils.isNotBlank(value)){
                            Product product =productMap.get(value);
                            afterSale.setBadProductId(product.getId());
                        }
                        break;
                    case 2:
                        if(StringUtils.isNotBlank(value)){
                            Depot badDepot =depotMap.get(value);
                            afterSale.setBadDepotId(badDepot.getId());
                            if(AfterSaleTypeEnum.窜货机.name().equals(type)){
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
                        afterSale.setMemory(value);
                        break;
                    case 7:
                        if(StringUtils.isNotBlank(value)){
                            Depot fromDepot=depotRepository.findByName(value);
                            afterSaleDetail.setFromDepotId(fromDepot.getId());
                        }
                        break;
                    case 8:
                        if(StringUtils.isNotBlank(value)){
                            Depot toDepot=depotRepository.findByName(value);
                            afterSaleDetail.setToDepotId(toDepot.getId());
                        }
                        break;
                    case 9:
                        if(StringUtils.isNotBlank(value)){
                            ProductIme replaceProductIme=productImeRepository.findByIme(value);
                            afterSaleDetail.setReplaceProductImeId(replaceProductIme.getId());
                            afterSaleDetail.setReplaceProductId(replaceProductIme.getProductId());
                        }
                        break;
                    case 10:
                        afterSaleFlee.setIme(value);
                        break;
                    case 11:
                        afterSaleFlee.setAddress(value);
                        break;
                    case 12:
                        afterSaleFlee.setBuyAmount(new BigDecimal(value));
                        break;
                    case 13:
                        afterSaleFlee.setContact(value);
                        break;
                    case 14:
                        afterSaleFlee.setMobilePhone(value);
                        break;
                    default:
                        break;
                }
            }
            if(StringUtils.isBlank(afterSale.getId())){
                afterSaleRepository.save(afterSale);
            }else {
                afterSaleRepository.save(afterSale);
            }
            afterSaleDetail.setAfterSaleId(afterSale.getId());
            afterSaleDetailRepository.save(afterSaleDetail);
            if(AfterSaleTypeEnum.窜货机.name().equals(type)){
                afterSaleFlee.setAfterSaleId(afterSale.getId());
                if(StringUtils.isBlank(afterSaleFlee.getId())){
                    afterSaleFleeRepository.save(afterSaleFlee);
                }else {
                    afterSaleFleeRepository.save(afterSaleFlee);
                }
            }
        }
    }


    //坏机返厂
    @Transactional
    public void toCompany(List<String> badImes,LocalDate toCompanyDate,String toCompanyRemarks) {
        List<AfterSale> afterSaleList=afterSaleRepository.findByBadProductImeIn(badImes);
        List<AfterSaleDetail> afterSaleDetailList=Lists.newArrayList();
        for(AfterSale afterSale:afterSaleList){
            AfterSaleDetail afterSaleDetail=new AfterSaleDetail();
            afterSaleDetail.setType("工厂录入");
            afterSaleDetail.setInputDate(toCompanyDate);
            afterSaleDetail.setRemarks(toCompanyRemarks);
            afterSaleDetail.setAfterSaleId(afterSale.getId());
            afterSaleDetailList.add(afterSaleDetail);
        }
        if(CollectionUtil.isNotEmpty(afterSaleDetailList)){
            afterSaleDetailRepository.save(afterSaleDetailList);
        }
    }


    //工厂返回
    @Transactional
    public void fromCompany(List<List<String>> datas,LocalDate fromCompanyDate) {
        List<String> imeList=Lists.newArrayList();
        List<String> productNameList=Lists.newArrayList();
        for (List<String> row : datas) {
            imeList.add( StringUtils.toString(row.get(0)).trim());
            productNameList.add( StringUtils.toString(row.get(1)).trim());
        }
        List<AfterSale> afterSaleList=afterSaleRepository.findByBadProductImeIn(imeList);
        List<ProductIme> productImeList=productImeRepository.findByImeList(imeList);
        List<AfterSaleDetail> afterSaleDetailList=afterSaleDetailRepository.findByAfterSaleIdInAndType(CollectionUtil.extractToList(afterSaleList,"id"),"工厂录入");
        List<Product> productList=productRepository.findByNameList(productNameList);
        Map<String,AfterSale> afterSaleMap=CollectionUtil.extractToMap(afterSaleList,"badProductImeId");
        Map<String,ProductIme> productImeMap=CollectionUtil.extractToMap(productImeList,"ime");
        Map<String,AfterSaleDetail> afterSaleDetailMap=CollectionUtil.extractToMap(afterSaleDetailList,"afterSaleId");
        Map<String,Product> productMap=CollectionUtil.extractToMap(productList,"name");
        for (List<String> row : datas) {
            ProductIme productIme=productImeMap.get(StringUtils.toString(row.get(0)).trim());
            AfterSale afterSale=afterSaleMap.get(productIme.getId());
            AfterSaleDetail afterSaleDetail=afterSaleDetailMap.get(afterSale.getId());
            afterSaleDetail.setReplaceDate(fromCompanyDate);
            for (int i = 0; i < row.size(); i++) {
                String value = StringUtils.toString(row.get(i)).trim();
                switch (i) {
                    case 2:
                        if(StringUtils.isNotBlank(value)){
                            Product product=productMap.get(value);
                            afterSaleDetail.setReplaceProductId(product.getId());
                        }
                        break;
                    case 4:
                        if(StringUtils.isNotBlank(value)){
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


    @Transactional
    public void synToFinance(){
    }

    public Page findPage(Pageable pageable, AfterSaleQuery afterSaleQuery){
        Page<AfterSale> page=afterSaleRepository.findPage(pageable,afterSaleQuery);
        return page;
    }

    public List<AfterSale> findFilter(Map<String,Object> map){
        List<AfterSale> page=afterSaleRepository.findFilter(map);
        return page;
    }
}
