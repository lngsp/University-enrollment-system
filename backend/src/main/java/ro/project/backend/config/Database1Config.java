package ro.project.backend.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableJpaRepositories(
        basePackages = "ro.project.backend.repositories.db1",
        entityManagerFactoryRef = "database1EntityManagerFactory",
        transactionManagerRef = "database1TransactionManager"
)
public class Database1Config {

    @Primary
    @Bean(name = "database1DataSource")
    @ConfigurationProperties(prefix = "spring.db1.datasource")
    public DataSource database1DataSource() {
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean(name = "database1EntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean database1EntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("database1DataSource") DataSource dataSource) {
        Map<String, String> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", "update");

        return builder
                .dataSource(dataSource)
                .packages("ro.project.backend.entities")
                .properties(properties)
                .persistenceUnit("database1")
                .build();
    }

    @Primary
    @Bean(name = "database1TransactionManager")
    public PlatformTransactionManager database1TransactionManager(
            @Qualifier("database1EntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
