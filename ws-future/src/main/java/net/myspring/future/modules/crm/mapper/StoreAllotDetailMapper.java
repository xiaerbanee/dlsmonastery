package net.myspring.future.modules.crm.mapper;

import net.myspring.future.common.mybatis.MyMapper;
import net.myspring.future.modules.crm.domain.StoreAllotDetail;
import net.myspring.future.modules.crm.dto.SimpleStoreAllotDetailDto;
import net.myspring.future.modules.crm.dto.StoreAllotDetailDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface StoreAllotDetailMapper extends MyMapper<StoreAllotDetail,String> {

    List<StoreAllotDetailDto> findByStoreAllotIds(List<String> storeAllotIds);

    void deleteByStoreAllotId(@Param("storeAllotId") String storeAllotId);

    List<SimpleStoreAllotDetailDto> findStoreAllotDetailsForFastAllot(@Param("billDate") LocalDate billDate, @Param("toStoreId") String toStoreId, @Param("status") String status, @Param("companyId") String companyId);

    List<SimpleStoreAllotDetailDto> findStoreAllotDetailListForNew(@Param("companyId")  String companyId);


}
