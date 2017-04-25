package net.myspring.cloud.modules.input.mapper;

import net.myspring.cloud.modules.input.domain.BdFlexItemGroup;
import net.myspring.cloud.modules.input.domain.BdFlexItemProperty;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by lihx on 2017/4/10.
 */
@Mapper
public interface BdFlexItemMapper {

    List<BdFlexItemGroup> findFlexItemGroup();

    List<BdFlexItemProperty> findBdFlexItemProperty();
}
