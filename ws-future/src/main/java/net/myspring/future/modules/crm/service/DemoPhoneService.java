package net.myspring.future.modules.crm.service;

import com.google.common.collect.Lists;
import com.mongodb.gridfs.GridFSFile;
import net.myspring.future.common.enums.AuditStatusEnum;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.common.utils.RequestUtils;
import net.myspring.future.modules.crm.domain.DemoPhone;
import net.myspring.future.modules.crm.dto.DemoPhoneDto;
import net.myspring.future.modules.crm.repository.DemoPhoneRepository;
import net.myspring.future.modules.crm.web.form.DemoPhoneForm;
import net.myspring.future.modules.crm.web.query.DemoPhoneQuery;
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
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class DemoPhoneService {

    @Autowired
    private DemoPhoneRepository demoPhoneRepository;

    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private GridFsTemplate tempGridFsTemplate;

    public DemoPhoneDto findOne(DemoPhoneDto demoPhoneDto) {
        if(!demoPhoneDto.isCreate()){
            DemoPhone demoPhone = demoPhoneRepository.findOne(demoPhoneDto.getId());
            demoPhoneDto = BeanUtil.map(demoPhone,DemoPhoneDto.class);
            cacheUtils.initCacheInput(demoPhoneDto);
        }
        return demoPhoneDto;
    }


    public Page<DemoPhoneDto> findPage(Pageable pageable, DemoPhoneQuery demoPhoneQuery) {
        Page<DemoPhoneDto> page = demoPhoneRepository.findPage(pageable, demoPhoneQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    public void delete(String id) {
        demoPhoneRepository.logicDelete(id);
    }

    public DemoPhone save(DemoPhoneForm demoPhoneForm) {
        DemoPhone demoPhone = BeanUtil.map(demoPhoneForm,DemoPhone.class);
        demoPhone.setStatus(AuditStatusEnum.已通过.name());
        demoPhoneRepository.save(demoPhone);
        return demoPhone;
    }

    public String findSimpleExcelSheets(Workbook workbook, DemoPhoneQuery demoPhoneQuery) {

        List<SimpleExcelColumn> simpleExcelColumnList = Lists.newArrayList();
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "createdDate", "申请日期"));
        /*simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "shop.office.name", "办事处"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "shop.district.province", "地区"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "shop.district.city", "城市"));*/
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "shopName", "门店"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "employeeName", "使用人姓名"));
        /*simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "employee.mobilePhone", "联系电话"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "shop.name", "用途"));*/
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "demoPhoneType", "演示机型"));
        /*simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "productIme.product.name", "货品机型"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "productIme.ime2", "机身条码"));*/
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "ime", "IMEI码"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "remarks", "备注"));

        List<DemoPhoneDto> demoPhoneDtos=demoPhoneRepository.findByFilter(demoPhoneQuery);
        cacheUtils.initCacheInput(demoPhoneDtos);
        SimpleExcelSheet simpleExcelSheet = new SimpleExcelSheet("演示用机", demoPhoneDtos, simpleExcelColumnList);
        SimpleExcelBook simpleExcelBook = new SimpleExcelBook(workbook,"演示用机"+ UUID.randomUUID()+".xlsx",simpleExcelSheet);
        ByteArrayInputStream byteArrayInputStream= ExcelUtils.doWrite(simpleExcelBook.getWorkbook(),simpleExcelBook.getSimpleExcelSheets());
        GridFSFile gridFSFile = tempGridFsTemplate.store(byteArrayInputStream,simpleExcelBook.getName(),"application/octet-stream; charset=utf-8", RequestUtils.getDbObject());
        return StringUtils.toString(gridFSFile.getId());
    }

}
