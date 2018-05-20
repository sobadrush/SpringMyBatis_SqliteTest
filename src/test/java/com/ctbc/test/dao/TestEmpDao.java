package com.ctbc.test.dao;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import org.springframework.test.context.ActiveProfiles;
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
//@ActiveProfiles(value = {"sqlite_env"})
@ActiveProfiles(value = {"mssql_env"})
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
	@Ignore
	@Rollback(true)
	public void test_005() throws SQLException {
		EmpVO empVO = new EmpVO(7001, "滅霸", "魔王", java.sql.Date.valueOf("2018-05-20"), 30);
		int pen = empMapper.updateByPrimaryKey(empVO);
		System.err.println("更新成功 : " + pen + " 筆");
		this.getAll();
	}

	@Test // 批量更新多筆 CASE...WHEN + <foreach>
	@Ignore
	@Rollback(true)
	public void test_006() throws SQLException {
		List<EmpVO> empList = new ArrayList<>();
		EmpVO empVO_01 = new EmpVO(7001, "滅霸", "魔王", java.sql.Date.valueOf("2018-05-20"), 40);
		EmpVO empVO_02 = new EmpVO(7002, "浩克", "英雄", java.sql.Date.valueOf("2018-05-21"), 40);
		EmpVO empVO_03 = new EmpVO(7003, "索爾", "英雄", java.sql.Date.valueOf("2018-05-22"), 40);
		empList.add(empVO_01);
		empList.add(empVO_02);
		empList.add(empVO_03);
		//-------------------------------------------------
		int pen = empMapper.updateEmpsBatchForSqlite2(empList);
		System.err.println("更新成功 : " + pen + " 筆");
		this.getAll();
	}

	@Test // 刪除一筆
	@Ignore
	@Rollback(true)
	public void test_007() throws SQLException {
		int pen = empMapper.deleteByPrimaryKey(7003);
		System.err.println("刪除成功 : " + pen + " 筆");
		this.getAll();
	}

	@Test // 刪除多筆 IN
	@Ignore
	@Rollback(true)
	public void test_008() throws SQLException {
		List<Integer> empNoList = new ArrayList<Integer>();
		empNoList.add(7001);
		empNoList.add(7002);
		empNoList.add(7003);
		empNoList.add(7007);
		//-------------------------------------------------
		EmpVOExample exp = new EmpVOExample();
		exp.createCriteria().andEmpnoIn(empNoList);
		int pen = empMapper.deleteByExample(exp);
		System.err.println("刪除成功 : " + pen + " 筆");
		this.getAll();
	}

	@Test // 模糊查詢 LIKE
	@Ignore
	@Rollback(true)
	public void test_009() throws SQLException {
		EmpVOExample exp = new EmpVOExample();
		exp.createCriteria().andEnameLike("%a%");
		List<EmpVO> empList = empMapper.selectByExample(exp);
		for (EmpVO empVO : empList) {
			System.out.println(empVO);
		}
	}

	@Test // EqualTo
	@Ignore
	@Rollback(true)
	public void test_010() throws SQLException {
		EmpVOExample exp = new EmpVOExample();
		exp.createCriteria().andEnameEqualTo("adams");
		List<EmpVO> empList = empMapper.selectByExample(exp);
		for (EmpVO empVO : empList) {
			System.out.println(empVO);
		}
	}

	@Test // GreaterThan
	@Ignore
	@Rollback(true)
	public void test_011() throws SQLException {
		EmpVOExample exp = new EmpVOExample();
		exp.createCriteria().andEmpnoGreaterThan(7010);
		List<EmpVO> empList = empMapper.selectByExample(exp);
		for (EmpVO empVO : empList) {
			System.out.println(empVO);
		}
	}

	@Test // DATE between (Sqlite資料庫 - 失敗, 查到0筆; MSSQL 成功！)
	@Ignore
	@Rollback(true)
	public void test_012() throws SQLException, ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
		java.util.Date date1 = sdf.parse("1982-01-01");
		java.util.Date date2 = sdf.parse("2018-01-01");
		System.err.println("DATE START : " + date1);
		System.err.println("DATE END : " + date2);
		EmpVOExample exp = new EmpVOExample();
//		exp.createCriteria().andHiredateBetween(java.sql.Date.valueOf("2018-01-01"), java.sql.Date.valueOf("1982-01-01"));
//		exp.createCriteria().andHiredateBetween(java.sql.Date.valueOf("1982-01-01"), java.sql.Date.valueOf("2018-01-01"));
		exp.createCriteria().andHiredateBetween(date1, date2);
		List<EmpVO> empList = empMapper.selectByExample(exp);
		for (EmpVO empVO : empList) {
			System.out.println(empVO);
		}
	}

	@Test // DATE HiredateEqualTo (Sqlite資料庫 - 失敗, 查到0筆 ; MSSQL 成功！)
	@Ignore
	@Rollback(true)
	public void test_013() throws SQLException, ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
		java.util.Date date1 = sdf.parse("1983-01-12");
		System.err.println("DATE START : " + date1);

		EmpVOExample exp = new EmpVOExample();
		exp.createCriteria().andHiredateEqualTo(date1);
		List<EmpVO> empList = empMapper.selectByExample(exp);
		for (EmpVO empVO : empList) {
			System.out.println(empVO);
		}
	}
	
	@Test // 多條件查詢 WHERE ( JOB = 'clerk'  AND ename LIKE '%a%')
	@Ignore
	@Rollback(true)
	public void test_014() throws SQLException, ParseException {
		EmpVOExample exp = new EmpVOExample();
		exp.createCriteria().andJobEqualTo("clerk").andEnameLike("%a%");
		List<EmpVO> empList = empMapper.selectByExample(exp);
		for (EmpVO empVO : empList) {
			System.out.println(empVO);
		}
	}
	
	@Test // 測試JOIN，根據【員工姓名】查詢【部門資訊】
	@Ignore
	@Rollback(true)
	public void test_015() throws SQLException {
		EmpVO empVO = new EmpVO();
		empVO.setEname("king");
		Map<String, Object> rtnMap = empMapper.getDeptInfoByEmpName(empVO);
		System.err.println("hiredate 型態 : " + rtnMap.get("hiredate").getClass().getName());// SQLITE: String ; MSSQL: java.sql.Date
		System.err.println(" >>>>> " + rtnMap);
	}

}
