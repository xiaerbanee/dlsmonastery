package net.myspring.basic.modules.hr.mapper;

import net.myspring.common.mybatis.MyMapper;
import net.myspring.basic.modules.hr.domain.Position;
import net.myspring.basic.modules.hr.dto.PositionDto;
import net.myspring.basic.modules.hr.web.query.PositionQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Mapper
public interface PositionMapper extends MyMapper<Position,String> {

    Page<PositionDto> findPage(Pageable pageable, @Param("p")PositionQuery positionQuery);

    List<Position> findByNameLike(@Param("name") String name);
}
