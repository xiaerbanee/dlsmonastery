package net.myspring.future.modules.crm.mapper;

import net.myspring.future.common.mybatis.MyMapper;
import net.myspring.future.modules.crm.domain.AfterSale;
import net.myspring.future.modules.crm.web.query.AfterSaleQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Mapper
public interface AfterSaleMapper extends MyMapper<AfterSale,String> {

    Page<AfterSale> findPage(@Param("pageable") Pageable pageable, @Param("p")AfterSaleQuery afterSaleQuery);

    List<AfterSale> findFilter(@Param("p") Map<String, Object> map);

    List<AfterSale> findByBadProductImeIn(List<String> imeList);

    String findMaxBusinessId(LocalDate dateStart);

    List<AfterSale> findByToFinanceDateIsNull();
}
