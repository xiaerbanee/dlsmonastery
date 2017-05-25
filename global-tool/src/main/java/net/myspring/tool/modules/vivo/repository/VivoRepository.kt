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
import net.myspring.util.collection.CollectionUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.repository.Query
import javax.persistence.EntityManager


/**
 * Created by admin on 2016/10/17.
 */
interface VivoRepository:VivoRepositoryCustom {

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
    select distinct CustomerID from S_Customers_M13e00 where CompanyID=  :mainCode
        """, nativeQuery = true)
   fun findCustomerIDs(mainCode: String): List<String>

   @Query("""
        SELECT
        DISTINCT CustomerID
        FROM
        S_Customers_R250082
        UNION
        SELECT
        DISTINCT CustomerID
        FROM
        S_Customers_R2500821
        union
        SELECT
        DISTINCT CustomerID
        FROM
        S_Customers_R2500822
        union
        SELECT
        DISTINCT CustomerID
        FROM
        S_Customers_R2500823
        """, nativeQuery = true)
   fun findIdvivoCustomerIDs(): List<String>

   @Query("""
        select *  from S_PlantStockSupply_M13e00
        where CompanyID= :mainCode
        and AccountDate= :date
        """, nativeQuery = true)
   fun getSPlantStockSupply(@Param("date") date: String, @Param("mainCode") mainCode: String): List<SPlantStockSupplyM13e00>

   @Query("""
        select * from S_PlantStockSupply_R2500821
        union
        select * from S_PlantStockSupply_R2500822
        union
        select * from S_PlantStockSupply_R2500823
        """, nativeQuery = true)
   fun getIdvivoSPlantStockSupply(@Param("date") date: String): List<SPlantStockSupplyM13e00>

   @Query("""
        select *  from S_PlantStockDealer_M13e00
        where CompanyID= :mainCode
        and AccountDate= :date
        """, nativeQuery = true)
   fun getSPlantStockDealer(@Param("date") date: String, @Param("mainCode") mainCode: String): List<SPlantStockDealerM13e00>

   @Query("""
        select * from S_PlantStockDealer_R2500821
        union
        select * from S_PlantStockDealer_R2500822
        union
        select * from S_PlantStockDealer_R2500823
        """, nativeQuery = true)
   fun getIdvivoSPlantStockDealer(@Param("date") date: String): List<SPlantStockDealerM13e00>

   @Query("""
        select *  from s_PlantStockStores_m13e00
        where CompanyID= :mainCode
        and AccountDate= :date
        """, nativeQuery = true)
   fun getSPlantStockStores(@Param("date") date: String, @Param("mainCode") mainCode: String): List<SPlantStockStoresM13e00>

   @Query("""
        select *  from S_PlantStockStores_R250082
        where AccountDate=#{date}
        """, nativeQuery = true)
   fun getIdvivoSPlantStockStores(@Param("date") date: String): List<SPlantStockStoresM13e00>

   @Query("""
        select ProductNo  from S_ProductItem000_M13e00
        """, nativeQuery = true)
  fun sProductItem(): List<String>

   @Query("""
        select ProductNo  from S_ProductItem000_R2500821
        union
        select ProductNo  from S_ProductItem000_R2500822
        union
        select ProductNo  from S_ProductItem000_R2500823
        """, nativeQuery = true)
   fun idvivoSProductItem(): List<String>
}


interface VivoRepositoryCustom {
   fun idvivoDeleteZones()
   fun insertZones(@Param("list") SZonesM13e00s: List<SZonesM13e00>)
   fun insertZonesR250082(@Param("list") SZonesM13e00s: List<SZonesM13e00>, @Param("name") name: String)
   fun insertCustomers(list: List<SCustomersM13e00>)
   fun insertCustomersR250082(@Param("list") list: List<SCustomersM13e00>, @Param("name") name: String)
   fun insertPlantStockSupply(list: List<SPlantStockSupplyM13e00>)
   fun insertPlantStockSupplyR250082(@Param("list") list: List<SPlantStockSupplyM13e00>, @Param("name") name: String)
   fun insertPlantStockDealer(list: List<SPlantStockDealerM13e00>)
   fun insertPlantStockDealerR250082(@Param("list") list: List<SPlantStockDealerM13e00>, @Param("name") name: String)
   fun insertPlantStockStores(list: List<SPlantStockStoresM13e00>)
   fun insertPlantStockStoresR250082(@Param("list") list: List<SPlantStockStoresM13e00>, @Param("name") name: String)
   fun deleteProductItemLend(mainCode: String)
   fun insertProductItemLend(list: List<SProductItemLendM13e00>)
   fun insertProductItem000(list: List<SProductItem000M13e00>)
   fun deleteProductItem000()
   fun deleteProductItem000R250082()
   fun deleteSStores()
   fun deleteSStoresR250082()
   fun insertSStores(@Param("list") list: List<SStoresM13e00>)
   fun insertSStoresR250082(@Param("list") list: List<SStoresM13e00>, @Param("name") name: String)
   fun insertProductItem000R250082(@Param("list") list: List<SProductItem000M13e00>, @Param("name") name: String)
   fun deleteProductItemStocks()
   fun deleteProductItemStocksR250082()
   fun insertProductItemStocks(@Param("list") list: List<SProductItemStocksM13e00>)
   fun insertProductItemStocksR250082(@Param("list") list: List<SProductItemStocksM13e00>, @Param("name") name: String)
   fun insertSPlantendproductsaleM13e00(list: List<SPlantEndProductSaleM13e00>)
   fun insertSPlantendproductsaleR250082(@Param("list") list: List<SPlantEndProductSaleM13e00>, @Param("name") name: String)
}


class VivoRepositoryImpl @Autowired constructor(val entityManager: EntityManager):VivoRepositoryCustom{

   override fun idvivoDeleteZones() {
      entityManager.createNativeQuery("delete from S_ZONES_R250082").executeUpdate();
      entityManager.createNativeQuery("delete from S_ZONES_R2500821").executeUpdate();
      entityManager.createNativeQuery("delete from S_ZONES_R2500822").executeUpdate();
      entityManager.createNativeQuery("delete from S_ZONES_R2500823").executeUpdate();
   }

   override fun insertZones(SZonesM13e00s: List<SZonesM13e00>) {
      var sb = StringBuffer()
      sb.append("""
           insert into S_ZONES_M13E00
           (zoneID,zoneName,shortCut,zoneDepth,zonePath,fatherID,subCount,zoneTypes)
           values (
        """)
      if (CollectionUtil.isNotEmpty(SZonesM13e00s)) {
         sb.append("""
                and ov.employee_id in :accountIds
            """)
      }
   }

   override fun insertZonesR250082(SZonesM13e00s: List<SZonesM13e00>, name: String) {


      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
   }

   override fun insertCustomers(list: List<SCustomersM13e00>) {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
   }

   override fun insertCustomersR250082(list: List<SCustomersM13e00>, name: String) {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
   }

   override fun insertPlantStockSupply(list: List<SPlantStockSupplyM13e00>) {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
   }

   override fun insertPlantStockSupplyR250082(list: List<SPlantStockSupplyM13e00>, name: String) {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
   }

   override fun insertPlantStockDealer(list: List<SPlantStockDealerM13e00>) {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
   }

   override fun insertPlantStockDealerR250082(list: List<SPlantStockDealerM13e00>, name: String) {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
   }

   override fun insertPlantStockStores(list: List<SPlantStockStoresM13e00>) {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
   }

   override fun insertPlantStockStoresR250082(list: List<SPlantStockStoresM13e00>, name: String) {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
   }

   override fun deleteProductItemLend(mainCode: String) {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
   }

   override fun insertProductItemLend(list: List<SProductItemLendM13e00>) {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
   }

   override fun insertProductItem000(list: List<SProductItem000M13e00>) {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
   }

   override fun deleteProductItem000() {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
   }

   override fun deleteProductItem000R250082() {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
   }

   override fun deleteSStores() {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
   }

   override fun deleteSStoresR250082() {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
   }

   override fun insertSStores(list: List<SStoresM13e00>) {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
   }

   override fun insertSStoresR250082(list: List<SStoresM13e00>, name: String) {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
   }

   override fun insertProductItem000R250082(list: List<SProductItem000M13e00>, name: String) {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
   }

   override fun deleteProductItemStocks() {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
   }

   override fun deleteProductItemStocksR250082() {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
   }

   override fun insertProductItemStocks(list: List<SProductItemStocksM13e00>) {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
   }

   override fun insertProductItemStocksR250082(list: List<SProductItemStocksM13e00>, name: String) {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
   }

   override fun insertSPlantendproductsaleM13e00(list: List<SPlantEndProductSaleM13e00>) {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
   }

   override fun insertSPlantendproductsaleR250082(list: List<SPlantEndProductSaleM13e00>, name: String) {
      TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
   }
}

