package net.myspring.future.modules.crm.mapper;

import net.myspring.future.common.mybatis.MyMapper;
import net.myspring.future.modules.crm.domain.ExpressOrder;
import net.myspring.future.modules.crm.dto.ExpressOrderDto;
import net.myspring.future.modules.crm.web.query.ExpressOrderQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Mapper
public interface ExpressOrderMapper extends MyMapper<ExpressOrder,String> {

    Page<ExpressOrderDto> findPage(Pageable pageable, @Param("p") ExpressOrderQuery expressOrderQuery);

    ExpressOrder findByExtendIdAndType(@Param("extendId") String extendId, @Param("extendType") String type);

    List<ExpressOrder> findByIds(List<String> ids);

    ExpressOrderDto findDto(@Param("id")  String id);
}
