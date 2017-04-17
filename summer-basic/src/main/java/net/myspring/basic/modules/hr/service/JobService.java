package net.myspring.basic.modules.hr.service;

import net.myspring.basic.common.utils.CacheUtils;
import net.myspring.basic.modules.hr.domain.Job;
import net.myspring.basic.modules.hr.dto.JobDto;
import net.myspring.basic.modules.hr.manager.JobManager;
import net.myspring.basic.modules.hr.mapper.JobMapper;
import net.myspring.basic.modules.hr.web.form.JobForm;
import net.myspring.basic.modules.hr.web.query.JobQuery;
import net.myspring.util.mapper.BeanUtil;
import net.myspring.util.text.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class JobService {

    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private JobManager jobManager;
    @Autowired
    private JobMapper jobMapper;

    public Page<JobDto> findPage(Pageable pageable, JobQuery jobQuery) {
        Page<JobDto> page = jobMapper.findPage(pageable, jobQuery);
        cacheUtils.initCacheInput(page.getContent());
        return page;
    }

    public Job findOne(String id){
        Job job = jobManager.findOne(id);
        return job;
    }

    public List<JobDto> findAll(){
        List<Job> jobList=jobMapper.findAll();
        List<JobDto> jobDtoList= BeanUtil.map(jobList,JobDto.class);
        return jobDtoList;
    }

    public JobForm findForm(JobForm jobForm){
        if(!jobForm.isCreate()){
            Job job=jobManager.findOne(jobForm.getId());
            jobForm= BeanUtil.map(job,JobForm.class);
            cacheUtils.initCacheInput(jobForm);
        }
        return jobForm;
    }

    public Job save(JobForm jobForm){
        Job job;
        if(StringUtils.isBlank(jobForm.getId())) {
            job = BeanUtil.map(jobForm, Job.class);
            job=jobManager.save(job);
        } else {
            job=jobManager.updateForm(jobForm);
        }
        return job;
    }

    public void logicDeleteOne(String id){
        jobMapper.logicDeleteOne(id);
    }
}
