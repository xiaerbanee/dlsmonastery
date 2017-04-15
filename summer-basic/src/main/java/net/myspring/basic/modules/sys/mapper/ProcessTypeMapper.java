package net.myspring.basic.modules.sys.mapper;

import net.myspring.basic.common.mybatis.MyMapper;
import net.myspring.basic.modules.sys.domain.ProcessType;
import net.myspring.basic.modules.sys.dto.ProcessTypeDto;
import net.myspring.basic.modules.sys.web.query.ProcessTypeQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

@Mapper
public interface ProcessTypeMapper extends MyMapper<ProcessType,String> {

    ProcessType findByName(String name);

    List<ProcessType> findEnabledAuditFileType();

    Page<ProcessTypeDto> findPage(Pageable pageable, @Param("p")ProcessTypeQuery processTypeQuery);

    List<String> findPositionNames(String permissionId);

}
