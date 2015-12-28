package entity;

import java.io.Serializable;

/**
 * ��Ʒ
 */
public class Commodity implements Serializable{
	private String comId;//��Ʒ���
	private String comName;//��Ʒ����
	private double price;//��Ʒ����
	private String unit;//��λ
	private int num;//��Ʒ���
	
	public Commodity(){
		
	}
	public Commodity(String comId,String comName,double price,String unit,int num){
		this.comId=comId;
		this.comName=comName;
		this.price=price;
		this.unit=unit;
		this.num=num;
	}
	
	public String getComId() {
		return comId;
	}
	public void setComId(String comId) {
		this.comId = comId;
	}
	public String getComName() {
		return comName;
	}
	public void setComName(String comName) {
		this.comName = comName;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
	}	
}
