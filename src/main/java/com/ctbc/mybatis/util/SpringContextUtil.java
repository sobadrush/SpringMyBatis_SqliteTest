package com.ctbc.mybatis.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * 取得SpringContext
 * @author Z00040180
 */
public class SpringContextUtil implements ApplicationContextAware {

	public SpringContextUtil() {
		super();
	}

	// Spring Context 
	private static ApplicationContext applicationContext;

	/**
	 * 實現ApplicationContextAware接口的callback方法
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		SpringContextUtil.applicationContext = applicationContext;
	}

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public static Object getBean(String beanId) throws BeansException {
		if (applicationContext == null) {
			return null;
		}
		return applicationContext.getBean(beanId);
	}

	public static <T> T getBean(String beanId, Class<T> clazz) throws BeansException {
		if (applicationContext == null) {
			return null;
		}
		return (T) applicationContext.getBean(beanId, clazz);
	} 
	
}










