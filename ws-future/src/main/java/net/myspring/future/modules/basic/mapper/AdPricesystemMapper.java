package net.myspring.future.modules.basic.mapper;

import net.myspring.future.common.mybatis.MyMapper;
import net.myspring.future.modules.basic.domain.AdPricesystem;
import net.myspring.future.modules.basic.dto.AdPricesystemDto;
import net.myspring.future.modules.basic.web.query.AdPricesystemQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Mapper
public interface AdPricesystemMapper extends MyMapper<AdPricesystem,String> {

    Page<AdPricesystemDto> findPage(Pageable pageable, @Param("p") AdPricesystemQuery adPricesystemQuery);

    List<AdPricesystem> findLabels(List<String> ids);

    List<AdPricesystem> findFilter(@Param("p") AdPricesystemQuery adPricesystemQuery);

    AdPricesystem findByName(String name);
}
