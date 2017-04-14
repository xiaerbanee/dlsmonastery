package net.myspring.future.modules.crm.mapper;

import net.myspring.future.common.mybatis.MyMapper;
import net.myspring.future.modules.crm.domain.AdPricesystemChange;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

@Mapper
public interface AdPricesystemChangeMapper extends MyMapper<AdPricesystemChange,String> {

    Page<AdPricesystemChange> findPage(Pageable pageable, @Param("p") Map<String, Object> map);

    List<AdPricesystemChange> findFilter(@Param("p") Map<String, Object> map);


}
