package net.myspring.basic.modules.hr.service;

import net.myspring.basic.common.utils.CacheUtils;
import net.myspring.basic.common.utils.SecurityUtils;
import net.myspring.basic.modules.hr.domain.Recruit;
import net.myspring.basic.modules.hr.dto.RecruitDto;
import net.myspring.basic.modules.hr.mapper.RecruitMapper;
import net.myspring.basic.modules.hr.web.form.RecruitForm;
import net.myspring.basic.modules.hr.web.query.RecruitQuery;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RecruitService {

    @Autowired
    private RecruitMapper recruitMapper;
    @Autowired
    private CacheUtils cacheUtils;

    public Page<RecruitDto> findPage(Pageable pageable, RecruitQuery recruitQuery) {
        Page<Recruit> page = recruitMapper.findPage(pageable, recruitQuery);
        Page<RecruitDto> recruitDtoPage= BeanUtil.map(page,RecruitDto.class);
        cacheUtils.initCacheInput(recruitDtoPage.getContent());
        return recruitDtoPage;
    }

    public Recruit findByMobilePhone(String mobilePhone){
        return recruitMapper.findByMobilePhone(mobilePhone);
    }

    public List<String> findNameByIds(List<String> ids){
        List<Recruit> recruitList = recruitMapper.findByIds(ids);
        return CollectionUtil.extractToList(recruitList,"name");
    }

    public Recruit findOne(String id){
        Recruit recruit = recruitMapper.findOne(id);
        return recruit;
    }

    public RecruitForm findForm(RecruitForm recruitForm){
        if(!recruitForm.isCreate()){
            Recruit recruit = recruitMapper.findOne(recruitForm.getId());
            recruitForm= BeanUtil.map(recruit,RecruitForm.class);
            cacheUtils.initCacheInput(recruitForm);
        }
        return recruitForm;
    }

    public Recruit save(RecruitForm recruitForm){
        Recruit recruit;
        if(recruitForm.isCreate()){
            recruit=BeanUtil.map(recruitForm,Recruit.class);
            recruitMapper.save(recruit);
        }else{
            recruitMapper.updateForm(recruitForm);
            recruit=recruitMapper.findOne(recruitForm.getId());
        }
        return recruit;
    }

    @Transactional
    public void logicDeleteOne(String id){
        recruitMapper.logicDeleteOne(id);
    }

    @Transactional
    public void update (Recruit recruit){
        List<Recruit> recruitList = recruitMapper.findByIds(recruit.getIds());
        for(Recruit item:recruitList){
            if(StringUtils.isNotEmpty(recruit.getApplyPositionName())){
                item.setApplyPositionName(recruit.getApplyPositionName());
            }
            if(StringUtils.isNotEmpty(recruit.getApplyFrom())){
                item.setApplyFrom(recruit.getApplyFrom());
            }
            if(StringUtils.isNotEmpty(recruit.getWorkArea())){
                item.setWorkArea(recruit.getWorkArea());
            }
            if(StringUtils.isNotEmpty(recruit.getWorkCategory())){
                item.setWorkCategory(recruit.getWorkCategory());
            }
            if(StringUtils.isNotEmpty(recruit.getEducation())){
                item.setEducation(recruit.getEducation());
            }
            if(recruit.getEntryRealDate()!=null){
                item.setEntryRealDate(recruit.getEntryRealDate());
            }
            recruitMapper.update(item);
        }
    }
}
