package com.ctbc.test.connection;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import _01_Config.RootConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@FixMethodOrder(value = MethodSorters.NAME_ASCENDING)
@ContextConfiguration(classes = { RootConfig.class })
public class TestConnection {

	private static int testNum = 1;
	
	@Autowired
	private DataSource ds;
	
	@Autowired
	private SqlSessionFactory sqlSessionFactory;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		String testNumStr = String.format("%03d", testNum++);
		System.out.println(" ==================== " + "test_" + testNumStr + " ==================== ");
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
//	@Ignore
	public void test_001() throws SQLException {
		String productName = ds.getConnection().getMetaData().getDatabaseProductName();
		System.err.println(" 資料庫廠商 >>> " + productName);
	}
	
	@Test
//	@Ignore
	public void test_002() throws SQLException {
		System.err.println(" sqlSessionFactory >>> " + sqlSessionFactory);
	}

}
