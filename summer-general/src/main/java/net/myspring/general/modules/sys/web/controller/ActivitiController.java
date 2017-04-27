package net.myspring.general.modules.sys.web.controller;

import net.myspring.general.common.utils.SecurityUtils;
import net.myspring.general.modules.sys.dto.ActivitiAuditDto;
import net.myspring.general.modules.sys.dto.ActivitiAuthenticatedDto;
import net.myspring.general.modules.sys.form.ActivitiAuditForm;
import net.myspring.general.modules.sys.form.ActivitiAuthenticatedForm;
import net.myspring.general.modules.sys.form.ActivitiNotifyForm;
import net.myspring.general.modules.sys.service.ActivitiService;
import net.myspring.util.text.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by wangzm on 2017/4/25.
 */
@RestController
@RequestMapping(value = "sys/activiti")
public class ActivitiController {

    @Autowired
    protected ActivitiService  activitiService;

    @RequestMapping(value = "authenticated")
    public ActivitiAuthenticatedDto authenticatedActiviti(@RequestBody ActivitiAuthenticatedForm activitiAuthenticatedForm){
        ActivitiAuthenticatedDto activitiAuthenticatedDto=new ActivitiAuthenticatedDto();
        if(StringUtils.isNotBlank(activitiAuthenticatedForm.getProcessTypeId())){
            activitiAuthenticatedDto=activitiService.authenticatedActiviti(activitiAuthenticatedForm);
        }
        return activitiAuthenticatedDto;
    }

    @RequestMapping(value = "audit")
    public ActivitiAuditDto audit(@RequestBody ActivitiAuditForm activitiAuditForm){
        ActivitiAuditDto activitiAuditDto=new ActivitiAuditDto();
        if(StringUtils.isNotBlank(activitiAuditForm.getProcessTypeId())&&StringUtils.isNotBlank(activitiAuditForm.getProcessInstanceId())){
            activitiAuditDto= activitiService.audit(activitiAuditForm);
        }
        return activitiAuditDto;
    }

    @RequestMapping(value = "notify")
    public boolean notify(@RequestBody ActivitiNotifyForm activitiNotifyForm){
        if(StringUtils.isNotBlank(activitiNotifyForm.getExtendId())&&StringUtils.isNotBlank(activitiNotifyForm.getName())){
            activitiService.notify(activitiNotifyForm);
            return true;
        }
        return false;
    }
}
