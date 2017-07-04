package net.myspring.basic.modules.sys.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.basic.common.enums.OfficeTypeEnum;
import net.myspring.basic.common.utils.CacheUtils;
import net.myspring.basic.common.utils.RequestUtils;
import net.myspring.basic.modules.hr.domain.OfficeLeader;
import net.myspring.basic.modules.hr.repository.OfficeLeaderRepository;
import net.myspring.basic.modules.sys.domain.Office;
import net.myspring.basic.modules.sys.domain.OfficeBusiness;
import net.myspring.basic.modules.sys.domain.OfficeRule;
import net.myspring.basic.modules.sys.dto.OfficeDto;
import net.myspring.basic.modules.sys.dto.OfficeRuleDto;
import net.myspring.basic.modules.sys.manager.OfficeManager;
import net.myspring.basic.modules.sys.repository.OfficeBusinessRepository;
import net.myspring.basic.modules.sys.repository.OfficeRepository;
import net.myspring.basic.modules.sys.repository.OfficeRuleRepository;
import net.myspring.basic.modules.sys.web.form.OfficeForm;
import net.myspring.basic.modules.sys.web.query.OfficeQuery;
import net.myspring.common.constant.CharConstant;
import net.myspring.common.constant.TreeConstant;
import net.myspring.common.response.RestResponse;
import net.myspring.common.tree.TreeNode;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.reflect.ReflectionUtil;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class OfficeService {

    @Autowired
    private OfficeRepository officeRepository;
    @Autowired
    private OfficeManager officeManager;
    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private OfficeRuleRepository officeRuleRepository;
    @Autowired
    private OfficeBusinessRepository officeBusinessRepository;
    @Autowired
    private OfficeLeaderRepository officeLeaderRepository;


    public List<Office> findByOfficeRuleName(String officeRuleName) {
        return officeRepository.findByOfficeRuleName(officeRuleName);
    }

    public Page<OfficeDto> findPage(Pageable pageable, OfficeQuery officeQuery) {
        Page<OfficeDto> page = officeRepository.findPage(pageable, officeQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    public List<Office> findByParentIdsLike(String parentId) {
        List<Office> officeList = officeRepository.findByParentIdsLike("%,"+parentId+",%");
        return officeList;
    }

    public List<String> getTopOfficeListByIdList(){
        List<Office> officeList=officeRepository.findByEnabledIsTrueAndIdIn(RequestUtils.getOfficeIdList());
        List<OfficeRule> officeRuleList=officeRuleRepository.findAllEnabled();
        Map<String,OfficeRule> officeRuleMap=officeRuleList.stream().collect(Collectors.toMap(OfficeRule::getId,OfficeRule -> OfficeRule));
        Map<Integer, List<String>> levelMap = Maps.newTreeMap();
        for(Office office:officeList){
            OfficeRule officeRule=officeRuleMap.get(office.getOfficeRuleId());
            Integer key = Integer.valueOf(officeRule.getLevel());
            if (!levelMap.containsKey(key)) {
                levelMap.put(key, Lists.newArrayList());
            }
            levelMap.get(key).add(office.getId());
        }
        List<Integer> keyList = Lists.newArrayList(levelMap.keySet());
        return levelMap.get(keyList.get(0));
    }


    public List<Office> findAll() {
        return officeRepository.findAll();
    }

    public List<String> getSameAreaByOfficeId(String officeId){
        List<Office> sameAreaByOfficeId = officeRepository.findSameAreaByOfficeId(officeId);
        return CollectionUtil.extractToList(sameAreaByOfficeId,"id");
    }

    public Office findOne(String id) {
        return officeRepository.findOne(id);
    }

    public OfficeDto findOne(OfficeDto officeDto) {
        if (!officeDto.isCreate()) {
            Office office = officeRepository.findOne(officeDto.getId());
            officeDto = BeanUtil.map(office, OfficeDto.class);
            if (OfficeTypeEnum.职能部门.name().equals(office.getType())) {
                List<OfficeBusiness> businessOffices = officeBusinessRepository.findBusinessIdById(office.getId());
                officeDto.setBusinessIdList(CollectionUtil.extractToList(businessOffices,"businessOfficeId"));
            }
            List<OfficeLeader> officeLeaderList = officeLeaderRepository.findByOfficeId(officeDto.getId());
            officeDto.setLeaderIdList(CollectionUtil.extractToList(officeLeaderList, "leaderId"));
            cacheUtils.initCacheInput(officeDto);
        }
        return officeDto;
    }

    public OfficeRuleDto findTopOfficeRule(){
        OfficeRule topOfficeRule = officeRuleRepository.findTopOfficeRule(new PageRequest(0,1)).getContent().get(0);
        return BeanUtil.map(topOfficeRule,OfficeRuleDto.class);
    }

    public Map<String,List<String>> getLastRuleMapByOfficeId(String officeId){
        List<Office> officeList=officeRepository.findByEnabledIsTrueAndParentId(officeId);
        OfficeRule officeRule=officeRuleRepository.findLastOfficeRule(new PageRequest(0,1)).getContent().get(0);
        List<Office> lastRuleOfficeList=officeRepository.findByParentIdsListLikeAndOfficeRuleId(CollectionUtil.extractToList(officeList,"id"),officeRule.getId());
        return getOfficeMap(officeList,lastRuleOfficeList);
    }

    public Map<String,List<String>> getLastRuleMapByOfficeRuleName(String officeRuleName){
        List<Office> officeList=officeRepository.findByOfficeRuleName(officeRuleName);
        for(int i=officeList.size()-1;i>0;i--){
            if(officeList.get(i).getPoint()==null||officeList.get(i).getPoint().compareTo(BigDecimal.ZERO)<=0){
                officeList.remove(i);
            }
        }
        OfficeRule officeRule=officeRuleRepository.findLastOfficeRule(new PageRequest(0,1)).getContent().get(0);
        List<Office> lastRuleOfficeList=officeRepository.findByParentIdsListLikeAndOfficeRuleId(CollectionUtil.extractToList(officeList,"id"),officeRule.getId());
        return getOfficeMap(officeList,lastRuleOfficeList);
    }

    private Map<String,List<String>> getOfficeMap(List<Office> officeList,List<Office> lastRuleOfficeList){
        Map<String,List<String>> map=Maps.newHashMap();
        if(CollectionUtil.isNotEmpty(lastRuleOfficeList)){
            for(Office office:lastRuleOfficeList){
                String key=getTopOfficeIdByParentIds(officeList,office.getParentIds());
                if(StringUtils.isNotBlank(key)){
                    if(!map.containsKey(key)){
                        map.put(key,Lists.newArrayList());
                    }
                    map.get(key).add(office.getId());
                }
            }
        }else {
            for(Office office:officeList){
                map.put(office.getId(),Lists.newArrayList(office.getId()));
            }
        }
        return map;
    }

    public RestResponse checkSave(OfficeForm officeForm) {
        Office parent = null;
        if(StringUtils.isNotBlank(officeForm.getParentId())){
            parent =officeRepository.findOne(officeForm.getParentId());
        }
        if (OfficeTypeEnum.业务部门.name().equals(officeForm.getType())) {
            OfficeRule topOfficeRule = officeRuleRepository.findTopOfficeRule(new PageRequest(0,1)).getContent().get(0);
            OfficeRule officeRule = officeRuleRepository.findOne(officeForm.getOfficeRuleId());
            if (StringUtils.isNotBlank(officeForm.getParentId()) && topOfficeRule.getId().equals(officeForm.getOfficeRuleId())) {
                return new RestResponse("顶级业务部门不能设置上级", null,false);
            } else if (StringUtils.isNotBlank(officeForm.getParentId())) {
                if (!officeRule.getParentId().equals(parent.getOfficeRuleId())) {
                    return new RestResponse("业务部门上级类型不正确", null,false);
                }
            } else {
                return new RestResponse("非顶级业务部门必须设置上级", null,false);
            }
        }
        officeForm.setParent(parent);
        return new RestResponse("验证成功", null);
    }

    public RestResponse checkDelete(OfficeForm officeForm) {
       Office office=officeRepository.findOne(officeForm.getId());
        StringBuilder stringBuilder=new StringBuilder();
        if (office.getLocked()) {
            stringBuilder.append("该机构已锁定，不能删除!\n");
        }
        if(office.getPoint()!=null ){
            if(BigDecimal.ZERO.compareTo(office.getPoint())!=0){
                stringBuilder.append("该机构点位不为0，不能删除！\n");
            }
        }
        List<Office> offices=officeRepository.findByParentIdsLike(office.getId());
        if(CollectionUtil.isNotEmpty(offices)){
            stringBuilder.append("该机构有下属机构，不能删除！\n");
        }
        if (StringUtils.isNotBlank(stringBuilder)) {
            return new RestResponse(stringBuilder.toString(), null,false);
        }
        return new RestResponse("验证成功", null);
    }

    @Transactional
    public Office save(OfficeForm officeForm) {
        Office office;
        if(StringUtils.isNotBlank(officeForm.getParentId())&&OfficeTypeEnum.业务部门.name().equals(officeForm.getType())){
            OfficeRule officeRule=officeRuleRepository.findTopOfficeRule(new PageRequest(0,1)).getContent().get(0);
            officeForm.setAreaId(officeManager.getOfficeIdByOfficeRule(officeForm.getParentId(),officeRule.getId()));
        }
        if (officeForm.isCreate()) {
            office = BeanUtil.map(officeForm, Office.class);
            officeRepository.save(office);
            if(StringUtils.isBlank(officeForm.getParentId())){
                officeForm.setAreaId(office.getId());
            }
            officeRepository.save(office);
        } else {
            if(StringUtils.isBlank(officeForm.getParentId())){
                officeForm.setAreaId(officeForm.getId());
            }
            office = officeRepository.findOne(officeForm.getId());
            String oldParentIds=office.getParentIds();
            ReflectionUtil.copyProperties(officeForm, office);
            officeRepository.save(office);
            List<Office> list = officeRepository.findByParentIdsLike("%," + office.getId() + ",%");
            for (Office item : list) {
                item.setParentIds(item.getParentIds().replace(oldParentIds, office.getParentIds()));
                item.setAreaId(office.getAreaId());
                officeRepository.save(item);
            }
        }
        List<OfficeBusiness> businessOfficeList = officeBusinessRepository.findAllBusinessIdById(office.getId());
        if (OfficeTypeEnum.职能部门.name().equals(officeForm.getType())&&CollectionUtil.isNotEmpty(officeForm.getOfficeIdList())) {
            List<String> businessOfficeIdList = CollectionUtil.extractToList(businessOfficeList, "businessOfficeId");
            List<String> removeIdList = CollectionUtil.subtract(businessOfficeIdList, officeForm.getOfficeIdList());
            List<String> addIdList = CollectionUtil.subtract(officeForm.getOfficeIdList(), businessOfficeIdList);
            List<OfficeBusiness> officeBusinessList = Lists.newArrayList();
            for (String businessOfficeId : addIdList) {
                officeBusinessList.add(new OfficeBusiness(office.getId(), businessOfficeId));
            }
            officeBusinessRepository.setEnabledByOfficeId(true, office.getId());
            if (CollectionUtil.isNotEmpty(removeIdList)) {
                officeBusinessRepository.setEnabledByBusinessOfficeIds(removeIdList);
            }
            if (CollectionUtil.isNotEmpty(addIdList)) {
                officeBusinessRepository.save(officeBusinessList);
            }
        } else if (CollectionUtil.isNotEmpty(businessOfficeList)) {
            officeBusinessRepository.setEnabledByOfficeId(false, office.getId());
        }

        List<OfficeLeader> officeLeaderList=officeLeaderRepository.findAllByOfficeId(office.getId());
        if (CollectionUtil.isNotEmpty(officeForm.getLeaderIdList())) {
            List<String> officeLeaderIdList = CollectionUtil.extractToList(officeLeaderList, "leaderId");
            List<String> removeIdList = CollectionUtil.subtract(officeLeaderIdList, officeForm.getLeaderIdList());
            List<String> addIdList = CollectionUtil.subtract(officeForm.getLeaderIdList(), officeLeaderIdList);
            List<OfficeLeader> leaderList = Lists.newArrayList();
            for (String leaderId : addIdList) {
                leaderList.add(new OfficeLeader(office.getId(), leaderId));
            }
            officeLeaderRepository.setEnabledByOfficeId(true, office.getId());
            if (CollectionUtil.isNotEmpty(removeIdList)) {
                officeLeaderRepository.setEnabledByLeaderIds(false,removeIdList);
            }
            if (CollectionUtil.isNotEmpty(addIdList)) {
                officeLeaderRepository.save(leaderList);
            }
        }else if(CollectionUtil.isNotEmpty(officeLeaderList)){
            officeLeaderRepository.setEnabledByOfficeId(false,office.getId());
        }
        return office;
    }

    @Transactional
    public void logicDelete(OfficeForm officeForm) {
        Office office=officeRepository.findOne(officeForm.getId());
        office.setName(office.getName()+"废弃"+ LocalDate.now());
        officeRepository.save(office);
    }

    public List<OfficeDto> findByFilter(OfficeQuery officeQuery) {
        List<Office> officeList = officeRepository.findByFilter(officeQuery);
        List<OfficeDto> officeDtoList = BeanUtil.map(officeList, OfficeDto.class);
        cacheUtils.initCacheInput(officeDtoList);
        return officeDtoList;
    }

    public List<OfficeRuleDto> findOfficeRuleList() {
        List<OfficeRule> officeRuleList = officeRuleRepository.findAllEnabled();
        List<OfficeRuleDto> officeRuleDtoList = BeanUtil.map(officeRuleList, OfficeRuleDto.class);
        return officeRuleDtoList;
    }

    public TreeNode getOfficeTree() {
        Office topOffice = officeRepository.findParentIdIsNull();
        TreeNode treeNode = new TreeNode("t"+topOffice.getId(), topOffice.getName());
        List<Office> officeList = officeRepository.findAllEnabled();
        getTreeNodeList(officeList, treeNode.getChildren(), TreeConstant.ROOT_PARENT_IDS+topOffice.getId()+CharConstant.COMMA);
        return treeNode;
    }

    public void getTreeNodeList(List<Office> officeList, List<TreeNode> childList, String parentIds) {
        for (Office office : officeList) {
            if (parentIds.equalsIgnoreCase(office.getParentIds())) {
                TreeNode treeNode = new TreeNode(office.getId(), office.getName());
                childList.add(treeNode);
                getTreeNodeList(officeList, treeNode.getChildren(), office.getParentIds() + office.getId() + CharConstant.COMMA);
            }
        }
    }

    public List<OfficeDto> findByIds(List<String> ids){
        List<Office> officeList=officeRepository.findByEnabledIsTrueAndIdIn(ids);
        List<OfficeDto> officeDtoList= BeanUtil.map(officeList,OfficeDto.class);
        return officeDtoList;
    }

    private String getTopOfficeIdByParentIds(List<Office> officeList ,String parentIds){
        for(Office office:officeList){
            if(parentIds.contains(office.getId())){
                return office.getId();
            }
        }
        return null;
    }

    public boolean checkLastLevel(String officeId){
        OfficeRule lastOfficeRule = officeRuleRepository.findLastOfficeRule(new PageRequest(0,1)).getContent().get(0);
        Office office=officeRepository.findOne(officeId);
        if(office!=null&&lastOfficeRule.getId().equals(office.getOfficeRuleId())){
            return true;
        }else {
            return false;
        }
    }
}
