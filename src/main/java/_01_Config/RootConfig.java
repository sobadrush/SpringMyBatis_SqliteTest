package _01_Config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@MapperScan(basePackages = { "com.ctbc.mapper" })
public class RootConfig {

	@Bean(name = "driverManagerDS")
	public DataSource driverManagerDatasource() {
		DriverManagerDataSource ds = new DriverManagerDataSource();
		ds.setUrl("jdbc:sqlite:" + System.getProperty("user.dir") + "/" + "testDB.db");
		ds.setDriverClassName("org.sqlite.JDBC");
		return ds;
	}

	@Bean
	public PlatformTransactionManager txManager(DataSource ds) {
		return new DataSourceTransactionManager(ds);
	}
	
	@Bean
	public SqlSessionFactory sqlSessionFactoryBean(@Qualifier("driverManagerDS") DataSource ds, ApplicationContext applicationContext) throws Exception {
		System.out.println("啟用連線池：" + ds);
		final SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
		sqlSessionFactory.setDataSource(ds);
		//sqlSessionFactory.setMapperLocations(applicationContext.getResources("classpath:META-INF/mybatis/mappers/**/*.xml"));
		//sqlSessionFactory.setMapperLocations(applicationContext.getResources("classpath:com/ctbc/model/vo/**.xml"));
		sqlSessionFactory.setMapperLocations(applicationContext.getResources("classpath:XML_Mappers/*.xml"));
		sqlSessionFactory.setTypeAliasesPackage("com.ctbc.model.vo");
		return sqlSessionFactory.getObject();
	}
}
