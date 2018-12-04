package _01_Config;

import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.ctbc.mybatis.util.SpringContextUtil;

@Configuration
@EnableTransactionManagement
@MapperScan(basePackages = { "com.ctbc.mapper" })
@ComponentScan(basePackages = { "com.ctbc.service"  })
public class RootConfig {

	@Value("${spring.profiles.active}")
	private String springActiveProfile;
	
	@Value("classpath:/_00_建立資料表/Create_SqliteDB_Script.sql")
	private Resource schemaScript_SQLITE;

	@Value("classpath:/_00_建立資料表/Insert_SqliteDB_Script.sql")
	private Resource dataScript_SQLITE;
	
	@Value("classpath:/_00_建立資料表/Create_MSSQL_Script.sql")
	private Resource schemaScript_MSSQL;
	
	@Value("classpath:/_00_建立資料表/Insert_MSSQL_Script.sql")
	private Resource dataScript_MSSQL;

	@Bean
	public SpringContextUtil springContextUtil() {
		return new SpringContextUtil();
	}
	
	@Bean(name = "driverManagerDS")
	@Profile("sqlite_env")
	public DataSource driverManagerDatasourceSqlite() {
		String connectionString = "jdbc:sqlite:" + System.getProperty("user.dir") + "/" + "testDB.db";
		DriverManagerDataSource ds = new DriverManagerDataSource();
		ds.setUrl(connectionString);
		ds.setDriverClassName("org.sqlite.JDBC");
		// ------------------------------------------
//		Properties props = new Properties();
//		props.setProperty("allowMultiQueries", "true");
//		ds.setConnectionProperties(props); // allowMultiQueries=true
		return ds;
	}

	@Bean(name = "driverManagerDS")
	@Profile("mssql_env")
	public DataSource driverManagerDatasourceMS() {
		DriverManagerDataSource ds = new DriverManagerDataSource();
		ds.setUrl("jdbc:sqlserver://localhost:1433;databaseName=DB_Emp_Dept");
		ds.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		ds.setUsername("sa");
		ds.setPassword("sa123456");
		return ds;
	}
	
	@Bean(name = "driverManagerDS")
	@Profile("mssql_itoa_env")
	public DataSource driverManagerDatasourceMS_ITOA() {
		DriverManagerDataSource ds = new DriverManagerDataSource();
		ds.setUrl("jdbc:sqlserver://172.24.17.52:1803;databaseName=ITOA_MAIN");
		ds.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		ds.setUsername("ITOA_MAIN_mod");
		ds.setPassword("f3ru9cj4");
		return ds;
	}

	@Bean
	public PlatformTransactionManager txManager(DataSource ds) {
		return new DataSourceTransactionManager(ds);
	}

	/*****************************************
	 ** DataBase Initializer (DB初始化元件) **
	 *****************************************/
	@Bean
	@Profile(value = { "sqlite_env" , "mssql_env" })
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
		System.out.println(springActiveProfile);
		
		final ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
		
		switch (springActiveProfile) {
			case "mssql_env":
			case "mssql_itoa_env":
				populator.addScript(this.schemaScript_MSSQL);
				populator.addScript(this.dataScript_MSSQL);
				break;
			case "sqlite_env":
				populator.addScript(this.schemaScript_SQLITE);
				populator.addScript(this.dataScript_SQLITE);
				break;
			default:
				break;
		}
		
		return populator;
	}

	@Bean(name = "sqlSessionFactoryBean")
	public SqlSessionFactory sqlSessionFactoryBean(@Qualifier("driverManagerDS") DataSource ds, ApplicationContext applicationContext) throws Exception {
		System.out.println("啟用連線池：" + ds);
		final SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
		sqlSessionFactory.setDataSource(ds);
		sqlSessionFactory.setConfigLocation(new ClassPathResource("mybatis-config.xml"));
		//sqlSessionFactory.setMapperLocations(applicationContext.getResources("classpath:META-INF/mybatis/mappers/**/*.xml"));
		//sqlSessionFactory.setMapperLocations(applicationContext.getResources("classpath:com/ctbc/model/vo/**.xml"));
		sqlSessionFactory.setMapperLocations(applicationContext.getResources("classpath:XML_Mappers/*.xml"));
		sqlSessionFactory.setTypeAliasesPackage("com.ctbc.model.vo");
		return sqlSessionFactory.getObject();
	}

	@Bean
	public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
		SqlSessionTemplate sqlSessTemplate = new SqlSessionTemplate(sqlSessionFactory, ExecutorType.SIMPLE);

		// insert/update/delete 無法在交易commit前返回異動的資料列數(會返回類似-2147482646的負數)
//		SqlSessionTemplate sqlSessTemplate = new SqlSessionTemplate(sqlSessionFactory, ExecutorType.BATCH); 
		return sqlSessTemplate;
	}
	
	public static void main(String[] args) {
		
//		System.setProperty("spring.profiles.active", "sqlite_env");
//		System.setProperty("spring.profiles.active", "mssql_env");
		System.setProperty("spring.profiles.active", "mssql_itoa_env");
		
		// 測試自動建表 & 填充資料
		ConfigurableApplicationContext ctx = new AnnotationConfigApplicationContext(RootConfig.class);
		
		try {
			String dsName = ctx.getBean(DataSource.class).getConnection().getMetaData().getDatabaseProductName();
			System.out.println("資料庫廠商 : " + dsName);
		} catch (BeansException | SQLException e) {
			e.printStackTrace();
		}
		
		ctx.close();
	}
	
}
