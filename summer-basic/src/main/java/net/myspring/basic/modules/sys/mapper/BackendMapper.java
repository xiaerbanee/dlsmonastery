package net.myspring.basic.modules.sys.mapper;

import net.myspring.common.mybatis.MyMapper;
import net.myspring.basic.modules.sys.domain.Backend;
import net.myspring.basic.modules.sys.dto.BackendDto;
import net.myspring.basic.modules.sys.dto.BackendMenuDto;
import net.myspring.basic.modules.sys.web.query.BackendQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Mapper
public interface BackendMapper extends MyMapper<Backend,String> {

    Page<BackendDto> findPage(Pageable pageable, @Param("p")BackendQuery backendQuery);

    List<Backend> findByNameLike(String name);

    List<BackendMenuDto> findByMenuList(List<String> menuList);
}
