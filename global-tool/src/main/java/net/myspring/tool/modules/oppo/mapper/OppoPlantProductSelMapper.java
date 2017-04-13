package net.myspring.tool.modules.oppo.mapper;


import net.myspring.mybatis.mapper.CrudMapper;
import net.myspring.tool.modules.oppo.domain.OppoPlantProductSel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OppoPlantProductSelMapper extends CrudMapper<OppoPlantProductSel,String> {

    List<OppoPlantProductSel> findFromFactory(@Param("companyId") String companyId, @Param("password") String password, @Param("branchId") String branchId);

}
