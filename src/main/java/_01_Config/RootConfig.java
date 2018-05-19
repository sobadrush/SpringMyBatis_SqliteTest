package _01_Config;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@MapperScan(basePackages = { "com.ctbc.mapper" })
public class RootConfig {

	@Value("classpath:/_00_建立資料表/Create_SqliteDB_Script.sql")
	private Resource schemaScript;

	@Value("classpath:/_00_建立資料表/Insert_SqliteDB_Script.sql")
	private Resource dataScript;

	@Bean(name = "driverManagerDS")
	public DataSource driverManagerDatasource() {
		Properties props = new Properties();
		props.setProperty("allowMultiQueries", "true");
		String connectionString = "jdbc:sqlite:" + System.getProperty("user.dir") + "/" + "testDB.db";
		DriverManagerDataSource ds = new DriverManagerDataSource(connectionString, props);
		ds.setUrl(connectionString); // allowMultiQueries=true
		ds.setDriverClassName("org.sqlite.JDBC");
		return ds;
	}

//	@Bean(name = "driverManagerDS")
//	public DataSource driverManagerDatasource() {
//		DriverManagerDataSource ds = new DriverManagerDataSource();
//		ds.setUrl("jdbc:sqlserver://localhost:1433;databaseName=DB_Emp_Dept");
//		ds.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
//		ds.setUsername("sa");
//		ds.setPassword("sa123456");
//		return ds;
//	}

	@Bean
	public PlatformTransactionManager txManager(DataSource ds) {
		return new DataSourceTransactionManager(ds);
	}

	/*****************************************
	 ** DataBase Initializer (DB初始化元件) **
	 *****************************************/
	@Bean
	public DataSourceInitializer dataSourceInitializer(DataSource dataSource) {
		final DataSourceInitializer initializer = new DataSourceInitializer();
		initializer.setDataSource(dataSource);
		initializer.setDatabasePopulator(this.databasePopulator());
		return initializer;
	}

	/*****************************************
	 ********** DataBase 資料填充器 **********
	 *****************************************/
	private DatabasePopulator databasePopulator() {
		final ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
		populator.addScript(this.schemaScript);
		populator.addScript(this.dataScript);
		return populator;
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

	public static void main(String[] args) {
		// 測試自動建表
		ConfigurableApplicationContext ctx = new AnnotationConfigApplicationContext(RootConfig.class);
		ctx.close();
	}
}
