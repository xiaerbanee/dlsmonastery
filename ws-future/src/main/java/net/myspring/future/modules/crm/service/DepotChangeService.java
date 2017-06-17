package net.myspring.future.modules.crm.service;

import com.google.common.collect.Lists;
import com.mongodb.gridfs.GridFSFile;
import net.myspring.future.common.enums.AuditStatusEnum;
import net.myspring.future.common.enums.DepotChangeEnum;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.basic.domain.Depot;
import net.myspring.future.modules.basic.domain.Pricesystem;
import net.myspring.future.modules.basic.dto.PricesystemDto;
import net.myspring.future.modules.basic.repository.DepotRepository;
import net.myspring.future.modules.basic.repository.PricesystemRepository;
import net.myspring.future.modules.crm.dto.DepotChangeDto;
import net.myspring.future.modules.crm.repository.DepotChangeRepository;
import net.myspring.future.modules.crm.domain.DepotChange;
import net.myspring.future.modules.crm.web.form.DepotChangeAuditForm;
import net.myspring.future.modules.crm.web.form.DepotChangeForm;
import net.myspring.future.modules.crm.web.query.DepotChangeQuery;
import net.myspring.util.excel.ExcelUtils;
import net.myspring.util.excel.SimpleExcelBook;
import net.myspring.util.excel.SimpleExcelColumn;
import net.myspring.util.excel.SimpleExcelSheet;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.text.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class DepotChangeService {

    @Autowired
    private DepotChangeRepository depotChangeRepository;
    @Autowired
    private DepotRepository depotRepository;
    @Autowired
    private PricesystemRepository pricesystemRepository;
    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private GridFsTemplate tempGridFsTemplate;

    public Page<DepotChangeDto> findPage(Pageable pageable, DepotChangeQuery depotChangeQuery){
        Page<DepotChangeDto> page = depotChangeRepository.findPage(pageable,depotChangeQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    public DepotChangeDto findOne(String id){
        DepotChangeDto depotChangeDto;
        if(StringUtils.isBlank(id)){
            depotChangeDto = new DepotChangeDto();
        }else{
            depotChangeDto = BeanUtil.map(depotChangeRepository.findOne(id),DepotChangeDto.class);
            cacheUtils.initCacheInput(depotChangeDto);
        }
        return depotChangeDto;
    }

    public void save(DepotChangeForm depotChangeForm){
        DepotChange depotChange;
        if(depotChangeForm.isCreate()){
            depotChange = BeanUtil.map(depotChangeForm,DepotChange.class);
            depotChange.setStatus(AuditStatusEnum.申请中.name());
            depotChangeRepository.save(depotChange);
        }
    }


    public void logicDelete(String id) {
        depotChangeRepository.logicDelete(id);
    }

    public DepotChangeForm getForm(DepotChangeForm depotChangeForm){
        depotChangeForm.getExtra().put("types", DepotChangeEnum.getList());
        depotChangeForm.getExtra().put("pricesystems",BeanUtil.map(pricesystemRepository.findAllEnabled(), PricesystemDto.class));
        return depotChangeForm;
    }

    public void audit(DepotChangeAuditForm depotChangeAuditForm){

        DepotChange depotChange = depotChangeRepository.findOne(depotChangeAuditForm.getId());
        Depot depot = depotRepository.findOne(depotChange.getDepotId());
        if(depotChangeAuditForm.getPass()){
            depotChange.setStatus(AuditStatusEnum.已通过.name());
            if(DepotChangeEnum.名称.name().equalsIgnoreCase(depotChange.getType())){
                depot.setName(depotChange.getNewValue());
            }
            if(DepotChangeEnum.价格体系.name().equalsIgnoreCase(depotChange.getType())){
                Pricesystem pricesystem = pricesystemRepository.findByName(depotChange.getNewValue());
                depot.setPricesystemId(pricesystem.getId());
            }
            if(DepotChangeEnum.信用额度.name().equalsIgnoreCase(depotChange.getType())){
                depot.setCredit(new BigDecimal(depotChange.getNewValue()));
            }
            if(DepotChangeEnum.是否让利.name().equalsIgnoreCase(depotChange.getType())){
                depot.setRebate(Boolean.TRUE.toString().equalsIgnoreCase(depotChange.getNewValue()));
            }
            if(DepotChangeEnum.有无导购.name().equalsIgnoreCase(depotChange.getType())){
                //TODO导购
            }
            depotRepository.save(depot);
        }else{
            depotChange.setStatus(AuditStatusEnum.未通过.name());
        }
        depotChange.setAuditRemarks(depotChangeAuditForm.getAuditRemarks());
        depotChangeRepository.save(depotChange);
    }


    public String findSimpleExcelSheets(Workbook workbook, DepotChangeQuery depotChangeQuery) {

        List<SimpleExcelColumn> simpleExcelColumnList = Lists.newArrayList();
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "depotName", "门店"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "type", "调整项"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "newValue", "最新数据"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "lastModifiedByName", "修改人"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "lastModifiedDate", "修改时间"));

        List<DepotChangeDto> depotChangeDtos = depotChangeRepository.findFilter(depotChangeQuery);
        cacheUtils.initCacheInput(depotChangeDtos);

        SimpleExcelSheet simpleExcelSheet = new SimpleExcelSheet("仓库调整", depotChangeDtos, simpleExcelColumnList);
        SimpleExcelBook simpleExcelBook = new SimpleExcelBook(workbook,"仓库调整"+ UUID.randomUUID()+".xlsx",simpleExcelSheet);
        ByteArrayInputStream byteArrayInputStream= ExcelUtils.doWrite(simpleExcelBook.getWorkbook(),simpleExcelBook.getSimpleExcelSheets());
        GridFSFile gridFSFile = tempGridFsTemplate.store(byteArrayInputStream,simpleExcelBook.getName(),"application/octet-stream; charset=utf-8", RequestUtils.getDbObject());
        return StringUtils.toString(gridFSFile.getId());
    }

}
