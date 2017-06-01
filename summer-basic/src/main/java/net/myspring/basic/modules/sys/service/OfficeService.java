package net.myspring.basic.modules.sys.service;

import com.google.common.collect.Lists;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
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

    public List<String> getOfficeFilterIds(String officeId) {
        List<String> officeIdList =officeManager.officeFilter(officeId);
        return officeIdList;
    }

    public List<Office> findByParentIdsLike(String parentId) {
        List<Office> officeList = officeRepository.findByParentIdsLike(parentId);
        return officeList;
    }


    public List<Office> findAll() {
        return officeRepository.findAll();
    }

    public OfficeDto searchById(OfficeQuery officeQuery) {
        OfficeDto officeDto=new OfficeDto();
        List<String> officeFilter = officeManager.officeFilter(RequestUtils.getRequestEntity().getOfficeId());
        officeQuery.setOfficeIds(officeFilter);
        List<Office> officeList = officeRepository.findByFilter(officeQuery);
        if(CollectionUtil.isNotEmpty(officeList)){
            officeDto= BeanUtil.map(officeList.get(0),OfficeDto.class);
            cacheUtils.initCacheInput(officeDto);
        }
        return officeDto;
    }

    public List<String> getSameAreaByOfficeId(String officeId){
        List<Office> sameAreaByOfficeId = officeRepository.findSameAreaByOfficeId(officeId);
        return CollectionUtil.extractToList(sameAreaByOfficeId,"id");
    }

    public OfficeDto findOne(OfficeDto officeDto) {
        if (!officeDto.isCreate()) {
            Office office = officeRepository.findOne(officeDto.getId());
            officeDto = BeanUtil.map(office, OfficeDto.class);
            if (OfficeTypeEnum.SUPPORT.name().equals(office.getType())) {
                List<OfficeBusiness> businessOffices = officeBusinessRepository.findBusinessIdById(office.getId());
                officeDto.setBusinessIdList(CollectionUtil.extractToList(businessOffices,"businessOfficeId"));
            }
            List<OfficeLeader> officeLeaderList = officeLeaderRepository.findByOfficeId(officeDto.getId());
            officeDto.setLeaderIdList(CollectionUtil.extractToList(officeLeaderList, "leaderId"));
            cacheUtils.initCacheInput(officeDto);
        }
        return officeDto;
    }

    public RestResponse check(OfficeForm officeForm) {
        Office parent = officeRepository.findOne(officeForm.getParentId());
        if (OfficeTypeEnum.BUSINESS.name().equals(officeForm.getType())) {
            OfficeRule topOfficeRule = officeRuleRepository.findTopOfficeRule(new PageRequest(0,1)).getContent().get(0);
            OfficeRule officeRule = officeRuleRepository.findOne(officeForm.getOfficeRuleId());
            if (parent != null && topOfficeRule.getId().equals(officeForm.getOfficeRuleId())) {
                return new RestResponse("顶级业务部门不能设置上级", null,false);
            } else if (parent != null) {
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

    public Office save(OfficeForm officeForm) {
        Office office;
        if(officeForm.getParent()!=null){
            OfficeRule officeRule=officeRuleRepository.findTopOfficeRule(new PageRequest(0,1)).getContent().get(0);
            officeForm.setAreaId(officeManager.getOfficeIdByOfficeRule(officeForm.getParent().getId(),officeRule.getId()));
        }
        if (officeForm.isCreate()) {
            office = BeanUtil.map(officeForm, Office.class);
            officeRepository.save(office);
            if(officeForm.getParent()==null){
                officeForm.setAreaId(office.getId());
            }
            officeRepository.save(office);
        } else {
            if(officeForm.getParent()==null){
                officeForm.setAreaId(officeForm.getId());
            }
            officeManager.officeFilter(officeForm.getId());
            office = officeRepository.findOne(officeForm.getId());
            String oldParentIds=office.getParentIds();
            ReflectionUtil.copyProperties(officeForm, office);
            officeRepository.save(office);
            List<Office> list = officeRepository.findByParentIdsLike("%," + office.getId() + ",%");
            for (Office item : list) {
                item.setParentIds(item.getParentIds().replace(oldParentIds, office.getParentIds()));
                officeRepository.save(item);
            }
        }

        List<OfficeBusiness> businessOfficeList = officeBusinessRepository.findAllBusinessIdById(office.getId());
        if (OfficeTypeEnum.SUPPORT.name().equals(officeForm.getType())&&CollectionUtil.isNotEmpty(officeForm.getOfficeIdList())) {
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
            for (String leaderId : officeForm.getLeaderIdList()) {
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

    public void logicDelete(Office office) {
        officeRepository.logicDelete(office.getId());
    }

    public List<OfficeDto> findByFilter(OfficeQuery officeQuery) {
        officeQuery.setOfficeIds(officeManager.officeFilter(RequestUtils.getRequestEntity().getOfficeId()));
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
}
