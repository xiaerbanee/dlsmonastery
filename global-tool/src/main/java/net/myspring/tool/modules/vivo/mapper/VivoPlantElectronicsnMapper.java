package net.myspring.tool.modules.vivo.mapper;

import net.myspring.tool.modules.vivo.domain.CommonEntity;
import net.myspring.tool.modules.vivo.domain.VivoPlantElectronicsn;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

/**
 * Created by admin on 2016/10/17.
 */
@Mapper
public interface VivoPlantElectronicsnMapper {

    int updateProductImeRetailDate(@Param("dateStart") LocalDate dateStart, @Param("dateEnd") LocalDate dateEnd, @Param("companyId") Long companyId);

    List<String> findSnImeis(@Param("snImeis") Collection<String> snImeis);

    List<CommonEntity> findNameQtyList(@Param("dateStart") LocalDate dateStart, @Param("dateEnd") LocalDate dateEnd);

    int save(List<VivoPlantElectronicsn> vivoPlantElectronicsns);
}
