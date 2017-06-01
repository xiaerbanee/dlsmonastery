package net.myspring.future.modules.crm.service;

import com.google.common.collect.Lists;
import com.mongodb.gridfs.GridFSFile;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.basic.domain.Product;
import net.myspring.future.modules.basic.repository.DepotRepository;
import net.myspring.future.modules.basic.repository.ProductRepository;
import net.myspring.future.modules.basic.service.DepotService;
import net.myspring.future.modules.crm.domain.ProductIme;
import net.myspring.future.modules.crm.domain.ProductImeSale;
import net.myspring.future.modules.crm.dto.ProductImeSaleDto;
import net.myspring.future.modules.crm.dto.StoreAllotDto;
import net.myspring.future.modules.crm.dto.StoreAllotImeDto;
import net.myspring.future.modules.crm.repository.ProductImeRepository;
import net.myspring.future.modules.crm.repository.ProductImeSaleRepository;
import net.myspring.future.modules.crm.repository.StoreAllotRepository;
import net.myspring.future.modules.crm.web.form.ProductImeSaleBackForm;
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
import org.elasticsearch.xpack.notification.email.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ProductImeSaleService {



    @Autowired
    private ProductImeSaleRepository productImeSaleRepository;
    @Autowired
    private ProductImeRepository productImeRepository;
    @Autowired
    private DepotRepository depotRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private GridFsTemplate tempGridFsTemplate;

    public Page<ProductImeSaleDto> findPage(Pageable pageable, ProductImeSaleQuery productImeSaleQuery) {
        Page<ProductImeSaleDto> page = productImeSaleRepository.findPage(pageable, productImeSaleQuery);
        cacheUtils.initCacheInput(page.getContent());

        return page;
    }

    public ProductImeSaleDto findDto(String id) {
        ProductImeSaleDto productImeSaleDto = productImeSaleRepository.findDto(id);
        cacheUtils.initCacheInput(productImeSaleDto);
        return productImeSaleDto;
    }

    public String checkForSale(List<String> imeList) {
        StringBuffer sb = new StringBuffer();
        List<ProductIme> productImeList = productImeRepository.findByImeList(imeList);
        Map<String, ProductIme> imeMap = CollectionUtil.extractToMap(productImeList, "ime");
        for(String ime:imeList){
            ProductIme productIme = imeMap.get(ime);
            if(productIme == null) {
                sb.append("串码："+ime+"在系统中不存在；");
            } else {
                Depot depot = depotRepository.findOne(productIme.getDepotId());
                if(depot.getIsHidden() != null && depot.getIsHidden()){
                    sb.append("串码："+ime+"的所属地点："+depot.getName()+" 被隐藏，请联系文员开通门店；");
                }
                if(productIme.getProductImeSaleId()!=null) {
                    sb.append("串码："+ime+"已核销；");
                } else if(StringUtils.isNotBlank(depot.getDepotStoreId())) {
                    sb.append("串码：" + ime + "的所属地点为："+depot.getName()+"，不是门店，无法核销；");
                    //TODO 需要增加判断，判断门店是否可以核销
//                } else if(!DepotUtils.isAccess(productIme.getDepot(),true)) {
//                    message.addText("message_product_ime_sale_not_have_ime",ime,"message_product_ime_sale_in_store",productIme.getDepotName(),"message_product_ime_sale_no_sale_permission");
                }else if(productIme.getProductImeUploadId() != null) {
                    sb.append("串码："+ime+"已上报,不能核销；");
                }
            }
        }

        return sb.toString();
    }


    public void sale(ProductImeSaleForm productImeSaleForm) {
        List<String> imeList = productImeSaleForm.getImeList();


        //TODO 获取employeeId
        String employeeId = "";
        ProductImeSale  latestProductImeSale= productImeSaleRepository.findTopByEnabledIsTrueAndEmployeeIdOrderByCreatedDateDesc(employeeId);
        Integer leftCredit =0;
        if(latestProductImeSale!=null){
            leftCredit=latestProductImeSale.getLeftCredit();
        }

        for (int i = 0; i < imeList.size(); i++) {
            ProductIme productIme = productImeRepository.findByIme(imeList.get(i));
            Depot depot = depotRepository.findOne(productImeSaleForm.getShopId());//TODO 需要修正 depotDao.findOne(depotIds[i]);

            ProductImeSale productImeSale = new ProductImeSale();
            productImeSale.setEmployeeId(employeeId);
            productImeSale.setProductImeId(productIme.getId());
            productImeSale.setShopId(depot.getId());
            productImeSale.setRemarks(productImeSaleForm.getRemarks());
            productImeSale.setBuyer(productImeSaleForm.getBuyer());
            productImeSale.setBuyerAge(productImeSaleForm.getBuyerAge());
            productImeSale.setBuyerSex(productImeSaleForm.getBuyerSex());
            productImeSale.setBuyerPhone(productImeSaleForm.getBuyerPhone());
            productImeSale.setBuyerSchool(productImeSaleForm.getBuyerSchool());
            productImeSale.setBuyerGrade(productImeSaleForm.getBuyerGrade());

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
            productIme.setDepotId(depot.getId());
            productIme.setRetailShopId(depot.getId());
            productImeRepository.save(productIme);

        }
    }

    public String checkForSaleBack(List<String> imeList) {

        StringBuffer sb = new StringBuffer();
        List<ProductIme> productImeList = productImeRepository.findByImeList(imeList);
        Map<String, ProductIme> imeMap = CollectionUtil.extractToMap(productImeList, "ime");
        for(String ime:imeList){
            ProductIme productIme = imeMap.get(ime);
            if(productIme == null) {
                sb.append("串码："+ime+"在系统中不存在；");
            } else {
                if(productIme.getProductImeSaleId() ==null) {
                    sb.append("串码："+ime+"还未被核销，不能退回；");
                    //TODO 需要增加判断，判断门店是否可以核销
//                } else if(!DepotUtils.isAccess(productIme.getDepot(),true)) {
//                    message.addText("message_product_ime_sale_not_have_ime",ime,"message_product_ime_sale_in_store",productIme.getDepotName(),"message_product_ime_sale_no_sale_permission");
                }
                if(productIme.getProductImeUploadId() != null) {
                    sb.append("串码："+ime+"已上报,不能退回；");
                }
            }
        }

        return sb.toString();

    }

    public void saleBack(ProductImeSaleBackForm productImeSaleBackForm) {
        List<String> imeList = productImeSaleBackForm.getImeList();
        String employeeId = "";//TODO 获取employeeId

        List<ProductIme> productImes = productImeRepository.findByImeList(imeList);
        ProductImeSale  latestProductImeSale= productImeSaleRepository.findTopByEnabledIsTrueAndEmployeeIdOrderByCreatedDateDesc(employeeId);
        Integer leftCredit =0;
        if(latestProductImeSale!=null){
            leftCredit=latestProductImeSale.getLeftCredit();
        }

        for(ProductIme productIme:productImes) {
            ProductImeSale productImeSale = productImeSaleRepository.findOne(productIme.getProductImeSaleId());

            productImeSale.setEnabled(false);
            productImeSaleRepository.save(productImeSale);

            Integer credit = 0-(productImeSale.getCredit()==null?0:productImeSale.getCredit());
            ProductImeSale saleBack = new ProductImeSale();
            saleBack.setEmployeeId(employeeId);
            saleBack.setProductImeId(productIme.getId());
            saleBack.setShopId(productImeSale.getShopId());
            saleBack.setRemarks(productImeSale.getRemarks());
            saleBack.setCredit(credit);
            leftCredit = leftCredit +credit;
            saleBack.setLeftCredit(leftCredit);
            saleBack.setIsBack(true);
            productImeSaleRepository.save(saleBack);

            productIme.setProductImeSaleId(null);
            productImeRepository.save(productIme);
        }


    }

    public String export(ProductImeSaleQuery productImeSaleQuery) {

        Workbook workbook = new SXSSFWorkbook(10000);

        List<SimpleExcelSheet> simpleExcelSheetList = Lists.newArrayList();
        List<SimpleExcelColumn> productImeSaleColumnList = Lists.newArrayList();
        productImeSaleColumnList.add(new SimpleExcelColumn(workbook, "productImeIme", "串码"));
        productImeSaleColumnList.add(new SimpleExcelColumn(workbook, "productImeProductName", "货品型号"));
        productImeSaleColumnList.add(new SimpleExcelColumn(workbook, "productImeMeid", "MEID"));
        productImeSaleColumnList.add(new SimpleExcelColumn(workbook, "shopAreaName", "办事处"));
        productImeSaleColumnList.add(new SimpleExcelColumn(workbook, "shopOfficeName", "考核区域"));
        productImeSaleColumnList.add(new SimpleExcelColumn(workbook, "shopName", "门店"));
        productImeSaleColumnList.add(new SimpleExcelColumn(workbook, "shopAreaType", "区域类型"));
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


        List<ProductImeSaleDto> productImeSaleDtoList = findPage(new PageRequest(0,10000), productImeSaleQuery).getContent();
        simpleExcelSheetList.add(new SimpleExcelSheet("核销列表", productImeSaleDtoList, productImeSaleColumnList));

        SimpleExcelBook simpleExcelBook = new SimpleExcelBook(workbook,"核销列表"+ LocalDate.now()+".xlsx", simpleExcelSheetList);
        ByteArrayInputStream byteArrayInputStream= ExcelUtils.doWrite(simpleExcelBook.getWorkbook(),simpleExcelBook.getSimpleExcelSheets());
        GridFSFile gridFSFile = tempGridFsTemplate.store(byteArrayInputStream,simpleExcelBook.getName(),"application/octet-stream; charset=utf-8", RequestUtils.getDbObject());
        return StringUtils.toString(gridFSFile.getId());

    }
}
