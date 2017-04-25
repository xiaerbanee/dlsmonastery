package net.myspring.cloud.modules.input.mapper;

import net.myspring.cloud.modules.input.domain.StkInventory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by lihx on 2017/4/6.
 */
@Mapper
public interface StkInventoryMapper {

    List<StkInventory> findByfStockIds(@Param("fStockIds") List<String> fStockIds);

    StkInventory findByStockIdAndMaterialId(@Param("fStockId") String fStockId,@Param("fMaterialId") String fMaterialId);
}
