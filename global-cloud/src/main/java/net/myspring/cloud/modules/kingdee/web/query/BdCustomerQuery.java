package net.myspring.cloud.modules.kingdee.web.query;

import com.google.common.collect.Lists;
import net.myspring.cloud.common.query.BaseQuery;
import net.myspring.common.constant.CharConstant;
import net.myspring.util.text.StringUtils;
import net.myspring.util.time.LocalDateUtils;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by liuj on 2017/5/16.
 */
public class BdCustomerQuery extends BaseQuery {
    private String customerGroup;
    private List<String> customerIdList = Lists.newArrayList();

    public List<String> getCustomerIdList() {
        return customerIdList;
    }

    public void setCustomerIdList(List<String> customerIdList) {
        this.customerIdList = customerIdList;
    }

    public String getCustomerGroup() {
        return customerGroup;
    }

    public void setCustomerGroup(String customerGroup) {
        this.customerGroup = customerGroup;
    }
}
