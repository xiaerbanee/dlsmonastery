package net.myspring.cloud.modules.sys.dto;

import net.myspring.common.constant.CharConstant;
import net.myspring.common.dto.DataDto;
import net.myspring.cloud.modules.kingdee.domain.BdFlexItemGroup;
import net.myspring.util.text.StringUtils;

import java.util.List;

/**
 * Created by lihx on 2017/4/11.
 */
public class BdFlexItemGroupDto extends DataDto<BdFlexItemGroup> {
    private String fId;
    private String fNumber;
    private String fName;

    public String getfId() {
        return fId;
    }

    public void setfId(String fId) {
        this.fId = fId;
    }

    public String getfNumber() {
        return fNumber;
    }

    public void setfNumber(String fNumber) {
        this.fNumber = fNumber;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public List<String> getFNames() {
        return StringUtils.getSplitList(getfName(), CharConstant.SLASH_LINE);
    }
}
