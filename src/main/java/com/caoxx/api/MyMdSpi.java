package com.caoxx.api;

import static org.hraink.futures.ctp.thostftdcuserapidatatype.ThostFtdcUserApiDataTypeLibrary.THOST_FTDC_AF_Delete;
import static org.hraink.futures.ctp.thostftdcuserapidatatype.ThostFtdcUserApiDataTypeLibrary.THOST_FTDC_CC_Immediately;
import static org.hraink.futures.ctp.thostftdcuserapidatatype.ThostFtdcUserApiDataTypeLibrary.THOST_FTDC_FCC_NotForceClose;
import static org.hraink.futures.ctp.thostftdcuserapidatatype.ThostFtdcUserApiDataTypeLibrary.THOST_FTDC_OPT_LimitPrice;
import static org.hraink.futures.ctp.thostftdcuserapidatatype.ThostFtdcUserApiDataTypeLibrary.THOST_FTDC_TC_GFD;
import static org.hraink.futures.ctp.thostftdcuserapidatatype.ThostFtdcUserApiDataTypeLibrary.THOST_FTDC_VC_AV;

import java.math.BigDecimal;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.swt.widgets.Display;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcDepthMarketDataField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcInputOrderActionField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcInputOrderField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcReqUserLoginField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcRspInfoField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcRspUserLoginField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcSpecificInstrumentField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcUserLogoutField;
import org.hraink.futures.jctp.md.JCTPMdApi;
import org.hraink.futures.jctp.md.JCTPMdSpi;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.caoxx.entity.Instrument;
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
		
		userLoginField.setBrokerID(mainControl.brokenId);
		userLoginField.setUserID(mainControl.investorNo);
		userLoginField.setPassword(mainControl.passwd);
		
//		userLoginField.setBrokerID("9999");
//		userLoginField.setUserID("119835");
//		userLoginField.setPassword("zhang747281");
		
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
		
		
//		subResult = mdApi.subscribeMarketData("cu1809","zn1809","ru1809","ni1809");
		subResult = mdApi.subscribeMarketData("al1812");
		System.out.println(subResult == 0 ? "订阅成功" : "订阅失败");
	}
	
	public double getMinRatio(String instrumentStr){
		double retval = 0;
		if(checkInstruMinValue1(instrumentStr)) {
    		return 1;
		}
		
		if(checkInstruMinValue2(instrumentStr)) {
			
    		return 2;
		}
		
		if(checkInstruMinValue5(instrumentStr)) {
			
    		return 5;
		}
		
		if(checkInstruMinValue10(instrumentStr)) {
			
    		return 10;
		}
		
		if(checkInstruMinValue005(instrumentStr)) {
			
    		return 0.05;
		}
		
		
		
		
		return retval;
	}
	public double getHYMinValue(String instrumentStr){
		double retval = 0;
		if(checkInstruMinValue1(instrumentStr)) {
    		return 1;
		}
		if(checkInstruMinValue2(instrumentStr)) {
    		return 2;
		}
		if(checkInstruMinValue5(instrumentStr)) {
    		return 5;
		}
		if(checkInstruMinValue10(instrumentStr)) {
    		return 10;
		}
		if(checkInstruMinValue005(instrumentStr)) {
			
    		return 0.05;
		}
		
		return retval;
	}
	
	public double checkMinValue(String instrumentStr,double value){
		double retval = 0;
		if(checkInstruMinValue1(instrumentStr)) {
			BigDecimal b = new BigDecimal(value);
    		double f1 = b.setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
    		retval = f1;
    		return retval;
		}
		
		if(checkInstruMinValue2(instrumentStr)) {
			value = value / 2;
			BigDecimal b = new BigDecimal(value);
    		double f1 = b.setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
    		retval = f1  * 2;
    		return retval;
		}
		
		if(checkInstruMinValue5(instrumentStr)) {
			value = value / 5;
			BigDecimal b = new BigDecimal(value);
    		double f1 = b.setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
    		retval = f1 * 5 ;
    		return retval;
		}
		
		if(checkInstruMinValue10(instrumentStr)) {
			value = value / 10;
			BigDecimal b = new BigDecimal(value);
    		double f1 = b.setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
    		retval = f1 * 10 ;
    		return retval;
		}
		
		if(checkInstruMinValue005(instrumentStr)) {
			value = value * 100 / 5;
			BigDecimal b = new BigDecimal(value);
    		double f1 = b.setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
    		retval = f1 / 100 * 5;
    		return retval;
		}
		
		
		
		
		return value;
	}
	
	public boolean checkInstruMinValue1(String instr){
		boolean retFlg = false;
		if(instr.length() == 6){			
			for (String hyName : mainControl.HYnameMinValue1) {
				if(instr.substring(0, 2).equals(hyName)){
					return true;
				}
			}
			
		}
		if(instr.length() == 5){			
			for (String hyName : mainControl.HYnameMinValue1) {
				if(instr.substring(0, 1).equals(hyName)){
					return true;
				}
			}
			
		}
		return retFlg;
	}
	
	public boolean checkInstruMinValue2(String instr){
		boolean retFlg = false;
		if(instr.length() == 6){			
			for (String hyName : mainControl.HYnameMinValue2) {
				if(instr.substring(0, 2).equals(hyName)){
					return true;
				}
			}
			
		}
		if(instr.length() == 5){			
			for (String hyName : mainControl.HYnameMinValue2) {
				if(instr.substring(0, 1).equals(hyName)){
					return true;
				}
			}
			
		}
		return retFlg;
	}
	
	public boolean checkInstruMinValue5(String instr){
		boolean retFlg = false;
		if(instr.length() == 6){			
			for (String hyName : mainControl.HYnameMinValue5) {
				if(instr.substring(0, 2).equals(hyName)){
					return true;
				}
			}
			
		}
		if(instr.length() == 5){			
			for (String hyName : mainControl.HYnameMinValue5) {
				if(instr.substring(0, 1).equals(hyName)){
					return true;
				}
			}
			
		}
		return retFlg;
	}
	
	public boolean checkInstruMinValue10(String instr){
		boolean retFlg = false;
		if(instr.length() == 6){			
			for (String hyName : mainControl.HYnameMinValue10) {
				if(instr.substring(0, 2).equals(hyName)){
					return true;
				}
			}
			
		}
		if(instr.length() == 5){			
			for (String hyName : mainControl.HYnameMinValue10) {
				if(instr.substring(0, 1).equals(hyName)){
					return true;
				}
			}
			
		}
		return retFlg;
	}
	
	public boolean checkInstruMinValue005(String instr){
		boolean retFlg = false;
		if(instr.length() == 6){			
			for (String hyName : mainControl.HYnameMinValue005) {
				if(instr.substring(0, 2).equals(hyName)){
					return true;
				}
			}
			
		}
		if(instr.length() == 5){			
			for (String hyName : mainControl.HYnameMinValue005) {
				if(instr.substring(0, 1).equals(hyName)){
					return true;
				}
			}
			
		}
		return retFlg;
	}

	@Override
	public void onRtnDepthMarketData(CThostFtdcDepthMarketDataField pDepthfutureMarket) {
		if(mainControl != null){
		    
		    String time = StringUtils.rightPad(pDepthfutureMarket.getUpdateTime()+"."+pDepthfutureMarket.getUpdateMillisec(), 12,'0');
		    time = StringUtils.rightPad(time, 20);
		    
		    String instrumentStr = StringUtils.rightPad(pDepthfutureMarket.getInstrumentID(),10).trim();
		    
             Display.getDefault().syncExec(new Runnable() {
                
                @Override
                public void run() {
                	String time = StringUtils.rightPad(pDepthfutureMarket.getUpdateTime()+"."+pDepthfutureMarket.getUpdateMillisec(), 12,'0');
        		    time = StringUtils.rightPad(time, 20);
        		    if(pDepthfutureMarket != null && checkTime(pDepthfutureMarket.getInstrumentID(),pDepthfutureMarket.getUpdateTime())){
        		    	mainControl.console.append(time+instrumentStr + pDepthfutureMarket.getLastPrice() +"\r\n");
                    	mainControl.insertFutureMarket(pDepthfutureMarket);
                    	if(null != mainControl.m20To1Map.get(instrumentStr) ){
                    		Instrument instrument = mainControl.m20To1Map.get(instrumentStr);
                    		if( null != instrument.getMa20To1Value()){
                    			//check最小变动单位
                    			
                    			//得到返回值
                    			double f1 = checkMinValue(instrumentStr,Double.valueOf(instrument.getMa20To1Value()));
//                    			BigDecimal b = new BigDecimal(Double.valueOf(instrument.getMa20To1Value()));
//                        		double f1 = b.setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
                        		
                        		//判断MA20每分钟指标是否变化
                        		if (instrument.getUpdateFlg() == 1 ){
                        			
                        		
                        			if(null == instrument.getStatus() || instrument.getStatus().equals(""))
                        			{
                        				if(pDepthfutureMarket.getLastPrice() > Double.valueOf(instrument.getMa20To1Value())){
                        					//开仓加一跳
                        					f1 = f1 + getHYMinValue(instrumentStr);
        	                    			//买开挂单
        	                    			orderinsert(instrumentStr,"0",f1,'0');
        	                    			
        	                    			instrument.setUpdateFlg(0);
        	                    			instrument.setStatus("1");
        	                    			mainControl.m20To1Map.put(instrumentStr, instrument);
        	                    			
        	                    			
        	                    		} else if(pDepthfutureMarket.getLastPrice() < Double.valueOf(instrument.getMa20To1Value())){
        	                    			//卖开挂单
        	                    			//开仓减一跳
        	                    			f1 = f1 - getHYMinValue(instrumentStr);
        	                    			orderinsert(instrumentStr,"0",f1,'1');
        	                    			instrument.setUpdateFlg(0);
        	                    			instrument.setStatus("1");
        	                    			mainControl.m20To1Map.put(instrumentStr, instrument);
        	                    		}
                        			} else if (instrument.getStatus().equals("1")){
                        				//判断是否有开仓，如果有先撤单
    	                    			logger.info("撤单");
    	                    	        CThostFtdcInputOrderActionField pInputOrderAction = new CThostFtdcInputOrderActionField();
    	                    	        pInputOrderAction.setBrokerID(mainControl.brokenId);
    	                    	        pInputOrderAction.setInvestorID(mainControl.investorNo);
    	                    	        
    	                    	        pInputOrderAction.setOrderRef(instrument.getOrderRef());
    	                    	        pInputOrderAction.setFrontID(instrument.getFrontID());
    	                    	        pInputOrderAction.setSessionID(instrument.getSessionID());

    	                    	        pInputOrderAction.setInstrumentID(instrumentStr);
    	                    	        pInputOrderAction.setActionFlag(THOST_FTDC_AF_Delete);
    	                    	        
    	                    	        
    	                    	        
    	                    	        //orderSysID+exchangeID 当做撤单标识
//    	                    	        pInputOrderAction.setOrderSysID(json.getString("orderSysID"));
//    	                    	        pInputOrderAction.setExchangeID(json.getString("exchangeID"));
    	                    	        
    	                    	        int result = mainControl.traderApi.reqOrderAction(pInputOrderAction, ++mainControl.nRequestID);
    	                    	        
    	                    	        mainControl.getOrderRetText().append("撤单操作|" + instrumentStr + "|" + result +"|"+"\r\n");
    	                    		
    	                    	        if(pDepthfutureMarket.getLastPrice() > Double.valueOf(instrument.getMa20To1Value())){
    		                    			//买开挂单
    	                    	        	//开仓加一跳
                        					f1 = f1 + getHYMinValue(instrumentStr);
    		                    			orderinsert(instrumentStr,"0",f1,'0');
    		                    			
    		                    			instrument.setUpdateFlg(0);
    		                    			instrument.setStatus("1");
    		                    			mainControl.m20To1Map.put(instrumentStr, instrument);
    		                    			
    		                    			
    		                    		} else if(pDepthfutureMarket.getLastPrice() < Double.valueOf(instrument.getMa20To1Value())){
    		                    			//卖开挂单
    		                    			//开仓减一跳
        	                    			f1 = f1 - getHYMinValue(instrumentStr);
    		                    			orderinsert(instrumentStr,"0",f1,'1');
    		                    			instrument.setUpdateFlg(0);
    		                    			instrument.setStatus("1");
    		                    			mainControl.m20To1Map.put(instrumentStr, instrument);
    		                    		}
    	                    		}
    	                    		
                        		}
                        		if(null != instrument.getStatus() && instrument.getStatus().equals("2")){
                            		//合约已成交
                        			//先判断合约盈亏
                        			//得到合约最小系数
                        			double getRatio = getMinRatio(instrumentStr);
                        			if(instrument.getDirection() == '0'){
                        				//买开的场合
                        				if(pDepthfutureMarket.getLastPrice() >= Double.valueOf(instrument.getTradeValue())){
                        					//赚钱 或平 要赚钱 > N点，开始记录最大有利值
                        					if(pDepthfutureMarket.getLastPrice() - Double.valueOf(instrument.getTradeValue()) > mainControl.nProfit * getRatio) {
                        						
                        						//计算出是否是最大有利值
                        						if(instrument.getMaxProfitValue() == 0 || instrument.getMaxProfitValue() < pDepthfutureMarket.getLastPrice()){
                        							//设置最大有利值
                        							instrument.setMaxProfitValue(pDepthfutureMarket.getLastPrice());
                        							//设置最大回调值 默认为30%
//                        							double tempa = instrument.getMaxProfitValue() - (pDepthfutureMarket.getLastPrice() - Double.valueOf(instrument.getTradeValue())) * mainControl.adverseValue * getRatio;
                        							//设置最大回调值 为回调3跳
                        							double tempa = instrument.getMaxProfitValue() - 3 * getHYMinValue(instrumentStr);
                        							instrument.setAdverseValue(tempa);
                        							mainControl.m20To1Map.put(instrumentStr, instrument);
                        						}
                        					}
                        					//如果有最大有利值且 最新价< 最大回调值  做市价平仓处理
                        					if(instrument.getMaxProfitValue() > 0 && pDepthfutureMarket.getLastPrice() < instrument.getAdverseValue()) {
                        						//市价盈利平仓处理
                        						orderinsert(instrumentStr,"1",pDepthfutureMarket.getLowestPrice(),'1');
                        						//参数还原
                        						Instrument Instrumenttemp = new Instrument();
                        						Instrumenttemp.setStatus("");
                        						mainControl.m20To1Map.put(instrumentStr, Instrumenttemp);
                        					}
                        				} else {
                        					// 亏钱
                        					if(Double.valueOf(instrument.getTradeValue()) - pDepthfutureMarket.getLastPrice() > mainControl.pingcangshu  * getRatio){
                        						//下强平 卖平
                        						orderinsert(instrumentStr,"1",pDepthfutureMarket.getLowestPrice(),'1');
                        						//参数还原
                        						Instrument Instrumenttemp = new Instrument();
                        						Instrumenttemp.setStatus("");
                        						mainControl.m20To1Map.put(instrumentStr, Instrumenttemp);
                        					}
                        				}
                        				
                        			} else
                        			{
                        				//卖开的场合
                        				if(pDepthfutureMarket.getLastPrice() <= Double.valueOf(instrument.getTradeValue())){
                        					//赚钱 或平 要赚钱 > N点，开始记录最大有利值
                        					if(Double.valueOf(instrument.getTradeValue()) - pDepthfutureMarket.getLastPrice()  > mainControl.nProfit  * getRatio) {
                        						
                        						//计算出是否是最大有利值
                        						if(instrument.getMaxProfitValue() == 0 || instrument.getMaxProfitValue() > pDepthfutureMarket.getLastPrice()){
                        							//设置最大有利值
                        							instrument.setMaxProfitValue(pDepthfutureMarket.getLastPrice());
                        							//设置最大回调值 默认为30%
//                        							double tempa = instrument.getMaxProfitValue() + (pDepthfutureMarket.getLastPrice() - Double.valueOf(instrument.getTradeValue())) * mainControl.adverseValue * getRatio;
                        							//设置最大回调值 为回调3跳
                        							double tempa = instrument.getMaxProfitValue() + 3 * getHYMinValue(instrumentStr);
                        							instrument.setAdverseValue(tempa);
                        							mainControl.m20To1Map.put(instrumentStr, instrument);
                        						}
                        						
                        					}
                        					
                        					//如果有最大有利值且 最新价< 最大回调值  做市价平仓处理
                        					if(instrument.getMaxProfitValue() > 0 && pDepthfutureMarket.getLastPrice() > instrument.getAdverseValue()) {
                        						//市价盈利平仓处理
                        						orderinsert(instrumentStr,"1",pDepthfutureMarket.getHighestPrice(),'0');
                        						//参数还原
                        						Instrument Instrumenttemp = new Instrument();
                        						Instrumenttemp.setStatus("");
                        						mainControl.m20To1Map.put(instrumentStr, Instrumenttemp);
                        					}
                        				} else {
                        					// 亏钱
                        					if(pDepthfutureMarket.getLastPrice() - Double.valueOf(instrument.getTradeValue())   > mainControl.pingcangshu* getRatio){
                        						//下强平 卖平
                        						orderinsert(instrumentStr,"1",pDepthfutureMarket.getHighestPrice(),'0');
                        						//参数还原
                        						Instrument Instrumenttemp = new Instrument();
                        						Instrumenttemp.setStatus("");
                        						mainControl.m20To1Map.put(instrumentStr, Instrumenttemp);
                        					}
                        				}
                        				
                        			}
                        			
                            	}
                    		}
                    		
                    	} 
                    	
                    	
                    	
                    	if(instrumentStr.equals("m1809")) {
                    		mainControl.setNewm1809(String.valueOf(pDepthfutureMarket.getLastPrice()));
                    		
                    	}
                    	
                    	if(instrumentStr.equals("rb1810")) {
                    		mainControl.setNewrb1810(String.valueOf(pDepthfutureMarket.getLastPrice()));
                    	}
                    	
        		    }
                	
                }
            });
		    
		    
			logger.info("CTP大商    "+time+instrumentStr + pDepthfutureMarket.getLastPrice());
//			mainControl.console.setCaretPosition(mainControl.console.getText().length()); 
		}
	}
	
	public void orderinsert(String instrumentIDStr,String combOffsetFlag,double price,char direction){
		//
        logger.info("下单操作");
        
        //check最小变动单位
		
		int tmpint = mainControl.atomicInteger.incrementAndGet();
		String order = String.valueOf(tmpint);
		//下单操作
		CThostFtdcInputOrderField inputOrderField=new CThostFtdcInputOrderField();
		//期货公司代码
		inputOrderField.setBrokerID(mainControl.brokenId);
		//投资者代码
		inputOrderField.setInvestorID(mainControl.investorNo);
		// 合约代码
		inputOrderField.setInstrumentID(instrumentIDStr);
		///报单引用
		inputOrderField.setOrderRef(order);
		// 用户代码
		inputOrderField.setUserID(mainControl.investorNo);
		// 报单价格条件
		inputOrderField.setOrderPriceType(THOST_FTDC_OPT_LimitPrice);
		// 买卖方向
		inputOrderField.setDirection(direction);
		// 组合开平标志
		if(mainControl.checkSHPosition(instrumentIDStr))
		{
			if (combOffsetFlag.equals("1")) {
				inputOrderField.setCombOffsetFlag("3");
			} else {
				inputOrderField.setCombOffsetFlag(combOffsetFlag);
			}
			
		} else {
			inputOrderField.setCombOffsetFlag(combOffsetFlag);
		}
		
		// 组合投机套保标志
		inputOrderField.setCombHedgeFlag("1");
		// 价格
		inputOrderField.setLimitPrice(price);
		// 数量
		inputOrderField.setVolumeTotalOriginal(1);
		// 有效期类型
		inputOrderField.setTimeCondition(THOST_FTDC_TC_GFD);
		// GTD日期
		inputOrderField.setGTDDate("");
		// 成交量类型
		inputOrderField.setVolumeCondition(THOST_FTDC_VC_AV);
		// 最小成交量
		inputOrderField.setMinVolume(0);
		// 触发条件
		inputOrderField.setContingentCondition(THOST_FTDC_CC_Immediately);
		// 止损价
		inputOrderField.setStopPrice(0);
		// 强平原因
		inputOrderField.setForceCloseReason(THOST_FTDC_FCC_NotForceClose);
		// 自动挂起标志
		inputOrderField.setIsAutoSuspend(0);
		
		mainControl.traderApi.reqOrderInsert(inputOrderField, ++mainControl.nRequestID);
		
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		String dateString = formatter.format(currentTime);
		if(combOffsetFlag.equals("0")){
			//开
			if(direction == '0'){
				//买
				mainControl.getOrderRetText().append("下单操作|" + instrumentIDStr + "|买开|" + String.valueOf(price) +"|"+dateString +"\r\n");
			} else {
				mainControl.getOrderRetText().append("下单操作|" + instrumentIDStr + "|卖开|" + String.valueOf(price) +"|"+dateString +"\r\n");
				
			}
			
		} else {
			//平
			if(direction == '0'){
				//买
				mainControl.getOrderRetText().append("下单操作|" + instrumentIDStr + "|买平|" + String.valueOf(price) +"|"+dateString +"\r\n");
			} else {
				mainControl.getOrderRetText().append("下单操作|" + instrumentIDStr + "|卖平|" + String.valueOf(price) +"|"+dateString +"\r\n");
				
			}
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
	
	public boolean checkTime(String instr,String time){
		
		//9:00 - 10:15 10:30 - 11:30 13:30-15:00 21:00-1:00 ZN CU NI HYnameAM1
		if(checkInstruAM1(instr)){
			String tempTime = time.substring(0,time.length()-2).replace(":", "");
			int intTime = Integer.valueOf(tempTime);
			if((intTime >= 900 && intTime <= 1015) || (intTime >=1030 && intTime <=1130) || (intTime >=1330 && intTime <=1500) || intTime >= 2100 || intTime <=100 ){
				return true;
			}
		}
		
		//9:00 - 10:15 10:30 - 11:30 13:30-15:00 21:00-23:00 RU HYnamePM23
		if(checkInstruPM23(instr)){
			String tempTime = time.substring(0,time.length()-2).replace(":", "");
			int intTime = Integer.valueOf(tempTime);
			if((intTime >= 900 && intTime <= 1015) || (intTime >=1030 && intTime <=1130) || (intTime >=1330 && intTime <=1500) || (intTime >= 2100 && intTime <=2300) ){
				return true;
			}
		}
		
		//9:00 - 10:15 10:30 - 11:30 13:30-15:00 21:00-23:30  HYnamePM2330
		
		if(checkInstruPM2330(instr)){
			String tempTime = time.substring(0,time.length()-2).replace(":", "");
			int intTime = Integer.valueOf(tempTime);
			if((intTime >= 900 && intTime <= 1015) || (intTime >=1030 && intTime <=1130) || (intTime >=1330 && intTime <=1500) || (intTime >= 2100 && intTime <=2330)){
				return true;
			}
		}
		
		//9:00 - 10:15 10:30 - 11:30 13:30-15:00 21:00-2:30  HYnameAM230
		
		if(checkInstruAM230(instr)){
			String tempTime = time.substring(0,time.length()-2).replace(":", "");
			int intTime = Integer.valueOf(tempTime);
			if((intTime >= 900 && intTime <= 1015) || (intTime >=1030 && intTime <=1130) || (intTime >=1330 && intTime <=1500) || intTime >= 2100 || intTime <=230 ){
				return true;
			}
		}
		
		//9:00 - 10:15 10:30 - 11:30 13:30-15:00  HYname000
		if(checkInstru000(instr)){
			String tempTime = instr.substring(0,time.length()-2).replace(":", "");
			int intTime = Integer.valueOf(tempTime);
			if((intTime >= 900 && intTime <= 1015) || (intTime >=1030 && intTime <=1130) || (intTime >=1330 && intTime <=1500) ){
				return true;
			}
		}
		
		
		return false;
	}
	
	public boolean checkInstruAM1(String instr){
		boolean retFlg = false;
		if(instr.length() == 6){			
			for (String hyName : mainControl.HYnameAM1) {
				if(instr.substring(0, 2).equals(hyName)){
					return true;
				}
			}
			
		}
		if(instr.length() == 5){			
			for (String hyName : mainControl.HYnameAM1) {
				if(instr.substring(0, 1).equals(hyName)){
					return true;
				}
			}
			
		}
		return retFlg;
	}
	
	public boolean checkInstruPM23(String instr){
		boolean retFlg = false;
		if(instr.length() == 6){			
			for (String hyName : mainControl.HYnamePM23) {
				if(instr.substring(0, 2).equals(hyName)){
					return true;
				}
			}
			
		}
		if(instr.length() == 5){			
			for (String hyName : mainControl.HYnamePM23) {
				if(instr.substring(0, 1).equals(hyName)){
					return true;
				}
			}
			
		}
		return retFlg;
	}
	
	public boolean checkInstruPM2330(String instr){
		boolean retFlg = false;
		if(instr.length() == 6){			
			for (String hyName : mainControl.HYnamePM2330) {
				if(instr.substring(0, 2).equals(hyName)){
					return true;
				}
			}
			
		}
		if(instr.length() == 5){			
			for (String hyName : mainControl.HYnamePM2330) {
				if(instr.substring(0, 1).equals(hyName)){
					return true;
				}
			}
			
		}
		return retFlg;
	}
	
	public boolean checkInstruAM230(String instr){
		boolean retFlg = false;
		if(instr.length() == 6){			
			for (String hyName : mainControl.HYnameAM230) {
				if(instr.substring(0, 2).equals(hyName)){
					return true;
				}
			}
			
		}
		if(instr.length() == 5){			
			for (String hyName : mainControl.HYnameAM230) {
				if(instr.substring(0, 1).equals(hyName)){
					return true;
				}
			}
			
		}
		return retFlg;
	}
	
	public boolean checkInstru000(String instr){
		boolean retFlg = false;
		if(instr.length() == 6){			
			for (String hyName : mainControl.HYname000) {
				if(instr.substring(0, 2).equals(hyName)){
					return true;
				}
			}
			
		}
		if(instr.length() == 5){			
			for (String hyName : mainControl.HYname000) {
				if(instr.substring(0, 1).equals(hyName)){
					return true;
				}
			}
			
		}
		return retFlg;
	}


}