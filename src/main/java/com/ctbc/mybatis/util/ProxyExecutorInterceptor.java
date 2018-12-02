package com.ctbc.mybatis.util;

import java.lang.reflect.Proxy;
import java.util.Properties;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Invocation;

/**
 * 使用插件生成代理执行器
 * Created by cd_huang on 2017/8/22.
 */
public class ProxyExecutorInterceptor implements Interceptor {

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		return null;
	}

	@Override
	public Object plugin(Object target) {
		if (target instanceof Executor == false) {
			return target;
		}
		return Proxy.newProxyInstance(
				target.getClass().getClassLoader(),
				target.getClass().getInterfaces(),
				new ProxyExecutorHandler((Executor) target));
	}

	@Override
	public void setProperties(Properties properties) {
	}
}
