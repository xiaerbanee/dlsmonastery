package net.myspring.future.modules.crm.mapper;

import net.myspring.future.common.mybatis.MyMapper;
import net.myspring.future.modules.crm.domain.ProductIme;
import net.myspring.future.modules.crm.dto.ProductImeDto;
import net.myspring.future.modules.crm.web.query.ProductImeQuery;
import net.myspring.future.modules.crm.web.query.ProductImeShipQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Mapper
public interface ProductImeMapper extends MyMapper<ProductIme, String> {

    List<ProductIme> findByImeList(List<String> imeList);

    ProductIme findByIme(String ime);

    List<ProductIme> findShipList(ProductImeShipQuery productImeShipQuery);

    List<ProductIme> findList(ProductImeQuery productImeQuery);

}
