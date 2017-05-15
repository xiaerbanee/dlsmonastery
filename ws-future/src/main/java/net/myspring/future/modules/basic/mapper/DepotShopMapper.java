package net.myspring.future.modules.basic.mapper;

import net.myspring.future.common.mybatis.MyMapper;
import net.myspring.future.modules.basic.domain.DepotShop;
import net.myspring.future.modules.basic.dto.DepotShopDto;
import net.myspring.future.modules.basic.web.query.DepotQuery;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Created by liuj on 2017/5/12.
 */

@Mapper
public interface DepotShopMapper extends MyMapper<DepotShop,String> {

    Page<DepotShopDto> findPage(Pageable pageable, DepotQuery depotShopQuery);

}