package com.caoxx.api;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;

import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcDepthMarketDataField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcReqUserLoginField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcRspInfoField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcRspUserLoginField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcSpecificInstrumentField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcUserLogoutField;
import org.hraink.futures.jctp.md.JCTPMdApi;
import org.hraink.futures.jctp.md.JCTPMdSpi;


import com.alibaba.fastjson.JSON;
import com.caoxx.swt.mainControl;



public class MyMdSpi extends JCTPMdSpi {
	
	static Logger logger = Logger.getLogger(MyMdSpi.class); 
	
	private JCTPMdApi mdApi;
	

	
	private mainControl mainControl;
	
	public MyMdSpi(JCTPMdApi mdApi) {
		this.mdApi = mdApi;
	}
	
	public MyMdSpi(JCTPMdApi mdApi, mainControl mainControl) {
		this.mdApi = mdApi;
		this.mainControl = mainControl;
	}
	
	@Override
	public void onFrontConnected() {
		System.out.println("准备登陆");
		//登陆
		CThostFtdcReqUserLoginField userLoginField = new CThostFtdcReqUserLoginField();
//		userLoginField.setBrokerID("6000");
//		userLoginField.setUserID("80509729");
//		userLoginField.setPassword("80509729");
		logger.info("准备登陆");
		/*userLoginField.setBrokerID("4080");
		userLoginField.setUserID("86001017");
		userLoginField.setPassword("074014");*/
		
		userLoginField.setBrokerID("9999");
		userLoginField.setUserID("090985");
		userLoginField.setPassword("caojiactp");
		
		mdApi.reqUserLogin(userLoginField, 112);
		System.out.println("登陆完成");
	}
	
	@Override
	public void onRspUserLogin(CThostFtdcRspUserLoginField pRspUserLogin, CThostFtdcRspInfoField pRspInfo, int nRequestID,
			boolean bIsLast) {
		System.out.println("登录回调");
		System.out.println(pRspUserLogin.getLoginTime());
		//订阅
		int subResult = -1;
		
	
		
		
		//logger.debug("读取文件合约信息："+JSON.toJSONString(ss));
		
		
		subResult = mdApi.subscribeMarketData("i1809","i1901");
		System.out.println(subResult == 0 ? "订阅成功" : "订阅失败");
	}

	@Override
	public void onRtnDepthMarketData(CThostFtdcDepthMarketDataField pDepthMarketData) {
		if(mainControl != null){
		    
		    String time = StringUtils.rightPad(pDepthMarketData.getUpdateTime()+"."+pDepthMarketData.getUpdateMillisec(), 12,'0');
		    time = StringUtils.rightPad(time, 20);
		    
		    String instrument = StringUtils.rightPad(pDepthMarketData.getInstrumentID(),10);
		    mainControl.console.append(time+instrument + pDepthMarketData.getLastPrice() +"\r\n");
			logger.info("CTP大商    "+time+instrument + pDepthMarketData.getLastPrice());
//			mainControl.console.setCaretPosition(mainControl.console.getText().length()); 
		}
	}
//	
	@Override
	public void onRspSubMarketData(CThostFtdcSpecificInstrumentField pSpecificInstrument, CThostFtdcRspInfoField pRspInfo, int nRequestID,
			boolean bIsLast) {
		
		System.out.println("订阅回报:" + bIsLast +" : "+ pRspInfo.getErrorID()+":"+pRspInfo.getErrorMsg());
		System.out.println("InstrumentID:" + pSpecificInstrument.getInstrumentID());
	}
	
	@Override
	public void onHeartBeatWarning(int nTimeLapse) {
	}
	
	@Override
	public void onFrontDisconnected(int nReason) {
	}
	
	@Override
	public void onRspError(CThostFtdcRspInfoField pRspInfo, int nRequestID,
			boolean bIsLast) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void onRspUnSubMarketData(
			CThostFtdcSpecificInstrumentField pSpecificInstrument,
			CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void onRspUserLogout(CThostFtdcUserLogoutField pUserLogout,
			CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
		// TODO Auto-generated method stub
	}


}