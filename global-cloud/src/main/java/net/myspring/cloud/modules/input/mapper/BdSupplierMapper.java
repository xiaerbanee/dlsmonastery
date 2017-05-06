package net.myspring.cloud.modules.input.mapper;

import net.myspring.cloud.modules.input.domain.BdSupplier;
import net.myspring.cloud.modules.input.dto.NameNumberDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by lihx on 2017/4/11.
 */
@Mapper
public interface BdSupplierMapper {

    List<BdSupplier> findAll();

    String findNameBySupplierId(@Param("supplierId")String supplierId);

    List<NameNumberDto> findNameAndNumber();
}
