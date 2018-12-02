package com.ctbc.test.connection;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
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
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
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
//@ActiveProfiles(profiles = { "sqlite_env" })
@ActiveProfiles(profiles = { "mssql_env" })
public class TestBatchInsert {

	@Autowired
	private DataSource ds;

	@Autowired
	private SqlSessionFactory sqlSessionFactory;

	@Autowired
	private SqlSessionTemplate sqlSessionBatchTemplate;

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
	 * 【真‧Batch INSERT】
	 * https://blog.csdn.net/e_wsq/article/details/54344540
	 * https://blog.csdn.net/qq_31457665/article/details/81130234
	 * https://blog.csdn.net/yangliuhbhd/article/details/80982254
	 */
	@Test
	@Ignore
	@Rollback(false)
	public void test_004() throws SQLException {

		SqlSession session = sqlSessionFactory.openSession(ExecutorType.BATCH, false/* auto-commit */);// 跟工廠要一個使用BATCH操作的session
		DeptMapper deptMapperBatch = session.getMapper(DeptMapper.class);// 由此session中取得mapper

//		DeptVO deptVO_01 = new DeptVO("A部", "中正區A");
//		DeptVO deptVO_02 = new DeptVO("B部", "中正區B");
//		DeptVO deptVO_03 = new DeptVO("C部", "中正區C");
//		DeptVO deptVO_04 = new DeptVO("D部", "中正區D");
//		DeptVO deptVO_05 = new DeptVO("E部", "中正區E");
//		DeptVO deptVO_06 = new DeptVO("F部", "中正區F");
//		DeptVO deptVO_07 = new DeptVO("G部", "中正區G");
//
//		List<DeptVO> deptList = new ArrayList<>();
//		deptList.add(deptVO_01);
//		deptList.add(deptVO_02);
//		deptList.add(deptVO_03);
//		deptList.add(deptVO_04);
//		deptList.add(deptVO_05);
//		deptList.add(deptVO_06);
//		deptList.add(deptVO_07);

		List<DeptVO> deptList = new ArrayList<DeptVO>();
		for (int i = 1 ; i <= 3000 ; i++) {
			DeptVO deptVO = new DeptVO(String.format("部門%03d", i), String.format("地區%03d", i));
			deptList.add(deptVO);
		}

		int size = deptList.size();
		int commitPeriod = 2;
		try {
			for (int i = 1 ; i <= size ; i++) {
				DeptVO voToInsert = deptList.get(i - 1);
				deptMapperBatch.addDept(voToInsert);
				if ((i % commitPeriod == 0 || i == size) && i >= commitPeriod) { // 每commitPeriod就commit給DB一次
					session.commit();
					session.clearCache();// 清理缓存，防止溢出
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		} finally {
			session.close();
		}
		;
	}

	@Test
	@Ignore
	@Rollback(true)
	public void test_005() {
		System.err.println(" sqlSessionTemplate >>> " + sqlSessionBatchTemplate);
	}

	@Test
	@Ignore
	@Rollback(false)
	public void test_006() {
		/**
		 * 等SQL-Spring Transaction結束後才Commit到DB
		 */
		DeptMapper deptMapper = sqlSessionBatchTemplate.getMapper(DeptMapper.class);

		for (int i = 1 ; i <= 3010 ; i++) {
			DeptVO deptVO = new DeptVO(String.format("部門%03d", i), String.format("地區%03d", i));

			int pen = deptMapper.addDept(deptVO);

			if (i % 1000 == 0) {
				// 此段非必要，每1000筆會先將SQL發給DB，但若中間發生Exception時會rollback
				sqlSessionBatchTemplate.flushStatements();
			}

			if (i == 1002) {
				throw new RuntimeException("我中斷囉~");
			}

			System.out.println("pen = " + pen);
		}

		System.out.println("=== 結束 ===");

	}

	@Test
//	@Ignore
	@Rollback(false)
	public void test_007() {
		
		List<DeptVO> dList = new ArrayList<>();
		for (int i = 1 ; i <= 3012 ; i++) {
			DeptVO deptVO = new DeptVO(String.format("部門%03d", i), String.format("地區%03d", i));
			dList.add(deptVO);
		}
		
 		boolean result = this.saveBatch(dList, 500);
		System.out.println("result = "  + result);
	}

	public boolean saveBatch(List<DeptVO> autoBatchList, int perNum) {
		// 【參考】https://blog.csdn.net/qq_24607837/article/details/81381934
		boolean flag = false;
		try (SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH);) {
			//由於資料庫對於插入插入字段的限制，在這裡對此批量插入的數據進行分批處理
			int batchCount = perNum;//每批commit的個數
			int batchLastIndex = batchCount - 1;// 每批最後一個的下標
			for (int index = 0 ; index < autoBatchList.size() - 1 ;) {
				if (batchLastIndex > autoBatchList.size() - 1) {
					batchLastIndex = autoBatchList.size() - 1;
//					deptMapper.addDeptsBatchV2(autoBatchList.subList(index, batchLastIndex + 1));
					sqlSession.insert("com.ctbc.mapper.DeptMapper.addDeptsBatchV2", autoBatchList.subList(index, batchLastIndex + 1));
					sqlSession.commit();
					break;// INSERT完畢，Break
				} else {
//					deptMapper.addDeptsBatchV2(autoBatchList.subList(index, batchLastIndex + 1));
					sqlSession.insert("com.ctbc.mapper.DeptMapper.addDeptsBatchV2", autoBatchList.subList(index, batchLastIndex + 1));
					sqlSession.commit();
					index = batchLastIndex + 1;// 設置下一輪INSERT的下標
					batchLastIndex = index + (batchCount - 1);
				}
			}

			sqlSession.commit();
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
}
