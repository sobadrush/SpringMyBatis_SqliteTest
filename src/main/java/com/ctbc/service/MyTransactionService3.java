package com.ctbc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.ctbc.mapper.DeptMapper;
import com.ctbc.model.vo.DeptVO;

import _01_Config.RootConfig;

@Service
public class MyTransactionService3 {

	@Autowired
	private DeptMapper deptMapper;
	
	@Autowired
	private MyTransactionService4 myTransactionService4;
	
//	@Autowired
//	private EmpMapper empMapper;
	
	@Transactional(propagation = Propagation.REQUIRED , isolation = Isolation.DEFAULT , rollbackFor = Exception.class, transactionManager = "txManager")
	public void method_A() {
		System.out.println(" >>>>>>>>>> method_A() <<<<<<<<<<");
		System.out.println(" >>>>>>>>>> method_A() <<<<<<<<<<");
		System.out.println(" >>>>>>>>>> method_A() <<<<<<<<<<");
		
		// (1)
		deptMapper.addDept(new DeptVO("國防部", "博愛特"));
		
		// (2)
		try {
			/**
			 * 把别的service方法try住，不希望它阻断我们的程序继续执行。表面上看合乎情理没毛病，but 
			 * 會噴： org.springframework.transaction.UnexpectedRollbackException: Transaction rolled back because it has been marked as rollback-only
			 * 
			 * ref. https://blog.csdn.net/f641385712/article/details/80445912
			 */
			myTransactionService4.method_B();
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			e.printStackTrace();
		}
		
	}
	
	
	public static void main(String[] args) { 
		
		System.setProperty("spring.profiles.active", "mssql_env");
		
		ConfigurableApplicationContext context = new AnnotationConfigApplicationContext(RootConfig.class);
		MyTransactionService3 myTransactionSvc3 = context.getBean("myTransactionService3", MyTransactionService3.class);
		
		myTransactionSvc3.method_A();
		
		context.close();
	}
	
}
