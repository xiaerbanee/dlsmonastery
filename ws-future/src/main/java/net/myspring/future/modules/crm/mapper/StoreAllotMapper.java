package net.myspring.future.modules.crm.mapper;

import net.myspring.future.common.mybatis.MyMapper;
import net.myspring.future.modules.crm.domain.StoreAllot;
import net.myspring.future.modules.crm.dto.StoreAllotDto;
import net.myspring.future.modules.crm.dto.StoreAllotImeDto;
import net.myspring.future.modules.crm.web.query.StoreAllotQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

@Mapper
public interface StoreAllotMapper extends MyMapper<StoreAllot,String> {

    Page<StoreAllotDto> findPage(Pageable pageable, @Param("p") StoreAllotQuery storeAllotQuery);




    String findMaxBusinessId(@Param("localDate") LocalDate localDate);

    StoreAllotDto findStoreAllotDtoById( @Param("id") String id);

    Page<StoreAllotImeDto> findStoreAllotImeDtoList(Pageable pageable, @Param("p") StoreAllotQuery storeAllotQuery);
}
