package com.ctbc.test.dao;

import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

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

import com.ctbc.mapper.DeptMapper;
import com.ctbc.model.vo.DeptVO;

import _01_Config.RootConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@FixMethodOrder(value = MethodSorters.NAME_ASCENDING)
@ContextConfiguration(classes = { RootConfig.class })
public class TestDeptDao {

	private static int testNum = 1;
	
	@Autowired
	private DataSource ds;
	
	@Autowired
	private DeptMapper deptMapper;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		String testNumStr = String.format("%03d", testNum++); 
		System.out.println(" =================================== " + "test_" + testNumStr + " =================================== ");
	}

	@After
	public void tearDown() throws Exception {
		System.out.println(" ================================================================================ \n");
	}

	@Test
//	@Ignore
	public void test_001() throws SQLException {
		System.out.println("deptMapper = " + deptMapper);
	}
	
	@Test
//	@Ignore
	public void test_002() throws SQLException {
		List<DeptVO> deptVOs = deptMapper.getAll();
		for (DeptVO deptVO : deptVOs) {
			System.out.println(deptVO);
		}
	}
	
	@Test
//	@Ignore
	public void test_003() throws SQLException {
		DeptVO deptVO = deptMapper.getDeptById(10);
		System.out.println(deptVO);
	}
	

}
