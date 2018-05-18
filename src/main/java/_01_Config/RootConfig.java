package _01_Config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
public class RootConfig {

	@Bean
	public DataSource driverManagerDatasource() {
		DriverManagerDataSource ds = new DriverManagerDataSource();
		ds.setUrl("jdbc:sqlite:" + System.getProperty("user.dir") + "/" + "testDB.db");
		ds.setDriverClassName("org.sqlite.JDBC");
		return ds;
	}

}
