package authority.impl;

import authority.Cashiers;
import biz.ClientBiz;
import entity.Orders;
import entity.User;

public class CashiersImpl implements Cashiers {

	@Override
	public Orders createOrders(User user) {
		ClientBiz clientBiz=new ClientBiz();
		return clientBiz.createOrders(user);
	}

	@Override
	public void scanCommodity(String comId) {
		ClientBiz clientBiz=new ClientBiz();
		clientBiz.scanCommodity(comId);
	}
}
