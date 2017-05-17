package net.myspring.cloud.modules.kingdee.mapper;

import net.myspring.cloud.modules.kingdee.domain.BdMaterial;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 物料
 * Created by lihx on 2017/5/12.
 */
@Mapper
public interface BdMaterialMapper {

    List<BdMaterial> findAll();

    BdMaterial findByName(@Param("name")String name);
}
