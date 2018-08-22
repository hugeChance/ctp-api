package com.caoxx.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository("quotaDao")
public class QuotaDao {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    public void insert(String instrumentID, double highestPrice, double lowestPrice){
        
        this.jdbcTemplate.update("insert into FUTURE_QUOTA values (?,?,?)", 
                instrumentID,
                highestPrice,
                lowestPrice);
    }
    
    public Map<String,Object> selectByInstrumentID(String instrumentID){
        
        return this.jdbcTemplate.queryForObject("select * from FUTURE_QUOTA where INSTRUMENT_ID = ?", 
                new RowMapper<Map<String,Object>>() {

                    @Override
                    public Map<String,Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
                        Map<String, Object> map = new HashMap<>();
                        map.put("INSTRUMENT_ID",rs.getString("INSTRUMENT_ID"));
                        map.put("HIGHEST_PRICE", rs.getDouble("HIGHEST_PRICE"));
                        map.put("LOWEST_PRICE", rs.getDouble("LOWEST_PRICE"));
                        return map;
                    }
                },instrumentID);
    }
    
    public void updateHighestPrice(String instrument, double price){
        
        this.jdbcTemplate.update("update FUTURE_QUOTA set HIGHEST_PRICE = ? where INSTRUMENT_ID = ?", 
                price,instrument);
    }
    
    public void updateLowestPrice(String instrument, double price){
        
        this.jdbcTemplate.update("update FUTURE_QUOTA set LOWEST_PRICE = ? where INSTRUMENT_ID = ?", 
                price,instrument);
    }

}
