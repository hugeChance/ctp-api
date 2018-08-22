package com.caoxx.entity;


public class TProduct {

	private int numi;
	private String  str;
	private double numj;
	
	public TProduct(int numi,String str,double numj)
	{
		this.numi = numi;
		this.str = str;
		this.numj = numj;
		
	}

	public int getNumi() {
		return numi;
	}

	public void setNumi(int numi) {
		this.numi = numi;
	}

	public String getStr() {
		return str;
	}

	public void setStr(String str) {
		this.str = str;
	}

	public double getNumj() {
		return numj;
	}

	public void setNumj(double numj) {
		this.numj = numj;
	}
}
