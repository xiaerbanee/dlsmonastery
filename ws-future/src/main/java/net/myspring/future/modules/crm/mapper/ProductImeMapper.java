package net.myspring.future.modules.crm.mapper;

import net.myspring.future.common.mybatis.MyMapper;
import net.myspring.future.modules.crm.domain.ProductIme;
import net.myspring.future.modules.crm.dto.ProductImeDto;
import net.myspring.future.modules.crm.dto.ProductImeHistoryDto;
import net.myspring.future.modules.crm.web.query.ProductImeQuery;
import net.myspring.future.modules.crm.web.query.ProductImeShipQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductImeMapper extends MyMapper<ProductIme, String> {

    List<ProductIme> findByImeList(List<String> imeList);

    ProductIme findByIme(String ime);

    List<ProductImeDto> findDtoListByImeList(@Param("imeList")List<String> imeList, @Param("companyId") String companyId);

    List<ProductIme> findShipList(ProductImeShipQuery productImeShipQuery);

    List<ProductImeDto> findList(@Param("p")ProductImeQuery productImeQuery);

    ProductImeDto getProductImeDetail(@Param("productImeId") String productImeId);

    List<ProductImeHistoryDto> getProductImeHistoryList(@Param("productImeId") String productImeId);

    List<ProductIme> findByImeLike(@Param("imeReverse")String ime, @Param("shopId")String shopId);

}
