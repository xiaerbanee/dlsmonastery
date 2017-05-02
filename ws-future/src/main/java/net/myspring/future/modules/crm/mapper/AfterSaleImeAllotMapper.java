package net.myspring.future.modules.crm.mapper;

import net.myspring.common.mybatis.MyMapper;
import net.myspring.future.modules.crm.domain.AfterSaleImeAllot;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

@Mapper
public interface AfterSaleImeAllotMapper extends MyMapper<AfterSaleImeAllot,String> {

    List<AfterSaleImeAllot> findByProductImeId(@Param("productImeId") String productImeId);

    List<AfterSaleImeAllot> findByAfterSaleId(String afterSaleId);

    Page<AfterSaleImeAllot> findPage(@Param("pageable") Pageable pageable, @Param("p") Map<String, Object> map);


}
