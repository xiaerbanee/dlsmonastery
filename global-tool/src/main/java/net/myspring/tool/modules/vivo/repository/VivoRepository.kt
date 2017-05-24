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



/**
 * Created by admin on 2016/10/17.
 */
interface VivoMapper {
   fun products(): List<VivoProducts>

   fun plantProducts(): List<VivoPlantProducts>

   fun plantSendimei(@Param("dateStart") dateStart: LocalDate, @Param("dateEnd") dateEnd: LocalDate, @Param("agentCodes") agentCodes: List<String>): List<VivoPlantSendimei>

   fun plantElectronicsn(@Param("dateStart") dateStart: LocalDate, @Param("dateEnd") dateEnd: LocalDate): List<VivoPlantElectronicsn>

   fun deleteZones(mainCode: String): Int

   fun idvivoDeleteZones(): Int

   fun insertZones(@Param("list") SZonesM13e00s: List<SZonesM13e00>): Int

   fun insertZonesR250082(@Param("list") SZonesM13e00s: List<SZonesM13e00>, @Param("name") name: String): Int

   fun findCustomerIDs(mainCode: String): List<String>

   fun findIdvivoCustomerIDs(): List<String>

   fun insertCustomers(list: List<SCustomersM13e00>): Int

   fun insertCustomersR250082(@Param("list") list: List<SCustomersM13e00>, @Param("name") name: String): Int

   fun insertPlantStockSupply(list: List<SPlantStockSupplyM13e00>): Int

   fun insertPlantStockSupplyR250082(@Param("list") list: List<SPlantStockSupplyM13e00>, @Param("name") name: String): Int

   fun insertPlantStockDealer(list: List<SPlantStockDealerM13e00>): Int

   fun insertPlantStockDealerR250082(@Param("list") list: List<SPlantStockDealerM13e00>, @Param("name") name: String): Int

   fun insertPlantStockStores(list: List<SPlantStockStoresM13e00>): Int

   fun insertPlantStockStoresR250082(@Param("list") list: List<SPlantStockStoresM13e00>, @Param("name") name: String): Int

   fun deleteProductItemLend(mainCode: String): Int

   fun insertProductItemLend(list: List<SProductItemLendM13e00>): Int

   fun insertProductItem000(list: List<SProductItem000M13e00>): Int

   fun deleteProductItem000(): Int

   fun deleteProductItem000R250082(): Int

   fun deleteSStores(): Int

   fun deleteSStoresR250082(): Int

   fun insertSStores(@Param("list") list: List<SStoresM13e00>): Int

   fun insertSStoresR250082(@Param("list") list: List<SStoresM13e00>, @Param("name") name: String): Int

   fun insertProductItem000R250082(@Param("list") list: List<SProductItem000M13e00>, @Param("name") name: String): Int

   fun deleteProductItemStocks(): Int

   fun deleteProductItemStocksR250082(): Int

   fun insertProductItemStocks(@Param("list") list: List<SProductItemStocksM13e00>): Int

   fun insertProductItemStocksR250082(@Param("list") list: List<SProductItemStocksM13e00>, @Param("name") name: String): Int

   fun insertSPlantendproductsaleM13e00(list: List<SPlantEndProductSaleM13e00>): Int

   fun insertSPlantendproductsaleR250082(@Param("list") list: List<SPlantEndProductSaleM13e00>, @Param("name") name: String): Int

   fun getSPlantStockSupply(@Param("date") date: String, @Param("mainCode") mainCode: String): List<SPlantStockSupplyM13e00>

   fun getIdvivoSPlantStockSupply(@Param("date") date: String): List<SPlantStockSupplyM13e00>

   fun getSPlantStockDealer(@Param("date") date: String, @Param("mainCode") mainCode: String): List<SPlantStockDealerM13e00>

   fun getIdvivoSPlantStockDealer(@Param("date") date: String): List<SPlantStockDealerM13e00>

   fun getSPlantStockStores(@Param("date") date: String, @Param("mainCode") mainCode: String): List<SPlantStockStoresM13e00>

   fun getIdvivoSPlantStockStores(@Param("date") date: String): List<SPlantStockStoresM13e00>

   val sProductItem: List<String>

   val idvivoSProductItem: List<String>
}
