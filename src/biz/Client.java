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
	// 控制台输入对象
	private static Scanner input = new Scanner(System.in);
	// 客户端业务对象
	private static ClientBiz clientBiz = new ClientBiz();

	public static void main(String[] args) {
		System.out.println("***********欢迎使用XX超市管理系统***********");
		System.out.println("1 .登陆\n2. 退出系统");
		System.out.println("*****************请选择数字1/2*******************");
		String num = input.nextLine();
		Client client = new Client();
		if (num.equals("1")) {
			// 调用登录视图
			client.createLoginView();
		}
		System.out.println("系统终止运行");
	}

	/**
	 * 登录视图
	 */
	public void createLoginView() {
		while (true) {
			System.out.print("用户名称：");
			String userName = input.nextLine();
			System.out.print("用户密码：");
			String passWord = input.nextLine();
			User user = new User();
			user.setUserName(userName);
			user.setPassWord(passWord);
			// 调用登录业务
			User nUser = clientBiz.login(user);
			if (nUser == null) {
				// 出错，继续登陆
				System.out.println("用户名或者密码输入错误！登录失败！");

			} else {
				if (nUser.getRole() instanceof StoreManager) {// 如果是库存管理员
					System.out.println("*****欢迎登录XX超市库存管理系统*****");
					doStoreMgr(user);// 执行库存管理员操作
				} else if (user.getRole() instanceof Cashier) {// 如果是收银员
					System.out.println("*****欢迎登录XX超市收银系统*****");
					doCashier(user);// 执行收银操作
				}
				break;
			}
		}
	}

	/**
	 * 库存管理员界面
	 * 
	 * @param user
	 *            用户对象
	 */
	public void doStoreMgr(User user) {
		System.out
				.println("请选择进行的操作：1.商品入库     2.商品出库     3.新增商品   4.查询全部商品     5.按查询号查询商品     6.退出");
		int codeId = Integer.parseInt(input.nextLine());
		try {
			switch (codeId) {
			case 1:
				// 商品入库
				if (in())
					System.out.println("入库成功！");
				else
					System.out.println("入库失败！");
				doStoreMgr(user);// 再次调用库管操作
				break;
			case 2:
				// 商品出库
				if (out())
					System.out.println("出库成功！");
				else
					System.out.println("出库失败！");
				doStoreMgr(user);// 再次调用库管操作
				break;
			case 3:
				// 新增商品
				Commodity commodity = inCommodityInfo();
				if (save(commodity))
					System.out.println("入库成功！");
				else
					System.out.println("入库失败！");
				doStoreMgr(user);// 再次调用库管操作
				break;
			case 4:
				List<Commodity> comList = clientBiz.query();
				queryAll(comList);
				doStoreMgr(user);// 再次调用库管操作
				break;
			case 5:
				// 按查询号查询商品
				queryById();
				doStoreMgr(user);// 再次调用库管操作
				break;
			case 6:
				System.out.println("谢谢使用！");
				return;
			default:
				throw new Exception();
			}
		} catch (Exception e) {
			System.out.println("请输入正确格式的数字");
			doStoreMgr(user);
		}
	}

	public void queryAll(List<Commodity> comList) {
		if (comList.size() == 0)
			System.out.println("库存没有商品！");
		else {
			System.out.println("商品编号\t\t商品名称\t\t商品单价\t\t单位\t\t数量");
			for (Commodity commodity : comList) {
				System.out.println(commodity.getComId() + "\t\t"
						+ commodity.getComName() + "\t\t"
						+ commodity.getPrice() + "\t\t" + commodity.getUnit()
						+ "\t\t" + commodity.getNum());
			}
		}
	}

	/**
	 * 收银员界面
	 * 
	 * @param user
	 *            用户对象
	 */
	public void doCashier(User user) {
		System.out.println("请选择进行的操作：1.扫描商品    2.修改数量   3.打印账单     4.退出");
		int codeId = Integer.parseInt(input.nextLine());
		try {
			switch (codeId) {
			case 1:
				// 扫描商品
				System.out.print("输入要扫描的商品编号：");
				String comId = input.nextLine();
				clientBiz.scanCommodity(comId);				
				doCashier(user);// 再次调用库管操作
				break;
			case 2:
				// 修改数量
				System.out.print("输入要扫描的商品编号：");
				String commId = input.nextLine();
				System.out.print("输入修改数量：");
				int num = Integer.parseInt(input.nextLine());
				clientBiz.updateItemNum(commId, num);
			case 3:
				// 打印账单
				Orders orders = clientBiz.createOrders(user);
				prinOrders(orders);
				doCashier(user);// 再次调用库管操作
				break;
			case 4:
				System.out.println("谢谢使用！");
				return;
			default:
				throw new Exception();
			}
		} catch (Exception e) {
			System.out.println("请输入正确格式的数字");
			doCashier(user);
		}
	}

	/**
	 * 打印账单
	 */
	public void prinOrders(Orders orders) {
		System.out.println("\t\t\t\t世纪华联超市");
		System.out.println("收银员号：" + orders.getUseId() + "\t小票号："
				+ orders.getOrderId());
		Map<String, Item> itemMap = clientBiz.itemMap;
		Iterator it = itemMap.keySet().iterator();
		int i = 1;// 商品序号
		double totalMoney = 0;// 总金额
		int count = 0;
		System.out.println("序\t商品名称\t\t数量\t单价\t金额");
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
		System.out.println("\t合计：\t\t" + count + "\t\t" + totalMoney);
	}

	/**
	 * 新增商品界面
	 * 
	 * @return 商品对象
	 */
	public Commodity inCommodityInfo() {
		System.out.print("输入商品编号：");
		String comId = input.nextLine();
		Commodity commodity = clientBiz.queryById(comId);
		if (commodity == null) {
			commodity = new Commodity();
			commodity.setComId(comId);
			System.out.print("输入商品名称：");
			commodity.setComName(input.nextLine());
			System.out.print("输入商品价格：");
			commodity.setPrice(Double.parseDouble(input.nextLine()));
			System.out.print("输入商品单位：");
			commodity.setUnit(input.nextLine());
			System.out.print("输入商品数量：");
			commodity.setNum(Integer.parseInt(input.nextLine()));
		} else {
			System.out.println("此编号已存在！");
			commodity = inCommodityInfo();
		}
		return commodity;
	}

	/**
	 * 新增商品
	 * 
	 * @param commodity
	 * @return
	 */
	public boolean save(Commodity commodity) {
		return clientBiz.in(commodity);
	}

	/**
	 * 入库
	 * 
	 * @param comId
	 * @return
	 */
	public boolean in() {
		System.out.print("输入商品编号：");
		Commodity commodity = clientBiz.queryById(input.nextLine());
		if (commodity == null) {
			System.out.println("没有此商品，请选择编号3：新增商品！");
			return false;
		} else {
			System.out.print("入库数量：");
			int num = Integer.parseInt(input.nextLine());
			if (num > 0) {
				commodity.setNum(num);
				return clientBiz.in(commodity);
			} else {
				System.out.print("请正确输入数量！");
				in();
			}
			return false;
		}
	}

	/**
	 * 出库
	 * 
	 * @param comId
	 * @return
	 */
	public boolean out() {
		System.out.print("输入商品编号：");
		Commodity commodity = clientBiz.queryById(input.nextLine());
		if (commodity == null) {
			System.out.println("没有此商品！");
			return false;
		} else {
			System.out.print("出库数量：");
			int num = Integer.parseInt(input.nextLine());
			if (num > 0) {
				commodity.setNum(-num);
				return clientBiz.in(commodity);
			} else {
				System.out.print("请正确输入数量！");
				out();
			}
			return false;
		}
	}

	public void queryById() {
		System.out.print("输入商品编号：");
		Commodity commodity = clientBiz.queryById(input.nextLine());
		if (commodity == null) {
			System.out.println("对不起，没有此商品！");
		} else {
			System.out.println("商品编号\t\t商品名称\t\t商品单价\t\t单位\t\t数量");
			System.out.println(commodity.getComId() + "\t\t"
					+ commodity.getComName() + "\t\t" + commodity.getPrice()
					+ "\t\t" + commodity.getUnit() + "\t\t"
					+ commodity.getNum());
		}
	}
}
