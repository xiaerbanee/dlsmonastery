package net.myspring.future.modules.crm.dto;

import net.myspring.common.dto.DataDto;
import net.myspring.future.modules.crm.domain.ProductIme;
import net.myspring.util.cahe.annotation.CacheInput;


public class ProductImeHistoryDto extends DataDto<ProductIme> {

	private String type;
	private String fromDepotId;
	@CacheInput(inputKey = "depots",inputInstance = "fromDepotId",outputInstance = "name")
	private String fromDepotName;
	private String toDepotId;
	@CacheInput(inputKey = "depots",inputInstance = "toDepotId",outputInstance = "name")
	private String toDepotName;

	public String getFromDepotId() {
		return fromDepotId;
	}

	public void setFromDepotId(String fromDepotId) {
		this.fromDepotId = fromDepotId;
	}

	public String getFromDepotName() {
		return fromDepotName;
	}

	public void setFromDepotName(String fromDepotName) {
		this.fromDepotName = fromDepotName;
	}

	public String getToDepotId() {
		return toDepotId;
	}

	public void setToDepotId(String toDepotId) {
		this.toDepotId = toDepotId;
	}

	public String getToDepotName() {
		return toDepotName;
	}

	public void setToDepotName(String toDepotName) {
		this.toDepotName = toDepotName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
