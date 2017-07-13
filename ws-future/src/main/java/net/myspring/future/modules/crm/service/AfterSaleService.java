package net.myspring.future.modules.crm.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.myspring.basic.common.util.CompanyConfigUtil;
import net.myspring.cloud.modules.sys.dto.KingdeeSynReturnDto;
import net.myspring.common.constant.CharConstant;
import net.myspring.common.enums.CompanyConfigCodeEnum;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.basic.domain.Product;
import net.myspring.future.modules.basic.manager.StkMisDeliveryManager;
import net.myspring.future.modules.basic.manager.StkTransferDirectManager;
import net.myspring.future.modules.basic.repository.DepotRepository;
import net.myspring.future.modules.basic.repository.ProductRepository;
import net.myspring.future.modules.crm.domain.*;
import net.myspring.future.modules.crm.dto.AfterSaleDto;
import net.myspring.future.modules.crm.dto.ProductImeDto;
import net.myspring.future.modules.crm.manager.RedisIdManager;
import net.myspring.future.modules.crm.repository.*;
import net.myspring.future.modules.crm.web.form.AfterSaleProductAllotForm;
import net.myspring.future.modules.crm.web.form.AfterSaleStoreAllotForm;
import net.myspring.future.modules.crm.web.query.AfterSaleQuery;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.excel.ExcelUtils;
import net.myspring.util.excel.SimpleExcelBook;
import net.myspring.util.excel.SimpleExcelColumn;
import net.myspring.util.excel.SimpleExcelSheet;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.text.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
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
    @Autowired
    private AfterSaleStoreAllotRepository afterSaleStoreAllotRepository;
    @Autowired
    private AfterSaleProductAllotRepository afterSaleProductAllotRepository;
    @Autowired
    private StkTransferDirectManager stkTransferDirectManager;
    @Autowired
    private StkMisDeliveryManager stkMisDeliveryManager;
    @Autowired
    private RedisIdManager redisIdManager;

    public Page<AfterSaleDto> findPage(Pageable pageable, AfterSaleQuery afterSaleQuery){
        Page<AfterSaleDto> afterSaleDtoPage=afterSaleRepository.findPage(pageable,afterSaleQuery);
        cacheUtils.initCacheInput(afterSaleDtoPage.getContent());
        return afterSaleDtoPage;
    }

    public List<AfterSaleDto> findFilter(AfterSaleQuery afterSaleQuery){
        List<AfterSaleDto> list=afterSaleRepository.findFilter(afterSaleQuery);
        cacheUtils.initCacheInput(list);
        return list;
    }


    public List<ProductImeDto> findFormData(List<String> imeList) {
        List<ProductImeDto> productImeList = productImeRepository.findDtoListByImeList(imeList);
        return productImeList;
    }

    public Map<String, AfterSaleDto> findDtoByImeList(List<String> imeList) {
        Map<String, AfterSaleDto> map = Maps.newHashMap();
        List<AfterSaleDto> afterSales = afterSaleRepository.findDtoByImeIn(imeList);
        if(CollectionUtil.isNotEmpty(afterSales)){
            Map<String,ProductIme> productImeMap=productImeRepository.findMap(CollectionUtil.extractToList(afterSales,"badProductImeId"));
            for (AfterSaleDto afterSale : afterSales) {
                map.put(productImeMap.get(afterSale.getBadProductImeId()).getIme(), afterSale);
            }
        }
        return map;
    }

    public Map<String, AfterSale> findByImeList(List<String> imeList) {
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
    @Transactional
    public void save(List<List<String>> datas, LocalDate toStoreDate) {
        Depot badStore = depotRepository.findOne(CompanyConfigUtil.findByCode(redisTemplate, CompanyConfigCodeEnum.BAD_STORE_ID.name()).getValue());
        List<String> imeList = Lists.newArrayList();
        List<String> depotNameList = Lists.newArrayList();
        for (List<String> row : datas) {
            listAddTrim(imeList, row.get(0));
            listAddTrim(imeList, row.get(3));
            listAddTrim(depotNameList, row.get(5));
        }
        List<ProductIme> productImeList = productImeRepository.findByEnabledIsTrueAndImeIn(imeList);
        List<Depot> depotList = depotRepository.findByNameList(depotNameList);
        Map<String, ProductIme> productImeMap = CollectionUtil.extractToMap(productImeList, "ime");
        Map<String, Depot> depotMap = CollectionUtil.extractToMap(depotList, "name");
        for (List<String> row : datas) {
            if (CollectionUtil.isNotEmpty(row)) {
                AfterSale afterSale = new AfterSale();
                ProductIme badProductIme = productImeMap.get(row.get(0));
                ProductIme toAreaProductIme = productImeMap.get(row.get(3));
                Depot toAreaDepot = depotMap.get(row.get(5));
                for (int i = 0; i < row.size(); i++) {
                    String value = StringUtils.toString(row.get(i));
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
                        case 5:
                            if (StringUtils.isNotBlank(value)) {
                                afterSale.setAreaDepotId(toAreaDepot.getId());
                                afterSale.setAreaDepotId(toAreaDepot.getId());
                            }
                            break;
                        case 6:
                            afterSale.setPackageStatus(value);
                            break;
                        case 7:
                            afterSale.setToStoreType(value);
                            break;
                        case 8:
                            afterSale.setMemory(value);
                            break;
                        case 9:
                            afterSale.setToStoreRemarks(value);
                            break;
                        default:
                            break;
                    }
                }
                LocalDate now = LocalDate.now();
                afterSale.setBusinessId(redisIdManager.getNextAfterSaleBusinessId(now));
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

        List<String> badImeList = Lists.newArrayList();
        List<String> imeList = Lists.newArrayList();
        List<String> depotNameList = Lists.newArrayList();
        List<String> productNameList = Lists.newArrayList();
        for (List<String> row : datas) {
            listAddTrim(badImeList, row.get(0));
            listAddTrim(imeList, row.get(3));
            listAddTrim(depotNameList, row.get(5));
            listAddTrim(productNameList, row.get(9));
        }
        Map<String, AfterSale> afterSaleMap = findByImeList(badImeList);
        if (afterSaleMap.size() > 0) {
            String goodStoreId = CompanyConfigUtil.findByCode(redisTemplate, CompanyConfigCodeEnum.GOOD_STORE_ID.name()).getValue();
            List<ProductIme> productImeList = productImeRepository.findByEnabledIsTrueAndImeIn(imeList);
            List<Depot> depotList = depotRepository.findByNameList(depotNameList);
            List<Product> productList = productRepository.findByNameIn(productNameList);
            Map<String, Product> productMap = CollectionUtil.extractToMap(productList, "name");
            Map<String, Depot> depotMap = CollectionUtil.extractToMap(depotList, "name");
            Map<String, ProductIme> productImeMap = CollectionUtil.extractToMap(productImeList, "ime");
            Map<String, ProductIme> toAreaProductImeMap = productImeRepository.findMap(CollectionUtil.extractToList(afterSaleMap.values(), "toAreaProductImeId"));
            for (List<String> row : datas) {
                String badIme = StringUtils.toString(row.get(0));
                AfterSale afterSale = afterSaleMap.get(badIme);
                for (int i = 0; i < row.size(); i++) {
                    String value = StringUtils.toString(row.get(i));
                    switch (i) {
                        case 3:
                            //更改替换机
                            if (StringUtils.isNotBlank(value)&&(afterSale.getToAreaToFinance() == null || !afterSale.getToAreaToFinance())) {
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
                        case 5:
                            if(StringUtils.isNotBlank(value)){
                                Depot areaDepot = depotMap.get(value);
                                afterSale.setAreaDepotId(areaDepot.getId());
                            }
                            break;
                        case 6:
                            afterSale.setPackageStatus(value);
                            break;
                        case 7:
                            afterSale.setToStoreType(value);
                            break;
                        case 8:
                            afterSale.setMemory(value);
                            break;
                        case 9:
                            //更改工厂返回
                            if (StringUtils.isNotBlank(value)&&(afterSale.getToAreaToFinance() == null || !afterSale.getFromCompanyToFinance() && StringUtils.isNotBlank(afterSale.getFromCompanyProductId()))) {
                                Product fromCompanyProduct = productMap.get(value);
                                afterSale.setFromCompanyProductId(fromCompanyProduct.getId());
                            }
                            break;
                        case 10:
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
        Map<String, AfterSale> afterSaleMap = findByImeList(badImes);
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
            listAddTrim(badImes,row.get(3));
            listAddTrim(productNames,row.get(1));
        }
        List<Product> productList = productRepository.findByNameIn(productNames);
        Map<String, Product> productMap = CollectionUtil.extractToMap(productList, "name");
        Map<String, AfterSale> afterSaleMap = findByImeList(badImes);
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

    @Transactional
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

    @Transactional
    public void synToFinance(){
        LocalDateTime date = LocalDateTime.now();
        
        String goodStoreId =CompanyConfigUtil.findByCode(redisTemplate,CompanyConfigCodeEnum.GOOD_STORE_ID.name()).getValue();
        String badStoreId = CompanyConfigUtil.findByCode(redisTemplate,CompanyConfigCodeEnum.BAD_STORE_ID.name()).getValue();
        String depositStoreId =CompanyConfigUtil.findByCode(redisTemplate,CompanyConfigCodeEnum.DEPOSIT_STORE_ID.name()).getValue();
        String disuseStoreId =CompanyConfigUtil.findByCode(redisTemplate,CompanyConfigCodeEnum.DISUSE_STORE_ID.name()).getValue();
        Depot goodStore = depotRepository.findOne(goodStoreId);
        Depot badStore = depotRepository.findOne(badStoreId);
        Depot depositStore = depotRepository.findOne(depositStoreId);
        Depot disuseStore = depotRepository.findOne(disuseStoreId);
        List<AfterSale> afterSales = afterSaleRepository.findByToFinanceDateIsNullAndEnabledIsTrue();
        List<AfterSaleStoreAllot> afterSaleStoreAllots = Lists.newArrayList();
        List<AfterSaleProductAllot> afterSaleProductAllots = Lists.newArrayList();
        List<String> productImeIdList=Lists.newArrayList();
        productImeIdList.addAll(CollectionUtil.extractToList(afterSales,"badProductImeId"));
        productImeIdList.addAll(CollectionUtil.extractToList(afterSales,"toAreaProductImeId"));
        productImeIdList=Lists.newArrayList(Sets.newHashSet(productImeIdList));
        Map<String,ProductIme> productImeMap=productImeRepository.findMap(productImeIdList);
        Map<String,Product> productMap=productRepository.findMap(CollectionUtil.extractToList(productImeMap.values(),"productId"));
        for(AfterSale afterSale:afterSales) {
            ProductIme badProductIme = productImeMap.get(afterSale.getBadProductImeId());
            ProductIme toAreaIme=productImeMap.get(afterSale.getToAreaProductImeId());
            if((badProductIme!=null&&StringUtils.isBlank(productMap.get(badProductIme.getProductId()).getCode()))||(toAreaIme!=null&&StringUtils.isBlank(productMap.get(toAreaIme.getProductId()).getCode()))){
                afterSale.setRemarks("淘汰机不需要步金蝶");
                continue;
            }
            //地区->仓库未同步
            if(!afterSale.getFromAreaToFinance()) {
                //如果没有返还机器,客户寄存机库->坏机库
                if(StringUtils.isBlank(afterSale.getToAreaProductImeId())) {
                    AfterSaleStoreAllot afterSaleStoreAllot = new AfterSaleStoreAllot(afterSale.getId(),badProductIme.getProductId(),depositStore.getId(),badStore.getId());
                    afterSaleStoreAllots.add(afterSaleStoreAllot);
                } else {
                    ProductIme toAreaProductIme = productImeMap.get(afterSale.getToAreaProductImeId());
                    //返还机型和坏机机型不一致，做盘点单
                    if(!toAreaProductIme.getProductId().equals(badProductIme.getProductId())) {
                        AfterSaleProductAllot afterSaleProductAllot = new AfterSaleProductAllot(afterSale.getId(),goodStore.getId(),toAreaProductIme.getProductId(),badProductIme.getProductId());
                        afterSaleProductAllots.add(afterSaleProductAllot);
                    }
                    //好机库->坏机库
                    AfterSaleStoreAllot afterSaleStoreAllot = new AfterSaleStoreAllot(afterSale.getId(),badProductIme.getProductId(),goodStore.getId(),badStore.getId());
                    afterSaleStoreAllots.add(afterSaleStoreAllot);
                    afterSale.setToAreaToFinance(true);
                }
                afterSale.setFromAreaToFinance(true);
            }
            if(!afterSale.getToAreaToFinance() && StringUtils.isNotBlank(afterSale.getToAreaProductImeId())) {
                ProductIme toAreaProductIme = productImeMap.get(afterSale.getToAreaProductImeId());;
                //返还机型和坏机机型不一致，做盘点单
                if(!toAreaProductIme.getProductId().equals(badProductIme.getProductId())) {
                    AfterSaleProductAllot afterSaleProductAllot = new AfterSaleProductAllot(afterSale.getId(),goodStore.getId(),toAreaProductIme.getProductId(),badProductIme.getProductId());
                    afterSaleProductAllots.add(afterSaleProductAllot);
                }
                //好机库->客户寄存机库
                AfterSaleStoreAllot afterSaleStoreAllot = new AfterSaleStoreAllot(afterSale.getId(),badProductIme.getProductId(),goodStore.getId(),depositStore.getId());
                afterSaleStoreAllots.add(afterSaleStoreAllot);
                afterSale.setToAreaToFinance(true);
            }
            if(!afterSale.getToCompanyToFinance() && afterSale.getToCompanyDate() !=null) {
                //坏机库->淘汰机库
                AfterSaleStoreAllot afterSaleStoreAllot = new AfterSaleStoreAllot(afterSale.getId(),badProductIme.getProductId(),badStore.getId(),disuseStore.getId());
                afterSaleStoreAllots.add(afterSaleStoreAllot);
                afterSale.setToCompanyToFinance(true);
            }
            if(!afterSale.getFromCompanyToFinance() && StringUtils.isNotBlank(afterSale.getFromCompanyProductId())) {
                //返还机型和坏机机型不一致，做盘点单
                if(!afterSale.getFromCompanyProductId().equals(badProductIme.getProductId())) {
                    AfterSaleProductAllot afterSaleProductAllot = new AfterSaleProductAllot(afterSale.getId(),disuseStore.getId(),badProductIme.getProductId(),afterSale.getFromCompanyProductId());
                    afterSaleProductAllots.add(afterSaleProductAllot);
                }
                //淘汰机库>到好机库
                AfterSaleStoreAllot afterSaleStoreAllot = new AfterSaleStoreAllot(afterSale.getId(),afterSale.getFromCompanyProductId(),disuseStore.getId(),goodStore.getId());
                afterSaleStoreAllots.add(afterSaleStoreAllot);
                afterSale.setFromCompanyToFinance(true);
            }
            if(afterSale.getFromAreaToFinance() && afterSale.getToAreaToFinance() && afterSale.getFromCompanyToFinance() && afterSale.getToCompanyToFinance()) {
                afterSale.setToFinanceDate(date);
            } else {
                afterSale.setToFinanceDate(null);
            }
        }
        afterSaleRepository.save(afterSales);
        //仓库调拨单
        if(CollectionUtil.isNotEmpty(afterSaleStoreAllots)) {
            Map<String,AfterSaleStoreAllotForm> allotMap = Maps.newHashMap();
            for(AfterSaleStoreAllot afterSaleStoreAllot:afterSaleStoreAllots) {
                AfterSaleStoreAllotForm afterSaleStoreAllotForm= BeanUtil.map(afterSaleStoreAllot,AfterSaleStoreAllotForm.class);
                String key =afterSaleStoreAllot.getProductId() + CharConstant.ENTER + afterSaleStoreAllot.getFromStoreId() + CharConstant.ENTER + afterSaleStoreAllot.getToStoreId();
                if(!allotMap.containsKey(key)) {
                    afterSaleStoreAllotForm.setQty(0);
                    allotMap.put(key,afterSaleStoreAllotForm);
                }
                allotMap.get(key).setQty(allotMap.get(key).getQty()+1);
            }
            //同步到金蝶
            KingdeeSynReturnDto kingdeeSynReturnDto = stkTransferDirectManager.synForAfterSale(Lists.newArrayList(allotMap.values()));
            String outCode = kingdeeSynReturnDto.getBillNo();
            for(AfterSaleStoreAllot afterSaleStoreAllot:afterSaleStoreAllots) {
                afterSaleStoreAllot.setOutCode(outCode);
            }
            afterSaleStoreAllotRepository.save(afterSaleStoreAllots);
        }
        //盘点单
        if(CollectionUtil.isNotEmpty(afterSaleProductAllots)) {
            //盘亏单
            Map<String,AfterSaleProductAllotForm> fromAllotMap = Maps.newHashMap();
            //盘盈单
            Map<String,AfterSaleProductAllotForm> toAllotMap = Maps.newHashMap();
            for(AfterSaleProductAllot afterSaleProductAllot:afterSaleProductAllots) {
                AfterSaleProductAllotForm afterSaleProductAllotForm=BeanUtil.map(afterSaleProductAllot,AfterSaleProductAllotForm.class);
                String fromKey =afterSaleProductAllot.getStoreId() + CharConstant.ENTER + afterSaleProductAllot.getFromProductId();
                if(!fromAllotMap.containsKey(fromKey)) {
                    afterSaleProductAllotForm.setFromQty(0);
                    fromAllotMap.put(fromKey, afterSaleProductAllotForm);
                }
                fromAllotMap.get(fromKey).setFromQty(fromAllotMap.get(fromKey).getFromQty()+1);

                String toKey =afterSaleProductAllot.getStoreId() + CharConstant.ENTER + afterSaleProductAllot.getToProductId();
                if(!toAllotMap.containsKey(toKey)) {
                    afterSaleProductAllotForm.setToQty(0);
                    toAllotMap.put(toKey, afterSaleProductAllotForm);
                }
                toAllotMap.get(toKey).setToQty(toAllotMap.get(toKey).getToQty()+1);
            }

            KingdeeSynReturnDto fromK3CloudSyn = stkMisDeliveryManager.synCKForAfterSale(Lists.newArrayList(fromAllotMap.values()));
            KingdeeSynReturnDto toK3CloudSyn = stkMisDeliveryManager.synTHForAfterSale(Lists.newArrayList(toAllotMap.values()));
            String toOutCode = toK3CloudSyn.getBillNo();
            String fromOutCode = fromK3CloudSyn.getBillNo();
            for(AfterSaleProductAllot afterSaleProductAllot:afterSaleProductAllots) {
                afterSaleProductAllot.setFromOutCode(fromOutCode);
                afterSaleProductAllot.setToOutCode(toOutCode);
            }
            afterSaleProductAllotRepository.save(afterSaleProductAllots);
        }
    }

    private void listAddTrim(List<String> list, String item) {
        if (StringUtils.isNotBlank(item)) {
            list.add(item.trim());
        }
    }

    public SimpleExcelBook export(AfterSaleQuery afterSaleQuery){
        Workbook workbook = new SXSSFWorkbook(10000);

        List<AfterSaleDto> afterSaleDtoList = findFilter(afterSaleQuery);

        List<SimpleExcelColumn> simpleExcelColumnList = Lists.newArrayList();
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"businessId","单号"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"badProductIme","坏机串码"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"badProductName","坏机货品"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"toAreaProductName","替换机货品"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"retailDepotName","地区"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"packageStatus","包装"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"memory","内存"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"toStoreType","退机类型"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"toStoreRemarks","退机备注"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"toStoreDate","退机日期"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"toCompanyDate","返厂日期"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"fromCompanyDate","返仓日期"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"toAreaProductIme","替换机串码"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"replaceProductImeId","垫机"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"badType","坏机类型"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"fromCompanyProductName","返仓型号"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"createdByName","创建人"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"createdDate","创建时间"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"lastModifiedByName","更新人"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"lastModifiedDate","更新时间"));

        SimpleExcelSheet simpleExcelSheet = new SimpleExcelSheet("导出数据",afterSaleDtoList,simpleExcelColumnList);
        ExcelUtils.doWrite(workbook,simpleExcelSheet);
        return new SimpleExcelBook(workbook,"售后列表"+LocalDate.now()+".xlsx",simpleExcelSheet);
    }
}
