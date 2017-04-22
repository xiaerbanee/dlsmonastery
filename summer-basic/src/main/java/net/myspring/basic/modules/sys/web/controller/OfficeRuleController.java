package net.myspring.basic.modules.sys.web.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.myspring.basic.common.enums.BoolEnum;
import net.myspring.basic.common.enums.DictMapCategoryEnum;
import net.myspring.basic.common.enums.JointTypeEnum;
import net.myspring.basic.common.utils.Global;
import net.myspring.basic.modules.sys.domain.Office;
import net.myspring.basic.modules.sys.domain.OfficeRule;
import net.myspring.basic.modules.sys.dto.OfficeBasicDto;
import net.myspring.basic.modules.sys.dto.OfficeDto;
import net.myspring.basic.modules.sys.dto.OfficeRuleDto;
import net.myspring.basic.modules.sys.service.OfficeRuleService;
import net.myspring.basic.modules.sys.web.form.OfficeForm;
import net.myspring.basic.modules.sys.web.form.OfficeRuleForm;
import net.myspring.basic.modules.sys.web.query.OfficeQuery;
import net.myspring.basic.modules.sys.web.query.OfficeRuleQuery;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Created by wangzm on 2017/4/22.
 */
@RestController
@RequestMapping(value = "sys/officeRule")
public class OfficeRuleController {

    @Autowired
    private OfficeRuleService officeRuleService;
    
    @RequestMapping(method = RequestMethod.GET)
    public Page<OfficeRuleDto> list(Pageable pageable, OfficeRuleQuery officeRuleQuery) {
        Page<OfficeRuleDto> page = officeRuleService.findPage(pageable,officeRuleQuery);
        return page;
    }

    @RequestMapping(value = "save")
    public RestResponse save(OfficeRuleForm officeRuleForm) {
        officeRuleService.save(officeRuleForm);
        return new RestResponse("保存成功", ResponseCodeEnum.saved.name());
    }


    @RequestMapping(value = "findOne")
    public OfficeRuleForm findOne(OfficeRuleForm officeRuleForm){
        officeRuleForm=officeRuleService.findForm(officeRuleForm);
        officeRuleForm.setBoolMap(BoolEnum.getMap());
        officeRuleForm.setOfficeRuleList(officeRuleService.findAll());
        return officeRuleForm;
    }

    @RequestMapping(value = "delete")
    public RestResponse delete(OfficeRuleForm officeRuleForm) {
        officeRuleService.logicDeleteOne(officeRuleForm);
        return new RestResponse("删除成功",ResponseCodeEnum.removed.name());
    }
}
