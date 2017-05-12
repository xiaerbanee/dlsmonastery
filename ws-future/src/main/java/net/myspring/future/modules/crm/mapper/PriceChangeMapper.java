package net.myspring.future.modules.crm.mapper;

import net.myspring.future.common.mybatis.MyMapper;
import net.myspring.future.modules.crm.domain.PriceChange;
import net.myspring.future.modules.crm.dto.PriceChangeDto;
import net.myspring.future.modules.crm.web.query.PriceChangeQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface PriceChangeMapper extends MyMapper<PriceChange,String> {

    List<PriceChange> finAllByEnabled(LocalDateTime uploadEndDate);

    Page<PriceChangeDto> findPage(Pageable pageable, @Param("p")PriceChangeQuery priceChangeQuery);

    PriceChange findNearPriceChange();
}
