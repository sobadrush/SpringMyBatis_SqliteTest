package com.ctbc.mybatis.util;

import java.sql.SQLException;
import java.util.List;

import org.apache.ibatis.executor.BatchExecutor;
import org.apache.ibatis.executor.BatchResult;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.transaction.Transaction;

/**
 * 扩展mybatis支持批处理和非批处理两种模式的复合执行器
 * Created by cd_huang on 2017/8/22.
 */
public class ComplexExecutor extends BatchExecutor {

	private ExecutorType originalExecutorType;

	public ComplexExecutor(Configuration configuration, Transaction transaction, ExecutorType originalExecutorType) {
		super(configuration, transaction);
		this.originalExecutorType = originalExecutorType;

	}

	public int doUpdate(MappedStatement ms, Object parameterObject) throws SQLException {
		int result = super.doUpdate(ms, parameterObject);
		if (!MybatisExecutorContext.isBatchExecutorMode()) {
			List<BatchResult> results = this.doFlushStatements(false);
			return results.get(0).getUpdateCounts()[0];
		}
		return result;
	}

	public List<BatchResult> doFlushStatements(boolean isRollback) throws SQLException {
		List<BatchResult> results = super.doFlushStatements(isRollback);
		MybatisExecutorContext.getCheckBatchResultHook().checkBatchResult(results);
		return results;
	}

	public ExecutorType getOriginalExecutorType() {
		return originalExecutorType;
	}

}
