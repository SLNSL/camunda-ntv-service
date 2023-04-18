package ru.ntv.config;

import org.postgresql.xa.PGXADataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;
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
        entityManagerFactoryRef = "ArticleDataSourceConfiguration",
        transactionManagerRef = "transactionManager",
        basePackages = {"ru.ntv.repo.article"}
)
public class ArticleDataSourceConfiguration {

    public Map<String, String> articleJpaProperties() {
        Map<String, String> articleJpaProperties = new HashMap<>();
        articleJpaProperties.put("hibernate.hbm2ddl.auto", "update");
        articleJpaProperties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        articleJpaProperties.put("hibernate.show_sql", "true");
        articleJpaProperties.put("hibernate.temp.use_jdbc_metadata_defaults", "false");
        articleJpaProperties.put("hibernate.transaction.jta.platform", "com.atomikos.icatch.jta.hibernate4.AtomikosPlatform");
        articleJpaProperties.put("javax.persistence.transactionType", "JTA");
        return articleJpaProperties;
    }

    @Bean(name = "articleEntityManagerFactoryBuilder")
    public EntityManagerFactoryBuilder articleEntityManagerFactoryBuilder() {
        return new EntityManagerFactoryBuilder(
                new HibernateJpaVendorAdapter(), articleJpaProperties(), null
        );
    }


    @Bean(name = "ArticleDataSourceConfiguration")
    public LocalContainerEntityManagerFactoryBean getPostgresEntityManager(
            @Qualifier("articleEntityManagerFactoryBuilder") EntityManagerFactoryBuilder articleEntityManagerFactoryBuilder,
            @Qualifier("articleDataSource") DataSource postgresDataSource
    ) {
        return articleEntityManagerFactoryBuilder
                .dataSource(postgresDataSource)
                .packages("ru.ntv.entity.articles")
                .persistenceUnit("postgres")
                .properties(articleJpaProperties())
                .jta(true)
                .build();
    }

    @Bean("articleDataSourceProperties")
    @ConfigurationProperties("spring.datasource.articles")
    public DataSourceProperties articleDataSourceProperties() {
        return new DataSourceProperties();
    }


    @Bean("articleDataSource")
    public DataSource articleDataSource(@Qualifier("articleDataSourceProperties") DataSourceProperties articleDataSourceProperties) {
        // return articleDataSourceProperties.initializeDataSourceBuilder().build();
        PGXADataSource ds = new PGXADataSource();
        ds.setUrl(articleDataSourceProperties.getUrl());
        ds.setUser(articleDataSourceProperties.getUsername());
        ds.setPassword(articleDataSourceProperties.getPassword());

        AtomikosDataSourceBean xaDataSource = new AtomikosDataSourceBean();
        xaDataSource.setXaDataSource(ds);
        xaDataSource.setUniqueResourceName("xa_article");
        return xaDataSource;
    }
}