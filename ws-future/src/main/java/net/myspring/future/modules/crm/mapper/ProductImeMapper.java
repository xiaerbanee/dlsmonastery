package net.myspring.future.modules.crm.mapper;

import net.myspring.common.mybatis.MyMapper;
import net.myspring.future.modules.crm.domain.ProductIme;
import net.myspring.future.modules.crm.model.ProductImeHistoryModel;
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

    List<ProductIme> findByImeLike(String ime);

    ProductIme findByIme(String ime);

    List<ProductIme> findByStoreId(String storeId);

    List<ProductIme> findByStoreAndImeList(@Param("storeId") String storeId, @Param("list") List<String> imeList);

    List<ProductIme> findByStoreAndBoxList(@Param("storeId") String storeId, @Param("list") List<String> boxList);

    List<ProductIme> findByStoreAndBoxImeAndIme(@Param("storeId") String storeId, @Param("boxList") List<String> boxIme, @Param("imeList") List<String> imeList);

    List<ProductImeHistoryModel> findProductImeHistoryModel(String id);

    Page<ProductIme> findPage(Pageable pageable, @Param("p") Map<String, Object> map);

    List<ProductIme> findLabels(List<String> ids);

    List<ProductIme> findByStoreAndImeLike(@Param("depotId") String depotId, @Param("ime") String ime);

    List<ProductIme> findByFilter(@Param("p") Map<String, Object> map);

    List<ProductIme> findByRetailDate(@Param("dateStart") LocalDate dateStart, @Param("dateEnd") LocalDate dateEnd);

}
