package net.myspring.basic.modules.hr.web.query;

import com.google.common.collect.Lists;
import net.myspring.basic.modules.hr.dto.JobDto;

import java.util.List;

/**
 * Created by lihx on 2017/4/7.
 */
public class PositionQuery {
    private String name;
    private String jobId;
    private List<JobDto> jobList= Lists.newArrayList();

    public List<JobDto> getJobList() {
        return jobList;
    }

    public void setJobList(List<JobDto> jobList) {
        this.jobList = jobList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }
}
