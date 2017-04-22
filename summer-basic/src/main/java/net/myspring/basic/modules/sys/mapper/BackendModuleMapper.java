package net.myspring.basic.modules.sys.mapper;

import net.myspring.basic.common.mybatis.MyMapper;
import net.myspring.basic.modules.sys.domain.BackendModule;
import net.myspring.basic.modules.sys.dto.BackendModuleDto;
import net.myspring.basic.modules.sys.web.query.BackendModuleQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Mapper
public interface BackendModuleMapper extends MyMapper<BackendModule,String>{

    Page<BackendModuleDto> findPage(Pageable pageable, @Param("p")BackendModuleQuery backendModuleQuery);

    List<BackendModule> findByBackendIds(List<String> backendIds);

    List<BackendModule> findByPositionId(String positionId);

}
