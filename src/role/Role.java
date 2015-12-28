package role;

import java.util.List;

import authority.Cashiers;
import authority.StoreMgr;
import entity.Commodity;
import entity.Orders;
import entity.User;



public abstract class Role {

	private String description;// ��ɫ��
	private StoreMgr storeMgr = null;//������ԱȨ��
	private Cashiers cashiers = null;//����ԱȨ��
	
	/**
	 * ��������
	 * @return ����
	 */
	public Orders createOrders(User user) {
		if(cashiers==null){
			System.out.println("��û������ԱȨ��");
			return null;
		}
		return cashiers.createOrders(user);
	}

	/**
	 * ɨ����Ʒ
	 * @param comId 
	 */
	public void scanCommodity(String comId) {
		if(cashiers==null){
			System.out.println("��û������ԱȨ��");
			return;
		}
		cashiers.scanCommodity(comId);
	}
	
	/**
	 * ���
	 * @param commodity ��Ʒ
	 */
	public void in(Commodity commodity) {
		if(storeMgr==null){
			System.out.println("��û�п�����ԱȨ��");
			return;
		}
		storeMgr.in(commodity);
	}

	/**
	 * ����
	 * @param comId ��Ʒ��� 
	 * @param num ����
	 */
	public void out(String comId, int num) {
		if(storeMgr==null){
			System.out.println("��û�п�����ԱȨ��");
			return;
		}
		storeMgr.out(comId, num);
	}

	/**
	 * ��ѯ���
	 * @return ��Ʒ�б�
	 */
	public List query() {
		if(storeMgr==null){
			System.out.println("��û�п�����ԱȨ��");
			return null;
		}
		return storeMgr.query();
	}

	/**
	 * ����Ų�ѯ��Ʒ
	 * @param comId ��Ʒ���
	 * @return ��Ʒ
	 */
	public Commodity queryById(String comId) {
		if(storeMgr==null){
			System.out.println("��û�п�����ԱȨ��");
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
