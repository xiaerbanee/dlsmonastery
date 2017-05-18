package net.myspring.future.modules.layout.mapper;

import net.myspring.future.common.mybatis.MyMapper;
import net.myspring.future.modules.layout.domain.AdPricesystemChange;
import net.myspring.future.modules.layout.dto.AdPricesystemChangeDto;
import net.myspring.future.modules.layout.web.query.AdPricesystemChangeQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

@Mapper
public interface AdPricesystemChangeMapper extends MyMapper<AdPricesystemChange,String> {

    Page<AdPricesystemChangeDto> findPage(Pageable pageable, @Param("p")AdPricesystemChangeQuery adPricesystemChangeQuery);

    List<AdPricesystemChangeDto> findFilter(@Param("p")AdPricesystemChangeQuery adPricesystemChangeQuery);


}
