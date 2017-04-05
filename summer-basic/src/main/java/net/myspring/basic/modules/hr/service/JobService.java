package net.myspring.basic.modules.hr.service;

import com.google.common.collect.Lists;
import net.myspring.basic.common.utils.CacheUtils;
import net.myspring.basic.modules.hr.domain.Job;
import net.myspring.basic.modules.hr.dto.JobDto;
import net.myspring.basic.modules.hr.manager.JobManager;
import net.myspring.basic.modules.hr.web.form.JobForm;
import net.myspring.basic.modules.sys.dto.DictMapDto;
import net.myspring.util.mapper.BeanMapper;
import net.myspring.util.text.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

@Service
public class JobService {

    @Autowired
    private CacheUtils cacheUtils;
    @Autowired
    private JobManager jobManager;

    public Page<JobDto> findPage(Pageable pageable, Map<String, Object> map) {
        Page<Job> page = jobManager.findPage(pageable, map);
        Page<JobDto> jobDtoPage = BeanMapper.convertPage(page, JobDto.class);
        cacheUtils.initCacheInput(jobDtoPage.getContent());
        return jobDtoPage;
    }

    public Job findOne(String id){
        Job job = jobManager.findOne(id);
        return job;
    }

    public List<JobDto> findAll(){
        List<Job> jobList=jobManager.findAll();
        List<JobDto> jobDtoList=BeanMapper.convertDtoList(jobList,JobDto.class);
        return jobDtoList;
    }

    public JobDto findDto(String id){
        Job job=findOne(id);
        JobDto jobDto=BeanMapper.convertDto(job,JobDto.class);
        cacheUtils.initCacheInput(jobDto);
        return jobDto;
    }

    public JobForm save(JobForm jobForm){
        if(StringUtils.isBlank(jobForm.getId())) {
            jobManager.save(jobForm);
        } else {
            jobManager.update(jobForm);
        }
        return jobForm;
    }

    public void logicDeleteOne(String id){
        jobManager.logicDeleteOne(id);
    }
}
