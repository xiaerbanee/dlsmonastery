package net.myspring.future.modules.crm.mapper;

import net.myspring.future.common.mybatis.MyMapper;
import net.myspring.future.modules.crm.domain.StoreAllotIme;
import net.myspring.future.modules.crm.dto.StoreAllotImeDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface StoreAllotImeMapper extends MyMapper<StoreAllotIme,String> {

    List<StoreAllotIme> findByProductImeId(@Param("productImeId") String productImeId);

    List<StoreAllotIme> findByStoreAllotFilter(@Param("storeAllotList") List<String> storeAllotList, @Param("p") Map<String, Object> map);

    List<StoreAllotImeDto> findByStoreAllotId(@Param("storeAllotId") String id);
}
