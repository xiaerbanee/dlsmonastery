package net.myspring.future.modules.crm.mapper;

import net.myspring.common.mybatis.MyMapper;
import net.myspring.future.modules.crm.domain.StoreAllotIme;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface StoreAllotImeMapper extends MyMapper<StoreAllotIme,String> {

    List<StoreAllotIme> findByProductImeId(@Param("productImeId") String productImeId);

    List<StoreAllotIme> findByStoreAllotFilter(@Param("storeAllotList") List<String> storeAllotList, @Param("p") Map<String, Object> map);
}
