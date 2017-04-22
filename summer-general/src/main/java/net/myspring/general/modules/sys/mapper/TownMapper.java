package net.myspring.general.modules.sys.mapper;

import net.myspring.general.common.mybatis.MyMapper;
import net.myspring.general.modules.sys.domain.Town;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TownMapper {

    List<Town> findByLikeName(String name);
}
