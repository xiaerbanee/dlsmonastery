package net.myspring.basic.modules.sys.mapper;

import net.myspring.basic.common.mybatis.MyMapper;
import net.myspring.basic.modules.sys.domain.ProcessType;
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

    Page<ProcessType> findPage(Pageable pageable, @Param("p") Map<String, Object> map);

    List<String> findPositionNames(String permissionId);

}
