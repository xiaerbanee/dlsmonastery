package net.myspring.future.modules.crm.mapper;

import net.myspring.common.mybatis.MyMapper;
import net.myspring.future.modules.crm.domain.StoreAllot;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Mapper
public interface StoreAllotMapper extends MyMapper<StoreAllot,String> {

    Page<StoreAllot> findPage(Pageable pageable, @Param("p") Map<String, Object> map);

    List<StoreAllot> findByFilter(@Param("p") Map<String, Object> map);

    String findMaxBusinessId(@Param("localDate") LocalDate localDate);

}
