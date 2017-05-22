package net.myspring.basic.modules.sys.web.controller;

import com.google.common.collect.Lists;
import net.myspring.basic.common.enums.JointTypeEnum;
import net.myspring.basic.common.enums.OfficeTypeEnum;
import net.myspring.basic.modules.sys.domain.Office;
import net.myspring.basic.modules.sys.domain.OfficeBusiness;
import net.myspring.basic.modules.sys.dto.OfficeDto;
import net.myspring.basic.modules.sys.service.OfficeRuleService;
import net.myspring.basic.modules.sys.service.OfficeService;
import net.myspring.basic.modules.sys.web.form.OfficeForm;
import net.myspring.basic.modules.sys.web.query.OfficeQuery;
import net.myspring.common.constant.CharConstant;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
        }
        return officeList;
    }

    @RequestMapping(value = "save")
    public RestResponse save(OfficeForm officeForm) {
        RestResponse restResponse=officeService.check(officeForm);
        if(!restResponse.getSuccess()){
            return restResponse;
        }
        officeForm.setOfficeIdList(StringUtils.getSplitList(officeForm.getOfficeIdStr(), CharConstant.COMMA));
        officeService.save(officeForm);
        return new RestResponse("保存成功", ResponseCodeEnum.saved.name());
    }


    @RequestMapping(value = "findOne")
    public OfficeDto findOne(OfficeDto officeDto) {
        officeDto = officeService.findOne(officeDto);
        return officeDto;
    }

    @RequestMapping(value = "getForm")
    public OfficeForm getForm(OfficeForm officeForm) {
        officeForm.setOfficeRuleList(officeService.findOfficeRuleList());
        officeForm.setJointTypeList(JointTypeEnum.getList());
        officeForm.setOfficeTypeList(OfficeTypeEnum.getList());
        return officeForm;
    }

    @RequestMapping(value = "getOfficeTree")
    public TreeNode getOfficeTree(String id) {
        if (StringUtils.isNotBlank(id)) {
            List<OfficeBusiness> businessList = officeService.findBusinessIdById(id);
            TreeNode treeNode = officeService.getOfficeTree(CollectionUtil.extractToList(businessList,"businessOfficeId"));
            return treeNode;
        }
        return null;
    }

    @RequestMapping(value = "delete")
    public RestResponse delete(Office office, BindingResult bindingResult) {
        officeService.logicDeleteOne(office);
        return new RestResponse("删除成功", ResponseCodeEnum.removed.name());
    }

    @RequestMapping(value = "findByIds")
    public List<OfficeDto> findByIds(String idStr){
        List<String> idList = StringUtils.getSplitList(idStr,CharConstant.COMMA);
        List<OfficeDto> officeDtoList = officeService.findByIds(idList);
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
}
