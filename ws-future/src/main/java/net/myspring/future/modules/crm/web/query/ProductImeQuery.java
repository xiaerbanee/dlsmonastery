package net.myspring.future.modules.crm.web.query;

import com.google.common.collect.Lists;
import net.myspring.future.common.query.BaseQuery;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by haos on 2017/5/12.
 */
public class ProductImeQuery extends BaseQuery{

    private String ime;
    private String boxIme;
    private String ime2;
    private String meid;
    private String depotName;
    private String InputName;
    private String imes;
    private String productName;
    private LocalDateTime createdDate;
    private LocalDateTime retailDate;
    private LocalDateTime saleDate;
    private LocalDateTime createTime;
    private String meids;
    private LocalDateTime month;

    private List<String> inputTypes= Lists.newArrayList();

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getBoxIme() {
        return boxIme;
    }

    public void setBoxIme(String boxIme) {
        this.boxIme = boxIme;
    }

    public String getIme2() {
        return ime2;
    }

    public void setIme2(String ime2) {
        this.ime2 = ime2;
    }

    public String getMeid() {
        return meid;
    }

    public void setMeid(String meid) {
        this.meid = meid;
    }

    public String getDepotName() {
        return depotName;
    }

    public void setDepotName(String depotName) {
        this.depotName = depotName;
    }

    public String getInputName() {
        return InputName;
    }

    public void setInputName(String inputName) {
        InputName = inputName;
    }

    public String getImes() {
        return imes;
    }

    public void setImes(String imes) {
        this.imes = imes;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getRetailDate() {
        return retailDate;
    }

    public void setRetailDate(LocalDateTime retailDate) {
        this.retailDate = retailDate;
    }

    public LocalDateTime getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(LocalDateTime saleDate) {
        this.saleDate = saleDate;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public String getMeids() {
        return meids;
    }

    public void setMeids(String meids) {
        this.meids = meids;
    }

    public LocalDateTime getMonth() {
        return month;
    }

    public void setMonth(LocalDateTime month) {
        this.month = month;
    }

    public List<String> getInputTypes() {
        return inputTypes;
    }

    public void setInputTypes(List<String> inputTypes) {
        this.inputTypes = inputTypes;
    }
}
