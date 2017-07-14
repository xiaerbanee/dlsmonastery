package net.myspring.future.modules.crm.service;

import com.google.common.collect.Lists;
import net.myspring.common.constant.CharConstant;
import net.myspring.common.exception.ServiceException;
import net.myspring.future.common.enums.AuditStatusEnum;
import net.myspring.future.common.enums.PriceChangeStatusEnum;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.basic.manager.DepotManager;
import net.myspring.future.modules.basic.repository.DepotRepository;
import net.myspring.future.modules.crm.domain.*;
import net.myspring.future.modules.crm.dto.ProductImeDto;
import net.myspring.future.modules.crm.repository.*;
import net.myspring.future.modules.crm.dto.PriceChangeDto;
import net.myspring.future.modules.crm.dto.PriceChangeImeDto;
import net.myspring.future.modules.crm.web.form.PriceChangeImeForm;
import net.myspring.future.modules.crm.web.form.PriceChangeImeUploadForm;
import net.myspring.future.modules.crm.web.query.PriceChangeImeQuery;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.excel.ExcelUtils;
import net.myspring.util.excel.SimpleExcelBook;
import net.myspring.util.excel.SimpleExcelColumn;
import net.myspring.util.excel.SimpleExcelSheet;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class PriceChangeImeService {

    @Autowired
    private PriceChangeImeRepository priceChangeImeRepository;
    @Autowired
    private ProductImeRepository productImeRepository;
    @Autowired
    private PriceChangeRepository priceChangeRepository;
    @Autowired
    private DepotRepository depotRepository;
    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private DepotManager depotManager;

    public PriceChangeImeDto findOne(String id){
        PriceChangeImeDto priceChangeImeDto = new PriceChangeImeDto();
        if(StringUtils.isNotBlank(id)){
            PriceChangeIme priceChangeIme=priceChangeImeRepository.findOne(id);
            priceChangeImeDto = BeanUtil.map(priceChangeIme,PriceChangeImeDto.class);
            if(priceChangeIme.getPriceChangeId()!=null){
                priceChangeImeDto.setPriceChangeName(priceChangeRepository.findOne(priceChangeIme.getPriceChangeId()).getName());
            }
            if(priceChangeIme.getProductImeId()!=null){
                priceChangeImeDto.setProductId(productImeRepository.findOne(priceChangeIme.getProductImeId()).getProductId());
                priceChangeImeDto.setIme(productImeRepository.findOne(priceChangeIme.getProductImeId()).getIme());
            }

            cacheUtils.initCacheInput(priceChangeImeDto);
        }

        return priceChangeImeDto;
    }

    public Page<PriceChangeImeDto> findPage(Pageable pageable, PriceChangeImeQuery priceChangeImeQuery){
        priceChangeImeQuery.setDepotIdList(depotManager.filterDepotIds(RequestUtils.getAccountId()));
        Page<PriceChangeImeDto> page=priceChangeImeRepository.findPage(pageable,priceChangeImeQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    public PriceChangeImeForm getForm(PriceChangeImeForm priceChangeImeForm){
        List<PriceChange> priceChange = priceChangeRepository.findByPriceChangeIme(PriceChangeStatusEnum.抽检中.name());
        priceChangeImeForm.getExtra().put("priceChangeDtos",BeanUtil.map(priceChange, PriceChangeDto.class));
        return priceChangeImeForm;
    }

    @Transactional
    public void delete(String id){
        priceChangeImeRepository.logicDelete(id);
    }

    @Transactional
    public void imageUpload(PriceChangeImeForm priceChangeImeForm){
        PriceChangeIme priceChangeIme = priceChangeImeRepository.findOne(priceChangeImeForm.getId());
        priceChangeIme.setImage(priceChangeImeForm.getImage());
        priceChangeIme.setStatus(AuditStatusEnum.申请中.name());
        priceChangeImeRepository.save(priceChangeIme);
    }

    @Transactional
    public void audit(PriceChangeImeForm priceChangeImeForm){
        if(priceChangeImeForm.getPass()!=null) {
            PriceChangeIme priceChangeIme = priceChangeImeRepository.findOne(priceChangeImeForm.getId());
            if (priceChangeImeForm.getPass()) {
                priceChangeIme.setStatus(AuditStatusEnum.已通过.name());
            } else {
                priceChangeIme.setStatus(AuditStatusEnum.未通过.name());
            }
            priceChangeIme.setAuditRemarks(priceChangeImeForm.getAuditRemarks());
            priceChangeIme.setAuditDate(LocalDateTime.now());
            priceChangeIme.setAuditBy(RequestUtils.getAccountId());
            priceChangeImeRepository.save(priceChangeIme);
        }
    }

    @Transactional
    public void save(PriceChangeImeUploadForm priceChangeImeUploadForm){
        String priceChangeId = priceChangeImeUploadForm.getPriceChangeId();
        List<String> productTypeIds = priceChangeRepository.findProductTypeIdsById(priceChangeId);
        List<List<String>> imeUploadList = priceChangeImeUploadForm.getImeUploadList();
        if(CollectionUtil.isEmpty(imeUploadList)){
            throw new ServiceException("未上传任何调价串码，保存失败");
        }
        List<String> shopNameList = new ArrayList<>();
        List<String> imeList = new ArrayList<>();
        List<String> remarksList = new ArrayList<>();
        for(List<String> row:imeUploadList){
            String shopName = StringUtils.toString(row.get(0)).trim();
            String ime = StringUtils.toString(row.get(1)).trim();
            String remarks = StringUtils.toString(row.get(2)).trim();
            if(StringUtils.isNotBlank(shopName)){
                shopNameList.add(shopName);
            }
            if(StringUtils.isNotBlank(ime)){
                imeList.add(ime);
            }
            remarksList.add(remarks);
        }
        if(CollectionUtil.isEmpty(shopNameList)||CollectionUtil.isEmpty(imeList)){
            throw new ServiceException("门店或者串码列表为空，保存失败");
        }
        if(shopNameList.size()!=imeList.size()){
            throw new ServiceException("门店与串码不一一匹配，长度不同，保存失败");
        }

        //检查门店、串码在系统中是否存在

        List<String> existShops = CollectionUtil.extractToList(depotRepository.findByNameList(shopNameList),"name");
        List<String> existImes = CollectionUtil.extractToList(productImeRepository.findByImeList(imeList),"ime");
        if(existImes.size()!=imeList.size()){
            String notExist = "";
            for(String ime:imeList){
                if(!existImes.contains(ime)){
                    notExist += StringUtils.join(ime,CharConstant.COMMA);
                }
            }
            for (String shopName:shopNameList){
                if(!existShops.contains(shopName)){
                    notExist +=StringUtils.join(shopName,CharConstant.COMMA);
                }
            }
            throw new ServiceException(notExist+"不存在,保存失败");
        }else{
            List<PriceChangeIme> priceChangeImes = new ArrayList<>();
            List<ProductImeDto> productImeDtos = productImeRepository.findDtoListByImeList(existImes);
            List<String> needSaveProductTypeIds = CollectionUtil.extractToList(productImeDtos,"productTypeId");
            List<String> needSaveProductImeIds = CollectionUtil.extractToList(productImeDtos,"id");
            if(productImeDtos == null){
                throw new ServiceException("未找到任何串码所对应的机型,保存失败");
            }
            for(String needSaveProductTypeId:needSaveProductTypeIds){
                if(!productTypeIds.contains(needSaveProductTypeId)){
                    throw new ServiceException("输入的串码中含有不是调价项目中的产品型号,保存失败");
                }
            }
            List<PriceChangeIme> existPriceChangeIme = priceChangeImeRepository.findByPriceChangeId(priceChangeId);
            List<String> existProductImeIds = CollectionUtil.extractToList(existPriceChangeIme,"productImeId");
            for(String productImeId:needSaveProductImeIds){
                if(existProductImeIds.contains(productImeId)){
                    throw new ServiceException("输入的串码中有串码已存在所选择的调价项目下,保存失败");
                }
            }
            Map<String,Depot> depotMap = CollectionUtil.extractToMap(depotRepository.findByNameList(shopNameList),"name");
            for(Integer i = 0;i<imeList.size();i++){
                PriceChangeIme priceChangeIme = new PriceChangeIme();
                priceChangeIme.setPriceChangeId(priceChangeId);
                priceChangeIme.setProductImeId(productImeDtos.get(i).getId());
                priceChangeIme.setShopId(depotMap.get(shopNameList.get(i)).getId());
                priceChangeIme.setSaleDate(productImeDtos.get(i).getProductImeSaleCreatedDate());
                priceChangeIme.setUploadDate(productImeDtos.get(i).getProductImeUploadCreatedDate());
                priceChangeIme.setStatus(AuditStatusEnum.申请中.name());
                priceChangeIme.setRemarks(remarksList.get(i));
                priceChangeIme.setIsCheck(false);
                priceChangeImes.add(priceChangeIme);
            }
            priceChangeImeRepository.save(priceChangeImes);
        }

    }

    public SimpleExcelBook export(PriceChangeImeQuery priceChangeImeQuery) {
        Workbook workbook = new SXSSFWorkbook(10000);

        List<SimpleExcelColumn> simpleExcelColumnList = Lists.newArrayList();

        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "priceChangeName", "调价项目"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "areaName", "办事处"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "officeName", "考核区域"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "shopName", "门店"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "productName", "货品型号"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "ime", "串码"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "saleDate", "销售日期"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "auditDate", "审批时间"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "status", "状态"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "createdByName", "创建人"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "createdDate", "创建时间"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "lastModifiedByName", "最后修改人"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "lastModifiedDate", "最后修改时间"));

        List<PriceChangeImeDto> priceChangeImeDtoList = findPage(new PageRequest(0,10000),priceChangeImeQuery).getContent();
        cacheUtils.initCacheInput(priceChangeImeDtoList);
        SimpleExcelSheet simpleExcelSheet = new SimpleExcelSheet("调价串码", priceChangeImeDtoList, simpleExcelColumnList);
        ExcelUtils.doWrite(workbook, simpleExcelSheet);
        return new SimpleExcelBook(workbook,"调价串码列表"+ LocalDateUtils.format(LocalDate.now())+".xlsx",simpleExcelSheet);
    }
}
