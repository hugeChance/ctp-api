package com.caoxx.api;


import static org.hraink.futures.ctp.thostftdcuserapidatatype.ThostFtdcUserApiDataTypeLibrary.THOST_FTDC_CC_Immediately;
import static org.hraink.futures.ctp.thostftdcuserapidatatype.ThostFtdcUserApiDataTypeLibrary.THOST_FTDC_D_Buy;
import static org.hraink.futures.ctp.thostftdcuserapidatatype.ThostFtdcUserApiDataTypeLibrary.THOST_FTDC_FCC_NotForceClose;
import static org.hraink.futures.ctp.thostftdcuserapidatatype.ThostFtdcUserApiDataTypeLibrary.THOST_FTDC_OPT_LimitPrice;
import static org.hraink.futures.ctp.thostftdcuserapidatatype.ThostFtdcUserApiDataTypeLibrary.THOST_FTDC_TC_GFD;
import static org.hraink.futures.ctp.thostftdcuserapidatatype.ThostFtdcUserApiDataTypeLibrary.THOST_FTDC_VC_AV;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

import org.apache.log4j.Logger;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcInputOrderActionField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcInputOrderField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcInstrumentField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcInvestorPositionDetailField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcInvestorPositionField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcOrderField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcQryInstrumentField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcQryInvestorPositionDetailField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcQryInvestorPositionField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcReqUserLoginField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcRspInfoField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcRspUserLoginField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcSettlementInfoConfirmField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcTradeField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcTradingAccountField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcUserLogoutField;
import org.hraink.futures.jctp.trader.JCTPTraderApi;
import org.hraink.futures.jctp.trader.JCTPTraderSpi;

import com.alibaba.fastjson.JSON;

import com.caoxx.swt.CaoxxApp;


/**
 * Custom TraderSpi
 * 
 * @author Hraink E-mail:Hraink@Gmail.com
 * @version 2013-1-25 下午11:46:13
 */
public class MyTraderSpi extends JCTPTraderSpi {
    
    static Logger logger = Logger.getLogger(MyTraderSpi.class);
	
	JCTPTraderApi traderApi;
	int nRequestID = 0;
	CaoxxApp CaoxxApp;
	
	//中证
	String brokerId = "4080";
	String userId = "86001017";
	String password = "074014";
	List<Socket> socketList;
	
	
	public MyTraderSpi(JCTPTraderApi traderApi,CaoxxApp CaoxxApp) {
		this.traderApi = traderApi;
		this.CaoxxApp = CaoxxApp;
	}
	public void onFrontConnected() {
	    logger.debug("前置机连接");
		CThostFtdcReqUserLoginField userLoginField = new CThostFtdcReqUserLoginField();
		/*System.out.println(JSON.toJSONString(coreappView.getMainAccount()));
		userLoginField.setBrokerID(coreappView.getMainAccount().getBrokerId());
		userLoginField.setUserID(coreappView.getMainAccount().getAccountNo());
		userLoginField.setPassword(coreappView.getMainAccount().getPasswd());
		
		
		traderApi.reqUserLogin(userLoginField, 1);*/
	      

		CThostFtdcReqUserLoginField pReqUserLoginField = new CThostFtdcReqUserLoginField();
        pReqUserLoginField.setBrokerID("9999");
        pReqUserLoginField.setUserID("090985");
        pReqUserLoginField.setPassword("caojiactp");
        //traderApi.reqUserLogin(pReqUserLoginField, 0);
		
//		CThostFtdcInputOrderField pInputOrder = new CThostFtdcInputOrderField();
//		
//		
//		traderApi.reqOrderInsert(pInputOrder, ++nRequestID);
	}
	
	@Override
	public void onRspUserLogin(CThostFtdcRspUserLoginField pRspUserLogin,
			CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
	    
	    logger.debug("登录请求响应："+JSON.toJSONString(pRspUserLogin));
	    
	    CaoxxApp.onRspUserLogin(pRspUserLogin, pRspInfo, nRequestID, bIsLast);
	    
//		System.out.println("TradingDay:" + traderApi.getTradingDay());
//		System.out.println(pRspInfo.getErrorID());
//		System.out.println(pRspUserLogin.getLoginTime());
//		System.out.println(pRspUserLogin.getCZCETime());
//		System.out.println(pRspUserLogin.getDCETime());
//		System.out.println(pRspUserLogin.getFFEXTime());
//		System.out.println(pRspUserLogin.getSHFETime());
//		System.out.println(pRspUserLogin.getMaxOrderRef());
//		
//		//查询持仓明细
//		CThostFtdcQryInvestorPositionDetailField positionField = new CThostFtdcQryInvestorPositionDetailField();
//		positionField.setBrokerID(brokerId);
//		positionField.setInstrumentID("a1705");
//		positionField.setInvestorID(userId);
//		traderApi.reqQryInvestorPositionDetail(positionField, ++nRequestID);
//		
//		
//		//确认结算单
//		CThostFtdcSettlementInfoConfirmField confirmField = new CThostFtdcSettlementInfoConfirmField();
//		traderApi.reqSettlementInfoConfirm(confirmField, ++nRequestID);
//
//		
//		//下单操作
//		CThostFtdcInputOrderField inputOrderField=new CThostFtdcInputOrderField();
//		//期货公司代码
//		inputOrderField.setBrokerID(brokerId);
//		//投资者代码
//		inputOrderField.setInvestorID(userId);
//		// 合约代码
//		inputOrderField.setInstrumentID("a1705");
//		///报单引用
//		inputOrderField.setOrderRef("000000000001");
//		// 用户代码
//		inputOrderField.setUserID(userId);
//		// 报单价格条件
//		inputOrderField.setOrderPriceType(THOST_FTDC_OPT_LimitPrice);
//		// 买卖方向
//		inputOrderField.setDirection(THOST_FTDC_D_Buy);
//		// 组合开平标志
//		inputOrderField.setCombOffsetFlag("0");
//		// 组合投机套保标志
//		inputOrderField.setCombHedgeFlag("1");
//		// 价格
//		inputOrderField.setLimitPrice(4220);
//		// 数量
//		inputOrderField.setVolumeTotalOriginal(1);
//		// 有效期类型
//		inputOrderField.setTimeCondition(THOST_FTDC_TC_GFD);
//		// GTD日期
//		inputOrderField.setGTDDate("");
//		// 成交量类型
//		inputOrderField.setVolumeCondition(THOST_FTDC_VC_AV);
//		// 最小成交量
//		inputOrderField.setMinVolume(0);
//		// 触发条件
//		inputOrderField.setContingentCondition(THOST_FTDC_CC_Immediately);
//		// 止损价
//		inputOrderField.setStopPrice(0);
//		// 强平原因
//		inputOrderField.setForceCloseReason(THOST_FTDC_FCC_NotForceClose);
//		// 自动挂起标志
//		inputOrderField.setIsAutoSuspend(0);
//		
//		traderApi.reqOrderInsert(inputOrderField, ++nRequestID);
	}
	
	@Override
	public void onRtnOrder(CThostFtdcOrderField pOrder) {
	    logger.debug("报单通知："+JSON.toJSONString(pOrder));
	    CaoxxApp.onRtnOrder(pOrder);
	}
	
	@Override
	public void onRspOrderInsert(CThostFtdcInputOrderField pInputOrder,
			CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
	    CaoxxApp.onRspOrderInsert(pInputOrder,pRspInfo,nRequestID,bIsLast);
	}
	
	@Override
	public void onRspOrderAction(
			CThostFtdcInputOrderActionField pInputOrderAction,
			CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
	    CaoxxApp.onRspOrderAction(pInputOrderAction,pRspInfo,nRequestID,bIsLast);
		
	}
	
	@Override
	public void onRtnTrade(CThostFtdcTradeField pTrade) {
	    CaoxxApp.onRtnTrade(pTrade);
	}
	
	@Override
	public void onRspQryInvestorPositionDetail(
			CThostFtdcInvestorPositionDetailField pInvestorPositionDetail,
			CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
//		System.out.println(JSON.toJSONString(pInvestorPositionDetail));
//		
//		System.out.println("持仓明细查询回调");
	    CaoxxApp.onRspQryInvestorPositionDetail(pInvestorPositionDetail,pRspInfo,nRequestID,bIsLast);
	}
	
	
	@Override
	public void onRspQryInvestorPosition(
			CThostFtdcInvestorPositionField pInvestorPosition,
			CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
//		System.out.println("持仓查询回调");
	    CaoxxApp.onRspQryInvestorPosition(pInvestorPosition, pRspInfo, nRequestID, bIsLast);
	}

	@Override
	public void onRspSettlementInfoConfirm(
			CThostFtdcSettlementInfoConfirmField pSettlementInfoConfirm,
			CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
//		System.out.println("结算单确认回调");
	    CaoxxApp.onRspSettlementInfoConfirm(pSettlementInfoConfirm, pRspInfo, nRequestID, bIsLast);
	}
	
	@Override
	public void onRspError(CThostFtdcRspInfoField pRspInfo, int nRequestID,
			boolean bIsLast) {
//		System.out.println("错误回调");
	    CaoxxApp.onRspError(pRspInfo, nRequestID, bIsLast);
	}
	
	@Override
	public void onErrRtnOrderInsert(CThostFtdcInputOrderField pInputOrder,
			CThostFtdcRspInfoField pRspInfo) {
//		System.out.println("报单录入错误回调");
	    CaoxxApp.onErrRtnOrderInsert(pInputOrder, pRspInfo);
	}
	
	
	/**
	 * 请求查询资金账户响应
	 * @param pTradingAccount
	 * @param pRspInfo
	 * @param nRequestID
	 * @param bIsLast
	 */
	@Override
	public void onRspQryTradingAccount(CThostFtdcTradingAccountField pTradingAccount, CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
	    CaoxxApp.onRspQryTradingAccount(pTradingAccount, pRspInfo,nRequestID,bIsLast);
		
	}
	
	/**
     * 登出请求响应
     * @param pUserLogout
     * @param pRspInfo
     * @param nRequestID
     * @param bIsLast
     */
	@Override
    public void onRspUserLogout(CThostFtdcUserLogoutField pUserLogout, CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
        logger.debug("登出请求响应："+JSON.toJSONString(pUserLogout));
        CaoxxApp.onRspUserLogout(pUserLogout, pRspInfo, nRequestID, bIsLast);
    }
    
	/**
     * 请求查询合约响应
     * @param pInstrument
     * @param pRspInfo
     * @param nRequestID
     * @param bIsLast
     */
    @Override
    public void onRspQryInstrument(CThostFtdcInstrumentField pInstrument, CThostFtdcRspInfoField pRspInfo,
            int nRequestID, boolean bIsLast) {
        logger.info("查询合约返回："+JSON.toJSONString(pInstrument));
        CaoxxApp.onRspQryInstrument(pInstrument,pRspInfo,nRequestID,bIsLast);
    }
	
	
}
