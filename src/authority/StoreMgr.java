package authority;

import java.util.List;

import entity.Commodity;



/**
 * ������ԱȨ��
 */
public interface StoreMgr {
	public void out(String comId,int num);//����
	public void in(Commodity commodity);//���
	public List query();//��ѯ���	
	public Commodity queryById(String comId);//����Ų�ѯ��Ʒ
}