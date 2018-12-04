package com.ctbc.mybatis.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.ibatis.executor.BaseExecutor;
import org.apache.ibatis.executor.BatchExecutor;
import org.apache.ibatis.executor.CachingExecutor;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.ExecutorException;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.reflection.ReflectionException;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;


/**
 * 代理拦截器，控制ComplexExecutor的初始化时机
 * Created by cd_huang on 2017/8/23.
 */
public class ProxyExecutorHandler implements InvocationHandler {

	private Executor target;

	private Executor realTarget;

	private boolean isInit = false;

	public ProxyExecutorHandler(Executor target) {
		this.target = target;
		MybatisExecutorContext.bindExecutor(this);
		if (MybatisExecutorContext.initComplexExecutorImmediately) {
			init();
		}

	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		if (!isInit && MybatisExecutorContext.isOpenExecutorMode()) {
			init();
		}
		try {
			return method.invoke(this.target, args);
		} finally {
			String methodName = method.getName();
			if ("commit".equals(methodName) || "rollback".equals(methodName) || "close".equals(methodName)) {
				MybatisExecutorContext.clean();
			}
		}
	}

	public void init() {
		this.target = initComplexExecutor(this.target);
		isInit = true;
	}

	/**
	 * 初始化ComplexExecutor
	 * 
	 * @param target
	 * @return
	 */
	public Executor initComplexExecutor(Executor target) {
		Executor initialTarget = target;
		Object[] result = ProxyExecutorHandler.getLastPluginFromJdkProxy(target);
		Object parentPlugin = result[0];
		target = (Executor) result[1];
		if (target instanceof ComplexExecutor) {
			realTarget = target;
			return initialTarget;
		}
		if (target instanceof BaseExecutor) {
			ComplexExecutor newTarget = new ComplexExecutor(ProxyExecutorHandler.getConfiguration(), target.getTransaction(), getOriginalExecutorType((Executor) target));
			realTarget = newTarget;
			if (parentPlugin == null) {
				return newTarget;
			} else {
				//替换掉Plugin的target属性  
				try {
					Field targetField = FieldUtils.getField(Plugin.class, "target", true);
					FieldUtils.writeField(targetField, parentPlugin, newTarget, true);
					return initialTarget;
				} catch (Throwable e) {
					throw new ReflectionException("replace property 'target' of Plugin error ！", e);
				}
			}
		}
		//替换掉CachingExecutor的delegate属性  
		if (target instanceof CachingExecutor) {
			try {
				Field delegateField = FieldUtils.getField(CachingExecutor.class, "delegate", true);
				Object delegate = FieldUtils.readField(delegateField, target, true);
				if (delegate instanceof BaseExecutor) {
					Executor newDelegate = new ComplexExecutor(ProxyExecutorHandler.getConfiguration(), ((BaseExecutor) delegate).getTransaction(), getOriginalExecutorType((Executor) delegate));
					realTarget = newDelegate;
					FieldUtils.writeField(delegateField, target, newDelegate, true);
					return initialTarget;
				}
			} catch (Throwable e) {
				throw new ReflectionException("replace property 'delegate' of CachingExecutor error ！", e);
			}

		}
		throw new ExecutorException("init ComplexExecutor error ！");
	}

	/**
	 * 获得jdk代理里最里层的一个Plugin对象
	 * 
	 * @param proxy
	 * @return 返回 最里层的一个Plugin对象 和 Plugin的target属性
	 */
	public static Object[] getLastPluginFromJdkProxy(Object proxy) {
		if (proxy instanceof Proxy) {
			InvocationHandler h = Proxy.getInvocationHandler(proxy);
			Object target;
			try {
				Field targetField = FieldUtils.getField(Plugin.class, "target", true);
				target = FieldUtils.readField(targetField, h, true);
			} catch (Throwable e) {
				throw new ReflectionException("get property 'target' of Plugin error ！", e);
			}
			if (target instanceof Proxy) {
				return ProxyExecutorHandler.getLastPluginFromJdkProxy(target);
			} else {
				return new Object[] { h, target };
			}
		} else {
			return new Object[] { null, proxy };
		}
	}

	private static Configuration configuration;

	public static Configuration getConfiguration() {
		if (configuration == null) {
			//从spring上下文中拿到SqlSessionFactory，从而拿到configuration  
//			configuration = SpringContextUtil.getApplicationContext().getBean(SqlSessionFactory.class).getConfiguration();
			configuration = SpringContextUtil.getBean("sqlSessionFactoryBean", SqlSessionFactory.class).getConfiguration();
		}
		return configuration;
	}

	public ExecutorType getOriginalExecutorType() {
		if (realTarget != null) {
			return getOriginalExecutorType(realTarget);
		}
		throw new ExecutorException("ComplexExecutor not init !");
	}

	public static ExecutorType getOriginalExecutorType(Executor executor) {
		if (executor instanceof ComplexExecutor) {
			return ((ComplexExecutor) executor).getOriginalExecutorType();
		}
		if (BatchExecutor.class.getName().equals(executor.getClass().getName())) {
			return ExecutorType.BATCH;
		} else {
			return ExecutorType.SIMPLE;
		}
	}

	public Executor getRealTarget() {
		return realTarget;
	}

	public Executor getTarget() {
		return target;
	}

	public void setTarget(Executor target) {
		this.target = target;
	}
}
