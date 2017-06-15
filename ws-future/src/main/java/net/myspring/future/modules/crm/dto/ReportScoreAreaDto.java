package net.myspring.future.modules.crm.dto;

import net.myspring.common.dto.DataDto;
import net.myspring.future.modules.crm.domain.ReportScoreArea;
import net.myspring.util.cahe.annotation.CacheInput;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ReportScoreAreaDto extends DataDto<ReportScoreArea> {
    private LocalDate scoreDate;//日期
    private String officeId;
    @CacheInput(inputKey = "offices",inputInstance = "officeId",outputInstance = "name")
    private String officeName;//考核区域
    @CacheInput(inputKey = "offices",inputInstance = "officeId",outputInstance = "point")
    private BigDecimal officePoint;//点位
    private BigDecimal score;//当日打分
    private BigDecimal monthScore;//当月打分
    private Integer dateRank;//当日排名
    private Integer monthRank;//当月排名
    private Integer saleQty;//当日销量
    private Integer monthSaleQty;//当月销量
    private Integer recentMonthSaleQty;//最近月销量
    private BigDecimal monthSaleMoney;//月累计销售
    private BigDecimal pointMonthSaleMoney;//月累计单点销售额


    public LocalDate getScoreDate() {
        return scoreDate;
    }

    public void setScoreDate(LocalDate scoreDate) {
        this.scoreDate = scoreDate;
    }

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    public BigDecimal getOfficePoint() {
        return officePoint;
    }

    public void setOfficePoint(BigDecimal officePoint) {
        this.officePoint = officePoint;
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

    public Integer getDateRank() {
        return dateRank;
    }

    public void setDateRank(Integer dateRank) {
        this.dateRank = dateRank;
    }

    public Integer getMonthRank() {
        return monthRank;
    }

    public void setMonthRank(Integer monthRank) {
        this.monthRank = monthRank;
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

    public BigDecimal getMonthSaleMoney() {
        return monthSaleMoney;
    }

    public void setMonthSaleMoney(BigDecimal monthSaleMoney) {
        this.monthSaleMoney = monthSaleMoney;
    }

    public BigDecimal getPointMonthSaleMoney() {
        return pointMonthSaleMoney;
    }

    public void setPointMonthSaleMoney(BigDecimal pointMonthSaleMoney) {
        this.pointMonthSaleMoney = pointMonthSaleMoney;
    }
}
