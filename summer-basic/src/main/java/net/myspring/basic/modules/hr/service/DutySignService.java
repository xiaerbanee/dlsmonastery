package net.myspring.basic.modules.hr.service;

import com.google.common.collect.Lists;
import net.myspring.basic.common.enums.AuditTypeEnum;
import net.myspring.basic.common.utils.CacheUtils;
import net.myspring.basic.common.utils.SecurityUtils;
import net.myspring.basic.modules.hr.domain.DutySign;
import net.myspring.basic.modules.hr.dto.DutySignDto;
import net.myspring.basic.modules.hr.mapper.DutySignMapper;
import net.myspring.basic.modules.hr.web.form.DutySignForm;
import net.myspring.basic.modules.hr.web.query.DutySignQuery;
import net.myspring.util.excel.SimpleExcelColumn;
import net.myspring.util.excel.SimpleExcelSheet;
import net.myspring.util.mapper.BeanMapper;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
    @Autowired
    private SecurityUtils securityUtils;

    public DutySign save(DutySignForm dutySignForm) {
        dutySignForm.setDutyDate(LocalDate.now());
        dutySignForm.setDutyTime(LocalTime.now());
        dutySignForm.setStatus(AuditTypeEnum.APPLY.getValue());
        dutySignForm.setEmployeeId(securityUtils.getEmployeeId());
        dutySignMapper.save(dutySignForm);
        return dutySignForm;
    }

    public Page<DutySignDto> findPage(Pageable pageable, DutySignQuery dutySignQuery) {
        Page<DutySign> page = dutySignMapper.findPage(pageable,dutySignQuery);
        Page<DutySignDto> dutySignDtoPage= BeanMapper.convertPage(page,DutySignDto.class);
        cacheUtils.initCacheInput(dutySignDtoPage.getContent());
        return dutySignDtoPage;
    }

    public List<DutySignDto> findByFilter(DutySignQuery dutySignQuery) {
        List<DutySign> dutySignList =  dutySignMapper.findByFilter(dutySignQuery);
        List<DutySignDto> dutySignDtoList=BeanMapper.convertDtoList(dutySignList,DutySignDto.class);
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

    public DutySignDto findDto(String id) {
        DutySign dutySign =findOne(id);
        DutySignDto dutySignDto=BeanMapper.convertDto(dutySign,DutySignDto.class);
        cacheUtils.initCacheInput(dutySignDto);
        return dutySignDto;
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
