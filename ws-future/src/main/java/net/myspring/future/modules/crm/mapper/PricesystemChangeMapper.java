package net.myspring.future.modules.crm.mapper;

import net.myspring.future.common.mybatis.MyMapper;
import net.myspring.future.modules.crm.domain.PricesystemChange;
import net.myspring.future.modules.crm.dto.PricesystemChangeDto;
import net.myspring.future.modules.crm.web.query.PricesystemChangeQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

@Mapper
public interface PricesystemChangeMapper extends MyMapper<PricesystemChange,String> {

    Page<PricesystemChangeDto> findPage(Pageable pageable, @Param("p")PricesystemChangeQuery pricesystemChangeQuery);

    void audit(@Param("ids")String[] ids,@Param("pass") Boolean pass);

    void auditOperation(@Param("id") String id,@Param("pass") Boolean pass);

}
