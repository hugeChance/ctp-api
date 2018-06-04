package com.caoxx.service;

import java.math.BigDecimal;

import org.apache.log4j.Logger;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcInputOrderField;
import org.hraink.futures.jctp.trader.JCTPTraderApi;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class TradeService {
    
    static Logger logger = Logger.getLogger(TradeService.class);
    
    private JCTPTraderApi traderApi;
    
    /**
     * 报单请求
     * @param requestBody
     * @param requestId
     * @return
     */
    public int reqOrderInsert(String requestBody,String requestId){
        
        JSONObject json = JSON.parseObject(requestBody);
        
        logger.info("报单请求入参："+requestBody+"; 请求id："+requestId);
        //下单操作
        CThostFtdcInputOrderField inputOrderField=new CThostFtdcInputOrderField();
        //期货公司代码
        inputOrderField.setBrokerID(json.getString("brokerID"));
        //投资者代码
        inputOrderField.setInvestorID(json.getString("investorID"));
        // 合约代码
        inputOrderField.setInstrumentID(json.getString("instrumentID"));
        ///报单引用     
        inputOrderField.setOrderRef(json.getString("orderRef"));
        // 操作人代码
        inputOrderField.setUserID(json.getString("userID"));
        // 报单价格条件
        inputOrderField.setOrderPriceType(json.getString("orderPriceType").toCharArray()[0]);
        // 买卖方向
        inputOrderField.setDirection(json.getString("direction").toCharArray()[0]);
        // 组合开平标志
        inputOrderField.setCombOffsetFlag(json.getString("combOffsetFlag"));
        // 组合投机套保标志
        inputOrderField.setCombHedgeFlag(json.getString("combHedgeFlag"));
        // 价格
        inputOrderField.setLimitPrice(json.getDoubleValue("limitPrice"));
        // 数量
        inputOrderField.setVolumeTotalOriginal(json.getIntValue("volumeTotalOriginal"));
        // 有效期类型
        inputOrderField.setTimeCondition(json.getString("timeCondition").toCharArray()[0]);
        // GTD日期
        inputOrderField.setGTDDate(json.getString("GTDDate"));
        // 成交量类型
        inputOrderField.setVolumeCondition(json.getString("volumeCondition").toCharArray()[0]);
        // 最小成交量
        inputOrderField.setMinVolume(json.getIntValue("minVolume"));
        // 触发条件
        inputOrderField.setContingentCondition(json.getString("contingentCondition").toCharArray()[0]);
        // 止损价
        inputOrderField.setStopPrice(json.getDoubleValue("stopPrice"));
        // 强平原因
        inputOrderField.setForceCloseReason(json.getString("forceCloseReason").toCharArray()[0]);
        // 自动挂起标志
        inputOrderField.setIsAutoSuspend(json.getIntValue("isAutoSuspend"));
        
        int result = traderApi.reqOrderInsert(inputOrderField, Integer.parseInt(requestId));
        
        return result;
    }
    
    
}
