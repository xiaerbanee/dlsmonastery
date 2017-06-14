package net.myspring.future.modules.crm.dto;

public class NameQty {
	private String name;
	private Long qty;
	
	public NameQty(String name, Long qty) {
		this.name =name;
		this.qty = qty;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getQty() {
		return qty;
	}
	public void setQty(Long qty) {
		this.qty = qty;
	}
}
