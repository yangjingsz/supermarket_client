package entity;

import java.util.List;

import role.Role;




public class User {
	private Role role = null;
	private String userName;
	private String passWord;
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}
	
	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	/**
	 * 登录
	 * @param username
	 * @param password
	 * @return
	 */
	public boolean login(String username, String password){
		if(username.equals(password)){
			return true;
		}
		return false;
	}
	
	/**
	 * 创建订单
	 * @return 订单
	 */
	public Orders createOrders(User user) {
		return role.createOrders(user);
	}

	/**
	 * 扫描商品
	 * @param comId 
	 */
	public void scanCommodity(String comId) {
		role.scanCommodity(comId);
	}
	
	/**
	 * 入库
	 * @param commodity 商品
	 */
	public void in(Commodity commodity) {
		role.in(commodity);
	}

	/**
	 * 出库
	 * @param comId 商品编号 
	 * @param num 数量
	 */
	public void out(String comId, int num) {
		role.out(comId, num);
	}

	/**
	 * 查询库存
	 * @return 商品列表
	 */
	public List query() {
		return role.query();
	}

	/**
	 * 按编号查询商品
	 * @param comId 商品编号
	 * @return 商品
	 */
	public Commodity queryById(String comId) {
		return role.queryById(comId);
	}
}
