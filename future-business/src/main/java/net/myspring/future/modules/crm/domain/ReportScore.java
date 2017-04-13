package net.myspring.future.modules.crm.domain;


import com.google.common.collect.Lists;
import net.myspring.common.domain.DataEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name="crm_report_score")
public class ReportScore extends DataEntity<ReportScore> {
    private LocalDate scoreDate;
    private BigDecimal score;
    private BigDecimal monthScore;
    private BigDecimal companyScore;
    private BigDecimal companyMonthScore;
    private Integer cardQty;
    private Integer monthCardQty;
    private Integer rank;
    private Integer saleQty=0;
    private Integer monthSaleQty=0;
    private Integer recentMonthSaleQty=0;
    private BigDecimal saleMoney=BigDecimal.ZERO;
    private BigDecimal monthSaleMoney=BigDecimal.ZERO;
    private BigDecimal recentMonthSaleMoney=BigDecimal.ZERO;
    private Integer version = 0;
    private BigDecimal totalSaleMoney;
    private BigDecimal totalMonthSaleMoney;
    private Integer totalSaleQty;
    private Integer totalMonthSaleQty;
    private Integer totalScoreSaleQty;
    private Integer totalMonthScoreSaleQty;
    private List<ReportScoreArea> reportScoreAreaList = Lists.newArrayList();
    private List<String> reportScoreAreaIdList = Lists.newArrayList();

    public LocalDate getScoreDate() {
        return scoreDate;
    }

    public void setScoreDate(LocalDate scoreDate) {
        this.scoreDate = scoreDate;
    }

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    public BigDecimal getMonthScore() {
        return monthScore;
    }

    public void setMonthScore(BigDecimal monthScore) {
        this.monthScore = monthScore;
    }

    public BigDecimal getCompanyScore() {
        return companyScore;
    }

    public void setCompanyScore(BigDecimal companyScore) {
        this.companyScore = companyScore;
    }

    public BigDecimal getCompanyMonthScore() {
        return companyMonthScore;
    }

    public void setCompanyMonthScore(BigDecimal companyMonthScore) {
        this.companyMonthScore = companyMonthScore;
    }

    public Integer getCardQty() {
        return cardQty;
    }

    public void setCardQty(Integer cardQty) {
        this.cardQty = cardQty;
    }

    public Integer getMonthCardQty() {
        return monthCardQty;
    }

    public void setMonthCardQty(Integer monthCardQty) {
        this.monthCardQty = monthCardQty;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public Integer getSaleQty() {
        return saleQty;
    }

    public void setSaleQty(Integer saleQty) {
        this.saleQty = saleQty;
    }

    public Integer getMonthSaleQty() {
        return monthSaleQty;
    }

    public void setMonthSaleQty(Integer monthSaleQty) {
        this.monthSaleQty = monthSaleQty;
    }

    public Integer getRecentMonthSaleQty() {
        return recentMonthSaleQty;
    }

    public void setRecentMonthSaleQty(Integer recentMonthSaleQty) {
        this.recentMonthSaleQty = recentMonthSaleQty;
    }

    public BigDecimal getSaleMoney() {
        return saleMoney;
    }

    public void setSaleMoney(BigDecimal saleMoney) {
        this.saleMoney = saleMoney;
    }

    public BigDecimal getMonthSaleMoney() {
        return monthSaleMoney;
    }

    public void setMonthSaleMoney(BigDecimal monthSaleMoney) {
        this.monthSaleMoney = monthSaleMoney;
    }

    public BigDecimal getRecentMonthSaleMoney() {
        return recentMonthSaleMoney;
    }

    public void setRecentMonthSaleMoney(BigDecimal recentMonthSaleMoney) {
        this.recentMonthSaleMoney = recentMonthSaleMoney;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public BigDecimal getTotalSaleMoney() {
        return totalSaleMoney;
    }

    public void setTotalSaleMoney(BigDecimal totalSaleMoney) {
        this.totalSaleMoney = totalSaleMoney;
    }

    public BigDecimal getTotalMonthSaleMoney() {
        return totalMonthSaleMoney;
    }

    public void setTotalMonthSaleMoney(BigDecimal totalMonthSaleMoney) {
        this.totalMonthSaleMoney = totalMonthSaleMoney;
    }

    public Integer getTotalSaleQty() {
        return totalSaleQty;
    }

    public void setTotalSaleQty(Integer totalSaleQty) {
        this.totalSaleQty = totalSaleQty;
    }

    public Integer getTotalMonthSaleQty() {
        return totalMonthSaleQty;
    }

    public void setTotalMonthSaleQty(Integer totalMonthSaleQty) {
        this.totalMonthSaleQty = totalMonthSaleQty;
    }

    public Integer getTotalScoreSaleQty() {
        return totalScoreSaleQty;
    }

    public void setTotalScoreSaleQty(Integer totalScoreSaleQty) {
        this.totalScoreSaleQty = totalScoreSaleQty;
    }

    public Integer getTotalMonthScoreSaleQty() {
        return totalMonthScoreSaleQty;
    }

    public void setTotalMonthScoreSaleQty(Integer totalMonthScoreSaleQty) {
        this.totalMonthScoreSaleQty = totalMonthScoreSaleQty;
    }

    public List<ReportScoreArea> getReportScoreAreaList() {
        return reportScoreAreaList;
    }

    public void setReportScoreAreaList(List<ReportScoreArea> reportScoreAreaList) {
        this.reportScoreAreaList = reportScoreAreaList;
    }

    public List<String> getReportScoreAreaIdList() {
        return reportScoreAreaIdList;
    }

    public void setReportScoreAreaIdList(List<String> reportScoreAreaIdList) {
        this.reportScoreAreaIdList = reportScoreAreaIdList;
    }
}
