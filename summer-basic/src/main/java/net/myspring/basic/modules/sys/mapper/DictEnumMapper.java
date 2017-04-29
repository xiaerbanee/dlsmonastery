package net.myspring.basic.modules.sys.mapper;

import net.myspring.common.mybatis.MyMapper;
import net.myspring.basic.modules.sys.domain.DictEnum;
import net.myspring.basic.modules.sys.dto.DictEnumDto;
import net.myspring.basic.modules.sys.web.query.DictEnumQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Mapper
public interface DictEnumMapper extends MyMapper<DictEnum,String> {

    List<DictEnum> findByCategory(String category);

    Page<DictEnumDto> findPage(Pageable pageable, @Param("p")DictEnumQuery dictEnumQuery);

    List<String> findDistinctCategory();

}
