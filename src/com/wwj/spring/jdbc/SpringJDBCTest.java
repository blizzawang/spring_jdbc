package com.wwj.spring.jdbc;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;

public class SpringJDBCTest {

	private ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");

	@Test
	public void testConnection() throws SQLException {
		DataSource dataSource = ctx.getBean(DataSource.class);
		System.out.println(dataSource.getConnection());
	}
	
	@Test
	public void testInsert() {
		JdbcTemplate jdbcTemplate = (JdbcTemplate) ctx.getBean("jdbcTemplate");
		String sql = "insert into user(name,password) values(?,?)";
		Object[] args = {"zhangsan","12345"};
		jdbcTemplate.update(sql,args);
	}
	
	@Test
	public void testUpdate() {
		JdbcTemplate jdbcTemplate = (JdbcTemplate) ctx.getBean("jdbcTemplate");
		String sql = "update user set password = ? where id = ?";
		Object[] args = {"admin","1"};
		jdbcTemplate.update(sql,args);
	}
	
	@Test
	public void testBatchInsert() {
		JdbcTemplate jdbcTemplate = (JdbcTemplate) ctx.getBean("jdbcTemplate");
		String sql = "insert into user(name,password) values(?,?)";
		List<Object[]> batchArgs = new ArrayList();
		batchArgs.add(new Object[]{"lisi","123"});
		batchArgs.add(new Object[]{"wangwu","1234"});
		batchArgs.add(new Object[]{"zhaoliu","12345"});
		jdbcTemplate.batchUpdate(sql, batchArgs);
	}
	
	@Test
	public void testGet() {
		JdbcTemplate jdbcTemplate = (JdbcTemplate) ctx.getBean("jdbcTemplate");
		String sql = "select id,name,password from user where id = ?";
		RowMapper<User> rowMapper = new BeanPropertyRowMapper<User>(User.class);
		User user = jdbcTemplate.queryForObject(sql, rowMapper,1);
		System.out.println(user);
	}
	
	@Test
	public void testGetMore() {
		JdbcTemplate jdbcTemplate = (JdbcTemplate) ctx.getBean("jdbcTemplate");
		String sql = "select id,name,password from user where id > ?";
		RowMapper<User> rowMapper = new BeanPropertyRowMapper<>(User.class);
		List<User> userList = jdbcTemplate.query(sql, rowMapper,2);
		System.out.println(userList);
	}
	
	@Test
	public void testSingleValue() {
		JdbcTemplate jdbcTemplate = (JdbcTemplate) ctx.getBean("jdbcTemplate");
		String sql = "select name from user where id = ?";
		String name = jdbcTemplate.queryForObject(sql, String.class,1);
		System.out.println(name);
	}
	
	@Test
	public void testNamedParameterJdbcTemplate() {
		NamedParameterJdbcTemplate namedParameterJdbcTemplate = (NamedParameterJdbcTemplate) ctx.getBean("namedParameterJdbcTemplate");
		String sql = "insert into user(name,password) values(:name,:psd)";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("name", "wangweijun");
		map.put("psd", "112233");
		namedParameterJdbcTemplate.update(sql,map);
	}
	
	@Test
	public void testSimpleNamedParameterJdbcTemplate() {
		NamedParameterJdbcTemplate namedParameterJdbcTemplate = (NamedParameterJdbcTemplate) ctx.getBean("namedParameterJdbcTemplate");
		String sql = "insert into user(name,password) values(:name,:password)";
		User user = new User();
		user.setName("zhaoliu");
		user.setPassword("223344");
		SqlParameterSource paramSource = new BeanPropertySqlParameterSource(user);
		namedParameterJdbcTemplate.update(sql, paramSource);
	}
}
