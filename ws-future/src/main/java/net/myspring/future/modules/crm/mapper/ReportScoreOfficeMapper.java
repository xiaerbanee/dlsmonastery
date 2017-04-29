package net.myspring.future.modules.crm.mapper;

import net.myspring.common.mybatis.MyMapper;
import net.myspring.future.modules.crm.domain.ReportScoreOffice;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

@Mapper
public interface ReportScoreOfficeMapper extends MyMapper<ReportScoreOffice,String> {

    Page<ReportScoreOffice> findPage(Pageable pageable, @Param("p") Map<String, Object> map);

}
