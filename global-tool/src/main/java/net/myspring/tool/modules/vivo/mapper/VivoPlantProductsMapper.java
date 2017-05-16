package net.myspring.tool.modules.vivo.mapper;

import net.myspring.tool.modules.vivo.domain.VivoPlantProducts;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 * Created by admin on 2016/10/17.
 */
@Mapper
public interface VivoPlantProductsMapper {

    List<String> findItemNumbers(@Param("itemNumbers") Collection<String> itemNumbers);

    int save(List<VivoPlantProducts> vivoPlantProducts);

    List<VivoPlantProducts> findAll();

    VivoPlantProducts findById(Long id);
}
