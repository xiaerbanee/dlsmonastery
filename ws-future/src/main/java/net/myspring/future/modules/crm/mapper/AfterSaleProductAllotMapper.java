package net.myspring.future.modules.crm.mapper;

import net.myspring.common.mybatis.MyMapper;
import net.myspring.future.modules.crm.domain.AfterSaleProductAllot;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

@Mapper
public interface AfterSaleProductAllotMapper extends MyMapper<AfterSaleProductAllot,String> {

    Page<AfterSaleProductAllot> findPage(@Param("pageable") Pageable pageable, @Param("p") Map<String, Object> map);

}
