package net.myspring.basic.modules.sys.mapper;

import net.myspring.basic.common.mybatis.MyMapper;
import net.myspring.basic.modules.sys.domain.DictMap;
import net.myspring.basic.modules.sys.dto.DictMapDto;
import net.myspring.basic.modules.sys.web.query.DictMapQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

@Mapper
public interface DictMapMapper extends MyMapper<DictMap,String> {

    List<String> findDistinctCategory();

    Page<DictMapDto> findPage(Pageable pageable, @Param("p")DictMapQuery dictMapQuery);

    List<DictMap> findByCategory(String category);


}
