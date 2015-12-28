package authority;

import java.util.List;

import entity.Commodity;



/**
 * 库存管理员权限
 */
public interface StoreMgr {
	public void out(String comId,int num);//出库
	public void in(Commodity commodity);//入库
	public List query();//查询库存	
	public Commodity queryById(String comId);//按编号查询商品
}