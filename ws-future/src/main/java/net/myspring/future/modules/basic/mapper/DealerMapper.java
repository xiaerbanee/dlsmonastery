package net.myspring.future.modules.basic.mapper;

import net.myspring.future.common.mybatis.MyMapper;
import net.myspring.future.modules.basic.domain.Dealer;
import net.myspring.future.modules.basic.dto.DealerDto;
import net.myspring.future.modules.basic.web.Query.DealerQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

@Mapper
public interface DealerMapper extends MyMapper<Dealer,String> {

    Page<DealerDto> findPage(Pageable pageable, @Param("p")DealerQuery dealerQuery);
}
