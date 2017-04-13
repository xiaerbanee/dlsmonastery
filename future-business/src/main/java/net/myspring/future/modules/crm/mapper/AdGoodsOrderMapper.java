package net.myspring.future.modules.crm.mapper;

import net.myspring.future.common.mybatis.MyMapper;
import net.myspring.future.modules.crm.domain.AdGoodsOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Map;

@Mapper
public interface AdGoodsOrderMapper extends MyMapper<AdGoodsOrder,String> {

    Page<AdGoodsOrder> findPage(@Param("pageable") Pageable pageable, @Param("p") Map<String, Object> map);

    String findMaxBusinessId(@Param("localDate") LocalDate localDate);

}
