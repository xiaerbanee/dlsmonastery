package net.myspring.basic.modules.hr.service;

import net.myspring.basic.modules.hr.domain.Recruit;
import net.myspring.basic.modules.hr.dto.RecruitDto;
import net.myspring.basic.modules.hr.repository.RecruitRepository;
import net.myspring.basic.modules.hr.web.form.RecruitForm;
import net.myspring.basic.modules.hr.web.query.RecruitQuery;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.reflect.ReflectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class RecruitService {

    @Autowired
    private RecruitRepository recruitRepository;

    public Page<RecruitDto> findPage(Pageable pageable, RecruitQuery recruitQuery){
        Page<RecruitDto> page=recruitRepository.findPage(pageable,recruitQuery);
        return page;
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
