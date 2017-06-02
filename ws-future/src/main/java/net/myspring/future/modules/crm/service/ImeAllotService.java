package net.myspring.future.modules.crm.service;

import com.google.common.collect.Lists;
import com.mongodb.gridfs.GridFSFile;
import net.myspring.common.exception.ServiceException;
import net.myspring.future.common.enums.AuditStatusEnum;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.basic.repository.DepotRepository;
import net.myspring.future.modules.crm.domain.ImeAllot;
import net.myspring.future.modules.crm.domain.ProductIme;
import net.myspring.future.modules.crm.dto.ExpressOrderDto;
import net.myspring.future.modules.crm.dto.ImeAllotDto;
import net.myspring.future.modules.crm.dto.ProductImeSaleDto;
import net.myspring.future.modules.crm.dto.StoreAllotDto;
import net.myspring.future.modules.crm.repository.ImeAllotRepository;
import net.myspring.future.modules.crm.repository.ProductImeRepository;
import net.myspring.future.modules.crm.web.form.ImeAllotForm;
import net.myspring.future.modules.crm.web.query.ImeAllotQuery;
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
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ImeAllotService {

    @Autowired
    private ImeAllotRepository imeAllotRepository;

    @Autowired
    private DepotRepository depotRepository;
    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private GridFsTemplate tempGridFsTemplate;

    @Autowired
    private ProductImeRepository productImeRepository;



//    public void save(ImeAllot imeAllot) {
//        List<String> imeList = StringUtils.getSplitList(imeAllot.getImeStr(), "");
//        List<ProductIme> productImeList = productImeRepository.findByImeList(imeList);
//        for(ProductIme productIme:productImeList) {
//            if(!productIme.getDepotId().equals(imeAllot.getToDepotId())) {
//                ImeAllot allot = new ImeAllot();
//                allot.setProductImeId(productIme.getId());
//                allot.setFromDepotId(productIme.getDepotId());
//                allot.setToDepotId(imeAllot.getToDepotId());
//                allot.setRemarks(imeAllot.getRemarks());
//                allot.setCrossArea(crossArea(imeAllot.getToDepotId(),productIme.getDepotId()));
//                imeAllotRepository.save(allot);
//                productIme.setDepotId(imeAllot.getToDepotId());
//                productImeRepository.save(productIme);
//            }
//        }
//    }

    public void logicDelete(String id) {
        imeAllotRepository.logicDelete(id);
    }

    public ImeAllot findOne(String id) {
        ImeAllot imeAllot = imeAllotRepository.findOne(id);
        return imeAllot;
    }


    public List<ImeAllot> findByProductImeId(String productImeId){
        return imeAllotRepository.findByProductImeId(productImeId);
    }


    private boolean getCrossArea(String toDepotId,String productImeDepotId){
        //TODO 需要实现是否跨地区
        return true;
    }

    public Page<ImeAllotDto> findPage(Pageable pageable, ImeAllotQuery imeAllotQuery) {
        Page<ImeAllotDto> page = imeAllotRepository.findPage(pageable,imeAllotQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    public void audit(String[] ids, boolean pass) {

        List<ImeAllot> imeAllots = imeAllotRepository.findAll(Arrays.asList(ids));
        for(ImeAllot imeAllot:imeAllots) {
            if(!AuditStatusEnum.申请中.name().equals(imeAllot.getStatus())){
                throw new ServiceException("不能审核状态不为‘申请中’的记录（ime："+productImeRepository.findOne(imeAllot.getProductImeId()).getIme()+"）");
            }
            imeAllot.setAuditRemarks("");
            imeAllot.setAuditBy(RequestUtils.getAccountId());
            imeAllot.setAuditDate(LocalDateTime.now());
            if(pass){
                ProductIme productIme =productImeRepository.findOne(imeAllot.getProductImeId());

                if(productIme.getDepotId().equals(imeAllot.getFromDepotId())) {
                    imeAllot.setStatus(AuditStatusEnum.已通过.toString());
                    if(productIme.getProductImeSaleId()==null && !productIme.getDepotId().equals(imeAllot.getToDepotId())) {
                        productIme.setDepotId(imeAllot.getToDepotId());
                        productIme.setRetailShopId(imeAllot.getToDepotId());
                        productImeRepository.save(productIme);
                    }
                } else {
                    imeAllot.setStatus(AuditStatusEnum.未通过.toString());
                    imeAllot.setAuditRemarks("串码已不在门店：" + depotRepository.findOne(imeAllot.getFromDepotId()).getName());
                }
            }else{
                imeAllot.setStatus(AuditStatusEnum.未通过.name());
            }
            imeAllotRepository.save(imeAllot);

        }
    }

    public String checkForImeAllot(List<String> imeList, boolean  checkAccess) {

        StringBuffer sb = new StringBuffer();
        List<ProductIme> productImeList = productImeRepository.findByImeList(imeList);
        Map<String, ProductIme> imeMap = CollectionUtil.extractToMap(productImeList, "ime");
        for(String ime : imeList){
            ProductIme productIme = imeMap.get(ime);
            if(productIme == null) {
                sb.append("串码："+ime+"在系统中不存在；");
            } else {
                if(productIme.getProductImeSaleId() !=null) {
                    sb.append("串码："+ime+"已核销；");
                }else if(productIme.getProductImeUploadId() != null) {
                    sb.append("串码："+ime+"已上报；");
                }else{
                    if(checkAccess) {
                        //TODO 需要增加判断，判断门店是否可以核销
//                        if(!DepotUtils.isAccess(productIme.getDepot(), true)) {
//                            message.addText("message_ime_allot_no_depot",productIme.getDepot().getName(),"message_ime_allot_no_allot_permission");
//                        }
                    }
                }
            }
        }

        return sb.toString();

    }

    public void save(ImeAllotForm imeAllotForm) {
        List<String> imeList = imeAllotForm.getImeList();

        List<ProductIme> productImes = productImeRepository.findByImeList(imeList);

        for(ProductIme productIme:productImes) {
            if(productIme.getProductImeSaleId()==null && productIme.getDepotId()!=null&&!productIme.getDepotId().equals(imeAllotForm.getToDepotId())) {
                Depot fromDepot = depotRepository.findOne(productIme.getDepotId());
                if(true) { //TODO 修改判断逻辑DepotUtils.isAccess(fromDepot,true)
                    ImeAllot imeAllot = new ImeAllot();
                    imeAllot.setProductImeId(productIme.getId());
                    imeAllot.setFromDepotId(fromDepot.getId());
                    imeAllot.setToDepotId(imeAllotForm.getToDepotId());
                    imeAllot.setStatus(AuditStatusEnum.已通过.name());
                    imeAllot.setRemarks(imeAllotForm.getRemarks());
                    imeAllot.setCrossArea(getCrossArea(imeAllotForm.getToDepotId(),productIme.getDepotId()));
                    imeAllot.setAuditRemarks(imeAllotForm.getRemarks());
                    imeAllot.setAuditBy(RequestUtils.getAccountId());
                    imeAllot.setAuditDate(LocalDateTime.now());
                    imeAllotRepository.save(imeAllot);

                    productIme.setDepotId(imeAllotForm.getToDepotId());
                    productIme.setRetailShopId(imeAllotForm.getToDepotId());
                    productImeRepository.save(productIme);

                } else {
                    ImeAllot imeAllot = new ImeAllot();
                    imeAllot.setProductImeId(productIme.getId());
                    imeAllot.setFromDepotId(fromDepot.getId());
                    imeAllot.setToDepotId(imeAllotForm.getToDepotId());
                    imeAllot.setStatus(AuditStatusEnum.申请中.name());
                    imeAllot.setRemarks(imeAllotForm.getRemarks());
                    imeAllot.setCrossArea(getCrossArea(imeAllotForm.getToDepotId(),productIme.getDepotId()));
                    imeAllotRepository.save(imeAllot);
                }
            }
        }
    }

    public String export(ImeAllotQuery imeAllotQuery) {

        Workbook workbook = new SXSSFWorkbook(10000);

        List<SimpleExcelSheet> simpleExcelSheetList = Lists.newArrayList();
        List<SimpleExcelColumn> imeAllotColumnList = Lists.newArrayList();
        imeAllotColumnList.add(new SimpleExcelColumn(workbook, "fromDepotName", "调拨前门店"));
        imeAllotColumnList.add(new SimpleExcelColumn(workbook, "toDepotName", "调拨后门店"));
        imeAllotColumnList.add(new SimpleExcelColumn(workbook, "productImeIme", "串码"));
        imeAllotColumnList.add(new SimpleExcelColumn(workbook, "productImeProductName", "产品名称"));
        imeAllotColumnList.add(new SimpleExcelColumn(workbook, "createdDate", "调拨时间"));
        imeAllotColumnList.add(new SimpleExcelColumn(workbook, "createdByName", "调拨人"));
        imeAllotColumnList.add(new SimpleExcelColumn(workbook, "remarks", "备注"));


        List<ImeAllotDto> imeAllotDtoList = findPage(new PageRequest(0,10000), imeAllotQuery).getContent();
        simpleExcelSheetList.add(new SimpleExcelSheet("调拨列表", imeAllotDtoList, imeAllotColumnList));

        SimpleExcelBook simpleExcelBook = new SimpleExcelBook(workbook,"调拨列表"+ LocalDate.now()+".xlsx", simpleExcelSheetList);
        ByteArrayInputStream byteArrayInputStream= ExcelUtils.doWrite(simpleExcelBook.getWorkbook(),simpleExcelBook.getSimpleExcelSheets());
        GridFSFile gridFSFile = tempGridFsTemplate.store(byteArrayInputStream,simpleExcelBook.getName(),"application/octet-stream; charset=utf-8", RequestUtils.getDbObject());
        return StringUtils.toString(gridFSFile.getId());

    }

    public ImeAllotDto findDto(String id) {
        ImeAllotDto imeAllotDto = imeAllotRepository.findDto(id);
        cacheUtils.initCacheInput(imeAllotDto);
        return imeAllotDto;
    }
}
