package net.myspring.basic.modules.sys.mapper;

import net.myspring.basic.common.mybatis.MyMapper;
import net.myspring.basic.modules.sys.domain.Menu;
import net.myspring.basic.modules.sys.dto.MenuDto;
import net.myspring.basic.modules.sys.web.query.MenuQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

@Mapper
public interface MenuMapper extends MyMapper<Menu,String> {

    Page<MenuDto> findPage(Pageable pageable, @Param("p") MenuQuery menuQuery);

    List<Menu> findByPermissionIsEmpty();

    List<Menu> findByPermissionIsNotEmpty();

    List<String> findDistinctCategory();

    List<Menu> findByMenuCategoryId( String menuCategoryId);

}
