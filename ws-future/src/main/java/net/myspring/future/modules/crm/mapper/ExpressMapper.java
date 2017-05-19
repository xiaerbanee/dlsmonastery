package net.myspring.future.modules.crm.mapper;

import net.myspring.future.common.mybatis.MyMapper;
import net.myspring.future.modules.crm.domain.Express;
import net.myspring.future.modules.crm.dto.ExpressDto;
import net.myspring.future.modules.crm.web.query.ExpressQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Mapper
public interface ExpressMapper extends MyMapper<Express,String> {

    Page<ExpressDto> findPage(Pageable pageable, @Param("p") ExpressQuery expressQuery);

    List<Express> findByExpressOrderId(String expressOrderId);

    ExpressDto findDto(String id);

}
