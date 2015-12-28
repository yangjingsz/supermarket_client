package entity;

import java.io.Serializable;
import java.util.Map;

public class Orders implements Serializable{
	private String orderId;//账单编号
	private Map<String,Item> itemMap;//订单商品名细
	private String useId;//操作人
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public Map<String, Item> getItemMap() {
		return itemMap;
	}
	public void setItemMap(Map<String, Item> itemMap) {
		this.itemMap = itemMap;
	}
	public String getUseId() {
		return useId;
	}
	public void setUseId(String useId) {
		this.useId = useId;
	}
}
