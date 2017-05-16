package net.myspring.tool.modules.vivo.mapper;

import net.myspring.tool.modules.vivo.domain.VivoProducts;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by admin on 2016/10/17.
 */
@Mapper
public interface VivoProductsMapper {

    List<String> findColorIds(List<String> colorIds);

    int batchSave(List<VivoProducts> vivoProducts);

    List<String> findAll();
}
