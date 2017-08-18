package net.myspring.basic.modules.hr.service;

import com.google.common.collect.Lists;
import net.myspring.basic.common.utils.CacheUtils;
import net.myspring.basic.modules.hr.domain.Recruit;
import net.myspring.basic.modules.hr.dto.RecruitDto;
import net.myspring.basic.modules.hr.dto.RecruitReportDto;
import net.myspring.basic.modules.hr.repository.RecruitRepository;
import net.myspring.basic.modules.hr.web.form.RecruitForm;
import net.myspring.basic.modules.hr.web.query.RecruitQuery;
import net.myspring.util.excel.ExcelUtils;
import net.myspring.util.excel.SimpleExcelBook;
import net.myspring.util.excel.SimpleExcelColumn;
import net.myspring.util.excel.SimpleExcelSheet;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.reflect.ReflectionUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;


@Service
public class RecruitService {

    @Autowired
    private RecruitRepository recruitRepository;

    @Autowired
    private CacheUtils cacheUtils;

    public Page<RecruitDto> findPage(Pageable pageable, RecruitQuery recruitQuery){
        Page<RecruitDto> page=recruitRepository.findPage(pageable,recruitQuery);
        return page;
    }


    public SimpleExcelBook exportReport(RecruitQuery recruitQuery){
        Workbook workbook = new SXSSFWorkbook(10000);
        List<RecruitReportDto> recruitReportDtoList = recruitRepository.findReportFilter(recruitQuery);
        cacheUtils.initCacheInput(recruitReportDtoList);
        List<SimpleExcelColumn> simpleExcelColumnList = Lists.newArrayList();
      /*  simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"year","年份"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"month","月份"));*/
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"exportTime","日期"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "applyPositionName", "岗位"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"toRegisterCount","通知面试人数"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"firstCount","初试人数"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"secondCount","复试人数"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"hireCount","录用人数"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"auditCount","资审人数"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"onjobCount","到岗人数"));

        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"registerRate","面试到达率"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"hireRate","录用率"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"onjobRate","到岗率"));

        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"onjob","到岗人员名单"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"afterOnjob","后期到岗人员名单"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"abandon","放弃/淘汰"));

     //   if(recruitQuery.getType().equals("岗位报表")) {
         //   simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "applyPositionName", "岗位"));


            SimpleExcelSheet simpleExcelSheet = new SimpleExcelSheet("导出数据",recruitReportDtoList,simpleExcelColumnList);
            ExcelUtils.doWrite(workbook,simpleExcelSheet);
            return new SimpleExcelBook(workbook,"岗位报表"+ LocalDate.now()+".xlsx",simpleExcelSheet);
       // }
       /* if(recruitQuery.getType().equals("渠道报表")) {
            simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"applyFrom","渠道"));

            SimpleExcelSheet simpleExcelSheet = new SimpleExcelSheet("导出数据",recruitDtoList,simpleExcelColumnList);
            ExcelUtils.doWrite(workbook,simpleExcelSheet);
            return new SimpleExcelBook(workbook,"渠道报表"+ LocalDate.now()+".xlsx",simpleExcelSheet);
        }

        if(recruitQuery.getType().equals("业务岗位渠道报表")) {
            simpleExcelColumnList.add(new SimpleExcelColumn(workbook, "applyFrom", "渠道"));

            SimpleExcelSheet simpleExcelSheet = new SimpleExcelSheet("导出数据",recruitDtoList,simpleExcelColumnList);
            ExcelUtils.doWrite(workbook,simpleExcelSheet);
            return new SimpleExcelBook(workbook,"业务岗位渠道报表"+ LocalDate.now()+".xlsx",simpleExcelSheet);
        }
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"onjobList","到岗人员名单"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"afterOnjobList","后期到岗人员名单"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"abandonList","放弃/淘汰"));


        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"registerToSecondCount","推复试人数"));//负责人 邀约
        //面试、复试
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"registerFirstCount","初试到达人数"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"toSecondCount","推复试人数"));
     //   simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"leaderName","复试人数"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"hireCount","通过复试"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"auditCount","资审人数"));


        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"firstRate","初试通过率"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"toSecondRate","复试到达率"));
        simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"secondRate","复试通过率"));

     //   if(recruitQuery.getType().equals("负责人报表")) {
            simpleExcelColumnList.add(new SimpleExcelColumn(workbook,"mainPerson","负责人"));

            SimpleExcelSheet simpleExcelSheet = new SimpleExcelSheet("导出数据",recruitDtoList,simpleExcelColumnList);
            ExcelUtils.doWrite(workbook,simpleExcelSheet);
            return new SimpleExcelBook(workbook,"负责人报表"+ LocalDate.now()+".xlsx",simpleExcelSheet);
     //   }
*/
      /*
        SimpleExcelSheet simpleExcelSheet = new SimpleExcelSheet("导出数据",recruitDtoList,simpleExcelColumnList);
        ExcelUtils.doWrite(workbook,simpleExcelSheet);
        return new SimpleExcelBook(workbook,"员工详情"+ LocalDate.now()+".xlsx",simpleExcelSheet);*/
    }



    public Recruit save(RecruitForm recruitForm){
        Recruit recruit;
        if(recruitForm.isCreate()){
            recruit= BeanUtil.map(recruitForm,Recruit.class);
            recruitRepository.save(recruit);
        }else {
            recruit=recruitRepository.findOne(recruitForm.getId());
            ReflectionUtil.copyProperties(recruitForm,recruit);
            recruitRepository.save(recruit);
        }
        return recruit;
    }

    public  RecruitDto findOne(RecruitDto recruitDto){
        if(!recruitDto.isCreate()){
            Recruit recruit=recruitRepository.findOne(recruitDto.getId());
            recruitDto=BeanUtil.map(recruit,RecruitDto.class);
        }
        return recruitDto;
    }

    @Transactional
    public void delete(String id){
        recruitRepository.logicDelete(id);
    }
}
