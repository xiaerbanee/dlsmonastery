package net.myspring.basic.modules.hr.web.controller;

import net.myspring.basic.modules.hr.dto.JobDto;
import net.myspring.basic.modules.hr.web.form.JobForm;
import net.myspring.basic.modules.hr.web.query.JobQuery;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.util.json.ObjectMapperUtils;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import net.myspring.basic.modules.hr.service.JobService;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "hr/job")
public class JobController {

    @Autowired
    private JobService jobService;

    @RequestMapping(method = RequestMethod.GET)
    public Page<JobDto> findPage(Pageable pageable, JobQuery jobQuery){
        Page<JobDto> page = jobService.findPage(pageable,jobQuery);
        return page;
    }

    @RequestMapping(value = "findOne")
    public JobForm findOne(JobForm jobForm){
        jobForm=jobService.findForm(jobForm);
        return jobForm;
    }

    @RequestMapping(value = "save")
    public RestResponse save(JobForm jobForm) {
        jobService.save(jobForm);
        return new RestResponse("保存成功", ResponseCodeEnum.saved.name());
    }

    @RequestMapping(value = "delete")
    public RestResponse delete(String id){
        jobService.logicDeleteOne(id);
        RestResponse restResponse = new RestResponse("刪除成功",ResponseCodeEnum.removed.name());
        return restResponse;
    }

}
