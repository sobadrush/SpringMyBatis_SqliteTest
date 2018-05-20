package com.ctbc.test.dao;

import java.sql.SQLException;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.ctbc.mapper.EmpMapper;
import com.ctbc.model.vo.EmpVO;
import com.ctbc.model.vo.EmpVOExample;

import _01_Config.RootConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@FixMethodOrder(value = MethodSorters.NAME_ASCENDING)
@ContextConfiguration(classes = { RootConfig.class })
@Transactional
public class TestEmpDao {
	private static int testNum = 1;

	@Autowired
	private EmpMapper empMapper;

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

	private void getAll() {
		EmpVOExample exp = new EmpVOExample();
		List<EmpVO> empList = empMapper.selectByExample(exp);
		for (EmpVO empVO : empList) {
			System.out.println(" 查全部 >>> " + empVO.toString());
		}
	}

	@Test
	@Ignore
	public void test_001() throws SQLException {
		System.out.println("empMapper = " + empMapper);
	}

	@Test // 查全部
	@Ignore
	public void test_002() throws SQLException {
		this.getAll();
	}

	@Test // 新增一筆
	@Ignore
	@Rollback(false)
	public void test_003() throws SQLException {
		EmpVO empVO = new EmpVO("蝙蝠俠", "DC英雄", java.sql.Date.valueOf("2018-05-20"), 20);
		int pen = empMapper.insert(empVO);
		System.err.println("新增成功 : " + pen + " 筆");
		this.getAll();
	}

	@Test // 查一筆
	@Ignore
	public void test_004() throws SQLException {
		EmpVO empVO = empMapper.selectByPrimaryKey(7002);
		System.err.println(" >>> " + empVO);
	}
	
	@Test // 更新一筆
//	@Ignore
	@Rollback(false)
	public void test_005() throws SQLException {
		 EmpVO empVO = new EmpVO(7001, "滅霸", "魔王" , java.sql.Date.valueOf("2018-05-20"), 30);
		 int pen = empMapper.updateByPrimaryKey(empVO);
		 System.err.println("更新成功 : " + pen + " 筆");
//		 this.getAll();
	}
	
	
}
