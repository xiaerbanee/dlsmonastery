package net.myspring.basic.modules.hr.dto;

import com.google.common.collect.Lists;
import net.myspring.basic.modules.hr.domain.Account;

import java.util.List;

/**
 * Created by admin on 2016/12/23.
 */
public class DutyDto {
    private String id;
    private String formatId;
    private Account account;
    private String dutyType;
    private String dutyDate;
    private String remarks;
    private List<String> actionList = Lists.newArrayList();

    public String getFormatId() {
        return formatId;
    }

    public void setFormatId(String formatId) {
        this.formatId = formatId;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getDutyType() {
        return dutyType;
    }

    public void setDutyType(String dutyType) {
        this.dutyType = dutyType;
    }

    public String getDutyDate() {
        return dutyDate;
    }

    public void setDutyDate(String dutyDate) {
        this.dutyDate = dutyDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getActionList() {
        return actionList;
    }

    public void setActionList(List<String> actionList) {
        this.actionList = actionList;
    }
}
