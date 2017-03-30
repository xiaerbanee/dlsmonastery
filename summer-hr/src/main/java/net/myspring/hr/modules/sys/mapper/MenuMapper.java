package net.myspring.hr.modules.sys.mapper;

import net.myspring.hr.common.mybatis.MyMapper;
import net.myspring.hr.modules.sys.domain.Menu;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MenuMapper extends MyMapper<Menu,String> {

}
