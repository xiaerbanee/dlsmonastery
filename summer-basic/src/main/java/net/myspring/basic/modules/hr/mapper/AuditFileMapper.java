package net.myspring.basic.modules.hr.mapper;

import net.myspring.basic.common.mybatis.MyMapper;
import net.myspring.basic.modules.hr.domain.AuditFile;
import net.myspring.basic.modules.hr.web.query.AuditFileQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

@Mapper
public interface AuditFileMapper extends MyMapper<AuditFile,String> {

    Page<AuditFile> findPage(Pageable pageable, @Param("p") AuditFileQuery auditFileQuery);
}
