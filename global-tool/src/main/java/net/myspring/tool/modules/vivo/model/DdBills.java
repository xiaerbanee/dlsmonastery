package net.myspring.tool.modules.vivo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 数据库表 :DD_Bills
 */
public class DdBills {
	private String ID;
	private String CustomBillID;
	private String PrevID;
	private String Name;
	private String UserID;
	private LocalDateTime CreatedTime;
	private String RecordDate;
	private String CompanyID;
	private String BussinessCompanyID;
	private String StoreID;
	private String BussinessStoreID;
	private String AuditingCaseID;
	private String BillStatus;
	private String IsCheckUp;
	private String IsComplete;
	private String CustomStr1;
	private String CustomStr2;
	private String CustomStr3;
	private String CustomStr4;
	private String CustomStr5;
	private String CustomDec1;
	private String CustomDec2;
	private String CustomDec3;
	private String CustomDec4;
	private String CustomDec5;
	private String Remark;
	private String CustomDate1;
	private String CustomDate2;
	private String C_FLAG;
	private List<DdBillsProduct> ddBillsProduct = Lists.newArrayList();
	private List<DdBillsProductList> ddBillsProductList = Lists.newArrayList();
	
	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getCustomBillID() {
		return CustomBillID;
	}

	public void setCustomBillID(String customBillID) {
		CustomBillID = customBillID;
	}

	public String getPrevID() {
		return PrevID;
	}

	public void setPrevID(String prevID) {
		PrevID = prevID;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getUserID() {
		return UserID;
	}

	public void setUserID(String userID) {
		UserID = userID;
	}

	public LocalDateTime getCreatedTime() {
		return CreatedTime;
	}

	public void setCreatedTime(LocalDateTime createdTime) {
		CreatedTime = createdTime;
	}

	public String getRecordDate() {
		return RecordDate;
	}

	public void setRecordDate(String recordDate) {
		RecordDate = recordDate;
	}

	public String getCompanyID() {
		return CompanyID;
	}

	public void setCompanyID(String companyID) {
		CompanyID = companyID;
	}

	public String getBussinessCompanyID() {
		return BussinessCompanyID;
	}

	public void setBussinessCompanyID(String bussinessCompanyID) {
		BussinessCompanyID = bussinessCompanyID;
	}

	public String getStoreID() {
		return StoreID;
	}

	public void setStoreID(String storeID) {
		StoreID = storeID;
	}

	public String getBussinessStoreID() {
		return BussinessStoreID;
	}

	public void setBussinessStoreID(String bussinessStoreID) {
		BussinessStoreID = bussinessStoreID;
	}

	public String getAuditingCaseID() {
		return AuditingCaseID;
	}

	public void setAuditingCaseID(String auditingCaseID) {
		AuditingCaseID = auditingCaseID;
	}

	public String getBillStatus() {
		return BillStatus;
	}

	public void setBillStatus(String billStatus) {
		BillStatus = billStatus;
	}

	public String getIsCheckUp() {
		return IsCheckUp;
	}

	public void setIsCheckUp(String isCheckUp) {
		IsCheckUp = isCheckUp;
	}

	public String getIsComplete() {
		return IsComplete;
	}

	public void setIsComplete(String isComplete) {
		IsComplete = isComplete;
	}

	public String getCustomStr1() {
		return CustomStr1;
	}

	public void setCustomStr1(String customStr1) {
		CustomStr1 = customStr1;
	}

	public String getCustomStr2() {
		return CustomStr2;
	}

	public void setCustomStr2(String customStr2) {
		CustomStr2 = customStr2;
	}

	public String getCustomStr3() {
		return CustomStr3;
	}

	public void setCustomStr3(String customStr3) {
		CustomStr3 = customStr3;
	}

	public String getCustomStr4() {
		return CustomStr4;
	}

	public void setCustomStr4(String customStr4) {
		CustomStr4 = customStr4;
	}

	public String getCustomStr5() {
		return CustomStr5;
	}

	public void setCustomStr5(String customStr5) {
		CustomStr5 = customStr5;
	}

	public String getCustomDec1() {
		return CustomDec1;
	}

	public void setCustomDec1(String customDec1) {
		CustomDec1 = customDec1;
	}

	public String getCustomDec2() {
		return CustomDec2;
	}

	public void setCustomDec2(String customDec2) {
		CustomDec2 = customDec2;
	}

	public String getCustomDec3() {
		return CustomDec3;
	}

	public void setCustomDec3(String customDec3) {
		CustomDec3 = customDec3;
	}

	public String getCustomDec4() {
		return CustomDec4;
	}

	public void setCustomDec4(String customDec4) {
		CustomDec4 = customDec4;
	}

	public String getCustomDec5() {
		return CustomDec5;
	}

	public void setCustomDec5(String customDec5) {
		CustomDec5 = customDec5;
	}

	public String getRemark() {
		return Remark;
	}

	public void setRemark(String remark) {
		Remark = remark;
	}

	public String getCustomDate1() {
		return CustomDate1;
	}

	public void setCustomDate1(String customDate1) {
		CustomDate1 = customDate1;
	}

	public String getCustomDate2() {
		return CustomDate2;
	}

	public void setCustomDate2(String customDate2) {
		CustomDate2 = customDate2;
	}

	public String getC_FLAG() {
		return C_FLAG;
	}

	public void setC_FLAG(String c_FLAG) {
		C_FLAG = c_FLAG;
	}

	@JsonIgnore
	public List<DdBillsProduct> getDdBillsProduct() {
		return ddBillsProduct;
	}

	public void setDdBillsProduct(List<DdBillsProduct> ddBillsProduct) {
		this.ddBillsProduct = ddBillsProduct;
	}

	@JsonIgnore
	public List<DdBillsProductList> getDdBillsProductList() {
		return ddBillsProductList;
	}

	public void setDdBillsProductList(List<DdBillsProductList> ddBillsProductList) {
		this.ddBillsProductList = ddBillsProductList;
	}

}
