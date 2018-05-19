package com.ctbc.test.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

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

import com.ctbc.mapper.DeptMapper;
import com.ctbc.model.vo.DeptVO;

import _01_Config.RootConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@FixMethodOrder(value = MethodSorters.NAME_ASCENDING)
@ContextConfiguration(classes = { RootConfig.class })
@Transactional
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

	private void getAll() {
		List<DeptVO> deptVOs = deptMapper.getAll();
		for (DeptVO deptVO : deptVOs) {
			System.out.println(deptVO);
		}
	}

	@Test
	@Ignore
	public void test_001() throws SQLException {
		System.out.println("deptMapper = " + deptMapper);
	}

	@Test
	@Ignore
	public void test_002() throws SQLException {
		this.getAll();
	}

	@Test
	@Ignore
	public void test_003() throws SQLException {
		ArrayList<Integer> ids = new ArrayList<Integer>();
		ids.add(20);
		ids.add(30);
		List<DeptVO> depts = deptMapper.getDeptByIdList(ids);
		for (DeptVO deptVO : depts) {
			System.out.println(">>> " + deptVO);
		}
	}

	@Test
	@Ignore
	public void test_004() throws SQLException {
		DeptVO deptVO = deptMapper.getDeptById(10);
		System.out.println(deptVO);
	}

	@Test
	@Ignore
	public void test_005() throws SQLException {
		Map<String, String> params = new HashMap<>();
		params.put("deptNumber", "20");
		DeptVO deptVO = deptMapper.getDeptByIdMap(params);
		System.out.println(deptVO);
	}

	@Test
	@Ignore
	@Rollback(false)
	public void test_006() throws SQLException {
		int pen = deptMapper.addDept(new DeptVO("國防部", "博愛區"));
		System.out.println("insert success : " + pen);
		this.getAll();
	}

	@Test
	@Ignore
	@Rollback(false)
	public void test_007() throws SQLException {
		int pen = deptMapper.updateDeptById(new DeptVO(10, "交通部", "博愛區"));
		System.out.println("update success : " + pen);
		this.getAll();
	}

	@Test
	@Ignore
	@Rollback(false)
	public void test_008() throws SQLException {
		int pen = deptMapper.delDeptById(20);
		System.out.println("delete success : " + pen);
		this.getAll();
	}

	@Test
	@Ignore
	@Rollback(false)
	public void test_009() throws SQLException {
		int pen = deptMapper.delDeptByIdArray(new int[] { 20, 30 });
		System.out.println("delete success : " + pen);
		this.getAll();
	}

	@Test
	@Ignore
	@Rollback(false)
	public void test_010() throws SQLException {
		List<DeptVO> dList = new ArrayList<>();
		dList.add(new DeptVO("國防部_1", "中正區_1"));
		dList.add(new DeptVO("國防部_2", "中正區_2"));
		int pen = deptMapper.addDeptsBatch(dList);
		System.err.println("insert batch success : " + pen);
		this.getAll();
	}

	@Test
	@Ignore
	@Rollback(false)
	public void test_011() throws SQLException {
		//
		// 批量更新, Sqlite會出錯
		//
		List<DeptVO> dList = new ArrayList<>();
		dList.add(new DeptVO(10, "交通部1", "大安區1"));
		dList.add(new DeptVO(20, "交通部2", "大安區2"));
		
		int pen;
		try {
			pen = deptMapper.updateDeptsBatch(dList);
			System.err.println("update batch success : " + pen + " 筆");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.getAll();
	}

	@Test
	@Ignore
	@Rollback(false)
	public void test_012() throws SQLException {
		List<DeptVO> dList = new ArrayList<>();
		dList.add(new DeptVO(10, "國防部_111", "中正區_111"));
		dList.add(new DeptVO(20, "國防部_222", "中正區_222"));
		
		int pen = deptMapper.updateDeptsBatchForSqlite(dList);
		System.err.println("update batch success : " + pen + " 筆");
		this.getAll();
	}
	
	@Test
//	@Ignore
	@Rollback(false)
	public void test_013() throws SQLException {
		List<DeptVO> dList = new ArrayList<>();
		dList.add(new DeptVO(30, "交通部_111", "大安區_111"));
		dList.add(new DeptVO(40, "交通部_222", "大安區_222"));
		
		int pen = deptMapper.updateDeptsBatchForSqlite2(dList);
		System.err.println("update batch success : " + pen + " 筆");
		this.getAll();
	}
	
}


