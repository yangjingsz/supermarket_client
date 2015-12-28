package biz;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import role.Role;
import role.impl.Cashier;
import role.impl.StoreManager;
import util.Datas;
import util.SysConstants;
import util.Tools;
import authority.Cashiers;
import authority.StoreMgr;
import authority.impl.CashiersImpl;
import authority.impl.StoreMgrImpl;
import entity.Commodity;
import entity.Item;
import entity.Orders;
import entity.User;

public class ClientBiz {
	Map<String, Item> itemMap = new HashMap<String, Item>();// ����
	Socket socket = null; // Scoketʵ��
	ObjectInputStream objectInputStream = null; // ����������ʵ��
	ObjectOutputStream objectOutputStream = null; // ���������ʵ��

	/**
	 * ʵ����Socket��ض���
	 */
	private void initSocket() {
		try {
			socket = new Socket(Tools.getValue(SysConstants.SYS_HOSTNAME),
					Integer.parseInt(Tools
							.getValue(SysConstants.SYS_LISTENERPORT)));
			objectInputStream = new ObjectInputStream(socket.getInputStream());
			objectOutputStream = new ObjectOutputStream(socket
					.getOutputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * �ر�Socket��ض���
	 */
	public void closeSocket() {
		try {
			objectInputStream.close();
			objectOutputStream.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ��¼
	 * 
	 * @param user
	 *            ��¼�û�
	 * @return �û�����
	 */
	public User login(User user) {
		// ʵ����Socket��ض���
		initSocket();
		// �������л��������
		Datas datas = new Datas();
		// ���ò�����ʶ
		datas.setFlag(SysConstants.SYS_LOGIN);
		// ���ô��������û���Ϣ
		datas.setUserName(user.getUserName());
		datas.setPassWord(user.getPassWord());
		try {
			// ����������ʹ������
			objectOutputStream.writeObject(datas);
			// ��ȡ���������صĴ������
			datas = (Datas) objectInputStream.readObject();
			if (datas.getRole() != null) {
				if (datas.getRole().equals("storeMgr")) {
					Role storeManager = new StoreManager();// ����������Ա��ɫ
					StoreMgr storeMgr = new StoreMgrImpl();// ����������ԱȨ��
					storeManager.setStoreMgr(storeMgr);// ����Ȩ��
					user.setRole(storeManager);// Ϊ�û���Ȩ��ɫ
				}
				if (datas.getRole().equals("cashier")) {
					Role cashier = new Cashier();// ��������Ա��ɫ
					Cashiers cashiers = new CashiersImpl();// ��������ԱȨ��
					cashier.setCashier(cashiers);// ����Ȩ��
					user.setRole(cashier);// Ϊ�û���Ȩ��ɫ
				}
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeSocket();
		}
		// ����
		return user;
	}

	/**
	 * ��������
	 * 
	 * @return ����
	 */
	public Orders createOrders(User user) {
		Orders orders = new Orders();
		orders.setItemMap(itemMap);
		/* ��ʽ����ǰʱ�� */
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
		Date date = new Date();
		String orderId = sdf.format(date);
		orders.setOrderId(orderId);// ���ö�����
		// ������������Ա
		orders.setUseId(user.getUserName());
		return orders;
	}

	/**
	 * ɨ����Ʒ
	 * 
	 * @param comId
	 */
	public void scanCommodity(String comId) {
		Commodity commodity = queryById(comId);
		if (itemMap.containsKey(comId)) {// �ж��Ƿ���Щ��Ʒ��
			System.out.println(itemMap.get(comId).getCommodity().getComName());
			if (updateItemNum(comId, 1)) {
				Item item = itemMap.get(comId);// �ô���Ʒ��
				item.setItemNum(item.getItemNum() + 1);// �޸Ĵ���Ʒ�������
				itemMap.put(commodity.getComId(), item);// ����Ʒ����붩����Ʒ��ϸ
			} else {
				System.out.println("��Ǹ����治����");
			}
		} else {// ���û�д���Ʒ��򴴽� һ����Ʒ��
			Item item = new Item();
			if (updateItemNum(comId, 1)) {
				item.setItemNum(1);
				item.setCommodity(commodity);
				item.setItemPrice(commodity.getPrice());
				item.setItemUnit(commodity.getUnit());
				System.out.println(commodity.getComName());
				itemMap.put(commodity.getComId(), item);// ����Ʒ����붩����Ʒ��ϸ
			} else {
				System.out.println("��Ǹ����治����");
			}
		}
	}

	/**
	 * �޸���Ʒ������
	 * 
	 * @param comId
	 *            ��Ʒ���
	 * @param num
	 *            ����
	 */
	public boolean updateItemNum(String comId, int num) {
		// baiyujuan

		if (itemMap.containsKey(comId)) {// �ж��Ƿ���Щ��Ʒ��
			Item item = itemMap.get(comId);// �ô���Ʒ��
			Commodity commodity=null;
			if (num == 1) {
				commodity= queryById(comId, 1);
			} else {
				commodity = queryById(comId, num
						- item.getCommodity().getNum());
			}
			if (commodity != null) {
				item.setItemNum(num);// �޸Ĵ���Ʒ�������
				itemMap.put(commodity.getComId(), item);// ����Ʒ����붩����Ʒ��ϸ
			} else {
				System.out.println("��治�㣡");
				return false;
			}
		} else {// ���û�д���Ʒ��򴴽� һ����Ʒ��
			Commodity commodity = queryById(comId, 1);
			if (commodity != null) {
				Item item = new Item();
				item.setItemNum(1);
				item.setCommodity(commodity);
				item.setItemPrice(commodity.getPrice());
				item.setItemUnit(commodity.getUnit());
				itemMap.put(commodity.getComId(), item);// ����Ʒ����붩����Ʒ��ϸ
			} else {
				System.out.println("��治�㣡");
				return false;
			}
		}
		return true;

	}

	/**
	 * ��ѯ�����Ʒ
	 * 
	 * @return ��Ʒ�б�
	 */
	public List<Commodity> query() {
		List<Commodity> comList = null;
		// ʵ����Socket��ض���
		initSocket();
		// �������л��������
		Datas datas = new Datas();
		// ���ò�����ʶ
		datas.setFlag(SysConstants.SYS_QUERY);
		try {
			// ����������ʹ������
			objectOutputStream.writeObject(datas);
			// ��ȡ���������صĴ������
			datas = (Datas) objectInputStream.readObject();
			comList = datas.getComList();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeSocket();
		}
		return comList;
	}

	/**
	 * ����Ų�ѯ��Ʒ
	 * 
	 * @param comId
	 *            ��Ʒ���
	 * @return ��Ʒ
	 */
	public Commodity queryById(String comId) {
		Commodity commodity = null;
		// ʵ����Socket��ض���
		initSocket();
		// �������л��������
		Datas datas = new Datas();
		// ���ô������Ʒ���
		datas.setComId(comId);
		// ���ò�����ʶ
		datas.setFlag(SysConstants.SYS_QUERYBYID);
		try {
			// ����������ʹ������
			objectOutputStream.writeObject(datas);
			// ��ȡ���������صĴ������
			datas = (Datas) objectInputStream.readObject();
			commodity = datas.getCommodity();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeSocket();
		}
		return commodity;
	}

	/**
	 * ����Ų�ѯ��Ʒ
	 * 
	 * @param comId
	 *            ��Ʒ���
	 * @return ��Ʒ
	 */
	public Commodity queryById(String comId, int num) {
		Commodity commodity = new Commodity();
		commodity.setComId(comId);
		commodity.setNum(num);
		// ʵ����Socket��ض���
		initSocket();
		// �������л��������
		Datas datas = new Datas();
		// ���ô������Ʒ���
		datas.setComId(comId);
		datas.setCommodity(commodity);
		// ���ò�����ʶ
		datas.setFlag(SysConstants.SYS_QUERYUPDATEBYID);
		try {
			// ����������ʹ������
			objectOutputStream.writeObject(datas);
			// ��ȡ���������صĴ������
			datas = (Datas) objectInputStream.readObject();
			commodity = null;
			commodity = datas.getCommodity();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeSocket();
		}
		return commodity;
	}

	/**
	 * ���
	 * 
	 * @param commodity
	 *            ��Ʒ����
	 * @return
	 */
	public boolean in(Commodity commodity) {
		// ʵ����Socket��ض���
		initSocket();
		// �������л��������
		Datas datas = new Datas();
		// ���ô������Ʒ��Ʒ����
		datas.setCommodity(commodity);
		// ���ò�����ʶ
		if(commodity.getNum()<0){
			datas.setFlag(SysConstants.SYS_QUERYUPDATEBYID);
		}else{
			datas.setFlag(SysConstants.SYS_INSERT);
		}
		
		try {
			// ����������ʹ������
			objectOutputStream.writeObject(datas);
			// ��ȡ���������صĴ������
			datas = (Datas) objectInputStream.readObject();
			if (datas.getFlag().equals(SysConstants.SYS_SUCCESS))
				return true;
			else
				return false;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeSocket();
		}
		return false;
	}
}
