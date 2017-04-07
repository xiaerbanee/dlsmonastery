package net.myspring.basic.modules.hr.mapper;

import net.myspring.basic.common.mybatis.MyMapper;
import net.myspring.basic.modules.hr.domain.Position;
import net.myspring.basic.modules.hr.web.query.PositionQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

@Mapper
public interface PositionMapper extends MyMapper<Position,String> {

    Page<Position> findPage(Pageable pageable, @Param("p")PositionQuery positionQuery);

    List<Position> findByNameLike(@Param("name") String name);

    List<String> findPermissionByPosition(@Param("positionId") String positionId);

    int deleteByPosition(@Param("positionId") String positionId);

    int savePositionAndPermission(@Param("positionId") String positionId,@Param("permissionIds") List<String> permissionIds);

}
