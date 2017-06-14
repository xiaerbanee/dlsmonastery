package net.myspring.basic.modules.sys.web.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.basic.common.enums.JointTypeEnum;
import net.myspring.basic.common.enums.OfficeTypeEnum;
import net.myspring.basic.modules.sys.domain.Office;
import net.myspring.basic.modules.sys.domain.OfficeBusiness;
import net.myspring.basic.modules.sys.dto.OfficeDto;
import net.myspring.basic.modules.sys.dto.OfficeRuleDto;
import net.myspring.basic.modules.sys.service.OfficeRuleService;
import net.myspring.basic.modules.sys.service.OfficeService;
import net.myspring.basic.modules.sys.web.form.OfficeForm;
import net.myspring.basic.modules.sys.web.query.OfficeQuery;
import net.myspring.common.constant.CharConstant;
import net.myspring.common.enums.JointLevelEnum;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.common.tree.TreeNode;
import net.myspring.util.collection.CollectionUtil;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "sys/office")
public class OfficeController {

    @Autowired
    private OfficeService officeService;
    @Autowired
    private OfficeRuleService officeRuleService;

    @RequestMapping(method = RequestMethod.GET)
    public Page<OfficeDto> list(Pageable pageable, OfficeQuery officeQuery) {
        Page<OfficeDto> page = officeService.findPage(pageable, officeQuery);
        return page;
    }

    @RequestMapping(value = "search")
    public List<OfficeDto> search(OfficeQuery officeQuery) {
        List<OfficeDto> officeDtos = Lists.newArrayList();
        if (StringUtils.isNotBlank(officeQuery.getName())) {
            officeDtos = officeService.findByFilter(officeQuery);
        }
        return officeDtos;
    }

    @RequestMapping(value = "getOfficeFilterIds")
    public List<String> getOfficeFilterIds(String officeId) {
        List<String> officeIdList = Lists.newArrayList();
        if (StringUtils.isNotBlank(officeId)) {
            officeIdList = officeService.getOfficeFilterIds(officeId);
        }
        return officeIdList;
    }

    @RequestMapping(value = "findByOfficeRuleName")
    public List<OfficeDto> findByOfficeRuleName(String officeRuleName) {
        List<OfficeDto> officeList = Lists.newArrayList();
        if (StringUtils.isNotBlank(officeRuleName)) {
            officeList = BeanUtil.map(officeService.findByOfficeRuleName(officeRuleName), OfficeDto.class);
        }else {
            OfficeRuleDto officeRuleDto=officeService.findTopOfficeRule();
            officeList = BeanUtil.map(officeService.findByOfficeRuleName(officeRuleDto.getName()), OfficeDto.class);
        }
        return officeList;
    }

    @RequestMapping(value = "save")
    public RestResponse save(OfficeForm officeForm) {
        RestResponse restResponse=officeService.check(officeForm);
        if(!restResponse.getSuccess()){
            return restResponse;
        }
        String areaId="";
        if(!officeForm.isCreate()){
            Office office=officeService.findOne(officeForm.getId());
            areaId=office.getAreaId();
        }
        Office office=officeService.save(officeForm);
        restResponse=new RestResponse("保存成功", ResponseCodeEnum.saved.name());
        if(StringUtils.isNotBlank(areaId)&&!areaId.equals(office.getAreaId())){
            restResponse=new RestResponse("保存成功,请到门店管理页面进行部门同步", ResponseCodeEnum.saved.name());
        }
        return restResponse;
    }


    @RequestMapping(value = "findOne")
    public OfficeDto findOne(OfficeDto officeDto) {
        officeDto = officeService.findOne(officeDto);
        return officeDto;
    }

    @RequestMapping(value = "getForm")
    public OfficeForm getForm(OfficeForm officeForm) {
        officeForm.getExtra().put("officeRuleList",officeService.findOfficeRuleList());
        officeForm.getExtra().put("jointTypeList",JointTypeEnum.getList());
        officeForm.getExtra().put("officeTypeList",OfficeTypeEnum.getList());
        officeForm.getExtra().put("jointLevelList",JointLevelEnum.getList());
        return officeForm;
    }

    @RequestMapping(value = "getOfficeTree")
    public TreeNode getOfficeTree() {
        TreeNode treeNode = officeService.getOfficeTree();
        return treeNode;
    }

    @RequestMapping(value = "delete")
    public RestResponse delete(Office office, BindingResult bindingResult) {
        officeService.logicDelete(office);
        return new RestResponse("删除成功", ResponseCodeEnum.removed.name());
    }

    @RequestMapping(value = "findByIds")
    public List<OfficeDto> findByIds(@RequestParam("idStr") List<String> ids){
        List<OfficeDto> officeDtoList = officeService.findByIds(ids);
        return officeDtoList;
    }

    @RequestMapping(value = "getSameAreaByOfficeId")
    public List<String> getSameAreaByOfficeId(String officeId){
        List<String> officeIds=Lists.newArrayList();
        if(StringUtils.isNotBlank(officeId)){
            officeIds=officeService.getSameAreaByOfficeId(officeId);
        }
        return officeIds;
    }

    @RequestMapping(value = "getChildOfficeIds")
    public List<String> getChildOfficeIds(String officeId){
        List<String> officeIds=Lists.newArrayList();
        if(StringUtils.isNotBlank(officeId)){
            List<Office> officeList = officeService.findByParentIdsLike(officeId);
            if(CollectionUtil.isNotEmpty(officeList)){
                officeIds=CollectionUtil.extractToList(officeList,"id");
            }else {
                officeIds=Lists.newArrayList(officeId);
            }
        }
        return officeIds;
    }

    @RequestMapping(value = "getLastRuleMapByOfficeId")
    public Map<String,List<String>> getLastRuleMapByOfficeId(String officeId){
        Map<String,List<String>> map= Maps.newHashMap();
        if(StringUtils.isNotBlank(officeId)){
            map = officeService.getLastRuleMapByOfficeId(officeId);
        }
        return map;
    }

    @RequestMapping(value = "getLastRuleMapByOfficeRuleName")
    public Map<String,List<String>> getLastRuleMapByOfficeRuleName(String officeRuleName){
        Map<String,List<String>> map= Maps.newHashMap();
        if(StringUtils.isNotBlank(officeRuleName)){
            map = officeService.getLastRuleMapByOfficeRuleName(officeRuleName);
        }
        return map;
    }

    @RequestMapping(value = "checkLastLevel")
    public boolean checkLastLevel(String officeId){
        return officeService.checkLastLevel(officeId);
    }

    @RequestMapping(value = "getQuery")
    public OfficeQuery getQuery(OfficeQuery officeQuery){
        return officeQuery;
    }
}
