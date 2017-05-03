package net.myspring.basic.modules.sys.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.myspring.basic.common.enums.OfficeRuleEnum;
import net.myspring.basic.common.utils.CacheUtils;
import net.myspring.basic.common.utils.Const;
import net.myspring.basic.common.utils.SecurityUtils;
import net.myspring.basic.modules.hr.domain.Account;
import net.myspring.basic.modules.hr.domain.OfficeLeader;
import net.myspring.basic.modules.hr.manager.AccountManager;
import net.myspring.basic.modules.hr.mapper.OfficeLeaderMapper;
import net.myspring.basic.modules.sys.domain.Office;
import net.myspring.basic.modules.sys.domain.OfficeBusiness;
import net.myspring.basic.modules.sys.domain.OfficeRule;
import net.myspring.basic.modules.sys.dto.OfficeDto;
import net.myspring.basic.modules.sys.dto.OfficeRuleDto;
import net.myspring.basic.modules.sys.manager.OfficeManager;
import net.myspring.basic.modules.sys.mapper.OfficeBusinessMapper;
import net.myspring.basic.modules.sys.mapper.OfficeMapper;
import net.myspring.basic.modules.sys.mapper.OfficeRuleMapper;
import net.myspring.basic.modules.sys.web.form.OfficeForm;
import net.myspring.basic.modules.sys.web.query.OfficeQuery;
import net.myspring.common.tree.TreeNode;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.mapper.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class OfficeService {

    @Autowired
    private OfficeMapper officeMapper;
    @Autowired
    private OfficeManager officeManager;
    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private AccountManager accountManager;
    @Autowired
    private OfficeRuleMapper officeRuleMapper;
    @Autowired
    private OfficeBusinessMapper officeBusinessMapper;
    @Autowired
    private OfficeLeaderMapper officeLeaderMapper;


    public List<Office> findByOfficeRuleName(String officeRuleName) {
        return officeMapper.findByOfficeRuleName(officeRuleName);
    }

    public Page<OfficeDto> findPage(Pageable pageable, OfficeQuery officeQuery) {
        Page<OfficeDto> page = officeMapper.findPage(pageable, officeQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    public List<String> getOfficeFilterIds(String accountId){
        List<String> officeIdList= Lists.newArrayList();
        if(accountId!=null){
            officeIdList= officeManager.officeFilter(accountId);
        }
        return officeIdList;
    }

    public List<Office> findByParentIdsLike(String parentId) {
        List<Office> officeList = officeMapper.findByParentIdsLike(parentId);
        return officeList;
    }


    public List<Office> findAll() {
        return officeMapper.findAll();
    }

    public Office findOne(String id) {
        Office office = officeMapper.findOne(id);
        return office;
    }

    public OfficeForm findForm(OfficeForm officeForm) {
        if(!officeForm.isCreate()){
            Office office = officeMapper.findOne(officeForm.getId());
            officeForm= BeanUtil.map(office,OfficeForm.class);
            OfficeRule officeRule=officeRuleMapper.findOne(office.getOfficeRuleId());
            if(officeRule!=null&& OfficeRuleEnum.SUPPORT.name().equals(officeRule.getType())){
                List<String> businessOffices=officeBusinessMapper.findBusinessIdById(office.getId());
                officeForm.setOfficeTree(getOfficeTree(businessOffices));
            }
            List<OfficeLeader> officeLeaderList=officeLeaderMapper.findByOfficeId(officeForm.getId());
            officeForm.setLeaderIdList(CollectionUtil.extractToList(officeLeaderList,"leaderId"));
            cacheUtils.initCacheInput(officeForm);
        }
        return officeForm;
    }


    public Office save(OfficeForm officeForm) {
        Office office;
        if (officeForm.isCreate()) {
            office=BeanUtil.map(officeForm,Office.class);
            office=officeManager.save(office);
        } else {
            office=officeManager.updateForm(officeForm);
            officeLeaderMapper.removeOfficeLeaderByOffice(office.getId());
        }
        List<String> businessOfficeIdList=officeBusinessMapper.findBusinessIdById(office.getId());
        List<String>removeIdList=CollectionUtil.subtract(businessOfficeIdList,officeForm.getOfficeIdList());
        List<String> addIdList=CollectionUtil.subtract(officeForm.getOfficeIdList(),businessOfficeIdList);
        List<OfficeBusiness> officeBusinessList=Lists.newArrayList();
        for(String businessOfficeId:addIdList){
            OfficeBusiness officeBusiness = new OfficeBusiness(office.getId(), businessOfficeId);
            officeBusiness.setCompanyId(SecurityUtils.getCompanyId());
            officeBusinessList.add(officeBusiness);
        }
        if(CollectionUtil.isNotEmpty(removeIdList)){
            officeBusinessMapper.removeByBusinessOfficeIds(removeIdList);
        }
        if(CollectionUtil.isNotEmpty(addIdList)){
            officeBusinessMapper.batchSave(officeBusinessList);
        }
        if(CollectionUtil.isNotEmpty(officeForm.getLeaderIdList())){
            List<OfficeLeader> officeLeaderList=Lists.newArrayList();
            for(String leaderId:officeForm.getLeaderIdList()){
                officeLeaderList.add(new OfficeLeader(office.getId(),leaderId));
            }
            officeLeaderMapper.batchSave(officeLeaderList);
        }
        return office;
    }

    public void logicDeleteOne(Office office) {
        officeMapper.logicDeleteOne(office.getId());
    }

    public List<OfficeDto> findByFilter(Map<String, Object> map) {
        List<Office> officeList = officeMapper.findByFilter(map);
        List<OfficeDto> officeDtoList= BeanUtil.map(officeList,OfficeDto.class);
        cacheUtils.initCacheInput(officeDtoList);
        return officeDtoList;
    }

    public List<String> findBusinessIdById(String id){
        return  officeBusinessMapper.findBusinessIdById(id);
    }

    public List<OfficeRuleDto> findTypeList(){
        List<OfficeRule> officeRuleList=officeRuleMapper.findAllEnabled();
        List<OfficeRuleDto> officeRuleDtoList=BeanUtil.map(officeRuleList,OfficeRuleDto.class);
        return officeRuleDtoList;
    }

    public TreeNode getOfficeTree(List<String> officeIdList){
        TreeNode treeNode = new TreeNode("0", "部门列表");
        List<Office> officeList=officeMapper.findAll();
        getTreeNodeList(officeList,treeNode.getChildren(),Const.ROOT_PARENT_IDS);
        treeNode.setChecked(Lists.newArrayList(Sets.newHashSet(officeIdList)));
        return treeNode;
    }

    public void getTreeNodeList(List<Office> officeList,List<TreeNode> childList,String parentIds) {
        for(Office office:officeList){
            if(parentIds.equalsIgnoreCase(office.getParentIds())){
                TreeNode treeNode=new TreeNode(office.getId(),office.getName());
                childList.add(treeNode);
                getTreeNodeList(officeList,treeNode.getChildren(),office.getParentIds()+office.getId()+Const.CHAR_COMMA);
            }
        }
    }
}
