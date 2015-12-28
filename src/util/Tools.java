package util;

import java.io.IOException;
import java.util.Properties;
/**
 * ��ȡproperties�ļ��Ĺ�����
 */
public class Tools {
	private static Properties pro = new Properties();

	/**
	 * ��ȡproperties�����ļ���Ϣ
	 */
	static{
		try {
			pro.load(Tools.class.getClassLoader().getResourceAsStream(SysConstants.SYS_PROPERTYNAME));
		} catch (IOException e) {
			e.printStackTrace(); 
		}
	}
	/**
	 * ����key�õ�value��ֵ
	 */
	public static String getValue(String key)
	{
		return pro.getProperty(key);
	}
}
