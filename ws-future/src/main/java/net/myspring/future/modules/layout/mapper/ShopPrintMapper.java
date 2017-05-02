package net.myspring.future.modules.layout.mapper;

import net.myspring.common.mybatis.MyMapper;
import net.myspring.future.modules.layout.domain.ShopPrint;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

@Mapper
public interface ShopPrintMapper extends MyMapper<ShopPrint,String> {

    Page<ShopPrint> findPage(Pageable pageable, @Param("p") Map<String, Object> map);

}
