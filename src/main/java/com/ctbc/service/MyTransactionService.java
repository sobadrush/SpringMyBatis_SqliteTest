package com.ctbc.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ctbc.mapper.DeptMapper;
import com.ctbc.model.vo.DeptVO;

import _01_Config.RootConfig;

@Transactional
@Service
public class MyTransactionService {

	@Autowired
	private SqlSessionFactory sqlSessionFactory;

	@Autowired
	private SqlSessionTemplate sqlSessionBatchTemplate;

	@Autowired
	private DeptMapper deptMapper;

//	@Transactional(propagation = Propagation.REQUIRED , isolation = Isolation.DEFAULT , transactionManager = "txManager")
//	public void testTransaction() {
//		deptMapper.addDept(new DeptVO("國防部A", "新北永和A"));
//		deptMapper.addDept(new DeptVO("國防部GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG", "新北永和A"));
//	}
//	
//	public static void main(String[] args) {
//		System.setProperty("spring.profiles.active", "mssql_env");
//		ConfigurableApplicationContext context = new AnnotationConfigApplicationContext(RootConfig.class);
//		MyTransactionService svcBean = context.getBean("myTransactionService", MyTransactionService.class);
//		svcBean.testTransaction();
//		context.close();
//	}

	public static void main(String[] args) {
		System.setProperty("spring.profiles.active", "mssql_env");
		ConfigurableApplicationContext context = new AnnotationConfigApplicationContext(RootConfig.class);
		MyTransactionService svcBean = context.getBean("myTransactionService", MyTransactionService.class);

		// 準備假資料
		List<DeptVO> dList = new ArrayList<>();
		for (int i = 1 ; i <= 3001 ; i++) {
			DeptVO deptVO = new DeptVO(String.format("部門%04d", i), String.format("地區%04d", i));
			dList.add(deptVO);
		}

//		svcBean.saveBatchServiceTypeA(dList); // java.sql.BatchUpdateException: 內送要求的參數太多。伺服器最多支援 2100 個參數。請減少參數數目後再重新傳送要求。
//		svcBean.saveBatchServiceTypeB(dList, 1000);
//		svcBean.saveBatchServiceTypeC(dList, 1000);
		svcBean.saveBatchServiceTypeD(dList, 1000);
//		svcBean.saveBatchServiceTypeE(dList, 1000);

		context.close();
	}

	/**
	 * 會出錯，java.sql.BatchUpdateException: 內送要求的參數太多。伺服器最多支援 2100 個參數。請減少參數數目後再重新傳送要求。
	 */
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, transactionManager = "txManager")
	public void saveBatchServiceTypeA(List<DeptVO> deptList) {
		deptMapper.addDeptsBatch(deptList);
	}

	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, transactionManager = "txManager")
	public void saveBatchServiceTypeB(List<DeptVO> deptList, int commitPen) {
		/**
		 * 等SQL-Spring Transaction結束後才Commit到DB
		 */
		DeptMapper deptMapper = sqlSessionBatchTemplate.getMapper(DeptMapper.class);

		for (int i = 1 ; i <= deptList.size() ; i++) {

			if (i == 1021) {
//				throw new RuntimeException("我中斷囉~測試RollBack");
			}

			int pen = deptMapper.addDept(deptList.get(i - 1));
			if ((i % commitPen == 0) && (i >= commitPen)) {
				sqlSessionBatchTemplate.flushStatements();
				sqlSessionBatchTemplate.clearCache();
			} else if (i == deptList.size()) {
				sqlSessionBatchTemplate.flushStatements();
				sqlSessionBatchTemplate.clearCache();
			}
		}
		System.out.println("=== 結束 ===");
	}

	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, transactionManager = "txManager")
	public void saveBatchServiceTypeC(List<DeptVO> deptList, int commitPen) {
		/**
		 * 等SQL-Spring Transaction結束後才Commit到DB
		 */
		DeptMapper deptMapper = sqlSessionBatchTemplate.getMapper(DeptMapper.class);
		int batchCount = commitPen;//每批commit的個數
		int batchLastIndex = batchCount - 1;// 每批最後一個的下標

		for (int i = 0 ; i < deptList.size() ; i++) {

			int pen = deptMapper.addDept(deptList.get(i));

			if (i == 1021) {
//				throw new RuntimeException("我中斷囉~測試RollBack");
			}

			if (i == batchLastIndex) {
				sqlSessionBatchTemplate.flushStatements();
				sqlSessionBatchTemplate.clearCache();

				batchLastIndex += batchCount;
				if (batchLastIndex >= deptList.size() - 1) {
					batchLastIndex = deptList.size() - 1;
				}

			}

		}
		System.out.println("=== 結束 ===");
	}

	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, transactionManager = "txManager")
	public boolean saveBatchServiceTypeD(List<DeptVO> autoBatchList, int perNum) {
		// 【參考】https://blog.csdn.net/qq_24607837/article/details/81381934

		boolean flag = false;
		try (SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH);) {
			//由於資料庫對於插入插入字段的限制，在這裡對此批量插入的數據進行分批處理
			int batchCount = perNum;//每批commit的個數
			int batchLastIndex = batchCount - 1;// 每批最後一個的下標
			for (int index = 0 ; index <= autoBatchList.size() - 1 ;) {
				if (batchLastIndex > autoBatchList.size() - 1) {
					batchLastIndex = autoBatchList.size() - 1;
//					deptMapper.addDeptsBatchV2(autoBatchList.subList(index, batchLastIndex + 1));
					sqlSession.insert("com.ctbc.mapper.DeptMapper.addDeptsBatchV2", autoBatchList.subList(index, batchLastIndex + 1));
					sqlSession.flushStatements();
//					sqlSession.commit();
					break;// INSERT完畢，break
				} else {
//					deptMapper.addDeptsBatchV2(autoBatchList.subList(index, batchLastIndex + 1));
					sqlSession.insert("com.ctbc.mapper.DeptMapper.addDeptsBatchV2", autoBatchList.subList(index, batchLastIndex + 1));
					sqlSession.flushStatements();
//					sqlSession.commit();
					index = batchLastIndex + 1;// 設置下一輪INSERT的下標
					batchLastIndex = index + (batchCount - 1);
				}
			}

//			sqlSession.commit();
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, transactionManager = "txManager")
	public boolean saveBatchServiceTypeE(List<DeptVO> autoBatchList, int perNum) {
		// 【參考】https://blog.csdn.net/qq_24607837/article/details/81381934

		// 由於資料庫對於插入插入字段的限制，在這裡對此批量插入的數據進行分批處理
		int batchCount = perNum;//每批commit的個數
		int batchLastIndex = batchCount - 1;// 每批最後一個的下標
		for (int index = 0 ; index <= autoBatchList.size() - 1 ;) {
			if (batchLastIndex > autoBatchList.size() - 1) {
				batchLastIndex = autoBatchList.size() - 1;
				int pp = sqlSessionBatchTemplate.insert("com.ctbc.mapper.DeptMapper.addDeptsBatchV2", autoBatchList.subList(index, batchLastIndex + 1));
				sqlSessionBatchTemplate.flushStatements();
				sqlSessionBatchTemplate.clearCache();
				System.out.println(" pp = " + pp);
				break;// INSERT完畢，break
			} else {
				int pp = sqlSessionBatchTemplate.insert("com.ctbc.mapper.DeptMapper.addDeptsBatchV2", autoBatchList.subList(index, batchLastIndex + 1));
				sqlSessionBatchTemplate.flushStatements();
				sqlSessionBatchTemplate.clearCache();
				System.out.println(" pp = " + pp);
				//--------------------------------------------------
				index = batchLastIndex + 1;// 設置下一輪INSERT的下界
				batchLastIndex = index + (batchCount - 1);// 設置下一輪INSERT的上界
				
				//--------------------------------------------------
				if (index == 2000) {
//					throw new RuntimeException("我中斷囉~測試RollBack");
				}
			}
		}

		return true;
	}

}
