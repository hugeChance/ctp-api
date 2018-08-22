package com.caoxx.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import com.caoxx.entity.Test;
@Component("minute1MA20Dao")
public class Minute1MA20Dao {

	
		@Autowired
		private JdbcTemplate jdbcTemplate;
		
		@SuppressWarnings("unchecked")  
	    public List<String>  select1MA20Avg(int seqId) {  
	  
	        List list1 = new ArrayList();  
	  
	        int paraNum1 = seqId;
	        
	        int paraNum2 = seqId - 19;
	        
	        String sql = "select to_char(round(avg(close_price),3)) as avg_close_price from INSTRUMENT_SEQUENCE where SEQUENCE_ID between ?  and ?";  
	  
	        list1 = (List) this.jdbcTemplate.query(sql, new Object[] { paraNum1, paraNum2 }, new ResultSetExtractor() {  
	  
	            public Object extractData(ResultSet rs) throws SQLException, DataAccessException {  
	  
	                List list = new ArrayList();  
	  
	                while (rs.next()) {  
	                    String retAvg = rs.getString("avg_close_price");
	                	
	  
	                    list.add(retAvg);  
	  
	                }  
	  
	                return list;  
	            }  
	        });  
	  
			return list1;  
	  
	    }  

}
