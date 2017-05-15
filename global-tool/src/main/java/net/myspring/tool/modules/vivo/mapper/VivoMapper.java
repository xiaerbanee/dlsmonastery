package net.myspring.tool.modules.vivo.mapper;

import net.myspring.tool.modules.vivo.domain.*;
import net.myspring.tool.modules.vivo.model.SCustomersM13e00;
import net.myspring.tool.modules.vivo.model.SZonesM13e00;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by admin on 2016/10/17.
 */
@Mapper
public interface VivoMapper {

   List<VivoProducts> products();

   List<VivoPlantProducts> plantProducts();

   List<VivoPlantSendimei> plantSendimei(@Param("dateStart") LocalDate dateStart, @Param("dateEnd") LocalDate dateEnd, @Param("agentCodes") List<String> agentCodes);

   List<VivoPlantElectronicsn> plantElectronicsn(@Param("dateStart") LocalDate dateStart, @Param("dateEnd") LocalDate dateEnd);

   int deleteZones(String mainCode);

   int idvivoDeleteZones();

   int insertZones( @Param("list")List<SZonesM13e00> SZonesM13e00s);

   int insertZonesR250082(@Param("list")List<SZonesM13e00> SZonesM13e00s,@Param("name")String name);

   List<String>  findCustomerIDs(String mainCode);

   List<String>  findIdvivoCustomerIDs();

   int insertCustomers(List<SCustomersM13e00> list);

   int insertCustomersR250082(@Param("list")List<SCustomersM13e00> list,@Param("name")String name);

   int insertPlantStockSupply(List<SPlantStockSupplyM13e00> list);

   int insertPlantStockSupplyR250082(@Param("list")List<SPlantStockSupplyM13e00> list,@Param("name")String name);

   int insertPlantStockDealer(List<SPlantStockDealerM13e00> list);

   int insertPlantStockDealerR250082(@Param("list")List<SPlantStockDealerM13e00> list,@Param("name")String name);

   int insertPlantStockStores(List<SPlantStockStoresM13e00> list);

   int insertPlantStockStoresR250082(@Param("list")List<SPlantStockStoresM13e00> list,@Param("name")String name);

   int deleteProductItemLend(String mainCode);

   int insertProductItemLend(List<SProductItemLendM13e00> list);

   int insertProductItem000(List<SProductItem000M13e00> list);

   int deleteProductItem000();

   int deleteProductItem000R250082();

   int deleteSStores();

   int deleteSStoresR250082();

   int insertSStores(@Param("list")List<SStoresM13e00> list);

   int insertSStoresR250082(@Param("list")List<SStoresM13e00> list,@Param("name")String name);

   int insertProductItem000R250082(@Param("list")List<SProductItem000M13e00> list,@Param("name")String name);

   int deleteProductItemStocks();

   int deleteProductItemStocksR250082();

   int insertProductItemStocks(@Param("list")List<SProductItemStocksM13e00> list);

   int insertProductItemStocksR250082(@Param("list")List<SProductItemStocksM13e00> list,@Param("name")String name);

   int insertSPlantendproductsaleM13e00(List<SPlantEndProductSaleM13e00> list);

   int insertSPlantendproductsaleR250082(@Param("list")List<SPlantEndProductSaleM13e00> list,@Param("name")String name);

   List<SPlantStockSupplyM13e00> getSPlantStockSupply(@Param("date") String date, @Param("mainCode") String mainCode);

   List<SPlantStockSupplyM13e00> getIdvivoSPlantStockSupply(@Param("date") String date);

   List<SPlantStockDealerM13e00> getSPlantStockDealer(@Param("date") String date, @Param("mainCode") String mainCode);

   List<SPlantStockDealerM13e00> getIdvivoSPlantStockDealer(@Param("date") String date);

   List<SPlantStockStoresM13e00> getSPlantStockStores(@Param("date") String date, @Param("mainCode") String mainCode);

   List<SPlantStockStoresM13e00> getIdvivoSPlantStockStores(@Param("date") String date);

   List<String> getSProductItem();

   List<String> getIdvivoSProductItem();
}
