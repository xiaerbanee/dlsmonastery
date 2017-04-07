package net.myspring.basic.modules.sys.mapper;

import net.myspring.basic.common.mybatis.MyMapper;
import net.myspring.basic.modules.sys.domain.MenuCategory;
import net.myspring.basic.modules.sys.web.query.MenuCategoryQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

@Mapper
public interface MenuCategoryMapper extends MyMapper<MenuCategory,String> {

    Page<MenuCategory> findPage(Pageable pageable, @Param("p")MenuCategoryQuery menuCategoryQuery);

    MenuCategory findByName(String name);
}
