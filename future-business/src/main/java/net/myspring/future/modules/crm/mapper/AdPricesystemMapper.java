package net.myspring.future.modules.crm.mapper;

import net.myspring.future.common.mybatis.MyMapper;
import net.myspring.future.modules.crm.domain.AdPricesystem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

@Mapper
public interface AdPricesystemMapper extends MyMapper<AdPricesystem,String> {

    Page<AdPricesystem> findPage(Pageable pageable, @Param("p") Map<String, Object> map);

    List<AdPricesystem> findLabels(List<String> ids);

    List<AdPricesystem> findFilter(@Param("p") Map<String, Object> map);

    AdPricesystem findByName(String name);
}
