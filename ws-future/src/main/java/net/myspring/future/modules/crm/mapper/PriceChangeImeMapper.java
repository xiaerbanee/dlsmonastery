package net.myspring.future.modules.crm.mapper;

import net.myspring.future.common.mybatis.MyMapper;
import net.myspring.future.modules.crm.domain.PriceChangeIme;
import net.myspring.future.modules.crm.dto.PriceChangeImeDto;
import net.myspring.future.modules.crm.web.query.PriceChangeImeQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Mapper
public interface PriceChangeImeMapper extends MyMapper<PriceChangeIme,String> {

    Page<PriceChangeImeDto> findPage(Pageable pageable, @Param("p")PriceChangeImeQuery priceChangeImeQuery);

    List<PriceChangeIme> findByPriceChangeId(String priceChangeId);

    List<PriceChangeIme> findByUploadDateIsNotNull(String priceChangeId);

    List<PriceChangeIme> findCheckList(String priceChangeId, LocalDate maxSaleDate);

}
