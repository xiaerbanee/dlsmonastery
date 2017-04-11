package net.myspring.cloud.modules.kingdee.dto;

import net.myspring.cloud.common.enums.CharEnum;
import net.myspring.cloud.modules.kingdee.domain.BdFlexItemGroup;
import net.myspring.util.text.StringUtils;

import java.util.List;

/**
 * Created by lihx on 2017/4/11.
 */
public class BdFlexItemGroupDto extends BdFlexItemGroup {

    public List<String> getFNames() {
        return StringUtils.getSplitList(getfName(), CharEnum.CHAR_SLASH_LINE.getValue());
    }
}
