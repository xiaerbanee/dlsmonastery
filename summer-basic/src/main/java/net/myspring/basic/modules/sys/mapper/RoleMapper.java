package net.myspring.basic.modules.sys.mapper;

import net.myspring.basic.modules.sys.domain.Role;
import net.myspring.basic.modules.sys.dto.RoleDto;
import net.myspring.basic.modules.sys.web.query.RoleQuery;
import net.myspring.common.mybatis.MyMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by wangzm on 2017/5/2.
 */
@Mapper
public interface RoleMapper extends MyMapper<Role,String>{

    Page<RoleDto> findPage(Pageable pageable, @Param("p")RoleQuery roleQuery);

    List<Role> findByNameLike(String name);
}
