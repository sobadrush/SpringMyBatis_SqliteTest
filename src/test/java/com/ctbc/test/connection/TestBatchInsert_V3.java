package com.ctbc.test.connection;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.ctbc.mapper.DeptMapper;
import com.ctbc.model.vo.DeptVO;

import _01_Config.RootConfig;
import net.coderbee.mybatis.batch.BatchParameter;

@RunWith(SpringJUnit4ClassRunner.class)
@FixMethodOrder(value = MethodSorters.NAME_ASCENDING)
@ContextConfiguration(classes = { RootConfig.class })
@Transactional
//@ActiveProfiles(profiles = { "sqlite_env" })
@ActiveProfiles(profiles = { "mssql_env" })
public class TestBatchInsert_V3 {

	@Autowired
	private DataSource ds;

	@Autowired
	private SqlSessionFactory sqlSessionFactory;

//	@Autowired
//	private SqlSessionTemplate sqlSessionBatchTemplate;

	@Autowired
	private DeptMapper deptMapper;

	@Rule
	public org.junit.rules.TestName testCaseName = new TestName();

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		System.out.println(" ========================================================= ");
		System.out.println(String.format(" ======================== %s ======================= ", testCaseName.getMethodName()));
		System.out.println(" ========================================================= ");
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	@Ignore
	@Rollback(true)
	public void test_001() throws SQLException {
		String productName = ds.getConnection().getMetaData().getDatabaseProductName();
		System.err.println(" 資料庫廠商 >>> " + productName);
	}

	@Test
	@Ignore
	@Rollback(true)
	public void test_002() throws SQLException {
		System.err.println(" sqlSessionFactory >>> " + sqlSessionFactory);
	}

	/**
	 * 基于 MyBatis 执行 SQL 批量操作的插件
	 * https://github.com/wen866595/MyBatis-batch
	 * TestCase說明：批量新增 List<DeptVO>
	 */
	@Test
	@Ignore
	@Rollback(false)
	public void test_003() {
		List<DeptVO> dList = new ArrayList<>();
		for (int i = 1 ; i <= 10000 ; i++) {
			dList.add(new DeptVO("部_" + i, "地_" + i));
		}

//		int pen = deptMapper.addDeptsBatch(dList); // 超過2100參數的錯誤

//		BatchParameter<DeptVO> depts = BatchParameter.wrap(dList); // DEFAULT_BATCH_SIZE = 1000
		BatchParameter<DeptVO> depts = BatchParameter.wrap(dList, 10); // 10: batchSize
		int pen = deptMapper.addDeptsBatchV3(depts);
		System.out.println("pen = " + pen);
	}

	/**
	 * 基于 MyBatis 执行 SQL 批量操作的插件
	 * https://github.com/wen866595/MyBatis-batch
	 * TestCase說明：批量新增 List<Map>
	 */
	@Test
	@Ignore
	@Rollback(false)
	public void test_004() {

		List<Map<String, Object>> deptMapList = new ArrayList<>();
		for (int i = 1 ; i <= 10000 ; i++) {
			Map<String, Object> deptMap = new HashMap<>();
			deptMap.put("deptName", "部門_" + i);
			deptMap.put("deptLoc", "地區_" + i);
			deptMapList.add(deptMap);
		}

		BatchParameter<Map<String, Object>> depts = BatchParameter.<Map<String, Object>> wrap(deptMapList /* , 500 */); // batchSize
		int pen = deptMapper.addDeptsBatchV3_ByMap(depts);
		System.out.println("pen = " + pen);
	}

	/**
	 * 基于 MyBatis 执行 SQL 批量操作的插件
	 * https://github.com/wen866595/MyBatis-batch
	 * TestCase說明：批量新增 List<Map> , SQL寫在 interface 上 @Insert
	 */
	@Test
//	@Ignore
	@Rollback(false)
	public void test_005() {

		List<Map<String, Object>> deptMapList = new ArrayList<>();
		for (int i = 1 ; i <= 10000 ; i++) {
			Map<String, Object> deptMap = new HashMap<>();
			deptMap.put("deptName", "部門_" + i);
			deptMap.put("deptLoc", "地區_" + i);
			deptMapList.add(deptMap);
		}

		Long start = System.currentTimeMillis();
		BatchParameter<Map<String, Object>> depts = BatchParameter.<Map<String, Object>> wrap(deptMapList , 500);// batchSize
		int pen = deptMapper.addDeptsBatchV3_ByMapUseAnnotation(depts);
		System.out.println("addDeptsBatchV3_ByMapUseAnnotation >>> pen = " + pen);
		Long end = System.currentTimeMillis();
		System.err.println("花費時間 >>> " + (end - start) / 1000.0);
	}
}
