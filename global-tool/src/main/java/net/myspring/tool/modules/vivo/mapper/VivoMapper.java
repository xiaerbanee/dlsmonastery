package net.myspring.tool.modules.vivo.mapper;

import net.myspring.tool.modules.vivo.domain.VivoPlantProducts;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by guolm on 2017/4/15.
 */
@Mapper
public interface VivoMapper  {

   List<VivoPlantProducts> findPlantProducts();

}
