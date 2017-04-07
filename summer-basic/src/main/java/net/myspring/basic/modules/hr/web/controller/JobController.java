package net.myspring.basic.modules.hr.web.controller;

import net.myspring.basic.modules.hr.dto.JobDto;
import net.myspring.basic.modules.hr.web.form.JobForm;
import net.myspring.common.response.ResponseCodeEnum;
import net.myspring.common.response.RestResponse;
import net.myspring.util.json.ObjectMapperUtils;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import net.myspring.basic.modules.hr.service.JobService;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "hr/job")
public class JobController {

    @Autowired
    private JobService jobService;

    @RequestMapping(method = RequestMethod.GET)
    public String findPage(HttpServletRequest request){
        SearchEntity searchEntity = RequestUtils.getSearchEntity(request);
        Page<JobDto> page = jobService.findPage(searchEntity.getPageable(),searchEntity.getParams());
        return ObjectMapperUtils.writeValueAsString(page);
    }

    @RequestMapping(value = "findOne")
    public String findOne(String id){
        JobDto jobDto=jobService.findDto(id);
        return ObjectMapperUtils.writeValueAsString(jobDto);
    }

    @RequestMapping(value = "save")
    public String save(JobForm jobForm) {
        jobService.save(jobForm);
        return ObjectMapperUtils.writeValueAsString(new RestResponse("保存成功", ResponseCodeEnum.saved.name()));
    }

    @RequestMapping(value = "delete")
    public String delete(String id){
        jobService.logicDeleteOne(id);
        RestResponse restResponse = new RestResponse("刪除成功",ResponseCodeEnum.removed.name());
        return ObjectMapperUtils.writeValueAsString(restResponse);
    }

}
