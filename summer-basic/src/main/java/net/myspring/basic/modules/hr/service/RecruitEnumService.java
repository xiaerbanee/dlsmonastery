package net.myspring.basic.modules.hr.service;


import com.google.common.collect.Maps;
import net.myspring.basic.common.enums.RecruitTypeEnum;
import net.myspring.basic.common.utils.CacheUtils;
import net.myspring.basic.modules.hr.domain.Account;
import net.myspring.basic.modules.hr.domain.RecruitEnum;
import net.myspring.basic.modules.hr.dto.RecruitEnumDto;
import net.myspring.basic.modules.hr.repository.AccountRepository;
import net.myspring.basic.modules.hr.repository.RecruitEnumRepository;
import net.myspring.basic.modules.hr.web.form.RecruitEnumForm;
import net.myspring.basic.modules.hr.web.query.RecruitEnumQuery;
import net.myspring.util.mapper.BeanUtil;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = false)
public class RecruitEnumService {

    @Autowired
    private RecruitEnumRepository recruitEnumRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private CacheUtils cacheUtils;


    public Page<RecruitEnumDto> findPage(Pageable pageable, RecruitEnumQuery recruitEnumQuery){
        Page<RecruitEnumDto> page=recruitEnumRepository.findPage(pageable,recruitEnumQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    public List<String> findValueByCategory(String category){
        List<RecruitEnum> recruitEnumList=recruitEnumRepository.findByCategoryAndEnabledIsTrue(category);
        return recruitEnumList.stream().map(RecruitEnum::getValue).collect(Collectors.toList());
    }

    @Transactional
    public List<RecruitEnum> save(RecruitEnumForm recruitEnumForm){
        boolean setValue=false;
        Map<String,Account> accountMap= Maps.newHashMap();
        if(RecruitTypeEnum.初试人.name().equals(recruitEnumForm.getCategory())||RecruitTypeEnum.复试人.name().equals(recruitEnumForm.getCategory())){
            setValue=true;
            accountMap=accountRepository.findMap(recruitEnumForm.getList());
        }
        List<RecruitEnum> recruitEnumList=recruitEnumRepository.findByCategoryAndEnabledIsTrue(recruitEnumForm.getCategory());
        Map<String,RecruitEnum> recruitEnumMap=recruitEnumList.stream().collect(Collectors.toMap(RecruitEnum::getValue,RecruitEnum->RecruitEnum));
        for(String value:recruitEnumForm.getList()){
            RecruitEnum recruitEnum=recruitEnumMap.get(value);
            if(recruitEnum==null){
                recruitEnum=new RecruitEnum();
            }else {
                recruitEnumList.remove(recruitEnum);
            }
            if(setValue){
                Account account=accountMap.get(value);
                recruitEnum.setLabel(account.getLoginName());
            }else {
                recruitEnum.setLabel(value);
            }
            recruitEnum.setValue(value);
            recruitEnum.setRemarks(recruitEnumForm.getRemarks());
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

    @Transactional
    public void delete(String id){
        recruitEnumRepository.logicDelete(id);
    }

}
