package net.myspring.basic.modules.hr.service;


import net.myspring.basic.modules.hr.domain.Recruit;
import net.myspring.basic.modules.hr.domain.RecruitEnum;
import net.myspring.basic.modules.hr.dto.RecruitDto;
import net.myspring.basic.modules.hr.dto.RecruitEnumDto;
import net.myspring.basic.modules.hr.repository.RecruitEnumRepository;
import net.myspring.basic.modules.hr.web.form.RecruitEnumForm;
import net.myspring.basic.modules.hr.web.form.RecruitForm;
import net.myspring.basic.modules.hr.web.query.RecruitEnumQuery;
import net.myspring.basic.modules.hr.web.query.RecruitQuery;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.reflect.ReflectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class RecruitEnumService {

    @Autowired
    private RecruitEnumRepository recruitEnumRepository;


    public Page<RecruitEnumDto> findPage(Pageable pageable, RecruitEnumQuery recruitEnumQuery){
        Page<RecruitEnumDto> page=recruitEnumRepository.findPage(pageable,recruitEnumQuery);
        return page;
    }

    public RecruitEnum save(RecruitEnumForm recruitEnumForm){
        RecruitEnum RecruitEnum;
        if(recruitEnumForm.isCreate()){
            RecruitEnum= BeanUtil.map(recruitEnumForm,RecruitEnum.class);
            recruitEnumRepository.save(RecruitEnum);
        }else {
            RecruitEnum=recruitEnumRepository.findOne(recruitEnumForm.getId());
            ReflectionUtil.copyProperties(recruitEnumForm,RecruitEnum);
            recruitEnumRepository.save(RecruitEnum);
        }
        return RecruitEnum;
    }

    public  RecruitEnumDto findOne(RecruitEnumDto recruitEnumDto){
        if(!recruitEnumDto.isCreate()){
            RecruitEnum recruitEnum=recruitEnumRepository.findOne(recruitEnumDto.getId());
            recruitEnumDto=BeanUtil.map(recruitEnum,RecruitEnumDto.class);
        }
        return recruitEnumDto;
    }

    public void delete(String id){
        recruitEnumRepository.logicDelete(id);
    }

}
