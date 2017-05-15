package net.myspring.tool.modules.vivo.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class PlantEndProductSale {
	private String companyID;
	private String endBillID;
	private String productID;
	private Integer saleCount;
	private String imei;
	private LocalDate billDate;
	private String dealerID;
	private BigDecimal salePrice;
	private LocalDateTime createdTime;

	public String getCompanyID() {
		return companyID;
	}

	public void setCompanyID(String companyID) {
		this.companyID = companyID;
	}

	public String getEndBillID() {
		return endBillID;
	}

	public void setEndBillID(String endBillID) {
		this.endBillID = endBillID;
	}

	public String getProductID() {
		return productID;
	}

	public void setProductID(String productID) {
		this.productID = productID;
	}

	public Integer getSaleCount() {
		return saleCount;
	}

	public void setSaleCount(Integer saleCount) {
		this.saleCount = saleCount;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public LocalDate getBillDate() {
		return billDate;
	}

	public void setBillDate(LocalDate billDate) {
		this.billDate = billDate;
	}

	public String getDealerID() {
		return dealerID;
	}

	public void setDealerID(String dealerID) {
		this.dealerID = dealerID;
	}

	public BigDecimal getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(BigDecimal salePrice) {
		this.salePrice = salePrice;
	}

	public LocalDateTime getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(LocalDateTime createdTime) {
		this.createdTime = createdTime;
	}
}
