package com.caoxx.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatterUtil {
	
	public static String getCurrentDateStr(){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		return simpleDateFormat.format(new Date());
	}
	
	
	public static String getDateStr(Date date){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		return simpleDateFormat.format(date);
	}

}
