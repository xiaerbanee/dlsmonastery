package net.myspring.basic.modules.sys.service;

import com.google.common.collect.Maps;
import net.myspring.basic.common.utils.CacheUtils;
import net.myspring.basic.modules.sys.domain.Office;
import net.myspring.basic.modules.sys.domain.OfficeRule;
import net.myspring.basic.modules.sys.dto.OfficeRuleDto;
import net.myspring.basic.modules.sys.repository.OfficeRepository;
import net.myspring.basic.modules.sys.repository.OfficeRuleRepository;
import net.myspring.basic.modules.sys.web.form.OfficeRuleForm;
import net.myspring.basic.modules.sys.web.query.OfficeRuleQuery;
import net.myspring.common.constant.TreeConstant;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.reflect.ReflectionUtil;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by wangzm on 2017/4/22.
 */
@Service
@Transactional(readOnly = true)
public class OfficeRuleService {
    
    @Autowired
    private OfficeRuleRepository officeRuleRepository;
    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private OfficeRepository officeRepository;
    
    public Page<OfficeRuleDto> findPage(Pageable pageable, OfficeRuleQuery officeRuleQuery) {
        Page<OfficeRuleDto> page = officeRuleRepository.findPage(pageable, officeRuleQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }



    public List<OfficeRuleDto> findAll(){
        List<OfficeRule> officeRuleList=officeRuleRepository.findByEnabledIsTrue();
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

    @Transactional
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

    @Transactional
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

    public Map<String,Map<String,String>> getOfficeRuleMap(){
        Map<String,Map<String,String>> map= Maps.newHashMap();
        List<OfficeRule> officeRuleList=officeRuleRepository.findByEnabledIsTrue();
        List<Office> officeList=officeRepository.findByEnabledIsTrue();
        Map<String,List<Office>> officeMap= CollectionUtil.extractToMapList(officeList,"officeRuleId");
        for(OfficeRule officeRule:officeRuleList){
            map.put(officeRule.getCode(),Maps.newHashMap());
            Map<String,String> ruleMap=Maps.newHashMap();
            for(Office childOffice:officeList){
                List<Office> offices=officeMap.get(officeRule.getId());
                String value=getValueByParentIds(offices,childOffice.getParentIds());
                ruleMap.put(childOffice.getId(),value==null?childOffice.getName():value);
            }
            map.put(officeRule.getCode(),ruleMap);
        }
        return map;
    }

    private String getValueByParentIds(List<Office> officeList ,String parentIds){
        for(Office office:officeList){
            if(parentIds.contains(','+office.getId()+',')){
                return office.getName();
            }
        }
        return null;
    }
}
