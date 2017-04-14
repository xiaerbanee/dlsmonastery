package net.myspring.future.modules.crm.mapper;

import net.myspring.future.common.mybatis.MyMapper;
import net.myspring.future.modules.crm.domain.Express;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

@Mapper
public interface ExpressMapper extends MyMapper<Express,String> {

    Page<Express> findPage(Pageable pageable, @Param("p") Map<String, Object> map);

    List<Express> findByExpressOrderId(String expressOrderId);
}
