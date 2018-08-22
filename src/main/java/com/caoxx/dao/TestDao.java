package com.caoxx.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.caoxx.entity.Test;

@Component("testDao")
public class TestDao {
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	
	public List<Test> selectAll(){
		return this.jdbcTemplate.query("select * from TEST", BeanPropertyRowMapper.newInstance(Test.class));
	}

}
