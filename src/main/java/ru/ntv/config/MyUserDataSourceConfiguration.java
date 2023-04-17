package ru.ntv.config;

import org.postgresql.xa.PGXADataSource;
import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableJpaRepositories(
        entityManagerFactoryRef = "MyUserDataSourceConfiguration",
        transactionManagerRef = "transactionManager",
        basePackages = {"ru.ntv.repo.user"}
)
public class MyUserDataSourceConfiguration {
    public Map<String, String> myUserJpaProperties() {
        Map<String, String> MyUserJpaProperties = new HashMap<>();
        MyUserJpaProperties.put("hibernate.hbm2ddl.auto", "update");
        MyUserJpaProperties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        MyUserJpaProperties.put("hibernate.show_sql", "true");
        MyUserJpaProperties.put("hibernate.temp.use_jdbc_metadata_defaults", "false");
        MyUserJpaProperties.put("hibernate.transaction.jta.platform", "com.atomikos.icatch.jta.hibernate4.AtomikosPlatform");
        MyUserJpaProperties.put("javax.persistence.transactionType", "JTA");
        return MyUserJpaProperties;
    }
    
    @Bean(name = "myUserEntityManagerFactoryBuilder")
    public EntityManagerFactoryBuilder myUserEntityManagerFactoryBuilder() {
        return new EntityManagerFactoryBuilder(
                new HibernateJpaVendorAdapter(), myUserJpaProperties(), null
        );
    }
    
    @Bean(name = "MyUserDataSourceConfiguration")
    public LocalContainerEntityManagerFactoryBean myUserEntityManager(
            @Qualifier("myUserEntityManagerFactoryBuilder") EntityManagerFactoryBuilder MyUserEntityManagerFactoryBuilder,
            @Qualifier("myUserDataSource") DataSource postgresDataSource
    ) {
        return MyUserEntityManagerFactoryBuilder
                .dataSource(postgresDataSource)
                .packages("ru.ntv.entity.users")
                .persistenceUnit("postgres")
                .properties(myUserJpaProperties())
                .jta(true)
                .build();
    }
    
    @Bean("myUserDataSourceProperties")
    @ConfigurationProperties("spring.datasource.users")
    public DataSourceProperties myUserDataSourceProperties() {
        return new DataSourceProperties();
    }
    
    @Bean("myUserDataSource")
    public DataSource MyUserDataSource(@Qualifier("myUserDataSourceProperties") DataSourceProperties MyUserDataSourceProperties) {
        PGXADataSource ds = new PGXADataSource();
        ds.setUrl(MyUserDataSourceProperties.getUrl());
        ds.setUser(MyUserDataSourceProperties.getUsername());
        ds.setPassword(MyUserDataSourceProperties.getPassword());

        AtomikosDataSourceBean xaDataSource = new AtomikosDataSourceBean();
        xaDataSource.setXaDataSource(ds);
        xaDataSource.setUniqueResourceName("xa_MyUser");
        return xaDataSource;
    }
}