package com.baby_billing.cdr_generator.configs;

import com.baby_billing.cdr_generator.entities.Client;
import com.baby_billing.cdr_generator.entities.History;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Objects;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.baby_billing.cdr_generator.repositories",
        entityManagerFactoryRef = "cdrManagerFactory",
        transactionManagerRef = "cdrTransactionManager")
public class CdrDatasourceConfiguration {
    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource.cdr")
    public DataSourceProperties cdrDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource.cdr.configuration")
    public DataSource cdrDataSource() {
        return cdrDataSourceProperties().initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();
    }

    @Primary
    @Bean(name = "cdrManagerFactory")
    @ConfigurationProperties("spring.jpa.cdr")
    public LocalContainerEntityManagerFactoryBean cdrManagerFactory(
            EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(cdrDataSource())
                .packages(Client.class, History.class)
                .build();
    }

    @Primary
    @Bean(name = "cdrTransactionManager")
    public PlatformTransactionManager cdrTransactionManager(
            final @Qualifier("cdrManagerFactory") LocalContainerEntityManagerFactoryBean cdrManagerFactory) {
        return new JpaTransactionManager(Objects.requireNonNull(cdrManagerFactory.getObject()));
    }
}
