package com.caoxx.entity;

public class Instrument {

	private String instrumentId;
	
	private String ma20To1Value;
	
	public String getInstrumentId() {
		return instrumentId;
	}

	public void setInstrumentId(String instrumentId) {
		this.instrumentId = instrumentId;
	}

	public String getMa20To1Value() {
		return ma20To1Value;
	}

	public void setMa20To1Value(String ma20To1Value) {
		this.ma20To1Value = ma20To1Value;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	private String status;
	
	private int updateFlg;

	public int getUpdateFlg() {
		return updateFlg;
	}

	public String getOrderRef() {
		return orderRef;
	}

	public void setOrderRef(String orderRef) {
		this.orderRef = orderRef;
	}

	public int getFrontID() {
		return frontID;
	}

	public void setFrontID(int frontID) {
		this.frontID = frontID;
	}

	public int getSessionID() {
		return SessionID;
	}

	public void setSessionID(int sessionID) {
		SessionID = sessionID;
	}

	public void setUpdateFlg(int updateFlg) {
		this.updateFlg = updateFlg;
	}
	
	private String orderRef;
	
	private int frontID;
	
	private int SessionID;
	
	private String tradeValue;

	public String getTradeValue() {
		return tradeValue;
	}

	public void setTradeValue(String tradeValue) {
		this.tradeValue = tradeValue;
	} 
	
	private char direction;

	public char getDirection() {
		return direction;
	}

	public void setDirection(char direction) {
		this.direction = direction;
	}
	
	private double maxProfitValue;

	public double getMaxProfitValue() {
		return maxProfitValue;
	}

	public void setMaxProfitValue(double maxProfitValue) {
		this.maxProfitValue = maxProfitValue;
	}
	
	private double adverseValue;

	public double getAdverseValue() {
		return adverseValue;
	}

	public void setAdverseValue(double adverseValue) {
		this.adverseValue = adverseValue;
	}
	
}
