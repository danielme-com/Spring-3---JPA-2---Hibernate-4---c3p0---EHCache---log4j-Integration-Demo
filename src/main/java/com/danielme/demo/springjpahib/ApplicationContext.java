package com.danielme.demo.springjpahib;

import java.beans.PropertyVetoException;
import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.mchange.v2.c3p0.ComboPooledDataSource;

@Configuration
@PropertySource("classpath:db.properties")
@ComponentScan("com.danielme")
@EnableTransactionManagement
public class ApplicationContext {

    @Bean(destroyMethod = "close")
    DataSource dataSource(Environment env) {
        ComboPooledDataSource ds = new ComboPooledDataSource();
        try {
            ds.setDriverClass(env.getRequiredProperty("jdbc.driverClassName"));
        } catch (IllegalStateException | PropertyVetoException ex) {
            throw new RuntimeException(
                    "error while setting the driver class name in the datasource", ex);
        }
        ds.setJdbcUrl(env.getRequiredProperty("jdbc.url"));
        ds.setUser(env.getRequiredProperty("jdbc.username"));
        ds.setPassword(env.getRequiredProperty("jdbc.password"));
        ds.setAcquireIncrement(env.getRequiredProperty("c3p0.acquire_increment", Integer.class));
        ds.setMinPoolSize(env.getRequiredProperty("c3p0.min_size", Integer.class));
        ds.setMaxPoolSize(env.getRequiredProperty("c3p0.max_size", Integer.class));
        ds.setMaxIdleTime(env.getRequiredProperty("c3p0.max_idle_time", Integer.class));
        ds.setUnreturnedConnectionTimeout(env.getRequiredProperty(
                "c3p0.unreturned_connection_timeout", Integer.class));

        return ds;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource,
            Environment env) {
        LocalContainerEntityManagerFactoryBean entityManager = new LocalContainerEntityManagerFactoryBean();
        //entityManager.setPersistenceXmlLocation("classpath*:META-INF/persistence.xml");
        //entityManager.setPersistenceUnitName("hibernatePersistenceUnit");
        entityManager.setPackagesToScan("com.danielme.demo.springjpahib");
        entityManager.setDataSource(dataSource);
        entityManager.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        
        Properties jpaProperties = new Properties();      
        jpaProperties.put("hibernate.hbm2ddl.auto", env.getRequiredProperty("hibernate.hbm2ddl.auto"));
        jpaProperties.put("hibernate.dialect", env.getRequiredProperty("hibernate.dialect"));
        jpaProperties.put("hibernate.connection.autocommit", env.getRequiredProperty("hibernate.connection.autocommit"));
        jpaProperties.put("hibernate.cache.use_second_level_cache", env.getRequiredProperty("hibernate.cache"));
        jpaProperties.put("hibernate.cache.use_query_cache", env.getRequiredProperty("hibernate.query.cache"));
        jpaProperties.put("hibernate.cache.provider_class", "org.hibernate.cache.EhCacheProvider");
        jpaProperties.put("hibernate.cache.region.factory_class", "org.hibernate.cache.ehcache.EhCacheRegionFactory");
        jpaProperties.put("hibernate.generate_statistics", env.getRequiredProperty("hibernate.statistics"));        
        jpaProperties.put("hibernate.show_sql", env.getRequiredProperty("hibernate.show_sql"));        
        jpaProperties.put("hibernate.format_sql", env.getRequiredProperty("hibernate.format_sql"));        
       
        entityManager.setJpaProperties(jpaProperties);

        return entityManager;
    }
    
    @Bean
    JpaTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }
}
