package net.myspring.basic.modules.sys.mapper;

import net.myspring.basic.common.mybatis.MyMapper;
import net.myspring.basic.modules.sys.domain.Backend;
import net.myspring.basic.modules.sys.dto.BackendDto;
import net.myspring.basic.modules.sys.web.query.BackendQuery;
import net.myspring.mybatis.mapper.CrudMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Mapper
public interface BackendMapper extends MyMapper<Backend,String> {

    Page<BackendDto> findPage(Pageable pageable, @Param("p")BackendQuery backendQuery);
}
