//package ru.ntv.config;
//
//import org.postgresql.xa.PGXADataSource;
//import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
//import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
//
//import javax.sql.DataSource;
//import java.util.HashMap;
//import java.util.Map;
//
//@Configuration
//@EnableJpaRepositories(
//        entityManagerFactoryRef = "PeopleDataSourceConfiguration",
//        transactionManagerRef = "transactionManager",
//        basePackages = {"ru.ntv.repo.user"}
//)
//public class PeopleDataSourceConfiguration {
//    public Map<String, String> peopleJpaProperties() {
//        Map<String, String> MyUserJpaProperties = new HashMap<>();
//        MyUserJpaProperties.put("hibernate.hbm2ddl.auto", "update");
//        MyUserJpaProperties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
//        MyUserJpaProperties.put("hibernate.show_sql", "true");
//        MyUserJpaProperties.put("hibernate.temp.use_jdbc_metadata_defaults", "false");
//        MyUserJpaProperties.put("hibernate.transaction.jta.platform", "com.atomikos.icatch.jta.hibernate4.AtomikosPlatform");
//        MyUserJpaProperties.put("javax.persistence.transactionType", "JTA");
//        return MyUserJpaProperties;
//    }
//
//    @Bean(name = "peopleEntityManagerFactoryBuilder")
//    public EntityManagerFactoryBuilder peopleEntityManagerFactoryBuilder() {
//        return new EntityManagerFactoryBuilder(
//                new HibernateJpaVendorAdapter(), peopleJpaProperties(), null
//        );
//    }
//
//    @Bean(name = "PeopleDataSourceConfiguration")
//    public LocalContainerEntityManagerFactoryBean peopleEntityManager(
//            @Qualifier("peopleEntityManagerFactoryBuilder") EntityManagerFactoryBuilder MyUserEntityManagerFactoryBuilder,
//            @Qualifier("peopleDataSource") DataSource postgresDataSource
//    ) {
//        return MyUserEntityManagerFactoryBuilder
//                .dataSource(postgresDataSource)
//                .packages("ru.ntv.entity.users")
//                .persistenceUnit("postgres")
//                .properties(peopleJpaProperties())
//                .jta(true)
//                .build();
//    }
//
//    @Bean("peopleDataSourceProperties")
//    @ConfigurationProperties("spring.datasource.people")
//    public DataSourceProperties peopleDataSourceProperties() {
//        return new DataSourceProperties();
//    }
//
//    @Bean("peopleDataSource")
//    public DataSource peopleDataSource(@Qualifier("peopleDataSourceProperties") DataSourceProperties MyUserDataSourceProperties) {
//        PGXADataSource ds = new PGXADataSource();
//        ds.setUrl(MyUserDataSourceProperties.getUrl());
//        ds.setUser(MyUserDataSourceProperties.getUsername());
//        ds.setPassword(MyUserDataSourceProperties.getPassword());
//
//        AtomikosDataSourceBean xaDataSource = new AtomikosDataSourceBean();
//        xaDataSource.setXaDataSource(ds);
//        xaDataSource.setUniqueResourceName("xa_people");
//        return xaDataSource;
//    }
//}