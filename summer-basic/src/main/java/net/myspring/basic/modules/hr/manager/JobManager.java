package net.myspring.basic.modules.hr.manager;

import net.myspring.basic.modules.hr.domain.Job;
import net.myspring.basic.modules.hr.mapper.JobMapper;
import net.myspring.basic.modules.hr.web.form.JobForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

/**
 * Created by admin on 2017/4/5.
 */
@Component
@CacheConfig(cacheNames = "jobs")
public class JobManager {
    @Autowired
    private JobMapper jobMapper;

    @Cacheable(key="#p0")
    public Job findOne(String id) {
        return jobMapper.findOne(id);
    }

    @CachePut(key="#p0.id")
    public Job save(Job job){
        jobMapper.save(job);
        return  job;
    }

    @CachePut(key="#p0.id")
    public Job update(Job job){
        jobMapper.update(job);
        return  jobMapper.findOne(job.getId());
    }

    @CachePut(key="#p0.id")
    public Job updateForm(JobForm jobForm){
        jobMapper.updateForm(jobForm);
        return  jobMapper.findOne(jobForm.getId());
    }
}
