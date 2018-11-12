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
import org.junit.Test;
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

@RunWith(SpringJUnit4ClassRunner.class)
@FixMethodOrder(value = MethodSorters.NAME_ASCENDING)
@ContextConfiguration(classes = { RootConfig.class })
@Transactional
@ActiveProfiles(profiles = { "sqlite_env" })
public class TestBatchInsert {

	private static int testNum = 1;

	@Autowired
	private DataSource ds;

	@Autowired
	private SqlSessionFactory sqlSessionFactory;

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
		System.err.println("========================================================= ");
		System.err.println(" ======================== " + "test_" + testNumStr + " ======================= ");
		System.err.println(" ========================================================= ");
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
	
	@Test
//	@Ignore
	@Rollback(false)
	public void test_003() throws SQLException {
		
		/**
		 * 【真‧Batch INSERT】
		 *  https://blog.csdn.net/e_wsq/article/details/54344540
		 */
		
		SqlSession session = sqlSessionFactory.openSession(ExecutorType.BATCH, false);
		
		DeptVO deptVO_01 = new DeptVO("A部","中正區A");
		DeptVO deptVO_02 = new DeptVO("B部","中正區B");
		DeptVO deptVO_03 = new DeptVO("C部","中正區C");
		DeptVO deptVO_04 = new DeptVO("D部","中正區D");
		DeptVO deptVO_05 = new DeptVO("E部","中正區E");
		DeptVO deptVO_06 = new DeptVO("F部","中正區F");
		DeptVO deptVO_07 = new DeptVO("G部","中正區G");
		
		List<DeptVO> deptList = new ArrayList<>();
		deptList.add(deptVO_01);
		deptList.add(deptVO_02);
		deptList.add(deptVO_03);
		deptList.add(deptVO_04);
		deptList.add(deptVO_05);
		deptList.add(deptVO_06);
		deptList.add(deptVO_07);
		
		int size = deptList.size();
		try {
			for (int i = 0 ; i < deptList.size() ; i++) {
				DeptVO voToInsert = deptList.get(i);
				deptMapper.addDept(voToInsert);
				if ( (i % 2 == 0 || i == size - 1) && i >= 2 ) {
					// 手动每1000个一提交，提交后无法回滚
					session.commit();
					session.clearCache();// 清理缓存，防止溢出
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			session.rollback();
		} finally{
			session.close();
		};
	}

}















