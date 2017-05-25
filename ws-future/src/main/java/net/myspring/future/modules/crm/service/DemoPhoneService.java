package net.myspring.future.modules.crm.service;

import net.myspring.future.common.enums.AuditStatusEnum;
import net.myspring.future.common.utils.CacheUtils;
import net.myspring.future.modules.crm.repository.DemoPhoneRepository;
import net.myspring.future.modules.crm.domain.DemoPhone;
import net.myspring.future.modules.crm.dto.DemoPhoneDto;
import net.myspring.future.modules.crm.mapper.DemoPhoneMapper;
import net.myspring.future.modules.crm.web.form.DemoPhoneForm;
import net.myspring.future.modules.crm.web.query.DemoPhoneQuery;
import net.myspring.util.mapper.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DemoPhoneService {

    @Autowired
    private DemoPhoneMapper demoPhoneMapper;
    @Autowired
    private DemoPhoneRepository demoPhoneRepository;

    @Autowired
    private CacheUtils cacheUtils;

    public DemoPhoneDto findOne(DemoPhoneDto demoPhoneDto) {
        if(!demoPhoneDto.isCreate()){
            DemoPhone demoPhone = demoPhoneRepository.findOne(demoPhoneDto.getId());
            demoPhoneDto = BeanUtil.map(demoPhone,DemoPhoneDto.class);
            cacheUtils.initCacheInput(demoPhoneDto);
        }
        return demoPhoneDto;
    }


    public Page<DemoPhoneDto> findPage(Pageable pageable, DemoPhoneQuery demoPhoneQuery) {
        Page<DemoPhoneDto> page = demoPhoneMapper.findPage(pageable, demoPhoneQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    public void delete(String id) {
        demoPhoneRepository.logicDeleteOne(id);
    }

    public DemoPhone save(DemoPhoneForm demoPhoneForm) {
        DemoPhone demoPhone = BeanUtil.map(demoPhoneForm,DemoPhone.class);
        demoPhone.setStatus(AuditStatusEnum.已通过.name());
        demoPhoneRepository.save(demoPhone);
        return demoPhone;
    }

//    public List<SimpleExcelSheet> findSimpleExcelSheets(Workbook workbook, Map<String, Object> map) {
//        List<SimpleExcelColumn> simpleExcelColumnList = Lists.newArrayList();
//        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "createdDate", "申请日期"));
//        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "shop.office.name", "办事处"));
//        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "shop.district.province", "地区"));
//        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "shop.district.city", "城市"));
//        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "shop.name", "门店"));
//        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "employee.name", "使用人姓名"));
//        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "employee.mobilePhone", "联系电话"));
//        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "shop.name", "用途"));
//        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "demoPhoneType.name", "演示机型"));
//        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "productIme.product.name", "货品机型"));
//        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "productIme.ime2", "机身条码"));
//        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "productIme.ime", "IMEI码"));
//        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "remarks", "备注"));
//
//        List<SimpleExcelSheet> simpleExcelSheetList = Lists.newArrayList();
//        List<DemoPhone> demoPhones=demoPhoneMapper.findByFilter(map);
//        SimpleExcelSheet simpleExcelSheet = new SimpleExcelSheet("演示用机", demoPhones, simpleExcelColumnList);
//        simpleExcelSheetList.add(simpleExcelSheet);
//        return simpleExcelSheetList;
//    }

}
