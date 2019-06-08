package com.ctbc.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.ctbc.mapper.DeptMapper;
import com.ctbc.mapper.EmpMapper;
import com.ctbc.model.vo.DeptVO;
import com.ctbc.model.vo.EmpVO;

import _01_Config.RootConfig;
import net.coderbee.mybatis.batch.BatchParameter;

@Transactional
@Service
public class MyTransactionService2 {

	@Autowired
	private DeptMapper deptMapper;

	@Autowired
	private EmpMapper empMapper; 
	

	public static void main(String[] args) {
		System.setProperty("spring.profiles.active", "mssql_env");
//		System.setProperty("spring.profiles.active", "mssql_itoa_env");
		
		ConfigurableApplicationContext context = new AnnotationConfigApplicationContext(RootConfig.class);
		MyTransactionService2 svcBean = context.getBean("myTransactionService2", MyTransactionService2.class);

		// 準備假資料
		List<DeptVO> dList = new ArrayList<>();
		for (int i = 1 ; i <= 3002 ; i++) {
			DeptVO deptVO = new DeptVO(String.format("部門%04d", i), String.format("地區%04d", i));
			dList.add(deptVO);
		}

//		svcBean.saveBatchServiceTypeA(dList); // java.sql.BatchUpdateException: 內送要求的參數太多。伺服器最多支援 2100 個參數。請減少參數數目後再重新傳送要求。
		svcBean.saveBatchServiceTypeB(dList, 100);

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
		
		BatchParameter<DeptVO> depts = BatchParameter.wrap(deptList, commitPen); // 10: batchSize
		int pen = deptMapper.addDeptsBatchV3(depts);
		System.err.println("新增depts成功 = " + pen + " 筆");
		
		EmpVO empVO = new EmpVO("蝙蝠俠", "DC英雄", java.sql.Date.valueOf("2018-05-20"), 20);
		int empPen = empMapper.insert(empVO);
		System.err.println("新增emp成功 : " + empPen + " 筆");
		
		EmpVO empVO2 = new EmpVO("超人", "克拉克_克拉克_克拉克_克拉克_克拉克_克拉克_克拉克_克拉克_克拉克_克拉克_克拉克_克拉克_克拉克_克拉克_克拉克_克拉克_克拉克_克拉克", java.sql.Date.valueOf("2018-05-20"), 20);
		int empPen2 = empMapper.insert(empVO2);
		System.err.println("新增emp成功 : " + empPen2 + " 筆");
		
	}


}
