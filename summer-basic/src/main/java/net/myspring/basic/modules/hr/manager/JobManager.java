package net.myspring.basic.modules.hr.manager;

import net.myspring.basic.modules.hr.domain.Job;
import net.myspring.basic.modules.hr.mapper.JobMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by admin on 2017/4/5.
 */
@Component
public class JobManager {
    @Autowired
    private JobMapper jobMapper;

    @Cacheable(value = "jobs",key="#p0")
    public Job findOne(String id) {
        return jobMapper.findOne(id);
    }

    public Page<Job> findPage(Pageable pageable, Map<String, Object> map) {
        Page<Job> page = jobMapper.findPage(pageable, map);
        return page;
    }

    @CachePut(value = "jobs",key="#p0.id")
    public Job save(Job job){
        jobMapper.save(job);
        return  job;
    }

    @CachePut(value = "jobs",key="#p0.id")
    public Job update(Job job){
        jobMapper.update(job);
        return  jobMapper.findOne(job.getId());
    }

    public void logicDeleteOne(String id) {
        jobMapper.logicDeleteOne(id);
    }
}
