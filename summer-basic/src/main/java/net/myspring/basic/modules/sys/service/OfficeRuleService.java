package net.myspring.basic.modules.sys.service;

import net.myspring.basic.common.utils.CacheUtils;
import net.myspring.basic.modules.sys.domain.OfficeRule;
import net.myspring.basic.modules.sys.dto.OfficeRuleDto;
import net.myspring.basic.modules.sys.repository.OfficeRuleRepository;
import net.myspring.basic.modules.sys.web.form.OfficeRuleForm;
import net.myspring.basic.modules.sys.web.query.OfficeRuleQuery;
import net.myspring.common.constant.TreeConstant;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.reflect.ReflectionUtil;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by wangzm on 2017/4/22.
 */
@Service
public class OfficeRuleService {
    
    @Autowired
    private OfficeRuleRepository officeRuleRepository;
    @Autowired
    private CacheUtils cacheUtils;
    
    public Page<OfficeRuleDto> findPage(Pageable pageable, OfficeRuleQuery officeRuleQuery) {
        Page<OfficeRuleDto> page = officeRuleRepository.findPage(pageable, officeRuleQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }



    public List<OfficeRuleDto> findAll(){
        List<OfficeRule> officeRuleList=officeRuleRepository.findAllEnabled();
        List<OfficeRuleDto> officeRuleDtoList=BeanUtil.map(officeRuleList,OfficeRuleDto.class);
        return officeRuleDtoList;
    }

    public OfficeRuleDto findOne(OfficeRuleDto officeRuleDto) {
        if(!officeRuleDto.isCreate()){
            OfficeRule officeRule = officeRuleRepository.findOne(officeRuleDto.getId());
            officeRuleDto= BeanUtil.map(officeRule,OfficeRuleDto.class);
            cacheUtils.initCacheInput(officeRuleDto);
        }
        return officeRuleDto;
    }

    public OfficeRule findOne(String id){
        OfficeRule officeRule=officeRuleRepository.findOne(id);
        return officeRule;
    }

    public OfficeRule save(OfficeRuleForm officeRuleForm) {
        OfficeRule officeRule;
        setParentIdsAndLevel(officeRuleForm);
        if (officeRuleForm.isCreate()) {
            officeRule=BeanUtil.map(officeRuleForm,OfficeRule.class);
            officeRuleRepository.save(officeRule);
        } else {
            officeRule = officeRuleRepository.findOne(officeRuleForm.getId());
            ReflectionUtil.copyProperties(officeRuleForm,officeRule);
            officeRuleRepository.save(officeRule);

        }
        return officeRule;
    }

    public void logicDelete(OfficeRuleForm officeRuleForm) {
        officeRuleRepository.logicDelete(officeRuleForm.getId());
    }

    private void setParentIdsAndLevel(OfficeRuleForm officeRuleForm){
        if(StringUtils.isBlank(officeRuleForm.getParentId())){
            officeRuleForm.setParentIds(TreeConstant.ROOT_PARENT_IDS);
            officeRuleForm.setLevel(1);
        }else {
            OfficeRule parent=officeRuleRepository.findOne(officeRuleForm.getParentId());
            if(parent!=null){
                String parentIds=parent.getParentIds()+officeRuleForm.getParentId();
                officeRuleForm.setParentIds(parentIds);
                officeRuleForm.setLevel(parent.getLevel()==null?1:parent.getLevel()+1);
            }
        }
    }
}
