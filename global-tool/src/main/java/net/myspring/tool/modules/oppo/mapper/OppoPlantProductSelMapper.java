package net.myspring.tool.modules.oppo.mapper;


import net.myspring.mybatis.mapper.CrudMapper;
import net.myspring.tool.modules.oppo.domain.OppoPlantProductSel;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OppoPlantProductSelMapper extends CrudMapper<OppoPlantProductSel,String> {
}
