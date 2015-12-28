package util;

import java.io.Serializable;
import java.util.List;

import entity.Commodity;



/**
 * ���л��������
 * �ڿͻ������������֮�䴫�ݵ����ݶ���
 * 
 */
public class Datas implements Serializable {
	private String flag;// ���ݵı�ʶ
	private String role;//�û���ɫ
	private String userName;//�û���
	private String passWord;//����
	private String comId;//��Ʒ���
	private Commodity commodity;// ��Ʒ����
	private List<Commodity> comList;//��Ʒ����
	public Datas() {
	}
	
	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}
	
	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Commodity getCommodity() {
		return commodity;
	}

	public void setCommodity(Commodity commodity) {
		this.commodity = commodity;
	}

	public List<Commodity> getComList() {
		return comList;
	}

	public void setComList(List<Commodity> comList) {
		this.comList = comList;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	public String getComId() {
		return comId;
	}

	public void setComId(String comId) {
		this.comId = comId;
	}	
	
}
