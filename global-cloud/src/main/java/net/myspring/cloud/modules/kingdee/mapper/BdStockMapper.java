package net.myspring.cloud.modules.kingdee.mapper;

import net.myspring.cloud.modules.kingdee.domain.BdStock;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 仓库
 * Created by lihx on 2017/5/12.
 */
@Mapper
public interface BdStockMapper {

    List<BdStock> findByNameLike(@Param("name")String name);
}
