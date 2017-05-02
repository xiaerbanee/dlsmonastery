package net.myspring.future.modules.crm.mapper;

import net.myspring.common.mybatis.MyMapper;
import net.myspring.future.modules.crm.domain.ExpressOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

@Mapper
public interface ExpressOrderMapper extends MyMapper<ExpressOrder,String> {

    Page<ExpressOrder> findPage(Pageable pageable, @Param("p") Map<String, Object> map);

    ExpressOrder findByExtendIdAndType(@Param("extendId") String id, @Param("extendType") String type);

    List<ExpressOrder> findLabels(List<String> ids);
}
