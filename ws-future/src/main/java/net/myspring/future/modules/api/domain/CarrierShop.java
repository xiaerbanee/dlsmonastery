package net.myspring.future.modules.api.domain;

import net.myspring.future.common.domain.DataEntity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "api_carrier_shop")
@Cacheable
public class CarrierShop extends DataEntity<CarrierShop> {
	private String name;
	private String code;
	private String type;

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
