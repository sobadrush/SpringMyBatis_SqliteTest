package com.ctbc.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MyTransactionService4 {

//	@Autowired
//	private DeptMapper deptMapper;
	
//	@Autowired
//	private EmpMapper empMapper;
	
	@Transactional(propagation = Propagation.REQUIRED , isolation = Isolation.DEFAULT , rollbackFor = Exception.class, transactionManager = "txManager")
	public void method_B() {
		System.out.println(" >>>>>>>>>> MyTransactionService4.method_B() <<<<<<<<<<"); 
		System.out.println(" >>>>>>>>>> MyTransactionService4.method_B() <<<<<<<<<<");
		System.out.println(" >>>>>>>>>> MyTransactionService4.method_B() <<<<<<<<<<");
		
		System.out.println(1/0);
	}
	
}
