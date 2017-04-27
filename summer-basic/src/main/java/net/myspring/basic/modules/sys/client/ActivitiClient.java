package net.myspring.basic.modules.sys.client;

import net.myspring.general.modules.sys.dto.ActivitiAuditDto;
import net.myspring.general.modules.sys.dto.ActivitiAuthenticatedDto;
import net.myspring.general.modules.sys.form.ActivitiAuditForm;
import net.myspring.general.modules.sys.form.ActivitiAuthenticatedForm;
import net.myspring.general.modules.sys.form.ActivitiNotifyForm;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by wangzm on 2017/4/25.
 */
@FeignClient("summer-general")
public interface ActivitiClient {

    @RequestMapping(method = RequestMethod.GET, value = "/sys/activiti/authenticated")
    ActivitiAuthenticatedDto authenticated(ActivitiAuthenticatedForm activitiAuthenticatedForm);

    @RequestMapping(method = RequestMethod.GET, value = "/sys/activiti/audit")
    ActivitiAuditDto audit(ActivitiAuditForm activitiAuditForm);

    @RequestMapping(method = RequestMethod.GET, value = "/sys/activiti/notify")
    boolean notify(ActivitiNotifyForm activitiNotifyForm);
}
