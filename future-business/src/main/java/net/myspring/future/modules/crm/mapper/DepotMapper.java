package net.myspring.future.modules.crm.mapper;

import net.myspring.future.common.mybatis.MyMapper;
import net.myspring.future.modules.crm.domain.Depot;
import net.myspring.future.modules.crm.model.DepotInventoryModel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface DepotMapper extends MyMapper<Depot,String> {

    List<DepotInventoryModel> findInventoryData(@Param("p") Map<String, Object> map);

    Page<Depot> findPage(Pageable pageable, @Param("p") Map<String, Object> map);

    Page<Depot> findDepotAccountPage(Pageable pageable, @Param("p") Map<String, Object> map);

    List<Depot> findShopAccountExportPage(@Param("p") Map<String, Object> map);

    List<Depot> findByTypes(List<Integer> depotTypes);

    List<Depot> findByOfficeId(String officeId);

    List<Depot> findByAccountId(String accountId);

    List<Depot> findByFilter(@Param("p") Map<String, Object> map);

    List<Depot> findByFilterAll(@Param("p") Map<String, Object> map);

    List<String> findChainIds(@Param("p") Map<String, Object> map);

    List<Depot> findByChainId(String chainId);

    List<Depot> findAllByOffice(@Param("officeIds") List<String> officeIds);

    List<Depot> findLabels(List<String> ids);

    List<Depot> findByAdPricesystemId(String adPricesystemId);

    Depot findByName(String name);

    List<Depot> findByOutGroupId(String outGroupId);

    List<Depot> findByOutTypeAndOutId(@Param("outType") String outType, @Param("outId") String outId);

    Depot findAllByNameAndOutId(@Param("name") String name, @Param("outId") String outId);

    int deleteDepotAccount(@Param("depotId") String depotId);

    int saveDepotAccount(@Param("depotId") String accountId, @Param("accountIds") List<String> accountIds);

    LocalDateTime getMaxOutDate(@Param("outType") String outType);

}