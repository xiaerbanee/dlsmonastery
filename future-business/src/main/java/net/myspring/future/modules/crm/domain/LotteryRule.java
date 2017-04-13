package net.myspring.future.modules.crm.domain;


import com.google.common.collect.Lists;
import net.myspring.common.domain.DataEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name="crm_lottery_rule")
public class LotteryRule extends DataEntity<LotteryRule> {
    private String name;
    private String type;
    private LocalDate dateStart;
    private LocalDate dateEnd;
    private String productIds;
    private String image;
    private Integer version = 0;
    private List<LotteryRuleDetail> lotteryRuleDetailList = Lists.newArrayList();
    private List<String> lotteryRuleDetailIdList = Lists.newArrayList();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDate getDateStart() {
        return dateStart;
    }

    public void setDateStart(LocalDate dateStart) {
        this.dateStart = dateStart;
    }

    public LocalDate getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(LocalDate dateEnd) {
        this.dateEnd = dateEnd;
    }

    public String getProductIds() {
        return productIds;
    }

    public void setProductIds(String productIds) {
        this.productIds = productIds;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public List<LotteryRuleDetail> getLotteryRuleDetailList() {
        return lotteryRuleDetailList;
    }

    public void setLotteryRuleDetailList(List<LotteryRuleDetail> lotteryRuleDetailList) {
        this.lotteryRuleDetailList = lotteryRuleDetailList;
    }

    public List<String> getLotteryRuleDetailIdList() {
        return lotteryRuleDetailIdList;
    }

    public void setLotteryRuleDetailIdList(List<String> lotteryRuleDetailIdList) {
        this.lotteryRuleDetailIdList = lotteryRuleDetailIdList;
    }
}
