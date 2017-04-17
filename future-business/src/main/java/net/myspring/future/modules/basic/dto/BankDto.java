package net.myspring.future.modules.basic.dto;

import com.google.common.collect.Lists;
import net.myspring.future.common.dto.DataDto;
import net.myspring.future.modules.basic.domain.Bank;
import net.myspring.future.modules.crm.domain.BankIn;
import net.myspring.future.modules.layout.domain.ShopDeposit;
import net.myspring.future.modules.layout.domain.ShopGoodsDeposit;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by lihx on 2017/4/17.
 */
public class BankDto extends DataDto<Bank> {
    private String name;
    private String code;
    private String outId;
    private LocalDateTime outDate;
    private Integer version;
    private String oldName;
    private String oldOutId;
    private List<String> accountIdList = Lists.newArrayList();
    private List<BankIn> bankInList = Lists.newArrayList();
    private List<String> bankInIdList = Lists.newArrayList();
    private List<ShopDeposit> shopDepositList = Lists.newArrayList();
    private List<String> shopDepositIdList = Lists.newArrayList();
    private List<ShopGoodsDeposit> shopGoodsDepositList = Lists.newArrayList();
    private List<String> shopGoodsDepositIdList = Lists.newArrayList();
    private List<String> employeePhoneDepositIdList = Lists.newArrayList();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getOutId() {
        return outId;
    }

    public void setOutId(String outId) {
        this.outId = outId;
    }

    public LocalDateTime getOutDate() {
        return outDate;
    }

    public void setOutDate(LocalDateTime outDate) {
        this.outDate = outDate;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getOldName() {
        return oldName;
    }

    public void setOldName(String oldName) {
        this.oldName = oldName;
    }

    public String getOldOutId() {
        return oldOutId;
    }

    public void setOldOutId(String oldOutId) {
        this.oldOutId = oldOutId;
    }

    public List<String> getAccountIdList() {
        return accountIdList;
    }

    public void setAccountIdList(List<String> accountIdList) {
        this.accountIdList = accountIdList;
    }

    public List<BankIn> getBankInList() {
        return bankInList;
    }

    public void setBankInList(List<BankIn> bankInList) {
        this.bankInList = bankInList;
    }

    public List<String> getBankInIdList() {
        return bankInIdList;
    }

    public void setBankInIdList(List<String> bankInIdList) {
        this.bankInIdList = bankInIdList;
    }

    public List<ShopDeposit> getShopDepositList() {
        return shopDepositList;
    }

    public void setShopDepositList(List<ShopDeposit> shopDepositList) {
        this.shopDepositList = shopDepositList;
    }

    public List<String> getShopDepositIdList() {
        return shopDepositIdList;
    }

    public void setShopDepositIdList(List<String> shopDepositIdList) {
        this.shopDepositIdList = shopDepositIdList;
    }

    public List<ShopGoodsDeposit> getShopGoodsDepositList() {
        return shopGoodsDepositList;
    }

    public void setShopGoodsDepositList(List<ShopGoodsDeposit> shopGoodsDepositList) {
        this.shopGoodsDepositList = shopGoodsDepositList;
    }

    public List<String> getShopGoodsDepositIdList() {
        return shopGoodsDepositIdList;
    }

    public void setShopGoodsDepositIdList(List<String> shopGoodsDepositIdList) {
        this.shopGoodsDepositIdList = shopGoodsDepositIdList;
    }

    public List<String> getEmployeePhoneDepositIdList() {
        return employeePhoneDepositIdList;
    }

    public void setEmployeePhoneDepositIdList(List<String> employeePhoneDepositIdList) {
        this.employeePhoneDepositIdList = employeePhoneDepositIdList;
    }
}
