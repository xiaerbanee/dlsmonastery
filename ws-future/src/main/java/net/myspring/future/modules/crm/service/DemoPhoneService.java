package net.myspring.future.modules.crm.service;

import com.google.common.collect.Lists;
import net.myspring.future.common.enums.AuditStatusEnum;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.basic.manager.DepotManager;
import net.myspring.future.modules.crm.domain.DemoPhone;
import net.myspring.future.modules.crm.dto.DemoPhoneDto;
import net.myspring.future.modules.crm.repository.DemoPhoneRepository;
import net.myspring.future.modules.crm.web.form.DemoPhoneForm;
import net.myspring.future.modules.crm.web.query.DemoPhoneQuery;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.excel.ExcelUtils;
import net.myspring.util.excel.SimpleExcelBook;
import net.myspring.util.excel.SimpleExcelColumn;
import net.myspring.util.excel.SimpleExcelSheet;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.time.LocalDateUtils;
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
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class DemoPhoneService {

    @Autowired
    private DemoPhoneRepository demoPhoneRepository;
    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private DepotManager depotManager;

    public DemoPhoneDto findOne(DemoPhoneDto demoPhoneDto) {
        if(!demoPhoneDto.isCreate()){
            DemoPhone demoPhone = demoPhoneRepository.findOne(demoPhoneDto.getId());
            demoPhoneDto = BeanUtil.map(demoPhone,DemoPhoneDto.class);
            cacheUtils.initCacheInput(demoPhoneDto);
        }
        return demoPhoneDto;
    }


    public Page<DemoPhoneDto> findPage(Pageable pageable, DemoPhoneQuery demoPhoneQuery) {
        demoPhoneQuery.setDepotIdList(depotManager.filterDepotIds(RequestUtils.getAccountId()));
        Page<DemoPhoneDto> page = demoPhoneRepository.findPage(pageable, demoPhoneQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    @Transactional
    public void delete(String id) {
        demoPhoneRepository.logicDelete(id);
    }

    @Transactional
    public String save(DemoPhoneForm demoPhoneForm) {
        Map<String,DemoPhone> demoPhoneMap = CollectionUtil.extractToMap(demoPhoneRepository.findAll(),"productImeId");
        if(demoPhoneMap.get(demoPhoneForm.getProductImeId())!=null){
            return "此串码已在演示用机中存在";
        }
        DemoPhone demoPhone = BeanUtil.map(demoPhoneForm,DemoPhone.class);
        demoPhone.setStatus(AuditStatusEnum.已通过.name());
        demoPhoneRepository.save(demoPhone);
        return null;
    }

    public SimpleExcelBook export(DemoPhoneQuery demoPhoneQuery) {
        Workbook workbook = new SXSSFWorkbook(10000);

        List<SimpleExcelColumn> simpleExcelColumnList = Lists.newArrayList();
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "createdDate", "申请日期"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "areaName", "办事处"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "shopName", "门店"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "employeeName", "使用人姓名"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "demoPhoneType", "演示机型"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "ime", "IMEI码"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "remarks", "备注"));

        List<DemoPhoneDto> demoPhoneDtos=findPage(new PageRequest(0,10000),demoPhoneQuery).getContent();
        cacheUtils.initCacheInput(demoPhoneDtos);
        SimpleExcelSheet simpleExcelSheet = new SimpleExcelSheet("演示用机", demoPhoneDtos, simpleExcelColumnList);
        ExcelUtils.doWrite(workbook, simpleExcelSheet);
        return new SimpleExcelBook(workbook,"演示用机列表"+ LocalDateUtils.format(LocalDate.now())+".xlsx",simpleExcelSheet);
    }

}
