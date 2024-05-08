package com.baby_billing.hrs.configs;

import com.baby_billing.hrs.converters.TariffRulesConverter;
import com.baby_billing.hrs.entities.HrsHistory;
import com.baby_billing.hrs.entities.Tariffs;
import com.baby_billing.hrs.entities.Traffic;

import com.baby_billing.hrs.models.Limits;
import com.baby_billing.hrs.models.OverLimit;
import com.baby_billing.hrs.models.Prepaid;
import com.baby_billing.hrs.models.TariffRules;
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
@EnableJpaRepositories(basePackages = {"com.baby_billing.hrs.repositories"},
        entityManagerFactoryRef = "hrsManagerFactory",
        transactionManagerRef = "hrsTransactionManager")
public class HrsDatasourceConfiguration {
    @Bean("hrsDataSourceProperties")
    @ConfigurationProperties("spring.datasource.hrs")
    public DataSourceProperties hrsDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean("hrsDataSource")
    @ConfigurationProperties("spring.datasource.hrs.configs")
    public HikariDataSource hrsDataSource() {
        return hrsDataSourceProperties().initializeDataSourceBuilder()
                .type(HikariDataSource.class)
                .build();
    }

    @Bean(name = "hrsManagerFactory")
    @ConfigurationProperties("spring.jpa.hrs")
    public LocalContainerEntityManagerFactoryBean hrsManagerFactory(
            EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(hrsDataSource())
                .packages(Tariffs.class, HrsHistory.class, Traffic.class, TariffRules.class, Prepaid.class, OverLimit.class, Limits.class, TariffRulesConverter.class)
                .build();
    }


    @Bean(name = "hrsTransactionManager")
    public PlatformTransactionManager hrsTransactionManager(
            final @Qualifier("hrsManagerFactory") LocalContainerEntityManagerFactoryBean hrsManagerFactory) {
        return new JpaTransactionManager(Objects.requireNonNull(hrsManagerFactory.getObject()));
    }
}
