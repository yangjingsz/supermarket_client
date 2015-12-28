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
	Map<String, Item> itemMap = new HashMap<String, Item>();// 订单
	Socket socket = null; // Scoket实例
	ObjectInputStream objectInputStream = null; // 对象输入流实例
	ObjectOutputStream objectOutputStream = null; // 对象输出流实例

	/**
	 * 实例化Socket相关对象
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
	 * 关闭Socket相关对象
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
	 * 登录
	 * 
	 * @param user
	 *            登录用户
	 * @return 用户对象
	 */
	public User login(User user) {
		// 实例化Socket相关对象
		initSocket();
		// 创建序列化传输对象
		Datas datas = new Datas();
		// 设置操作标识
		datas.setFlag(SysConstants.SYS_LOGIN);
		// 设置传输对象的用户信息
		datas.setUserName(user.getUserName());
		datas.setPassWord(user.getPassWord());
		try {
			// 向服务器发送传输对象
			objectOutputStream.writeObject(datas);
			// 获取服务器返回的传输对象
			datas = (Datas) objectInputStream.readObject();
			if (datas.getRole() != null) {
				if (datas.getRole().equals("storeMgr")) {
					Role storeManager = new StoreManager();// 创建库存管理员角色
					StoreMgr storeMgr = new StoreMgrImpl();// 创建库存管理员权限
					storeManager.setStoreMgr(storeMgr);// 分配权限
					user.setRole(storeManager);// 为用户授权角色
				}
				if (datas.getRole().equals("cashier")) {
					Role cashier = new Cashier();// 创建收银员角色
					Cashiers cashiers = new CashiersImpl();// 创建收银员权限
					cashier.setCashier(cashiers);// 分配权限
					user.setRole(cashier);// 为用户授权角色
				}
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeSocket();
		}
		// 返回
		return user;
	}

	/**
	 * 创建订单
	 * 
	 * @return 订单
	 */
	public Orders createOrders(User user) {
		Orders orders = new Orders();
		orders.setItemMap(itemMap);
		/* 格式化当前时间 */
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
		Date date = new Date();
		String orderId = sdf.format(date);
		orders.setOrderId(orderId);// 设置订单号
		// 设置收银操作员
		orders.setUseId(user.getUserName());
		return orders;
	}

	/**
	 * 扫描商品
	 * 
	 * @param comId
	 */
	public void scanCommodity(String comId) {
		Commodity commodity = queryById(comId);
		if (itemMap.containsKey(comId)) {// 判断是否有些商品项
			System.out.println(itemMap.get(comId).getCommodity().getComName());
			if (updateItemNum(comId, 1)) {
				Item item = itemMap.get(comId);// 得此商品项
				item.setItemNum(item.getItemNum() + 1);// 修改此商品项的数量
				itemMap.put(commodity.getComId(), item);// 将商品项放入订单商品名细
			} else {
				System.out.println("抱歉，库存不够！");
			}
		} else {// 如果没有此商品项，则创建 一个商品项
			Item item = new Item();
			if (updateItemNum(comId, 1)) {
				item.setItemNum(1);
				item.setCommodity(commodity);
				item.setItemPrice(commodity.getPrice());
				item.setItemUnit(commodity.getUnit());
				System.out.println(commodity.getComName());
				itemMap.put(commodity.getComId(), item);// 将商品项放入订单商品名细
			} else {
				System.out.println("抱歉，库存不够！");
			}
		}
	}

	/**
	 * 修改商品项数量
	 * 
	 * @param comId
	 *            商品编号
	 * @param num
	 *            数量
	 */
	public boolean updateItemNum(String comId, int num) {
		// baiyujuan

		if (itemMap.containsKey(comId)) {// 判断是否有些商品项
			Item item = itemMap.get(comId);// 得此商品项
			Commodity commodity=null;
			if (num == 1) {
				commodity= queryById(comId, 1);
			} else {
				commodity = queryById(comId, num
						- item.getCommodity().getNum());
			}
			if (commodity != null) {
				item.setItemNum(num);// 修改此商品项的数量
				itemMap.put(commodity.getComId(), item);// 将商品项放入订单商品名细
			} else {
				System.out.println("库存不足！");
				return false;
			}
		} else {// 如果没有此商品项，则创建 一个商品项
			Commodity commodity = queryById(comId, 1);
			if (commodity != null) {
				Item item = new Item();
				item.setItemNum(1);
				item.setCommodity(commodity);
				item.setItemPrice(commodity.getPrice());
				item.setItemUnit(commodity.getUnit());
				itemMap.put(commodity.getComId(), item);// 将商品项放入订单商品名细
			} else {
				System.out.println("库存不足！");
				return false;
			}
		}
		return true;

	}

	/**
	 * 查询库存商品
	 * 
	 * @return 商品列表
	 */
	public List<Commodity> query() {
		List<Commodity> comList = null;
		// 实例化Socket相关对象
		initSocket();
		// 创建序列化传输对象
		Datas datas = new Datas();
		// 设置操作标识
		datas.setFlag(SysConstants.SYS_QUERY);
		try {
			// 向服务器发送传输对象
			objectOutputStream.writeObject(datas);
			// 获取服务器返回的传输对象
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
	 * 按编号查询商品
	 * 
	 * @param comId
	 *            商品编号
	 * @return 商品
	 */
	public Commodity queryById(String comId) {
		Commodity commodity = null;
		// 实例化Socket相关对象
		initSocket();
		// 创建序列化传输对象
		Datas datas = new Datas();
		// 设置传输的商品编号
		datas.setComId(comId);
		// 设置操作标识
		datas.setFlag(SysConstants.SYS_QUERYBYID);
		try {
			// 向服务器发送传输对象
			objectOutputStream.writeObject(datas);
			// 获取服务器返回的传输对象
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
	 * 按编号查询商品
	 * 
	 * @param comId
	 *            商品编号
	 * @return 商品
	 */
	public Commodity queryById(String comId, int num) {
		Commodity commodity = new Commodity();
		commodity.setComId(comId);
		commodity.setNum(num);
		// 实例化Socket相关对象
		initSocket();
		// 创建序列化传输对象
		Datas datas = new Datas();
		// 设置传输的商品编号
		datas.setComId(comId);
		datas.setCommodity(commodity);
		// 设置操作标识
		datas.setFlag(SysConstants.SYS_QUERYUPDATEBYID);
		try {
			// 向服务器发送传输对象
			objectOutputStream.writeObject(datas);
			// 获取服务器返回的传输对象
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
	 * 入库
	 * 
	 * @param commodity
	 *            商品对象
	 * @return
	 */
	public boolean in(Commodity commodity) {
		// 实例化Socket相关对象
		initSocket();
		// 创建序列化传输对象
		Datas datas = new Datas();
		// 设置传输的商品商品对象
		datas.setCommodity(commodity);
		// 设置操作标识
		if(commodity.getNum()<0){
			datas.setFlag(SysConstants.SYS_QUERYUPDATEBYID);
		}else{
			datas.setFlag(SysConstants.SYS_INSERT);
		}
		
		try {
			// 向服务器发送传输对象
			objectOutputStream.writeObject(datas);
			// 获取服务器返回的传输对象
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
