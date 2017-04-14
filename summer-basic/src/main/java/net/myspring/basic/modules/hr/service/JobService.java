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
        Page<Job> page = jobMapper.findPage(pageable, jobQuery);
        Page<JobDto> jobDtoPage = BeanUtil.map(page, JobDto.class);
        cacheUtils.initCacheInput(jobDtoPage.getContent());
        return jobDtoPage;
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

    public JobDto findDto(String id){
        Job job=findOne(id);
        JobDto jobDto= BeanUtil.map(job,JobDto.class);
        cacheUtils.initCacheInput(jobDto);
        return jobDto;
    }

    public JobForm save(JobForm jobForm){
        if(StringUtils.isBlank(jobForm.getId())) {
            jobManager.save(BeanUtil.map(jobForm,Job.class));
        } else {
            jobManager.updateForm(jobForm);
        }
        return jobForm;
    }

    public void logicDeleteOne(String id){
        jobMapper.logicDeleteOne(id);
    }
}
