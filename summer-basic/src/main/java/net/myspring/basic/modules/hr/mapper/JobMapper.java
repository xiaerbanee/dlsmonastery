package net.myspring.basic.modules.hr.mapper;

import net.myspring.common.mybatis.MyMapper;
import net.myspring.basic.modules.hr.domain.Job;
import net.myspring.basic.modules.hr.dto.JobDto;
import net.myspring.basic.modules.hr.web.query.JobQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Mapper
public interface JobMapper extends MyMapper<Job,String> {

    Page<JobDto> findPage(Pageable pageable, @Param("p")JobQuery jobQuery);
}
