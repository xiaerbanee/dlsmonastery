package net.myspring.cloud.modules.input.mapper;

import net.myspring.cloud.modules.input.domain.BdSettleType;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by lihx on 2017/4/5.
 */
@Mapper
public interface BdSettleTypeMapper {

    List<BdSettleType> findAll();
}
