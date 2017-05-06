package net.myspring.cloud.modules.input.mapper;

import net.myspring.cloud.modules.input.domain.BdMaterial;
import net.myspring.cloud.modules.input.dto.NameNumberDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by lihx on 2017/4/5.
 */
@Mapper
public interface BdMaterialMapper {

    List<BdMaterial> findAll();

    List<BdMaterial> findByDate(@Param("maxOutDate")LocalDateTime maxOutDate);

    List<String> findNameByNameLike(@Param("name")String name);

    List<NameNumberDto> findNameAndNumber();

    List<NameNumberDto> findCategory();

    List<NameNumberDto> findGroup();
}
