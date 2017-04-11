package net.myspring.cloud.modules.kingdee.mapper;

import net.myspring.cloud.modules.kingdee.domain.BdFlexItemGroup;
import net.myspring.cloud.modules.kingdee.domain.BdFlexItemProperty;

import java.util.List;

/**
 * Created by lihx on 2017/4/10.
 */
public interface BdFlexItemMapper {

    List<BdFlexItemGroup> findFlexItemGroup();

    List<BdFlexItemProperty> findBdFlexItemProperty();
}
