package com.guotaian;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.gta.qts.c2j.adaptee.structure.BuySellLevelInfo;
import com.gta.qts.c2j.adaptee.structure.BuySellLevelInfo3;
import com.gta.qts.c2j.adaptee.structure.SZSE_BuySellLevelInfo3;

public abstract class AFileOutBase {
	public static final String seperator = System.getProperty("file.separator");
	public static final String path = System.getProperty("user.dir");
	public static boolean isCsv = false;//.csv or .txt
	public static final String TAB = isCsv ? "," : "\t";
	public static final String fileDir = "JavaFileData" + seperator + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	public static boolean stopping;
	
	public static String getFileHHmm(Long yyyyMMddHHmmssMMM, int freq) {
		if (freq == 0) {
			return new SimpleDateFormat("HHmm").format(new Date());
		}
		
		String timeStr = null;
		if (yyyyMMddHHmmssMMM == null || yyyyMMddHHmmssMMM == 0L) {
			timeStr = new SimpleDateFormat("yyyyMMddHHmmssMMM").format(new Date());
		}
		else {
			timeStr = String.valueOf(yyyyMMddHHmmssMMM);
		}
		
		int currHour = Integer.parseInt(timeStr.substring(8, 10));
		int currMinute = Integer.parseInt(timeStr.substring(10, 12));
		int currentMinutes = currHour * 60 + currMinute;
		int splitMinutes = (currentMinutes / freq) * freq;
		int splitHour = splitMinutes / 60;
		int splitMinute = splitMinutes % 60;
		
		return (splitHour < 10 ? "0" : "") + splitHour + (splitMinute < 10 ? "0" : "") + splitMinute;
	}
	
	public static <T> String getFileName(Class<?> clazz, String HHmm) {
		String yyyyMMdd = new SimpleDateFormat("yyyyMMdd").format(new Date());
		return clazz.getSimpleName()+"_"+yyyyMMdd+"_"+HHmm+"_Base"+(isCsv ? ".csv" : ".txt");
	}
	
	public static <T> String getSnapFileName(Class<?> clazz) {
		String date = new SimpleDateFormat("yyyyMMdd_HHmmssSSS").format(new Date());
		return clazz.getSimpleName()+"_"+date+"_Base_snap"+(isCsv ? ".csv" : ".txt");
	}
	
	public static <T> String getSimpleListFileName(String prefix) {
		String date = new SimpleDateFormat("yyyyMMdd_HHmmssSSS").format(new Date());
		return prefix+"_"+date+"_Base"+(isCsv ? ".csv" : ".txt");
	}
	
	public static void createFolder() {
		File dirDirectory= new File(path+seperator+fileDir+seperator) ;
		if(!dirDirectory.exists()) {
			 dirDirectory.mkdirs() ;
		}
	}
	
	public static File CreateFile(String fileName){
		File dirDirectory= new File(path+seperator+fileDir+seperator) ;
		if(!dirDirectory.exists()) {
			 dirDirectory.mkdirs() ;
		}
		String filePath = path+seperator+fileDir+seperator+fileName;
//		System.out.println("CreateFile "+filePath);
	    File file= new File(filePath) ;
			if(!file.exists()){
				try {
					file.createNewFile();
					return file ;
				} catch (IOException e) {
					e.printStackTrace();
			}
		   }else{
			   return file ;
		   }
			return null ;
	}

	public static String byteArr2StringAndTrim(byte[] bytes){
		try{
			return new String(bytes,0,bytes.length,"UTF-8").trim();
		}catch(UnsupportedEncodingException e){
			e.printStackTrace();
			return null ;
		}
	}
	
	public static DecimalFormat getDecimalFormat() {
		DecimalFormat df = new DecimalFormat("#.######");
		df.setMinimumFractionDigits(6);
		df.setMaximumFractionDigits(6);
		df.setRoundingMode(RoundingMode.DOWN);
		return df;
	}

	public static DecimalFormat getDecimalFormat8() {
		DecimalFormat df = new DecimalFormat("#.########");
		df.setMinimumFractionDigits(8);
		df.setMaximumFractionDigits(8);
		df.setRoundingMode(RoundingMode.DOWN);
		return df;
	}
	
	public static String unsigned2String(int v) {
		return String.valueOf((long)(0xFFFFFFFF & v));
	}
	
	public static String double2String(DecimalFormat df, double fieldVal) {
		if (df == null) {
			df = getDecimalFormat();
		}
		return df.format(fieldVal);
	}

	public static String double2String8(DecimalFormat df, double fieldVal) {
		if (df == null) {
			df = getDecimalFormat8();
		}
		return df.format(fieldVal);
	}
	
	public static String levelInfo2StringWithTab(DecimalFormat df, BuySellLevelInfo[] fieldVal) {
		StringBuilder sb = new StringBuilder();
		BuySellLevelInfo info = null;
		for(int i=0;i<fieldVal.length;i++){
			info = fieldVal[i];
			sb.append(double2String(df,info.Price)).append(TAB);
			sb.append(info.Volume).append(TAB);
		}
		return sb.toString();
	}

	public static String levelInfo3ToStringWithTab(DecimalFormat df, BuySellLevelInfo3[] fieldVal) {
		StringBuilder sb = new StringBuilder();
		BuySellLevelInfo3 info = null;
		for(int i=0;i<fieldVal.length;i++){
			info = fieldVal[i];
			sb.append(double2String(df,info.Price)).append(TAB);
			sb.append(info.Volume).append(TAB);
			sb.append(info.TotalOrderNo).append(TAB);
		}
		return sb.toString();
	}

	public static String szseLevelInfo3ToStringWithTab(DecimalFormat df, SZSE_BuySellLevelInfo3[] fieldVal) {
		StringBuilder sb = new StringBuilder();
		SZSE_BuySellLevelInfo3 info = null;
		for(int i=0;i<fieldVal.length;i++){
			info = fieldVal[i];
			sb.append(double2String(df,info.Price)).append(TAB);
			sb.append(double2String(df,info.Volume)).append(TAB);
			sb.append(info.TotalOrderNo).append(TAB);
		}
		return sb.toString();
	}

	public static BufferedWriter createWriter(String fileName, String title) {
		BufferedWriter bw = null;
		try {
			File f = CreateFile(fileName);
			FileOutputStream fos = new FileOutputStream(f,true);
			if (f.length() < 1) {
				byte[] bom = new byte[]{(byte)0xEF,(byte)0xBB,(byte)0xBF};
				fos.write(bom);
			}
			bw = new BufferedWriter(new OutputStreamWriter(fos,"UTF-8")) ;
			if (title != null) {
				bw.write(title);
				bw.newLine();
				bw.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bw;
	}

	public static SimpleDateFormat getLocalTimeFormat() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	}
	
	public static void closeWriter(BufferedWriter bw) {
		if (bw != null) {
			try {
				bw.flush();
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			bw = null;
		}
	}
	
	public static String getLocalTimeTitle() {
		return getLocalTimeFormat().format(new Date());
	}

	public static boolean isStopping() {
		return stopping;
	}

	public static void setStopping(boolean stopping) {
		AFileOutBase.stopping = stopping;
	}
}
