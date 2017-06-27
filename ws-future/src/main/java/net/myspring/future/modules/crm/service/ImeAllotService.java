package net.myspring.future.modules.crm.service;

import com.google.common.collect.Lists;
import net.myspring.common.exception.ServiceException;
import net.myspring.future.common.enums.AuditStatusEnum;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.basic.manager.DepotManager;
import net.myspring.future.modules.basic.repository.DepotRepository;
import net.myspring.future.modules.crm.domain.ImeAllot;
import net.myspring.future.modules.crm.domain.ProductIme;
import net.myspring.future.modules.crm.dto.ImeAllotDto;
import net.myspring.future.modules.crm.repository.ImeAllotRepository;
import net.myspring.future.modules.crm.repository.ProductImeRepository;
import net.myspring.future.modules.crm.web.form.ImeAllotBatchForm;
import net.myspring.future.modules.crm.web.form.ImeAllotForm;
import net.myspring.future.modules.crm.web.form.ImeAllotSimpleForm;
import net.myspring.future.modules.crm.web.query.ImeAllotQuery;
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

import java.io.ByteArrayInputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
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
    private DepotManager depotManager;
    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private ProductImeRepository productImeRepository;

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

        StringBuilder sb = new StringBuilder();
        List<ProductIme> productImeList = productImeRepository.findByEnabledIsTrueAndCompanyIdAndImeIn(RequestUtils.getCompanyId(), imeList);
        Map<String, ProductIme> imeMap = CollectionUtil.extractToMap(productImeList, "ime");
        for(String ime : imeList){
            ProductIme productIme = imeMap.get(ime);
            if(productIme == null) {
                sb.append("串码：").append(ime).append("在系统中不存在；");
            } else {
                Depot depot = depotRepository.findOne(productIme.getDepotId());
                if(productIme.getProductImeSaleId() !=null) {
                    sb.append("串码：").append(ime).append("已核销；");
                }else if(productIme.getProductImeUploadId() != null) {
                    sb.append("串码：").append(ime).append("已上报；");
                }else{
                    if(checkAccess) {
                        if(!depotManager.isAccess(depot.getId(), true,RequestUtils.getAccountId(),RequestUtils.getOfficeId())) {
                            sb.append("您没有串码：").append(ime).append("所在门店：").append(depot.getName()).append("的调拨权限，将自动生成调拨申请单；");
                        }
                    }
                }
            }
        }

        return sb.toString();

    }

    public void allot(ImeAllotForm imeAllotForm) {

        List<String> imeList = imeAllotForm.getImeList();

        List<ProductIme> productImes = productImeRepository.findByEnabledIsTrueAndCompanyIdAndImeIn(RequestUtils.getCompanyId(), imeList);
        Depot toDepot = depotRepository.findOne(imeAllotForm.getToDepotId());

        for(ProductIme productIme:productImes) {
            if(productIme.getProductImeSaleId()==null && productIme.getDepotId()!=null&&!productIme.getDepotId().equals(imeAllotForm.getToDepotId())) {
                Depot fromDepot = depotRepository.findOne(productIme.getDepotId());

                if(depotManager.isAccess(fromDepot.getId(), true,RequestUtils.getAccountId(),RequestUtils.getOfficeId())) {
                    ImeAllot imeAllot = new ImeAllot();
                    imeAllot.setProductImeId(productIme.getId());
                    imeAllot.setFromDepotId(fromDepot.getId());
                    imeAllot.setToDepotId(imeAllotForm.getToDepotId());
                    imeAllot.setStatus(AuditStatusEnum.已通过.name());
                    imeAllot.setRemarks(imeAllotForm.getRemarks());
                    imeAllot.setCrossArea(!fromDepot.getAreaId().equals(toDepot.getAreaId()));
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
                    imeAllot.setCrossArea(!fromDepot.getAreaId().equals(toDepot.getAreaId()));
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
        return null;

    }

    public ImeAllotDto findDto(String id) {
        ImeAllotDto imeAllotDto = imeAllotRepository.findDto(id);
        cacheUtils.initCacheInput(imeAllotDto);
        return imeAllotDto;
    }

    public List<String> findToDepotNameList() {
        List<Depot> depotList = depotRepository.findByEnabledIsTrueAndDepotShopIdIsNotNullAndCompanyId(RequestUtils.getCompanyId());
        return CollectionUtil.extractToList(depotList, "name");
    }

    public void batchAllot(ImeAllotBatchForm imeAllotBatchForm) {

        List<String> imeList =  CollectionUtil.extractToList(imeAllotBatchForm.getImeAllotSimpleFormList(),"ime");
        String errMsg = checkForImeAllot(imeList, true);
        if(StringUtils.isNotBlank(errMsg)){
            throw new ServiceException(errMsg);
        }

        for (ImeAllotSimpleForm imeAllotSimpleForm : imeAllotBatchForm.getImeAllotSimpleFormList()) {

            Depot toDepot=depotRepository.findByEnabledIsTrueAndCompanyIdAndName(RequestUtils.getCompanyId(), imeAllotSimpleForm.getToDepotName());
            if(toDepot == null){
                throw new ServiceException("调拨后门店在本公司无效或者不存在");
            }

            ProductIme productIme = productImeRepository.findByEnabledIsTrueAndCompanyIdAndIme(RequestUtils.getCompanyId(), imeAllotSimpleForm.getIme());
            if(productIme == null){
                throw new ServiceException("串码："+imeAllotSimpleForm.getIme()+" 在本公司中无效或者不存在");
            }

            ImeAllotForm imeAllotForm = new ImeAllotForm();
            imeAllotForm.setImeStr(imeAllotSimpleForm.getIme());
            imeAllotForm.setToDepotId(toDepot.getId());
            imeAllotForm.setRemarks(imeAllotSimpleForm.getRemarks());
            allot(imeAllotForm);
        }

    }
}
