package com.baby_billing.brt.configs;

import com.baby_billing.brt.entities.BrtHistory;
import com.baby_billing.brt.entities.Client;
import com.baby_billing.brt.entities.TariffPaymentHistory;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.Objects;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = {"com.baby_billing.brt.repositories"},
        entityManagerFactoryRef = "brtManagerFactory",
        transactionManagerRef = "brtTransactionManager")
public class BrtDatasourceConfiguration {
    @Bean("brtDataSourceProperties")
    @ConfigurationProperties("spring.datasource.brt")
    public DataSourceProperties brtDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean("brtDataSource")
    @ConfigurationProperties("spring.datasource.brt.configs")
    public HikariDataSource brtDataSource() {
        return brtDataSourceProperties().initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();
    }

    @Bean(name = "brtManagerFactory")
    @ConfigurationProperties("spring.jpa.brt")
    public LocalContainerEntityManagerFactoryBean brtManagerFactory(
            EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(brtDataSource())
                .packages(Client.class, BrtHistory.class, TariffPaymentHistory.class)
                .build();
    }

    @Bean(name = "brtTransactionManager")
    public PlatformTransactionManager brtTransactionManager(
            final @Qualifier("brtManagerFactory") LocalContainerEntityManagerFactoryBean brtManagerFactory) {
        return new JpaTransactionManager(Objects.requireNonNull(brtManagerFactory.getObject()));
    }
}
