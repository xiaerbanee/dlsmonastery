package net.myspring.future.modules.crm.web.form;

import net.myspring.common.constant.CharConstant;
import net.myspring.common.form.BaseForm;
import net.myspring.future.modules.crm.domain.ImeAllot;
import net.myspring.future.modules.crm.domain.ReportScore;
import net.myspring.util.text.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangzm on 2017/4/21.
 */
public class ReportScoreForm extends BaseForm<ReportScore> {

    private LocalDate scoreDate;
    private BigDecimal score;
    private BigDecimal monthScore;
    private BigDecimal companyScore;
    private BigDecimal companyMonthScore;
    private Integer cardQty;
    private Integer monthCardQty;
    private Integer rank;

    public LocalDate getScoreDate() {
        if(scoreDate==null){
            scoreDate= LocalDate.now().minusDays(1);
        }
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
}
