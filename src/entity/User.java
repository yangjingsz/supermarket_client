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
	 * ��¼
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
	 * ��������
	 * @return ����
	 */
	public Orders createOrders(User user) {
		return role.createOrders(user);
	}

	/**
	 * ɨ����Ʒ
	 * @param comId 
	 */
	public void scanCommodity(String comId) {
		role.scanCommodity(comId);
	}
	
	/**
	 * ���
	 * @param commodity ��Ʒ
	 */
	public void in(Commodity commodity) {
		role.in(commodity);
	}

	/**
	 * ����
	 * @param comId ��Ʒ��� 
	 * @param num ����
	 */
	public void out(String comId, int num) {
		role.out(comId, num);
	}

	/**
	 * ��ѯ���
	 * @return ��Ʒ�б�
	 */
	public List query() {
		return role.query();
	}

	/**
	 * ����Ų�ѯ��Ʒ
	 * @param comId ��Ʒ���
	 * @return ��Ʒ
	 */
	public Commodity queryById(String comId) {
		return role.queryById(comId);
	}
}
