package net.myspring.tool.modules.vivo.repository;

import net.myspring.tool.modules.vivo.domain.*
import net.myspring.tool.modules.vivo.model.SCustomersM13e00
import net.myspring.tool.modules.vivo.model.SZonesM13e00
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDate
import javax.persistence.EntityManager


/**
 * Created by admin on 2016/10/17.
 */
@Repository
interface VivoRepository:VivoRepositoryCustom {

   @Query("""
        select *  from VR_Products
        """, nativeQuery = true)
   fun products(): MutableList<VivoProducts>

   @Query("""
        select *  from vr_products_m13e00
        """, nativeQuery = true)
   fun plantProducts(): MutableList<VivoPlantProducts>

   @Query("""
        select *  from vr_plant_sendimei_m13e00 t1
        where t1.createdTime >= :dateStart
        and t1.createdTime <= :dateEnd
        and t1.companyId in :agentCodes
        """, nativeQuery = true)
   fun plantSendimei(@Param("dateStart") dateStart: LocalDate, @Param("dateEnd") dateEnd: LocalDate, @Param("agentCodes") agentCodes: MutableList<String>): MutableList<VivoPlantSendimei>

   @Query("""
        select snImei,companyId,productId,retailDate,createTime
        from vr_plant_electronicsn_m13e00  t1
        where t1.retailDate >= :dateStart
        and t1.retailDate <= :dateEnd
        """, nativeQuery = true)
   fun plantElectronicsn(@Param("dateStart") dateStart: LocalDate, @Param("dateEnd") dateEnd: LocalDate): MutableList<VivoPlantElectronicsn>

   @Query("""
        delete from S_ZONES_M13E00 where ZoneId = ?1
        """, nativeQuery = true)
   fun deleteZones(mainCode: String): Int


   @Query("""
    select distinct CustomerID from S_Customers_M13e00 where CompanyID=  :mainCode
        """, nativeQuery = true)
   fun findCustomerIDs(mainCode: String): MutableList<String>

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
   fun findIdvivoCustomerIDs(): MutableList<String>

   @Query("""
        select *  from S_PlantStockSupply_M13e00
        where CompanyID= :mainCode
        and AccountDate= :date
        """, nativeQuery = true)
   fun getSPlantStockSupply(@Param("date") date: String, @Param("mainCode") mainCode: String): MutableList<SPlantStockSupplyM13e00>

   @Query("""
        select * from S_PlantStockSupply_R2500821
        union
        select * from S_PlantStockSupply_R2500822
        union
        select * from S_PlantStockSupply_R2500823
        """, nativeQuery = true)
   fun getIdvivoSPlantStockSupply(@Param("date") date: String): MutableList<SPlantStockSupplyM13e00>

   @Query("""
        select *  from S_PlantStockDealer_M13e00
        where CompanyID= :mainCode
        and AccountDate= :date
        """, nativeQuery = true)
   fun getSPlantStockDealer(@Param("date") date: String, @Param("mainCode") mainCode: String): MutableList<SPlantStockDealerM13e00>

   @Query("""
        select * from S_PlantStockDealer_R2500821
        union
        select * from S_PlantStockDealer_R2500822
        union
        select * from S_PlantStockDealer_R2500823
        """, nativeQuery = true)
   fun getIdvivoSPlantStockDealer(@Param("date") date: String): MutableList<SPlantStockDealerM13e00>

   @Query("""
        select *  from s_PlantStockStores_m13e00
        where CompanyID= :mainCode
        and AccountDate= :date
        """, nativeQuery = true)
   fun getSPlantStockStores(@Param("date") date: String, @Param("mainCode") mainCode: String): MutableList<SPlantStockStoresM13e00>

   @Query("""
        select *  from S_PlantStockStores_R250082
        where AccountDate=:date
        """, nativeQuery = true)
   fun getIdvivoSPlantStockStores(@Param("date") date: String): MutableList<SPlantStockStoresM13e00>

   @Query("""
        select ProductNo  from S_ProductItem000_M13e00
        """, nativeQuery = true)
  fun sProductItem(): MutableList<String>

   @Query("""
        select ProductNo  from S_ProductItem000_R2500821
        union
        select ProductNo  from S_ProductItem000_R2500822
        union
        select ProductNo  from S_ProductItem000_R2500823
        """, nativeQuery = true)
   fun idvivoSProductItem(): MutableList<String>


   @Query("""
        delete from S_ProductItemLend_M13e00 where CompanyID =?1
        """, nativeQuery = true)
   fun deleteProductItemLend(mainCode: String)
}


interface VivoRepositoryCustom {
   fun idvivoDeleteZones()
   fun insertZones(@Param("list") sZonesM13e00s: MutableList<SZonesM13e00>)
   fun insertZonesR250082(@Param("list") sZonesM13e00s: MutableList<SZonesM13e00>, @Param("name") name: String)
   fun insertCustomers(sCustomersM13e00s: MutableList<SCustomersM13e00>)
   fun insertCustomersR250082(@Param("list") sCustomersM13e00s: MutableList<SCustomersM13e00>, @Param("name") name: String)
   fun insertPlantStockSupply(sPlantStockSupplyM13e00s: MutableList<SPlantStockSupplyM13e00>)
   fun insertPlantStockSupplyR250082(@Param("list") sPlantStockSupplyM13e00s: MutableList<SPlantStockSupplyM13e00>, @Param("name") name: String)
   fun insertPlantStockDealer(sPlantStockDealerM13e00s: MutableList<SPlantStockDealerM13e00>)
   fun insertPlantStockDealerR250082(@Param("list") sPlantStockDealerM13e00s: MutableList<SPlantStockDealerM13e00>, @Param("name") name: String)
   fun insertPlantStockStores(sPlantStockStoresM13e00s: MutableList<SPlantStockStoresM13e00>)
   fun insertPlantStockStoresR250082(@Param("list") sPlantStockStoresM13e00s: MutableList<SPlantStockStoresM13e00>, @Param("name") name: String)
   fun insertProductItemLend(sProductItemLendM13e00s: MutableList<SProductItemLendM13e00>)
   fun insertProductItem000(sProductItem000M13e00s: MutableList<SProductItem000M13e00>)
   fun deleteProductItem000()
   fun deleteProductItem000R250082()
   fun deleteSStores()
   fun deleteSStoresR250082()
   fun insertSStores(@Param("list") sStoresM13e00s: MutableList<SStoresM13e00>)
   fun insertSStoresR250082(@Param("list") sStoresM13e00s: MutableList<SStoresM13e00>, @Param("name") name: String)
   fun insertProductItem000R250082(@Param("list") sProductItem000M13e00s: MutableList<SProductItem000M13e00>, @Param("name") name: String)
   fun deleteProductItemStocks()
   fun deleteProductItemStocksR250082()
   fun insertProductItemStocks(@Param("list") sProductItemStocksM13e00s: MutableList<SProductItemStocksM13e00>)
   fun insertProductItemStocksR250082(@Param("list") sProductItemStocksM13e00s: MutableList<SProductItemStocksM13e00>, @Param("name") name: String)
   fun insertSPlantendproductsaleM13e00(sPlantEndProductSaleM13e00s: MutableList<SPlantEndProductSaleM13e00>)
   fun insertSPlantendproductsaleR250082(@Param("list") sPlantEndProductSaleM13e00s: MutableList<SPlantEndProductSaleM13e00>, @Param("name") name: String)
}


class VivoRepositoryImpl @Autowired constructor(val entityManager: EntityManager):VivoRepositoryCustom{

   override fun idvivoDeleteZones() {
      entityManager.createNativeQuery("delete from S_ZONES_R250082").executeUpdate();
      entityManager.createNativeQuery("delete from S_ZONES_R2500821").executeUpdate();
      entityManager.createNativeQuery("delete from S_ZONES_R2500822").executeUpdate();
      entityManager.createNativeQuery("delete from S_ZONES_R2500823").executeUpdate();
   }

   override fun insertZones(sZonesM13e00s: MutableList<SZonesM13e00>) {
      for(sZonesM13e00 in sZonesM13e00s) {
         var query = entityManager.createNativeQuery("""
           insert into S_ZONES_M13E00
           (zoneID,zoneName,shortCut,zoneDepth,zonePath,fatherID,subCount,zoneTypes)
           values (
            :zoneID,
            :zoneName,
            :shortCut,
            :zoneDepth,
            :zonePath,
            :fatherID,
            :subCount,
            :zoneTypes
        """)
      }
   }

   override fun insertZonesR250082(sZonesM13e00s: MutableList<SZonesM13e00>, name: String) {
      for (sZonesM13e00 in sZonesM13e00s){
         var query=entityManager.createNativeQuery("""
        insert into S_ZONES_?2
        (zoneID,zoneName,shortCut,zoneDepth,zonePath,fatherID,subCount,zoneTypes)
        values
            (
            :zoneID,
            :zoneName,
            :shortCut,
            :zoneDepth,
            :zonePath,
            :fatherID,
            :subCount,
            :zoneTypes
            )
         """)
      }
   }

   override fun insertCustomers(sCustomersM13e00s: MutableList<SCustomersM13e00>) {
      for (sCustomersM13e00 in sCustomersM13e00s){
         var query=entityManager.createNativeQuery("""
        insert into S_Customers_M13e00
        (customerID,
        customerName,
        Shortcut,
        customerType,
        customerSort,
        customerKASort,
        address,
        postcode,
        telephone,
        fax,
        linkMan,
        linkTel,
        remark,
        inUse,
        zoneID,
        companyID,
        recordDate,
        customerLevel,
        manager,
        customerstr1,
        customerstr2,
        customerstr3,
        customerstr4,
        customerstr5,
        customerstr6,
        customerstr7,
        customerstr8,
        customerstr9,
        customerstr10,
        latentCustomers
        )VALUES
            (
            :customerID,
            :customerName,
            :Shortcut,
            :customerType,
            :customerSort,
            :customerKASort,
            :address,
            :postcode,
            :telephone,
            :fax,
            :linkMan,
            :linkTel,
            :remark,
            :inUse,
            :zoneID,
            :companyID,
            :recordDate,
            :customerLevel,
            :manager,
            :customerstr1,
            :customerstr2,
            :customerstr3,
            :customerstr4,
            :customerstr5,
            :customerstr6,
            :customerstr7,
            :customerstr8,
            :customerstr9,
            :customerstr10,
            :latentCustomers
            )
         """)
      }
   }

   override fun insertCustomersR250082(sCustomersM13e00s: MutableList<SCustomersM13e00>, name: String) {
      for (sCustomersM13e00 in sCustomersM13e00s){
         var query=entityManager.createNativeQuery("""
        insert into  S_Customers_?2
        (customerID,
        customerName,
        zoneID,
        companyID,
        recordDate,
        customerLevel,
        manager,
        customerstr1,
        customerstr2,
        customerstr3,
        customerstr4,
        customerstr5,
        customerstr6,
        customerstr7,
        customerstr8,
        customerstr9,
        customerstr10,
        latentCustomers
        )VALUES
            (
            :customerID,
            :customerName,
            :zoneID,
            :companyID,
            :recordDate,
            :customerLevel,
            :manager,
            :customerstr1,
            :customerstr2,
            :customerstr3,
            :customerstr4,
            :customerstr5,
            :customerstr6,
            :customerstr7,
            :customerstr8,
            :customerstr9,
            :customerstr10,
            :latentCustomers
            )
         """)
      }
   }

   override fun insertPlantStockSupply(sPlantStockSupplyM13e00s: MutableList<SPlantStockSupplyM13e00>) {
      for (sPlantStockSupplyM13e00 in sPlantStockSupplyM13e00s){
         var query=entityManager.createNativeQuery("""
         insert into S_PlantStockSupply_M13e00
        (companyID,supplyID,productID,createdTime,sumstock,useablestock,bad,accountDate)
        values
            :companyID,
            :supplyID,
            :productID,
            :createdTime,
            :sumstock,
            :useablestock,
            :bad,
            :accountDate
         """)
      }
   }

   override fun insertPlantStockSupplyR250082(sPlantStockSupplyM13e00s: MutableList<SPlantStockSupplyM13e00>, name: String) {
      for (sPlantStockSupplyM13e00 in sPlantStockSupplyM13e00s){
         var query=entityManager.createNativeQuery("""
         insert into S_PlantStockSupply_?2
        (companyID,supplyID,productID,createdTime,sumstock,useablestock,bad,accountDate)
        values
            (
            :companyID,
            :supplyID,
            :productID,
            :createdTime,
            :sumstock,
            :useablestock,
            :bad,
            :accountDate
            )
         """)
      }
   }

   override fun insertPlantStockDealer(sPlantStockDealerM13e00s: MutableList<SPlantStockDealerM13e00>) {
      for (sPlantStockDealerM13e00 in sPlantStockDealerM13e00s){
         var query=entityManager.createNativeQuery("""
       insert into S_PlantStockDealer_M13e00 (companyID,dealerID,productID,createdTime,sumstock,useablestock,bad,accountDate) values
            (
            :companyID,
            :dealerID,
            :productID,
            :createdTime,
            :sumstock,
            :useablestock,
            :bad,
            :accountDate
            )
         """)
      }
   }

   override fun insertPlantStockDealerR250082(sPlantStockDealerM13e00s: MutableList<SPlantStockDealerM13e00>, name: String) {
      for (sPlantStockDealerM13e00 in sPlantStockDealerM13e00s){
         var query=entityManager.createNativeQuery("""
         insert into S_PlantStockDealer_?2
        (companyID,dealerID,productID,createdTime,sumstock,useablestock,bad,accountDate) values
            (
            :companyID,
            :dealerID,
            :productID,
            :createdTime,
            :sumstock,
            :useablestock,
            :bad,
            :accountDate
            )
         """)
      }
   }

   override fun insertPlantStockStores(sPlantStockStoresM13e00s: MutableList<SPlantStockStoresM13e00>) {
      for (sPlantStockStoresM13e00 in sPlantStockStoresM13e00s){
         var query=entityManager.createNativeQuery("""
        insert into S_PlantStockStores_M13e00(companyID,storeID,productID,createdTime,sumstock,useablestock,bad,accountDate) values
            (
            :companyID,
            :storeID,
            :productID,
            :createdTime,
            :sumstock,
            :useablestock,
            :bad,
            :accountDate
            )
         """)
      }
   }

   override fun insertPlantStockStoresR250082(sPlantStockStoresM13e00s: MutableList<SPlantStockStoresM13e00>, name: String) {
      for (sPlantStockStoresM13e00 in sPlantStockStoresM13e00s){
         var query=entityManager.createNativeQuery("""
        insert into S_PlantStockStores_?2 (companyID,storeID,productID,createdTime,sumstock,useablestock,bad,accountDate)
        values
            (
            :companyID,
            :storeID,
            :productID,
            :createdTime,
            :sumstock,
            :useablestock,
            :bad,
            :accountDate
            )
         """)
      }
   }

   override fun insertProductItemLend(sProductItemLendM13e00s: MutableList<SProductItemLendM13e00>) {
      for (sProductItemLendM13e00 in sProductItemLendM13e00s){
         var query=entityManager.createNativeQuery("""
          insert into S_ProductItemLend_M13e00
        (
        companyID,
        productID,
        productNo,
        storeID,
        customerID,
        subCustomerID,
        status,
        statusInfo,
        isReturnProfit,
        isLock,
        remark
        ) values
            (
            :companyID,
            :productID,
            :productNo,
            :storeID,
            :customerID,
            :subCustomerID,
            :status,
            :statusInfo,
            :isReturnProfit,
            :isLock,
            :remark
            )
         """)
      }
   }

   override fun insertProductItem000(sProductItem000M13e00s: MutableList<SProductItem000M13e00>) {
      for (sProductItem000M13e00 in sProductItem000M13e00s){
         var query=entityManager.createNativeQuery("""
          insert into S_ProductItem000_M13e00 (companyID,productID,productNo,storeID,customerID,subCustomerID,status,statusInfo,isReturnProfit,isLock,remark) values
            (
            :companyID,
            :productID,
            :productNo,
            :storeID,
            :customerID,
            :subCustomerID,
            :status,
            :statusInfo,
            :isReturnProfit,
            :isLock,
            :remark
            )
         """)
      }
   }

   override fun deleteProductItem000() {
      entityManager.createNativeQuery(" delete from  S_ProductItem000_M13E00").executeUpdate();
   }

   override fun deleteProductItem000R250082() {
      entityManager.createNativeQuery(" delete from  S_ProductItem000_R250082").executeUpdate();
      entityManager.createNativeQuery(" delete from  S_ProductItem000_R2500821").executeUpdate();
      entityManager.createNativeQuery(" delete from  S_ProductItem000_R2500822").executeUpdate();
      entityManager.createNativeQuery(" delete from  S_ProductItem000_R2500823").executeUpdate();
   }

   override fun deleteSStores() {
      entityManager.createNativeQuery(" delete from  S_Stores_M13E00").executeUpdate();
   }

   override fun deleteSStoresR250082() {
      entityManager.createNativeQuery(" delete from  S_Stores_R250082").executeUpdate();
      entityManager.createNativeQuery(" delete from  S_Stores_R2500821").executeUpdate();
      entityManager.createNativeQuery(" delete from  S_Stores_R2500822").executeUpdate();
      entityManager.createNativeQuery(" delete from  S_Stores_R2500823").executeUpdate();
   }

   override fun insertSStores(sStoresM13e00s: MutableList<SStoresM13e00>) {
      for ( sStoresM13e00 in sStoresM13e00s){
         var query=entityManager.createNativeQuery("""
          insert into S_Stores_M13E00
        (storeID,storeName,remark,shortCut) values
            (
            :storeID,
            :storeName,
            :remark,
            :shortCut
            )
         """)
      }
   }

   override fun insertSStoresR250082(sStoresM13e00s: MutableList<SStoresM13e00>, name: String) {
      for (sStoresM13e00 in sStoresM13e00s ){
         var query=entityManager.createNativeQuery("""
          insert into S_Stores_?2
        (storeID,storeName,remark,shortCut) values
            (
            :storeID,
            :storeName,
            :remark,
            :shortCut
            )
         """)

      }
   }

   override fun insertProductItem000R250082(sProductItem000M13e00s: MutableList<SProductItem000M13e00>, name: String) {
      for (sProductItem000M13e00 in sProductItem000M13e00s){
         var query=entityManager.createNativeQuery("""
         insert into S_ProductItem000_?2
        (companyID,productID,productNo,storeID,customerID,subCustomerID,status,statusInfo,isReturnProfit,isLock,remark) values
            (
            :companyID,
            :productID,
            :productNo,
            :storeID,
            :customerID,
            :subCustomerID,
            :status,
            :statusInfo,
            :isReturnProfit,
            :isLock,
            :remark
            )
         """)
      }
   }

   override fun deleteProductItemStocks() {
      entityManager.createNativeQuery(" delete from  S_ProductItemStocks_M13E00").executeUpdate();
   }

   override fun deleteProductItemStocksR250082() {
      entityManager.createNativeQuery(" delete from  S_ProductItemStocks_R250082").executeUpdate();
      entityManager.createNativeQuery(" delete from  S_ProductItemStocks_R2500821").executeUpdate();
      entityManager.createNativeQuery(" delete from  S_ProductItemStocks_R2500822").executeUpdate();
      entityManager.createNativeQuery(" delete from  S_ProductItemStocks_R2500823").executeUpdate();
   }

   override fun insertProductItemStocks(sProductItemStocksM13e00s: MutableList<SProductItemStocksM13e00>) {
      for (sProductItemStocksM13e00 in sProductItemStocksM13e00s){
         var query=entityManager.createNativeQuery("""
           insert into S_ProductItemStocks_M13E00 (companyID,productID,productNo,storeID,customerID,subCustomerID,status,statusInfo,isReturnProfit,isLock,remark) 
           values
            (
            :companyID,
            :productID,
            :productNo,
            :storeID,
            :customerID,
            :subCustomerID,
            :status,
            :statusInfo,
            :isReturnProfit,
            :isLock,
            :remark
            )
         """)

      }
   }

   override fun insertProductItemStocksR250082(sProductItemStocksM13e00s: MutableList<SProductItemStocksM13e00>, name: String) {
      for (sProductItemStocksM13e00 in sProductItemStocksM13e00s){
         var query=entityManager.createNativeQuery("""
          insert into S_ProductItemStocks_?2 (companyID,productID,productNo,storeID,customerID,subCustomerID,status,statusInfo,isReturnProfit,isLock,remark)
          values
            (
            :companyID,
            :productID,
            :productNo,
            :storeID,
            :customerID,
            :subCustomerID,
            :status,
            :statusInfo,
            :isReturnProfit,
            :isLock,
            :remark
            )
         """)

      }
   }

   override fun insertSPlantendproductsaleM13e00(sPlantEndProductSaleM13e00s: MutableList<SPlantEndProductSaleM13e00>) {
      for (sPlantEndProductSaleM13e00 in sPlantEndProductSaleM13e00s){
         var query=entityManager.createNativeQuery("""
          insert into S_PlantEndProductSale_M13e00(companyID,endBillID,productID,saleCount,imei,billDate,dealerID,salePrice,createdTime) 
          values
            (
            :companyID,
            :endBillID,
            :productID,
            :saleCount,
            :imei,
            :billDate,
            :dealerID,
            :salePrice,
            :createdTime
            )
         """)

      }
   }

   override fun insertSPlantendproductsaleR250082(sPlantEndProductSaleM13e00s: MutableList<SPlantEndProductSaleM13e00>, name: String) {
      for (sPlantEndProductSaleM13e00 in sPlantEndProductSaleM13e00s){
         var query=entityManager.createNativeQuery("""
         insert into S_PlantEndProductSale_?2 (companyID,endBillID,productID,saleCount,imei,billDate,dealerID,salePrice,createdTime)
         values
            (
            :companyID,
            :endBillID,
            :productID,
            :saleCount,
            :imei,
            :billDate,
            :dealerID,
            :salePrice,
            :createdTime
            )
         """)

      }
   }
}

