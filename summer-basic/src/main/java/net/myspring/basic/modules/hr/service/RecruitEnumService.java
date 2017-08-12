package net.myspring.basic.modules.hr.service;


import net.myspring.basic.modules.hr.domain.RecruitEnum;
import net.myspring.basic.modules.hr.dto.RecruitEnumDto;
import net.myspring.basic.modules.hr.repository.RecruitEnumRepository;
import net.myspring.basic.modules.hr.web.form.RecruitEnumForm;
import net.myspring.basic.modules.hr.web.query.RecruitEnumQuery;
import net.myspring.util.mapper.BeanUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RecruitEnumService {

    @Autowired
    private RecruitEnumRepository recruitEnumRepository;


    public Page<RecruitEnumDto> findPage(Pageable pageable, RecruitEnumQuery recruitEnumQuery){
        Page<RecruitEnumDto> page=recruitEnumRepository.findPage(pageable,recruitEnumQuery);
        return page;
    }

    public List<String> findValueByCategory(String category){
        List<RecruitEnum> recruitEnumList=recruitEnumRepository.findByCategory(category);
        return recruitEnumList.stream().map(RecruitEnum::getValue).collect(Collectors.toList());
    }

    public List<RecruitEnum> save(RecruitEnumForm recruitEnumForm){
        List<RecruitEnum> recruitEnumList=recruitEnumRepository.findByCategory(recruitEnumForm.getCategory());
        Map<String,RecruitEnum> recruitEnumMap=recruitEnumList.stream().collect(Collectors.toMap(RecruitEnum::getValue,RecruitEnum->RecruitEnum));
        for(String value:recruitEnumForm.getList()){
            RecruitEnum recruitEnum=recruitEnumMap.get(value);
            if(recruitEnum==null){
                recruitEnum=new RecruitEnum();
            }else {
                recruitEnumList.remove(recruitEnum);
            }
            recruitEnum.setValue(value);
            recruitEnum.setCategory(recruitEnumForm.getCategory());
            recruitEnumRepository.save(recruitEnum);
        }
        if(CollectionUtils.isNotEmpty(recruitEnumList)){
            recruitEnumRepository.loginDeleteByIdList(recruitEnumList.stream().map(RecruitEnum::getId).collect(Collectors.toList()));
        }
        return recruitEnumList;
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
