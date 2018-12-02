package com.ctbc.mybatis.util;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import org.apache.ibatis.executor.BatchResult;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.ExecutorException;
import org.apache.ibatis.session.ExecutorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * mybatis执行器上下文
 * Created by cd_huang on 2017/8/22.
 */
public class MybatisExecutorContext {

	private static Logger logger = LoggerFactory.getLogger(MybatisExecutorContext.class);

	private static final ThreadLocal<ExecutorType> currentExecutorType = new ThreadLocal<>();

	private static final ThreadLocal<ProxyExecutorHandler> executorResource = new ThreadLocal<>();

	private static final ThreadLocal<CheckBatchResultHook> checkBatchResultHook = new ThreadLocal<>();
	/**
	 * 是否立刻初始化ComplexExecutor
	 */
	public static boolean initComplexExecutorImmediately = true;

	public static void openSimpleExecutorMode() {
		currentExecutorType.set(ExecutorType.SIMPLE);
	}

	public static void openBatchExecutorMode() {
		currentExecutorType.set(ExecutorType.BATCH);
	}

	public static void closeExecutorMode() {
		currentExecutorType.remove();
	}

	public static List<BatchResult> doFlushStatements() {
		ProxyExecutorHandler interceptor = executorResource.get();
		if (interceptor == null) {
			return Collections.emptyList();
		}
		Executor executor = interceptor.getRealTarget();
		if (executor instanceof ComplexExecutor) {
			try {
				return ((ComplexExecutor) executor).doFlushStatements(false);
			} catch (SQLException e) {
				throw new RuntimeException("doFlushStatements error!", e);
			}
		}
		throw new ExecutorException("doFlushStatements must invoke on ExecutorType.BATCH!");
	}

	public static void bindExecutor(ProxyExecutorHandler interceptor) {
		executorResource.set(interceptor);
	}

	public static boolean isOpenExecutorMode() {
		return currentExecutorType.get() != null;
	}

	public static Boolean isBatchExecutorMode() {
		ExecutorType executorType = currentExecutorType.get();
		if (executorType == null) {
			ProxyExecutorHandler interceptor = executorResource.get();
			executorType = interceptor == null ? null : interceptor.getOriginalExecutorType();
		}
		return ExecutorType.BATCH.equals(executorType);
	}

	public static void clean() {
		currentExecutorType.remove();
		executorResource.remove();
	}

	private static CheckBatchResultHook defaultCheckBatchResultHook = new DefaultCheckBatchResultHook();

	public static CheckBatchResultHook getCheckBatchResultHook() {
		CheckBatchResultHook hook = checkBatchResultHook.get();
		return hook == null ? defaultCheckBatchResultHook : hook;
	}

	public static void setCheckBatchResultHook(CheckBatchResultHook hook) {
		checkBatchResultHook.set(hook);
	}

	/**
	 * 校验批处理结果的默认机制(默认更新影响记录数为0的时候logger打印警告信息)
	 */
	private static class DefaultCheckBatchResultHook implements CheckBatchResultHook {
		@Override
		public boolean checkBatchResult(List<BatchResult> results) {
			for (BatchResult result : results) {
				for (int i = 0 ; i < result.getUpdateCounts().length ; i++) {
					if (result.getUpdateCounts()[i] <= 0) {
						logger.warn(" sql statementId " + result.getMappedStatement().getId() + "," + result.getMappedStatement().getSqlCommandType().name() + " effect 0 rows ! ");
					}
				}
			}
			return true;
		}
	}
}
