package com.nay.spring.config;

import java.beans.PropertyVetoException;
import java.util.Properties;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.mchange.v2.c3p0.ComboPooledDataSource;

@Configuration
@EnableWebMvc
@EnableTransactionManagement
@ComponentScan(basePackages = "com.nay.spring")
@PropertySource({"classpath:resources/psql.properties"})
public class AppConfig implements WebMvcConfigurer {
	
	@Autowired
	private Environment environment;
	
	private Logger logger=Logger.getLogger(getClass().getName());
	
	
	@Bean
	public DataSource dataSource() {
		ComboPooledDataSource pooledDataSource = new ComboPooledDataSource();
		
		try {
			pooledDataSource.setDriverClass(environment.getProperty("jdbc.driver"));
			pooledDataSource.setJdbcUrl(environment.getProperty("jdbc.url"));
			pooledDataSource.setUser(environment.getProperty("jdbc.username"));
			pooledDataSource.setPassword(environment.getProperty("jdbc.password"));
			
			logger.info(">>>>> jdbc.url="+environment.getProperty("jdbc.url"));
			logger.info(">>>>> jdbc.user="+environment.getProperty("jdbc.username"));
			
			pooledDataSource.setInitialPoolSize(getIntProperty("connection.pool.initialPoolSize"));
			pooledDataSource.setMinPoolSize(getIntProperty("connection.pool.minPoolSize"));
			pooledDataSource.setMaxPoolSize(getIntProperty("connection.pool.maxPoolSize"));
			pooledDataSource.setMaxIdleTime(getIntProperty("connection.Pool.maxIdleTime"));
			
			
		} catch (PropertyVetoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return pooledDataSource;
	}
	
	private Properties getHibernateProperties() {
		Properties prop=new Properties();
		prop.setProperty("hibernate.dialect", environment.getProperty("hibernate.dialect"));
		prop.setProperty("hibernate.show_sql",environment.getProperty("hibernate.show_sql"));
		return prop;
	}

	private int getIntProperty(String property) {
		int value=Integer.parseInt(environment.getProperty(property));
		return value;
	}
	
	@Bean
	public LocalSessionFactoryBean sessionFactory() {
		LocalSessionFactoryBean sessionFactory=new LocalSessionFactoryBean();
		sessionFactory.setDataSource(dataSource());
		sessionFactory.setPackagesToScan(environment.getProperty("hibernate.packagesToScan"));
		sessionFactory.setHibernateProperties(getHibernateProperties());
		return sessionFactory;
	}
	
	@Bean
	@Autowired
	public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
		HibernateTransactionManager txManager=new HibernateTransactionManager();
		txManager.setSessionFactory(sessionFactory);
		return txManager;
		
	}
}
