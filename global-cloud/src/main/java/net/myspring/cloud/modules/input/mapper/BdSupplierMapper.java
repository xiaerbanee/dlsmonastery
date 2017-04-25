package net.myspring.cloud.modules.input.mapper;

import net.myspring.cloud.modules.input.domain.BdSupplier;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by lihx on 2017/4/11.
 */
@Mapper
public interface BdSupplierMapper {

    List<BdSupplier> findAll();
}
