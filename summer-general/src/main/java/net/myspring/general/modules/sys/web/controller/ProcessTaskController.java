package net.myspring.general.modules.sys.web.controller;

import net.myspring.general.common.utils.RequestUtils;
import net.myspring.general.modules.sys.dto.ProcessTaskDto;
import net.myspring.general.modules.sys.service.ProcessTaskService;
import net.myspring.general.modules.sys.web.query.ProcessTaskQuery;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.bouncycastle.cert.ocsp.Req;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by wangzm on 2017/6/5.
 */
@RestController
@RequestMapping(value = "sys/processTask")
public class ProcessTaskController {

    @Autowired
    private ProcessTaskService processTaskService;

    @RequestMapping(value = "getQuery")
    public ProcessTaskQuery getQuery(ProcessTaskQuery processTaskQuery) {
        return processTaskQuery;
    }
    @RequestMapping(method = RequestMethod.GET)
    public Page<ProcessTaskDto> list(ProcessTaskQuery processTaskQuery, Pageable pageable) {
        if(!RequestUtils.getAdmin()){
            processTaskQuery.setPositionId(RequestUtils.getPositionId());
        }
        Page<ProcessTaskDto> page=processTaskService.findPage(pageable,processTaskQuery);
        return page;
    }

}
