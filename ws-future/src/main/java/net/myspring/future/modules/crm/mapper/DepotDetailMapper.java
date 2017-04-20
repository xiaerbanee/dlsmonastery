package net.myspring.future.modules.crm.mapper;

import net.myspring.future.modules.crm.domain.DepotDetail;
import net.myspring.mybatis.mapper.CrudMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

@Mapper
public interface DepotDetailMapper  extends CrudMapper<DepotDetail,String> {
    Page<DepotDetail> findPage(Pageable pageable, @Param("p") Map<String, Object> map);

    List<DepotDetail> findByFilter(@Param("p") Map<String, Object> map);
}
