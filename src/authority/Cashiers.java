package authority;

import entity.Orders;
import entity.User;


/**
 * ����ԱȨ��
 */
public interface Cashiers {
	public Orders createOrders(User user);//��������
	public void scanCommodity(String comId);//ɨ����Ʒ
}
