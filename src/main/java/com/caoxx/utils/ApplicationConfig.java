package com.caoxx.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Properties;

import org.bridj.util.StringUtils;

public class ApplicationConfig {
	
	private static final String APPLICATION_PROPERTY_FILE_NAME = "application.properties";
	
	private static File sFile = new File(APPLICATION_PROPERTY_FILE_NAME);
	
	private static Properties properties;
	
	static {
		
		try {
			if (!sFile.exists()) {
				sFile.createNewFile();
			}
			System.out.println("配置文件路徑："+sFile.getAbsolutePath());
			
			properties = new Properties();
			
			FileInputStream fis = new FileInputStream(sFile);
			
			properties.load(fis);
			
			fis.close();
			
	         //初始化交易地址
            if(getProperty("port") == null || getProperty("port").trim().equals("")){
                setProperty("port", "3398");
            }
			
			/*//初始化交易地址
			if(getProperty("tradeAddr") == null || getProperty("tradeAddr").trim().equals("")){
				setProperty("tradeAddr", "10.0.0.202");
			}
			
			//初始化行情地址
			if(getProperty("marketAddr") == null || getProperty("marketAddr").trim().equals("")){
				setProperty("marketAddr", "10.0.0.204");
			}
			
			//行情前置机
			if(getProperty("tradeFrontAddr") == null || getProperty("tradeFrontAddr").trim().equals("")){
				setProperty("tradeFrontAddr", "tcp://180.169.116.119:41205");
			}*/
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 通过属性名称查询属性值
	 * @param key
	 * @return
	 */
	public static String getProperty(String key){
		
		String value = "";
		try {
			value = properties.getProperty(key);
			
			System.out.println(value);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return value;
	}
	
	
	/**
	 * 设置属性
	 * @param key
	 * @param value
	 */
	public static void setProperty(String key, String value){
		
		//先删除所有
		//removeAllProperties();
		
		
		try {
			properties.setProperty(key, value);
			
			//URL url = UserConfig.class.getClassLoader().getResource(PROPERTY_FILE_NAME);
			
			//String afterDecode = URLDecoder.decode(url.getFile(), "UTF-8");  
			
			//System.out.println(afterDecode);
			//OutputStream out = new FileOutputStream(afterDecode);
			
			OutputStream out = new FileOutputStream(sFile);
			//保存属性
			properties.store(out, "");
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
