package net.myspring.basic.modules.hr.service;

import com.google.common.collect.Lists;
import net.myspring.common.enums.AuditTypeEnum;
import net.myspring.basic.common.utils.CacheUtils;
import net.myspring.basic.common.utils.RequestUtils;
import net.myspring.basic.modules.hr.domain.DutySign;
import net.myspring.basic.modules.hr.dto.DutySignDto;
import net.myspring.basic.modules.hr.mapper.DutySignMapper;
import net.myspring.basic.modules.hr.web.form.DutySignForm;
import net.myspring.basic.modules.hr.web.query.DutySignQuery;
import net.myspring.util.excel.SimpleExcelColumn;
import net.myspring.util.excel.SimpleExcelSheet;
import net.myspring.util.mapper.BeanUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Created by liuj on 2016/11/18.
 */
@Service
@Transactional
public class DutySignService {

    @Autowired
    private DutySignMapper dutySignMapper;
    @Autowired
    private CacheUtils cacheUtils;


    public DutySign save(DutySignForm dutySignForm) {
        dutySignForm.setDutyDate(LocalDate.now());
        dutySignForm.setDutyTime(LocalTime.now());
        dutySignForm.setStatus(AuditTypeEnum.APPLYING.toString());
        dutySignForm.setEmployeeId(RequestUtils.getRequestEntity().getEmployeeId());
        DutySign dutySign=BeanUtil.map(dutySignForm,DutySign.class);
        dutySignMapper.save(dutySign);
        return dutySign;
    }

    public Page<DutySignDto> findPage(Pageable pageable, DutySignQuery dutySignQuery) {
        Page<DutySignDto> page = dutySignMapper.findPage(pageable,dutySignQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    public List<DutySignDto> findByFilter(DutySignQuery dutySignQuery) {
        List<DutySign> dutySignList =  dutySignMapper.findByFilter(dutySignQuery);
        List<DutySignDto> dutySignDtoList= BeanUtil.map(dutySignList,DutySignDto.class);
        cacheUtils.initCacheInput(dutySignDtoList);
        return  dutySignDtoList;
    }

    public void logicDeleteOne(String id) {
        dutySignMapper.logicDeleteOne(id);
    }

    public DutySign findOne(String id) {
        DutySign dutySign = dutySignMapper.findOne(id);
        return dutySign;
    }

    public DutySignForm getForm(DutySignForm dutySignForm) {
        if(!dutySignForm.isCreate()){
            DutySign dutySign =findOne(dutySignForm.getId());
            dutySignForm= BeanUtil.map(dutySign,DutySignForm.class);
            cacheUtils.initCacheInput(dutySignForm);
        }
        return dutySignForm;
    }

    public SimpleExcelSheet findSimpleExcelSheet(Workbook workbook, DutySignQuery dutySignQuery){
        List<DutySign> dutySignList = dutySignMapper.findByFilter(dutySignQuery);
        List<SimpleExcelColumn> simpleExcelColumnList=Lists.newArrayList();
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"employee.name","姓名"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"dutyDate","日期"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"extendMap.week","星期"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"dutyTime","时间"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"address","地址"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"uuid","手机识别码"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"netType","网络"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"attachment","照片网址"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"remarks","备注"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"status","状态"));
        SimpleExcelSheet simpleExcelSheet = new SimpleExcelSheet("签到列表",dutySignList,simpleExcelColumnList);
        return simpleExcelSheet;
    }
}
