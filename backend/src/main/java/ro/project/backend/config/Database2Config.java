package ro.project.backend.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableJpaRepositories(
        basePackages = "ro.project.backend.repositories.db2",
        entityManagerFactoryRef = "database2EntityManagerFactory",
        transactionManagerRef = "database2TransactionManager"
)
public class Database2Config {

    @Bean(name = "database2DataSource")
    @ConfigurationProperties(prefix = "spring.db2.datasource")
    public DataSource database2DataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "database2EntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean database2EntityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("database2DataSource") DataSource dataSource) {
        Map<String, String> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", "update");

        return builder
                .dataSource(dataSource)
                .packages("ro.project.backend.entities")
                .properties(properties)
                .persistenceUnit("database2")
                .build();
    }

    @Bean(name = "database2TransactionManager")
    public PlatformTransactionManager database2TransactionManager(
            @Qualifier("database2EntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}

