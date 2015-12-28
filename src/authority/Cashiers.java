package authority;

import entity.Orders;
import entity.User;


/**
 * 收银员权限
 */
public interface Cashiers {
	public Orders createOrders(User user);//创建订单
	public void scanCommodity(String comId);//扫描商品
}
