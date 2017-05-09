package net.myspring.basic.modules.hr.mapper;

import net.myspring.basic.modules.hr.domain.Position;
import net.myspring.common.cache.IdCacheKeyGenerator;
import net.myspring.basic.modules.hr.dto.PositionDto;
import net.myspring.basic.modules.hr.web.query.PositionQuery;
import net.myspring.basic.common.mybatis.MyProvider;
import net.myspring.mybatis.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.cache.annotation.CacheDefaults;
import javax.cache.annotation.CachePut;
import javax.cache.annotation.CacheResult;
import javax.cache.annotation.CacheValue;
import java.util.List;

@Mapper
@CacheDefaults(cacheName = "positions")
public interface PositionMapper extends BaseMapper<Position,String> {

    @CacheResult
    @SelectProvider(type = MyProvider.class, method = MyProvider.FIND_ONE)
    Position findOne(String id);

    @CachePut(cacheKeyGenerator = IdCacheKeyGenerator.class)
    @InsertProvider(type = MyProvider.class, method = MyProvider.SAVE)
    @Options(useGeneratedKeys = true)
    int save(@CacheValue Position position);

    @CachePut(cacheKeyGenerator = IdCacheKeyGenerator.class)
    @UpdateProvider(type = MyProvider.class, method = MyProvider.UPDATE)
    int update(@CacheValue Position position);

    @UpdateProvider(type=MyProvider.class, method = MyProvider.LOGIC_DELETE_ONE)
    int logicDeleteOne(String id);

    @SelectProvider(type = MyProvider.class, method = MyProvider.FIND_ALL_ENABLED)
    List<Position> findAllEnabled();

    @SelectProvider(type = MyProvider.class, method = MyProvider.FIND_ALL)
    List<Position> findAll();

    Page<PositionDto> findPage(Pageable pageable, @Param("p")PositionQuery positionQuery);

    List<Position> findByNameLike(@Param("name") String name);
}
