package net.myspring.future.modules.crm.domain;


import net.myspring.future.common.domain.CompanyEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name="crm_report_score")
public class ReportScore extends CompanyEntity<ReportScore> {
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

}
