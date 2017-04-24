package net.myspring.basic.modules.sys.web.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.basic.common.enums.DictMapCategoryEnum;
import net.myspring.basic.common.enums.JointTypeEnum;
import net.myspring.basic.common.utils.Const;
import net.myspring.basic.common.utils.Global;
import net.myspring.basic.modules.sys.domain.Office;
import net.myspring.basic.modules.sys.dto.OfficeBasicDto;
import net.myspring.basic.modules.sys.dto.OfficeDto;
import net.myspring.basic.modules.sys.service.OfficeService;
import net.myspring.basic.modules.sys.web.form.OfficeForm;
import net.myspring.basic.modules.sys.web.query.OfficeQuery;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "sys/office")
public class OfficeController {

    @Autowired
    private OfficeService officeService;

    @RequestMapping(method = RequestMethod.GET)
    public Page<OfficeDto> list(Pageable pageable, OfficeQuery officeQuery) {
        Page<OfficeDto> page = officeService.findPage(pageable,officeQuery);
        return page;
    }

    @RequestMapping(value = "search")
    public List<OfficeDto> search(String name,String officeType,String parentOfficeId) {
        List<OfficeDto> officeDtos = Lists.newArrayList();
        if(StringUtils.isNotBlank(name)) {
            Map<String, Object> map = Maps.newHashMap();
            map.put("name", name);
            if (StringUtils.isNotBlank(parentOfficeId)) {
                map.put("parentOfficeId",parentOfficeId);
            }
            officeDtos = officeService.findByFilter(map);
        }
         return officeDtos;
    }

    @RequestMapping(value = "getOfficeFilterIds")
    public List<String> getOfficeFilterIds(String accountId){
        List<String> officeIdList=Lists.newArrayList();
        if(StringUtils.isNotBlank(accountId)){
            officeIdList=officeService.getOfficeFilterIds(accountId);
        }
        return officeIdList;
    }

    @RequestMapping(value = "findByType")
    public List<OfficeBasicDto> findByType(String type){
        List<OfficeBasicDto> officeList=Lists.newArrayList();
        if(StringUtils.isNotBlank(type)){
            officeList= BeanUtil.map(officeService.findByType(type),OfficeBasicDto.class);
        }
        return officeList;
    }

    @RequestMapping(value = "save")
    public RestResponse save(OfficeForm officeForm) {
        officeForm.setOfficeIdList(StringUtils.getSplitList(officeForm.getOfficeIdStr(), Const.CHAR_COMMA));
        officeService.save(officeForm);
        return new RestResponse("保存成功", ResponseCodeEnum.saved.name());
    }


    @RequestMapping(value = "findOne")
    public OfficeForm findOne(OfficeForm officeForm){
        officeForm=officeService.findForm(officeForm);
        officeForm.setOfficeTypeList(officeService.findTypeList());
        officeForm.setJointTypeList(JointTypeEnum.getList());
        return officeForm;
    }

    @RequestMapping(value = "getOfficeTree")
    public TreeNode getOfficeTree(String id){
        if(StringUtils.isNotBlank(id)){
            List<String> officeIds=officeService.findBusinessIdById(id);
            TreeNode treeNode=officeService.getOfficeTree(officeIds);
            return treeNode;
        }
        return null;
    }

    @RequestMapping(value = "delete")
    public RestResponse delete(Office office,BindingResult bindingResult) {
        officeService.logicDeleteOne(office);
        return new RestResponse("删除成功",ResponseCodeEnum.removed.name());
    }
}
