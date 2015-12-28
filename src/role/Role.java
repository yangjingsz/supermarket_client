package role;

import java.util.List;

import authority.Cashiers;
import authority.StoreMgr;
import entity.Commodity;
import entity.Orders;
import entity.User;



public abstract class Role {

	private String description;// 角色名
	private StoreMgr storeMgr = null;//库存管理员权限
	private Cashiers cashiers = null;//收银员权限
	
	/**
	 * 创建订单
	 * @return 订单
	 */
	public Orders createOrders(User user) {
		if(cashiers==null){
			System.out.println("您没有收银员权限");
			return null;
		}
		return cashiers.createOrders(user);
	}

	/**
	 * 扫描商品
	 * @param comId 
	 */
	public void scanCommodity(String comId) {
		if(cashiers==null){
			System.out.println("您没有收银员权限");
			return;
		}
		cashiers.scanCommodity(comId);
	}
	
	/**
	 * 入库
	 * @param commodity 商品
	 */
	public void in(Commodity commodity) {
		if(storeMgr==null){
			System.out.println("您没有库存管理员权限");
			return;
		}
		storeMgr.in(commodity);
	}

	/**
	 * 出库
	 * @param comId 商品编号 
	 * @param num 数量
	 */
	public void out(String comId, int num) {
		if(storeMgr==null){
			System.out.println("您没有库存管理员权限");
			return;
		}
		storeMgr.out(comId, num);
	}

	/**
	 * 查询库存
	 * @return 商品列表
	 */
	public List query() {
		if(storeMgr==null){
			System.out.println("您没有库存管理员权限");
			return null;
		}
		return storeMgr.query();
	}

	/**
	 * 按编号查询商品
	 * @param comId 商品编号
	 * @return 商品
	 */
	public Commodity queryById(String comId) {
		if(storeMgr==null){
			System.out.println("您没有库存管理员权限");
			return null;
		}
		return storeMgr.queryById(comId);
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public StoreMgr getStoreMgr() {
		return storeMgr;
	}


	public void setStoreMgr(StoreMgr storeMgr) {
		this.storeMgr = storeMgr;
	}

	public Cashiers getCashier() {
		return cashiers;
	}

	public void setCashier(Cashiers cashiers) {
		this.cashiers = cashiers;
	}
	
}
