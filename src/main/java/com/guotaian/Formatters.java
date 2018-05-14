package com.guotaian;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public class Formatters {
	private SimpleDateFormat sdf = AFileOutBase.getLocalTimeFormat();
	private DecimalFormat df6 = AFileOutBase.getDecimalFormat();
	private DecimalFormat df8 = AFileOutBase.getDecimalFormat8();
	
	public SimpleDateFormat getSdf() {
		return sdf;
	}
	public DecimalFormat getDf6() {
		return df6;
	}
	public DecimalFormat getDf8() {
		return df8;
	}
}
