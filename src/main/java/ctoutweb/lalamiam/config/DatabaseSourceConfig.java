//package ctoutweb.lalamiam.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.PropertySource;
//import org.springframework.core.env.Environment;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//import org.springframework.jdbc.datasource.DriverManagerDataSource;
//import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
//import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//
//import javax.sql.DataSource;
//import java.util.Properties;
//
//@Configuration
//@ComponentScan(basePackages = {"ctoutweb.lalamiam.repository"})
//@EnableJpaRepositories(
//        basePackages = {"ctoutweb.lalamiam.repository"},
//        entityManagerFactoryRef = "entityManagerFactory"
//)
//@EnableTransactionManagement
//@PropertySource({"classpath:application.properties"})
//public class DatabaseSourceConfig {
//  private final Environment environment;
//
//  public DatabaseSourceConfig(Environment environment) {
//    this.environment = environment;
//  }
//
//  @Bean(name = "entityManagerFactory")
//  public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
//    LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
//    em.setDataSource(getDatasource());
//    em.setJpaProperties(getJpaProperties());
//    em.setPackagesToScan(new String[] {"ctoutweb.lalamiam.repository.entity"});
//    em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
//    return em;
//  }
//
//  private DataSource getDatasource() {
//    DriverManagerDataSource dataSource = new DriverManagerDataSource();
//    dataSource.setDriverClassName(environment.getProperty("datasource.driver"));
//    dataSource.setUrl(environment.getProperty("datasource.url"));
//    dataSource.setUsername(environment.getProperty("datasource.username"));
//    dataSource.setPassword(environment.getProperty("datasource.password"));
//    return dataSource;
//  }
//
//  private Properties getJpaProperties() {
//    Properties properties = new Properties();
//    properties.setProperty("hibernate-dialect", environment.getProperty("datasource.dialect"));
//    properties.setProperty("hibernate.default_schema", environment.getProperty("datasource.schema"));
//    return properties;
//  }
//}
