package net.myspring.cloud.modules.kingdee.domain;

import net.myspring.common.constant.CharConstant;
import net.myspring.util.text.StringUtils;

import java.util.List;

/**
 * 科目对应-核算维度组
 * Created by lihx on 2017/4/10.
 */
public class BdFlexItemGroup {
    private String FId;
    private String FNumber;
    private String FName;

    public String getFId() {
        return FId;
    }

    public void setFId(String FId) {
        this.FId = FId;
    }

    public String getFNumber() {
        return FNumber;
    }

    public void setFNumber(String FNumber) {
        this.FNumber = FNumber;
    }

    public String getFName() {
        return FName;
    }

    public void setFName(String FName) {
        this.FName = FName;
    }

    public List<String> getFNames() {
        return StringUtils.getSplitList(getFName(), CharConstant.SLASH_LINE);
    }
}
