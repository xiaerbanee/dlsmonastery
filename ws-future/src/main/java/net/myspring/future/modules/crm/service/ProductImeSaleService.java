package net.myspring.future.modules.crm.service;

import com.google.common.collect.Lists;
import net.myspring.common.constant.CharConstant;
import net.myspring.common.exception.ServiceException;
import net.myspring.future.common.constant.ServiceConstant;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.basic.domain.Product;
import net.myspring.future.modules.basic.dto.DepotDto;
import net.myspring.future.modules.basic.manager.DepotManager;
import net.myspring.future.modules.basic.repository.DepotRepository;
import net.myspring.future.modules.basic.repository.ProductRepository;
import net.myspring.future.modules.crm.domain.ProductIme;
import net.myspring.future.modules.crm.domain.ProductImeSale;
import net.myspring.future.modules.crm.dto.ProductImeForSaleDto;
import net.myspring.future.modules.crm.dto.ProductImeSaleDto;
import net.myspring.future.modules.crm.repository.ProductImeRepository;
import net.myspring.future.modules.crm.repository.ProductImeSaleRepository;
import net.myspring.future.modules.crm.web.form.ProductImeSaleBackForm;
import net.myspring.future.modules.crm.web.form.ProductImeSaleDetailForm;
import net.myspring.future.modules.crm.web.form.ProductImeSaleForm;
import net.myspring.future.modules.crm.web.query.ProductImeSaleQuery;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.excel.ExcelUtils;
import net.myspring.util.excel.SimpleExcelBook;
import net.myspring.util.excel.SimpleExcelColumn;
import net.myspring.util.excel.SimpleExcelSheet;
import net.myspring.util.text.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
@Transactional(readOnly = true)
public class ProductImeSaleService {

    @Autowired
    private ProductImeSaleRepository productImeSaleRepository;
    @Autowired
    private ProductImeRepository productImeRepository;
    @Autowired
    private DepotRepository depotRepository;
    @Autowired
    private DepotManager depotManager;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CacheUtils cacheUtils;

    public Page<ProductImeSaleDto> findPage(Pageable pageable, ProductImeSaleQuery productImeSaleQuery) {
        productImeSaleQuery.setDepotIdList(depotManager.filterDepotIds(RequestUtils.getAccountId()));
        Page<ProductImeSaleDto> page = productImeSaleRepository.findPage(pageable, productImeSaleQuery);
        cacheUtils.initCacheInput(page.getContent());

        return page;
    }

    public ProductImeSaleDto findDto(String id) {
        ProductImeSaleDto productImeSaleDto = productImeSaleRepository.findDto(id);
        cacheUtils.initCacheInput(productImeSaleDto);
        return productImeSaleDto;
    }

    @Transactional
    @Deprecated
    public void sale(ProductImeSaleForm productImeSaleForm) {
        List<String> imeList = productImeSaleForm.getImeList();

        String employeeId = RequestUtils.getEmployeeId();
        Integer leftCredit = getLeftCredit(employeeId);

        Map<String, ProductImeSaleDetailForm> detailFormMap = CollectionUtil.extractToMap(productImeSaleForm.getProductImeSaleDetailList(), "productImeId");
        List<ProductIme> productImeList = productImeRepository.findByEnabledIsTrueAndImeIn(imeList);
        Map<String, ProductIme> imeMap = CollectionUtil.extractToMap(productImeList, "ime");
        for(String ime : imeList){
            ProductIme productIme = imeMap.get(ime);
            if(productIme == null) {
                throw new ServiceException("串码：" + ime + "在系统中不存在；");
            }
            String saleShopId = productIme.getDepotId();
            if(detailFormMap.get(productIme.getId())!=null &&StringUtils.isNotBlank(detailFormMap.get(productIme.getId()).getSaleShopId())){
                saleShopId = detailFormMap.get(productIme.getId()).getSaleShopId();
            }

            Depot saleShop = depotRepository.findOne(saleShopId);
            if(saleShop.getIsHidden() != null && saleShop.getIsHidden()){
                throw new ServiceException("串码：" + ime + "的所属地点："+saleShop.getName()+"被隐藏，请联系文员开通门店；");
            }
            if(productIme.getProductImeSaleId()!=null) {
                throw new ServiceException("串码：" + ime + "已核销；");
            }
            if(StringUtils.isNotBlank(saleShop.getDepotStoreId())) {
                throw new ServiceException("串码：" + ime + "的所属地点为："+saleShop.getName()+"，不是门店，无法核销；");
            }
            if(!depotManager.isAccess(saleShop.getId(), true, RequestUtils.getAccountId())) {
                throw new ServiceException("您没有串码：" + ime + "所在门店："+saleShop.getName()+"的核销权限，请先将串码调拨至您管辖的门店；");
            }
            if(productIme.getProductImeUploadId() != null) {
                throw new ServiceException("串码：" + ime + "已上报,不能核销；");
            }

            ProductImeSale productImeSale = new ProductImeSale();
            productImeSale.setEmployeeId(employeeId);
            productImeSale.setProductImeId(productIme.getId());
            productImeSale.setShopId(saleShopId);
            productImeSale.setIsBack(false);
            productImeSale.setRemarks(productImeSaleForm.getRemarks());
            productImeSale.setBuyer(productImeSaleForm.getBuyer());
            productImeSale.setBuyerAge(productImeSaleForm.getBuyerAge());
            productImeSale.setBuyerSex(productImeSaleForm.getBuyerSex());
            productImeSale.setBuyerPhone(productImeSaleForm.getBuyerPhone());

            Product product = productRepository.findOne(productIme.getProductId());
            //设置积分
            Integer credit = 0;
            if(product.getRetailPrice() != null){
                credit = product.getRetailPrice().intValue()/1000;
            }
            leftCredit = leftCredit + credit;
            productImeSale.setCredit(credit);
            productImeSale.setLeftCredit(leftCredit);
            productImeSaleRepository.save(productImeSale);

            productIme.setProductImeSaleId(productImeSale.getId());
            productIme.setDepotId(saleShopId);
            productIme.setRetailShopId(saleShopId);
            productImeRepository.save(productIme);
        }
    }

    private Integer getLeftCredit(String employeeId) {
        ProductImeSale latestProductImeSale= productImeSaleRepository.findTopByEnabledIsTrueAndEmployeeIdOrderByCreatedDateDesc(employeeId);
        Integer leftCredit =0;
        if(latestProductImeSale!=null){
            leftCredit=latestProductImeSale.getLeftCredit();
        }
        return leftCredit;
    }

    public String checkForSaleBack(List<String> imeList) {

        StringBuilder sb = new StringBuilder();
        List<ProductIme> productImeList = productImeRepository.findByEnabledIsTrueAndImeIn( imeList);
        Map<String, ProductIme> imeMap = CollectionUtil.extractToMap(productImeList, "ime");
        List<String> accountDepotIdList = depotManager.getFilterDepotIds(false, RequestUtils.getAccountId());

        for(String ime:imeList){
            ProductIme productIme = imeMap.get(ime);
            if(productIme == null) {
                sb.append("串码：").append(ime).append("在系统中不存在；");
            } else {
                if(productIme.getProductImeSaleId() ==null) {
                    sb.append("串码：").append(ime).append("还未被核销，不能退回；");
                } else if(!RequestUtils.getAdmin() && !accountDepotIdList.contains(productIme.getDepotId())){
                    sb.append("您没有串码：").append(ime).append("所在门店的核销退回权限；");
                }
                if(productIme.getProductImeUploadId() != null) {
                    sb.append("串码：").append(ime).append("已上报，不能退回；");
                }
            }
        }
        return sb.toString();
    }

    @Transactional
    public void saleBack(ProductImeSaleBackForm productImeSaleBackForm) {
        List<String> imeList = productImeSaleBackForm.getImeList();
        String employeeId = RequestUtils.getEmployeeId();

        List<ProductIme> productImes = productImeRepository.findByEnabledIsTrueAndImeIn(imeList);
        Map<String, ProductImeSale> saleMap = productImeSaleRepository.findMap(CollectionUtil.extractToList(productImes, "productImeSaleId"));
        List<ProductIme> productImeList = productImeRepository.findByEnabledIsTrueAndImeIn(imeList);
        Map<String, ProductIme> imeMap = CollectionUtil.extractToMap(productImeList, "ime");
        List<String> accountDepotIdList = depotManager.getFilterDepotIds(false, RequestUtils.getAccountId());
        Integer leftCredit = getLeftCredit(employeeId);

        for(String ime : imeList) {
            ProductIme productIme = imeMap.get(ime);
            checkProductImeForSaleBack(ime, productIme);

            if(!RequestUtils.getAdmin() && !accountDepotIdList.contains(productIme.getDepotId())){
                throw new ServiceException("您没有串码："+ime+"所在门店的核销退回权限；");
            }

            ProductImeSale productImeSale = saleMap.get(productIme.getProductImeSaleId());
            productImeSale.setEnabled(false);
            productImeSaleRepository.save(productImeSale);

            Integer credit = 0-(productImeSale.getCredit()==null?0:productImeSale.getCredit());
            leftCredit = leftCredit +credit;

            ProductImeSale saleBack = new ProductImeSale();
            saleBack.setEmployeeId(employeeId);
            saleBack.setProductImeId(productIme.getId());
            saleBack.setShopId(productImeSale.getShopId());
            saleBack.setRemarks(productImeSale.getRemarks());
            saleBack.setCredit(credit);
            saleBack.setLeftCredit(leftCredit);
            saleBack.setIsBack(true);
            productImeSaleRepository.save(saleBack);

            productIme.setProductImeSaleId(null);
            productImeRepository.save(productIme);
        }
    }

    public SimpleExcelBook export(ProductImeSaleQuery productImeSaleQuery) {

        Workbook workbook = new SXSSFWorkbook(ServiceConstant.EXPORT_MAX_ROW_NUM);

        List<SimpleExcelColumn> productImeSaleColumnList = Lists.newArrayList();
        productImeSaleColumnList.add(new SimpleExcelColumn(workbook, "productImeIme", "串码"));
        productImeSaleColumnList.add(new SimpleExcelColumn(workbook, "productImeProductName", "货品型号"));
        productImeSaleColumnList.add(new SimpleExcelColumn(workbook, "productImeMeid", "MEID"));
        productImeSaleColumnList.add(new SimpleExcelColumn(workbook, "shopAreaName", "办事处"));
        productImeSaleColumnList.add(new SimpleExcelColumn(workbook, "shopOfficeName", "考核区域"));
        productImeSaleColumnList.add(new SimpleExcelColumn(workbook, "shopName", "门店"));
        productImeSaleColumnList.add(new SimpleExcelColumn(workbook, "depotShopAreaType", "区域类型"));
        productImeSaleColumnList.add(new SimpleExcelColumn(workbook, "employeeName", "核销人"));
        productImeSaleColumnList.add(new SimpleExcelColumn(workbook, "createdByName", "创建人"));
        productImeSaleColumnList.add(new SimpleExcelColumn(workbook, "createdDate", "创建时间"));
        productImeSaleColumnList.add(new SimpleExcelColumn(workbook, "buyer", "用户姓名"));
        productImeSaleColumnList.add(new SimpleExcelColumn(workbook, "buyerAge", "年龄"));
        productImeSaleColumnList.add(new SimpleExcelColumn(workbook, "buyerSex", "性别"));
        productImeSaleColumnList.add(new SimpleExcelColumn(workbook, "buyerPhone", "电话"));
        productImeSaleColumnList.add(new SimpleExcelColumn(workbook, "buyerSchool", "学校"));
        productImeSaleColumnList.add(new SimpleExcelColumn(workbook, "buyerGrade", "年级"));
        productImeSaleColumnList.add(new SimpleExcelColumn(workbook, "lotteryDate", "抽奖时间"));
        productImeSaleColumnList.add(new SimpleExcelColumn(workbook, "hongbao", "红包"));

        List<ProductImeSaleDto> productImeSaleDtoList = findPage(new PageRequest(0,ServiceConstant.EXPORT_MAX_ROW_NUM), productImeSaleQuery).getContent();

        SimpleExcelSheet simpleExcelSheet = new SimpleExcelSheet("核销列表", productImeSaleDtoList, productImeSaleColumnList);
        ExcelUtils.doWrite(workbook, simpleExcelSheet);
        return new SimpleExcelBook(workbook,"核销列表"+ LocalDate.now()+".xlsx", simpleExcelSheet);

    }

    public List<ProductImeForSaleDto> findProductImeForSaleDto(String imeStr) {
        if (StringUtils.isBlank(imeStr)) {
            return new ArrayList<>();
        }
        List<String> imeList = StringUtils.getSplitList(imeStr, CharConstant.ENTER);
        List<ProductImeForSaleDto> result = productImeSaleRepository.findProductImeForSaleDto(imeList);
        cacheUtils.initCacheInput(result);

            for(ProductImeForSaleDto productImeForSaleDto : result) {
                productImeForSaleDto.setEditable(false);
                productImeForSaleDto.setFromChain(false);
                    //如果用户对此串码是可以编辑的，那么直接出库的门店为所在门店，否则说明是该串码在连锁门店，需要用户选择对应核销门店。
                    if(productImeForSaleDto.getProductImeSaleId()==null && depotManager.isAccess(productImeForSaleDto.getDepotId(),true, RequestUtils.getAccountId()) && StringUtils.isBlank(productImeForSaleDto.getDepotDepotStoreId())) {
                        productImeForSaleDto.setEditable(true);
                        //如果是关联连锁体系的，核销为自己管辖的连锁体系
                        if(!depotManager.isAccess(productImeForSaleDto.getDepotId(),false, RequestUtils.getAccountId())) {
                            productImeForSaleDto.setFromChain(true);
                            productImeForSaleDto.setAccessChainDepotList(new ArrayList<>());
                            List<Depot> chainDepotList = depotRepository.findByChainId(productImeForSaleDto.getDepotChainId());
                            for(Depot depot : chainDepotList) {
                                if(depotManager.isAccess(depot.getId(), false, RequestUtils.getAccountId())) {
                                    DepotDto depotDto = new DepotDto();
                                    depotDto.setId(depot.getId());
                                    depotDto.setName(depot.getName());
                                    productImeForSaleDto.getAccessChainDepotList().add(depotDto);
                                    if(StringUtils.isBlank(productImeForSaleDto.getSaleShopId())&&chainDepotList.size() == 1){
                                        productImeForSaleDto.setSaleShopId(depot.getId());
                                    }
                                }
                            }
                        }
                    }
            }
        return result;
    }

    @Transactional
    public void saleIme(ProductImeSaleForm productImeSaleForm) {

        if(StringUtils.isNotBlank(productImeSaleForm.getSaleShopId()) && !depotManager.isAccess(productImeSaleForm.getSaleShopId(), false, RequestUtils.getAccountId())) {
            Depot saleShop = depotRepository.findOne(productImeSaleForm.getSaleShopId());
            throw new ServiceException("您没有门店："+saleShop.getName()+"的核销权限，请选择您管辖的门店进行核销；");
        }

        List<String> imeList = productImeSaleForm.getImeList();
        Integer leftCredit = getLeftCredit(RequestUtils.getEmployeeId());
        List<ProductIme> productImeList = productImeRepository.findByEnabledIsTrueAndImeIn(imeList);
        Map<String, Depot> depotMap = depotRepository.findMap(CollectionUtil.extractToList(productImeList, "depotId"));
        Map<String, ProductIme> imeMap = CollectionUtil.extractToMap(productImeList, "ime");
        Map<String, Product> productMap = productRepository.findMap(CollectionUtil.extractToList(productImeList, "productId"));
        Map<String, List<Depot>> chainDepotMap = getChainDepotMap(depotMap.values());

        for(String ime : imeList){
            ProductIme productIme = imeMap.get(ime);

            checkProductImeForSale(ime, productIme);

            Depot productImeDepot = depotMap.get(productIme.getDepotId());
            String saleShopId = decideSaleShopId(productImeSaleForm, productIme, productImeDepot,  chainDepotMap.get(productImeDepot.getChainId()));

            Product product = productMap.get(productIme.getProductId());
            Integer credit = (product.getRetailPrice() != null ? product.getRetailPrice().intValue()/1000 : 0);
            leftCredit = leftCredit + credit;

            ProductImeSale productImeSale = new ProductImeSale();
            productImeSale.setEmployeeId(RequestUtils.getEmployeeId());
            productImeSale.setProductImeId(productIme.getId());
            productImeSale.setShopId(saleShopId);
            productImeSale.setIsBack(false);
            productImeSale.setRemarks(productImeSaleForm.getRemarks());
            productImeSale.setBuyer(productImeSaleForm.getBuyer());
            productImeSale.setBuyerAge(productImeSaleForm.getBuyerAge());
            productImeSale.setBuyerSex(productImeSaleForm.getBuyerSex());
            productImeSale.setBuyerPhone(productImeSaleForm.getBuyerPhone());
            productImeSale.setCredit(credit);
            productImeSale.setLeftCredit(leftCredit);
            productImeSaleRepository.save(productImeSale);

            productIme.setProductImeSaleId(productImeSale.getId());
            productIme.setDepotId(saleShopId);
            productIme.setRetailShopId(saleShopId);
            productImeRepository.save(productIme);
        }
    }

    private String decideSaleShopId(ProductImeSaleForm productImeSaleForm, ProductIme productIme, Depot productImeDepot, List<Depot> depots) {

        if(StringUtils.isNotBlank(productImeDepot.getDepotStoreId())){
            throw new ServiceException(String.format("串码%s所在位置为仓库，不能核销", productIme.getIme()));
        }

        List<String> possibleSaleDepotIds = getPossibleSaleDepotIds(productIme, productImeDepot, depots);

        if(CollectionUtil.isEmpty(possibleSaleDepotIds)){
            throw new ServiceException(String.format("本帐号没有串码%s所在门店或者所在连锁体系中任何门店的核销权限。", productIme.getIme()));
        }else if(StringUtils.isNotBlank(productImeSaleForm.getSaleShopId())){
            if(possibleSaleDepotIds.contains(productImeSaleForm.getSaleShopId())){
                return productImeSaleForm.getSaleShopId();
            }
            throw new ServiceException(String.format("不能将串码%s核销在所选门店。", productIme.getIme()));
        }else if(possibleSaleDepotIds.size() == 1){
            return possibleSaleDepotIds.get(0);
        }else{
            throw new ServiceException(String.format("串码%s可以被核销在多个门店，系统无法确认该核销在哪个门店。请单独核销该串码，并指定核销门店", productIme.getIme()));
        }
    }

    private List<String> getPossibleSaleDepotIds(ProductIme productIme, Depot productImeDepot, List<Depot> depots) {
        List<String> possibleProductImeSaleDepotIds;
        if(StringUtils.isBlank(productImeDepot.getChainId())){
            possibleProductImeSaleDepotIds = Lists.newArrayList(productIme.getDepotId());
        }else{
            possibleProductImeSaleDepotIds = CollectionUtil.extractToList(depots, "id");
        }

        if(RequestUtils.getAdmin()){
            return possibleProductImeSaleDepotIds;
        }

        List<String> possibleSaleDepotIds = depotManager.getFilterDepotIds(false, RequestUtils.getAccountId());
        possibleSaleDepotIds.retainAll(possibleProductImeSaleDepotIds);
        return possibleSaleDepotIds;
    }

    private Map<String, List<Depot>> getChainDepotMap(Collection<Depot> depots) {
        List<String> chainIdList = CollectionUtil.extractToList(depots, "chainId");

        if(CollectionUtil.isNotEmpty(chainIdList)){
            List<Depot> chainDepotList = depotRepository.findByEnabledIsTrueAndChainIdIn(chainIdList);
            return CollectionUtil.extractToMapList(chainDepotList, "chainId");
        }

        return new HashMap<>();
    }

    private void checkProductImeForSaleBack(String ime, ProductIme productIme) {
        if(productIme == null) {
            throw new ServiceException("串码：" + ime + "在系统中不存在；");
        }
        if(productIme.getProductImeSaleId()==null) {
            throw new ServiceException("串码：" + ime + "未核销，不能核销退回；");
        }
        if(productIme.getProductImeUploadId() != null) {
            throw new ServiceException("串码：" + ime + "已上报，不能核销退回；");
        }
    }

    private void checkProductImeForSale(String ime, ProductIme productIme) {
        if(productIme == null) {
            throw new ServiceException("串码：" + ime + "在系统中不存在；");
        }
        if(productIme.getProductImeSaleId()!=null) {
            throw new ServiceException("串码：" + ime + "已核销；");
        }
        if(productIme.getProductImeUploadId() != null) {
            throw new ServiceException("串码：" + ime + "已上报，不能核销；");
        }
    }

    public String checkForSale(List<String> imeList) {
        try{
            List<ProductIme> productImeList = productImeRepository.findByEnabledIsTrueAndImeIn(imeList);
            Map<String, ProductIme> imeMap = CollectionUtil.extractToMap(productImeList, "ime");

            for(String ime : imeList){
                checkProductImeForSale(ime, imeMap.get(ime));
            }
            return null;
        }catch(ServiceException e){
            return e.getMessage();
        }
    }
}
