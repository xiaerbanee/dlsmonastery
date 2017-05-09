package net.myspring.future.modules.crm.mapper;

import net.myspring.future.common.mybatis.MyMapper;
import net.myspring.future.modules.crm.domain.PriceChange;
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

    Page<PriceChange> findPage(Pageable pageable, @Param("p") Map<String, Object> map);

    PriceChange findNearPriceChange();
}
