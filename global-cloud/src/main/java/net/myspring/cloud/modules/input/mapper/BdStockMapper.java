package net.myspring.cloud.modules.input.mapper;

import net.myspring.cloud.modules.input.domain.BdStock;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by lihx on 2017/4/5.
 */
@Mapper
public interface BdStockMapper {

    List<BdStock> findAll();

    List<BdStock> findByDate(@Param("maxOutDate")LocalDateTime maxOutDate);
}
