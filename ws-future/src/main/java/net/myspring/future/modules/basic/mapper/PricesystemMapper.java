package net.myspring.future.modules.basic.mapper;

import net.myspring.common.mybatis.MyMapper;
import net.myspring.future.modules.basic.domain.Pricesystem;
import net.myspring.future.modules.basic.dto.PricesystemDto;
import net.myspring.future.modules.basic.web.query.PricesystemQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Mapper
public interface PricesystemMapper extends MyMapper<Pricesystem,String> {

    Page<PricesystemDto> findPage(Pageable pageable, @Param("p")PricesystemQuery pricesystemQuery);

    List<Pricesystem> findLabels(List<String> ids);
}
