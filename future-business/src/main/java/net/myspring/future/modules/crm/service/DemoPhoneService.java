package net.myspring.future.modules.crm.service;

import com.google.common.collect.Lists;
import net.myspring.future.modules.crm.domain.*;
import net.myspring.future.modules.crm.mapper.DemoPhoneMapper;
import net.myspring.future.modules.crm.mapper.ProductImeMapper;
import net.myspring.util.excel.SimpleExcelColumn;
import net.myspring.util.excel.SimpleExcelSheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DemoPhoneService {

    @Autowired
    private DemoPhoneMapper demoPhoneMapper;
    @Autowired
    private ProductImeMapper productImeMapper;

    public DemoPhone findOne(String id) {
        DemoPhone demoPhone = demoPhoneMapper.findOne(id);
        return demoPhone;
    }


    public Page<DemoPhone> findPage(Pageable pageable, Map<String, Object> map) {
        Page<DemoPhone> page = demoPhoneMapper.findPage(pageable, map);
        return page;
    }

    public void delete(DemoPhone demoPhone) {
       demoPhoneMapper.logicDeleteOne(demoPhone.getId());
    }

    public DemoPhone save(DemoPhone demoPhone) {
        demoPhone.setStatus("");
        demoPhoneMapper.save(demoPhone);
        return demoPhone;
    }

    public List<SimpleExcelSheet> findSimpleExcelSheets(Workbook workbook, Map<String, Object> map) {
        List<SimpleExcelColumn> simpleExcelColumnList = Lists.newArrayList();
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "createdDate", "申请日期"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "shop.office.name", "办事处"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "shop.district.province", "地区"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "shop.district.city", "城市"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "shop.name", "门店"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "employee.name", "使用人姓名"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "employee.mobilePhone", "联系电话"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "shop.name", "用途"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "demoPhoneType.name", "演示机型"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "productIme.product.name", "货品机型"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "productIme.ime2", "机身条码"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "productIme.ime", "IMEI码"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "remarks", "备注"));

        List<SimpleExcelSheet> simpleExcelSheetList = Lists.newArrayList();
        List<DemoPhone> demoPhones=demoPhoneMapper.findByFilter(map);
        SimpleExcelSheet simpleExcelSheet = new SimpleExcelSheet("演示用机", demoPhones, simpleExcelColumnList);
        simpleExcelSheetList.add(simpleExcelSheet);
        return simpleExcelSheetList;
    }

}
