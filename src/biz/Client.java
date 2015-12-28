package biz;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import role.impl.Cashier;
import role.impl.StoreManager;
import entity.Commodity;
import entity.Item;
import entity.Orders;
import entity.User;

public class Client {
	// ����̨�������
	private static Scanner input = new Scanner(System.in);
	// �ͻ���ҵ�����
	private static ClientBiz clientBiz = new ClientBiz();

	public static void main(String[] args) {
		System.out.println("***********��ӭʹ��XX���й���ϵͳ***********");
		System.out.println("1 .��½\n2. �˳�ϵͳ");
		System.out.println("*****************��ѡ������1/2*******************");
		String num = input.nextLine();
		Client client = new Client();
		if (num.equals("1")) {
			// ���õ�¼��ͼ
			client.createLoginView();
		}
		System.out.println("ϵͳ��ֹ����");
	}

	/**
	 * ��¼��ͼ
	 */
	public void createLoginView() {
		while (true) {
			System.out.print("�û����ƣ�");
			String userName = input.nextLine();
			System.out.print("�û����룺");
			String passWord = input.nextLine();
			User user = new User();
			user.setUserName(userName);
			user.setPassWord(passWord);
			// ���õ�¼ҵ��
			User nUser = clientBiz.login(user);
			if (nUser == null) {
				// ����������½
				System.out.println("�û�����������������󣡵�¼ʧ�ܣ�");

			} else {
				if (nUser.getRole() instanceof StoreManager) {// ����ǿ�����Ա
					System.out.println("*****��ӭ��¼XX���п�����ϵͳ*****");
					doStoreMgr(user);// ִ�п�����Ա����
				} else if (user.getRole() instanceof Cashier) {// ���������Ա
					System.out.println("*****��ӭ��¼XX��������ϵͳ*****");
					doCashier(user);// ִ����������
				}
				break;
			}
		}
	}

	/**
	 * ������Ա����
	 * 
	 * @param user
	 *            �û�����
	 */
	public void doStoreMgr(User user) {
		System.out
				.println("��ѡ����еĲ�����1.��Ʒ���     2.��Ʒ����     3.������Ʒ   4.��ѯȫ����Ʒ     5.����ѯ�Ų�ѯ��Ʒ     6.�˳�");
		int codeId = Integer.parseInt(input.nextLine());
		try {
			switch (codeId) {
			case 1:
				// ��Ʒ���
				if (in())
					System.out.println("���ɹ���");
				else
					System.out.println("���ʧ�ܣ�");
				doStoreMgr(user);// �ٴε��ÿ�ܲ���
				break;
			case 2:
				// ��Ʒ����
				if (out())
					System.out.println("����ɹ���");
				else
					System.out.println("����ʧ�ܣ�");
				doStoreMgr(user);// �ٴε��ÿ�ܲ���
				break;
			case 3:
				// ������Ʒ
				Commodity commodity = inCommodityInfo();
				if (save(commodity))
					System.out.println("���ɹ���");
				else
					System.out.println("���ʧ�ܣ�");
				doStoreMgr(user);// �ٴε��ÿ�ܲ���
				break;
			case 4:
				List<Commodity> comList = clientBiz.query();
				queryAll(comList);
				doStoreMgr(user);// �ٴε��ÿ�ܲ���
				break;
			case 5:
				// ����ѯ�Ų�ѯ��Ʒ
				queryById();
				doStoreMgr(user);// �ٴε��ÿ�ܲ���
				break;
			case 6:
				System.out.println("ллʹ�ã�");
				return;
			default:
				throw new Exception();
			}
		} catch (Exception e) {
			System.out.println("��������ȷ��ʽ������");
			doStoreMgr(user);
		}
	}

	public void queryAll(List<Commodity> comList) {
		if (comList.size() == 0)
			System.out.println("���û����Ʒ��");
		else {
			System.out.println("��Ʒ���\t\t��Ʒ����\t\t��Ʒ����\t\t��λ\t\t����");
			for (Commodity commodity : comList) {
				System.out.println(commodity.getComId() + "\t\t"
						+ commodity.getComName() + "\t\t"
						+ commodity.getPrice() + "\t\t" + commodity.getUnit()
						+ "\t\t" + commodity.getNum());
			}
		}
	}

	/**
	 * ����Ա����
	 * 
	 * @param user
	 *            �û�����
	 */
	public void doCashier(User user) {
		System.out.println("��ѡ����еĲ�����1.ɨ����Ʒ    2.�޸�����   3.��ӡ�˵�     4.�˳�");
		int codeId = Integer.parseInt(input.nextLine());
		try {
			switch (codeId) {
			case 1:
				// ɨ����Ʒ
				System.out.print("����Ҫɨ�����Ʒ��ţ�");
				String comId = input.nextLine();
				clientBiz.scanCommodity(comId);				
				doCashier(user);// �ٴε��ÿ�ܲ���
				break;
			case 2:
				// �޸�����
				System.out.print("����Ҫɨ�����Ʒ��ţ�");
				String commId = input.nextLine();
				System.out.print("�����޸�������");
				int num = Integer.parseInt(input.nextLine());
				clientBiz.updateItemNum(commId, num);
			case 3:
				// ��ӡ�˵�
				Orders orders = clientBiz.createOrders(user);
				prinOrders(orders);
				doCashier(user);// �ٴε��ÿ�ܲ���
				break;
			case 4:
				System.out.println("ллʹ�ã�");
				return;
			default:
				throw new Exception();
			}
		} catch (Exception e) {
			System.out.println("��������ȷ��ʽ������");
			doCashier(user);
		}
	}

	/**
	 * ��ӡ�˵�
	 */
	public void prinOrders(Orders orders) {
		System.out.println("\t\t\t\t���ͻ�������");
		System.out.println("����Ա�ţ�" + orders.getUseId() + "\tСƱ�ţ�"
				+ orders.getOrderId());
		Map<String, Item> itemMap = clientBiz.itemMap;
		Iterator it = itemMap.keySet().iterator();
		int i = 1;// ��Ʒ���
		double totalMoney = 0;// �ܽ��
		int count = 0;
		System.out.println("��\t��Ʒ����\t\t����\t����\t���");
		System.out
				.println("---------------------------------------------------");
		while (it.hasNext()) {
			Item item = itemMap.get(it.next());
			System.out.println(i + "\t" + item.getCommodity().getComName()
					+ item.getItemUnit() + "\t" + item.getItemNum() + "\t"
					+ item.getItemPrice() + "\t" + item.getItemNum()
					* item.getItemPrice());
			i++;
			totalMoney += item.getItemNum() * item.getItemPrice();
			count += item.getItemNum();
		}
		System.out
				.println("---------------------------------------------------");
		System.out.println("\t�ϼƣ�\t\t" + count + "\t\t" + totalMoney);
	}

	/**
	 * ������Ʒ����
	 * 
	 * @return ��Ʒ����
	 */
	public Commodity inCommodityInfo() {
		System.out.print("������Ʒ��ţ�");
		String comId = input.nextLine();
		Commodity commodity = clientBiz.queryById(comId);
		if (commodity == null) {
			commodity = new Commodity();
			commodity.setComId(comId);
			System.out.print("������Ʒ���ƣ�");
			commodity.setComName(input.nextLine());
			System.out.print("������Ʒ�۸�");
			commodity.setPrice(Double.parseDouble(input.nextLine()));
			System.out.print("������Ʒ��λ��");
			commodity.setUnit(input.nextLine());
			System.out.print("������Ʒ������");
			commodity.setNum(Integer.parseInt(input.nextLine()));
		} else {
			System.out.println("�˱���Ѵ��ڣ�");
			commodity = inCommodityInfo();
		}
		return commodity;
	}

	/**
	 * ������Ʒ
	 * 
	 * @param commodity
	 * @return
	 */
	public boolean save(Commodity commodity) {
		return clientBiz.in(commodity);
	}

	/**
	 * ���
	 * 
	 * @param comId
	 * @return
	 */
	public boolean in() {
		System.out.print("������Ʒ��ţ�");
		Commodity commodity = clientBiz.queryById(input.nextLine());
		if (commodity == null) {
			System.out.println("û�д���Ʒ����ѡ����3��������Ʒ��");
			return false;
		} else {
			System.out.print("���������");
			int num = Integer.parseInt(input.nextLine());
			if (num > 0) {
				commodity.setNum(num);
				return clientBiz.in(commodity);
			} else {
				System.out.print("����ȷ����������");
				in();
			}
			return false;
		}
	}

	/**
	 * ����
	 * 
	 * @param comId
	 * @return
	 */
	public boolean out() {
		System.out.print("������Ʒ��ţ�");
		Commodity commodity = clientBiz.queryById(input.nextLine());
		if (commodity == null) {
			System.out.println("û�д���Ʒ��");
			return false;
		} else {
			System.out.print("����������");
			int num = Integer.parseInt(input.nextLine());
			if (num > 0) {
				commodity.setNum(-num);
				return clientBiz.in(commodity);
			} else {
				System.out.print("����ȷ����������");
				out();
			}
			return false;
		}
	}

	public void queryById() {
		System.out.print("������Ʒ��ţ�");
		Commodity commodity = clientBiz.queryById(input.nextLine());
		if (commodity == null) {
			System.out.println("�Բ���û�д���Ʒ��");
		} else {
			System.out.println("��Ʒ���\t\t��Ʒ����\t\t��Ʒ����\t\t��λ\t\t����");
			System.out.println(commodity.getComId() + "\t\t"
					+ commodity.getComName() + "\t\t" + commodity.getPrice()
					+ "\t\t" + commodity.getUnit() + "\t\t"
					+ commodity.getNum());
		}
	}
}
