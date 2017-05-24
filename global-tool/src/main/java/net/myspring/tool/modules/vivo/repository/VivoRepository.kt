package net.myspring.tool.modules.vivo.repository;

import net.myspring.tool.modules.vivo.domain.*;
import net.myspring.tool.modules.vivo.model.SCustomersM13e00;
import net.myspring.tool.modules.vivo.model.SZonesM13e00;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;
import net.myspring.tool.modules.vivo.domain.SPlantStockStoresM13e00
import net.myspring.tool.modules.vivo.domain.SPlantStockDealerM13e00
import net.myspring.tool.modules.vivo.domain.SPlantStockSupplyM13e00
import net.myspring.tool.modules.vivo.domain.SPlantEndProductSaleM13e00
import net.myspring.tool.modules.vivo.domain.SProductItemStocksM13e00
import net.myspring.tool.modules.vivo.domain.SProductItem000M13e00
import net.myspring.tool.modules.vivo.domain.SStoresM13e00
import net.myspring.tool.modules.vivo.domain.SProductItemLendM13e00
import net.myspring.tool.modules.vivo.domain.VivoPlantElectronicsn
import net.myspring.tool.modules.vivo.domain.VivoPlantSendimei
import net.myspring.tool.modules.vivo.domain.VivoPlantProducts
import net.myspring.tool.modules.vivo.domain.VivoProducts
import org.springframework.data.jpa.repository.Query


/**
 * Created by admin on 2016/10/17.
 */
interface VivoMapper {


   @Query("""
        select *  from VR_Products
        """, nativeQuery = true)
   fun products(): List<VivoProducts>

   @Query("""
        select *  from vr_products_m13e00
        """, nativeQuery = true)
   fun plantProducts(): List<VivoPlantProducts>

   @Query("""
        select *  from vr_plant_sendimei_m13e00 t1
        where t1.createdTime >= :dateStart
        and t1.createdTime <= :dateEnd
        and t1.companyId in :agentCodes
        """, nativeQuery = true)
   fun plantSendimei(@Param("dateStart") dateStart: LocalDate, @Param("dateEnd") dateEnd: LocalDate, @Param("agentCodes") agentCodes: List<String>): List<VivoPlantSendimei>

   @Query("""
        select snImei,companyId,productId,retailDate,createTime
        from vr_plant_electronicsn_m13e00  t1
        where t1.retailDate >= :dateStart
        and t1.retailDate <= :dateEnd
        """, nativeQuery = true)
   fun plantElectronicsn(@Param("dateStart") dateStart: LocalDate, @Param("dateEnd") dateEnd: LocalDate): List<VivoPlantElectronicsn>

   @Query("""
        delete from S_ZONES_M13E00 where ZoneId = ?1
        """, nativeQuery = true)
   fun deleteZones(mainCode: String): Int

   @Query("""

        """, nativeQuery = true)
   fun idvivoDeleteZones(): Int

   @Query("""

        """, nativeQuery = true)
   fun insertZones(@Param("list") SZonesM13e00s: List<SZonesM13e00>): Int

   @Query("""

        """, nativeQuery = true)
   fun insertZonesR250082(@Param("list") SZonesM13e00s: List<SZonesM13e00>, @Param("name") name: String): Int

   @Query("""

        """, nativeQuery = true)
   fun findCustomerIDs(mainCode: String): List<String>

   @Query("""

        """, nativeQuery = true)
   fun findIdvivoCustomerIDs(): List<String>

   @Query("""

        """, nativeQuery = true)
   fun insertCustomers(list: List<SCustomersM13e00>): Int

   @Query("""

        """, nativeQuery = true)
   fun insertCustomersR250082(@Param("list") list: List<SCustomersM13e00>, @Param("name") name: String): Int

   @Query("""

        """, nativeQuery = true)
   fun insertPlantStockSupply(list: List<SPlantStockSupplyM13e00>): Int

   @Query("""

        """, nativeQuery = true)
   fun insertPlantStockSupplyR250082(@Param("list") list: List<SPlantStockSupplyM13e00>, @Param("name") name: String): Int

   @Query("""

        """, nativeQuery = true)
   fun insertPlantStockDealer(list: List<SPlantStockDealerM13e00>): Int

   @Query("""

        """, nativeQuery = true)
   fun insertPlantStockDealerR250082(@Param("list") list: List<SPlantStockDealerM13e00>, @Param("name") name: String): Int

   @Query("""

        """, nativeQuery = true)
   fun insertPlantStockStores(list: List<SPlantStockStoresM13e00>): Int

   @Query("""

        """, nativeQuery = true)
   fun insertPlantStockStoresR250082(@Param("list") list: List<SPlantStockStoresM13e00>, @Param("name") name: String): Int

   @Query("""

        """, nativeQuery = true)
   fun deleteProductItemLend(mainCode: String): Int

   @Query("""

        """, nativeQuery = true)
   fun insertProductItemLend(list: List<SProductItemLendM13e00>): Int

   @Query("""

        """, nativeQuery = true)
   fun insertProductItem000(list: List<SProductItem000M13e00>): Int

   @Query("""

        """, nativeQuery = true)
   fun deleteProductItem000(): Int

   @Query("""

        """, nativeQuery = true)
   fun deleteProductItem000R250082(): Int

   @Query("""

        """, nativeQuery = true)
   fun deleteSStores(): Int

   @Query("""

        """, nativeQuery = true)
   fun deleteSStoresR250082(): Int

   @Query("""

        """, nativeQuery = true)
   fun insertSStores(@Param("list") list: List<SStoresM13e00>): Int

   @Query("""

        """, nativeQuery = true)
   fun insertSStoresR250082(@Param("list") list: List<SStoresM13e00>, @Param("name") name: String): Int

   @Query("""

        """, nativeQuery = true)
   fun insertProductItem000R250082(@Param("list") list: List<SProductItem000M13e00>, @Param("name") name: String): Int

   @Query("""

        """, nativeQuery = true)
   fun deleteProductItemStocks(): Int

   @Query("""

        """, nativeQuery = true)
   fun deleteProductItemStocksR250082(): Int

   @Query("""

        """, nativeQuery = true)
   fun insertProductItemStocks(@Param("list") list: List<SProductItemStocksM13e00>): Int

   @Query("""

        """, nativeQuery = true)
   fun insertProductItemStocksR250082(@Param("list") list: List<SProductItemStocksM13e00>, @Param("name") name: String): Int

   @Query("""

        """, nativeQuery = true)
   fun insertSPlantendproductsaleM13e00(list: List<SPlantEndProductSaleM13e00>): Int

   @Query("""

        """, nativeQuery = true)
   fun insertSPlantendproductsaleR250082(@Param("list") list: List<SPlantEndProductSaleM13e00>, @Param("name") name: String): Int

   @Query("""

        """, nativeQuery = true)
   fun getSPlantStockSupply(@Param("date") date: String, @Param("mainCode") mainCode: String): List<SPlantStockSupplyM13e00>

   @Query("""

        """, nativeQuery = true)
   fun getIdvivoSPlantStockSupply(@Param("date") date: String): List<SPlantStockSupplyM13e00>

   @Query("""

        """, nativeQuery = true)
   fun getSPlantStockDealer(@Param("date") date: String, @Param("mainCode") mainCode: String): List<SPlantStockDealerM13e00>

   @Query("""

        """, nativeQuery = true)
   fun getIdvivoSPlantStockDealer(@Param("date") date: String): List<SPlantStockDealerM13e00>

   @Query("""

        """, nativeQuery = true)
   fun getSPlantStockStores(@Param("date") date: String, @Param("mainCode") mainCode: String): List<SPlantStockStoresM13e00>

   @Query("""

        """, nativeQuery = true)
   fun getIdvivoSPlantStockStores(@Param("date") date: String): List<SPlantStockStoresM13e00>

   @Query("""

        """, nativeQuery = true)
  fun sProductItem(): List<String>

   @Query("""

        """, nativeQuery = true)
   fun idvivoSProductItem(): List<String>
}
