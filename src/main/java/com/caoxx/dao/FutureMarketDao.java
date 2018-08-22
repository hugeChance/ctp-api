package com.caoxx.dao;

import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcDepthMarketDataField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.caoxx.entity.*;
@Repository("futureMarketDao")
public class FutureMarketDao {

	@Autowired
    private JdbcTemplate jdbcTemplate;
	
	public int insert(CThostFtdcDepthMarketDataField dataField){
    	String sql = "insert into t_future_market("
    			+"INSTRUMENT_ID,"
    			+"EXCHANGE_ID,"
    			+"EXCHANGE_INST_ID,"
    			+"OPEN_PRICE,"
    			+"CLOSE_PRICE,"
    			+"PRE_CLOSE_PRICE,"
    			+"HIGHEST_PRICE,"
    			+"LOWEST_PRICE,"
    			+"LAST_PRICE,"
    			+"VOLUME,"
    			+"TURNOVER,"
    			+"BID_PRICE1,"
    			+"ASK_PRICE1,"
    			+"BID_VOLUME1,"
    			+"ASK_VOLUME1,"
    			+"UPPER_LIMIT_PRICE,"
    			+"LOWER_LIMIT_PRICE,"
    			+"CURR_DELTA,"
    			+"PRE_DELTA,"
    			+"PRE_SETTLEMENT_PRICE,"
    			+"SETTLEMENT_PRICE,"
    			+"OPEN_INTEREST,"
    			+"TRADING_DAY,"
    			+"UPDATE_TIME,"
    			+"UPDATE_MILLISEC,"
    			+"CREATE_TIME) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,sysdate)";
    	
    	return this.jdbcTemplate.update(sql, new Object[]{
    			dataField.getInstrumentID(),
    			dataField.getExchangeID(),
    			dataField.getExchangeInstID(),
    			Double.MAX_VALUE==dataField.getOpenPrice()?0:dataField.getOpenPrice(),
    			String.valueOf(dataField.getClosePrice()),
    			Double.MAX_VALUE==dataField.getPreClosePrice()?0:dataField.getPreClosePrice(),
    			Double.MAX_VALUE==dataField.getHighestPrice()?0:dataField.getHighestPrice(),
    			Double.MAX_VALUE==dataField.getLowestPrice()?0:dataField.getLowestPrice(),
    			Double.MAX_VALUE==dataField.getLastPrice()?0:dataField.getLastPrice(),
    			dataField.getVolume(),
    			Double.MAX_VALUE==dataField.getTurnover()?0:dataField.getTurnover(),
    			Double.MAX_VALUE==dataField.getBidPrice1()?0:dataField.getBidPrice1(),
    			Double.MAX_VALUE==dataField.getAskPrice1()?0:dataField.getAskPrice1(),
    			dataField.getBidVolume1(),
    			dataField.getAskVolume1(),
    			Double.MAX_VALUE==dataField.getUpperLimitPrice()?0:dataField.getUpperLimitPrice(),
    			Double.MAX_VALUE==dataField.getLowerLimitPrice()?0:dataField.getLowerLimitPrice(),
    			String.valueOf(dataField.getCurrDelta()),
    			String.valueOf(dataField.getPreDelta()),
    			Double.MAX_VALUE==dataField.getPreSettlementPrice()?0:dataField.getPreSettlementPrice(),
    			String.valueOf(dataField.getSettlementPrice()),
    			Double.MAX_VALUE==dataField.getOpenInterest()?0:dataField.getOpenInterest(),
    			dataField.getTradingDay(),
    			dataField.getUpdateTime(),
    			dataField.getUpdateMillisec()});
    }
	
	public int insertBak(CThostFtdcDepthMarketDataField dataField){
    	String sql = "insert into t_future_market_bak("
    			+"INSTRUMENT_ID,"
    			+"EXCHANGE_ID,"
    			+"EXCHANGE_INST_ID,"
    			+"OPEN_PRICE,"
    			+"CLOSE_PRICE,"
    			+"PRE_CLOSE_PRICE,"
    			+"HIGHEST_PRICE,"
    			+"LOWEST_PRICE,"
    			+"LAST_PRICE,"
    			+"VOLUME,"
    			+"TURNOVER,"
    			+"BID_PRICE1,"
    			+"ASK_PRICE1,"
    			+"BID_VOLUME1,"
    			+"ASK_VOLUME1,"
    			+"UPPER_LIMIT_PRICE,"
    			+"LOWER_LIMIT_PRICE,"
    			+"CURR_DELTA,"
    			+"PRE_DELTA,"
    			+"PRE_SETTLEMENT_PRICE,"
    			+"SETTLEMENT_PRICE,"
    			+"OPEN_INTEREST,"
    			+"TRADING_DAY,"
    			+"UPDATE_TIME,"
    			+"UPDATE_MILLISEC,"
    			+"CREATE_TIME) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,sysdate)";
    	
    	return this.jdbcTemplate.update(sql, new Object[]{
    			dataField.getInstrumentID(),
    			dataField.getExchangeID(),
    			dataField.getExchangeInstID(),
    			Double.MAX_VALUE==dataField.getOpenPrice()?0:dataField.getOpenPrice(),
    			String.valueOf(dataField.getClosePrice()),
    			Double.MAX_VALUE==dataField.getPreClosePrice()?0:dataField.getPreClosePrice(),
    			Double.MAX_VALUE==dataField.getHighestPrice()?0:dataField.getHighestPrice(),
    			Double.MAX_VALUE==dataField.getLowestPrice()?0:dataField.getLowestPrice(),
    			Double.MAX_VALUE==dataField.getLastPrice()?0:dataField.getLastPrice(),
    			dataField.getVolume(),
    			Double.MAX_VALUE==dataField.getTurnover()?0:dataField.getTurnover(),
    			Double.MAX_VALUE==dataField.getBidPrice1()?0:dataField.getBidPrice1(),
    			Double.MAX_VALUE==dataField.getAskPrice1()?0:dataField.getAskPrice1(),
    			dataField.getBidVolume1(),
    			dataField.getAskVolume1(),
    			Double.MAX_VALUE==dataField.getUpperLimitPrice()?0:dataField.getUpperLimitPrice(),
    			Double.MAX_VALUE==dataField.getLowerLimitPrice()?0:dataField.getLowerLimitPrice(),
    			String.valueOf(dataField.getCurrDelta()),
    			String.valueOf(dataField.getPreDelta()),
    			Double.MAX_VALUE==dataField.getPreSettlementPrice()?0:dataField.getPreSettlementPrice(),
    			String.valueOf(dataField.getSettlementPrice()),
    			Double.MAX_VALUE==dataField.getOpenInterest()?0:dataField.getOpenInterest(),
    			dataField.getTradingDay(),
    			dataField.getUpdateTime(),
    			dataField.getUpdateMillisec()});
    }
}
