package net.myspring.future.modules.crm.mapper;

import net.myspring.future.common.mybatis.MyMapper;
import net.myspring.future.modules.crm.domain.Dealer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

@Mapper
public interface DealerMapper extends MyMapper<Dealer,String> {

    Page<Dealer> findPage(Pageable pageable, @Param("p") Map<String, Object> map);
}
