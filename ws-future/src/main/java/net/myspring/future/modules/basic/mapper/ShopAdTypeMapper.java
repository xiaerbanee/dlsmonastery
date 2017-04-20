package net.myspring.future.modules.basic.mapper;

import net.myspring.future.common.mybatis.MyMapper;
import net.myspring.future.modules.basic.domain.ShopAdType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

@Mapper
public interface ShopAdTypeMapper extends MyMapper<ShopAdType,String> {

    List<ShopAdType> findAllByEnabled();

    Page<ShopAdType> findPage(Pageable pageable, @Param("p") Map<String, Object> map);

    List<ShopAdType> findLabels(List<String> ids);

}
