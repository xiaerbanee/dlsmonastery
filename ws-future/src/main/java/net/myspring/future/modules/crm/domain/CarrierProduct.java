package net.myspring.future.modules.crm.domain;

import net.myspring.future.common.domain.CompanyEntity;

import javax.persistence.*;

@Entity
@Table(name = "api_carrier_product")
public class CarrierProduct extends CompanyEntity<CarrierProduct> {
	private String name;
	private String productId;

	private String mallProductTypeName;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getMallProductTypeName() {
		return mallProductTypeName;
	}

	public void setMallProductTypeName(String mallProductTypeName) {
		this.mallProductTypeName = mallProductTypeName;
	}
}
