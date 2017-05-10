package net.myspring.future.modules.crm.mapper;

import net.myspring.future.common.mybatis.MyMapper;
import net.myspring.future.modules.crm.domain.StoreAllotDetail;
import net.myspring.future.modules.crm.dto.StoreAllotDetailDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface StoreAllotDetailMapper extends MyMapper<StoreAllotDetail,String> {

    List<StoreAllotDetailDto> findByStoreAllotIds(List<String> storeAllotIds);

    List<StoreAllotDetailDto> getStoreAllotDetailListForNew(@Param("companyId") String companyId);

    void deleteByStoreAllotId(@Param("storeAllotId") String storeAllotId);

    List<StoreAllotDetailDto> findStoreAllotDetailsForFastAllot(@Param("billDate") LocalDate billDate, @Param("toStoreId") String toStoreId, @Param("status") String status);
}
