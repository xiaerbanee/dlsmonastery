package net.myspring.general.modules.sys.mapper;

import net.myspring.general.common.mybatis.MyProvider;
import net.myspring.general.modules.sys.domain.Town;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TownMapper {

    List<Town> findByNameLike(String name);
}
