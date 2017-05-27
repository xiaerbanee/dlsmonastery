package net.myspring.future.modules.crm.domain;


import net.myspring.future.common.domain.IdEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="crm_lottery_rule_detail")
public class LotteryRuleDetail extends IdEntity<LotteryRuleDetail> {
    private String name;
    private Integer qty;
    private String maxType;
    private Integer maxQty;

    private String lotteryRuleId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public String getMaxType() {
        return maxType;
    }

    public void setMaxType(String maxType) {
        this.maxType = maxType;
    }

    public Integer getMaxQty() {
        return maxQty;
    }

    public void setMaxQty(Integer maxQty) {
        this.maxQty = maxQty;
    }

    public String getLotteryRuleId() {
        return lotteryRuleId;
    }

    public void setLotteryRuleId(String lotteryRuleId) {
        this.lotteryRuleId = lotteryRuleId;
    }
}
