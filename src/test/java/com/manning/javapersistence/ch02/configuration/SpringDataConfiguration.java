package com.manning.javapersistence.ch02.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.util.Properties;

//스프링 데이터 리포지토리를 활성화하기 위해 인수로 받은 패지키를 스캔
@EnableJpaRepositories("com.manning.javapersistence.ch02.repositories")
public class SpringDataConfiguration {

    //데이터 소스빈을 생성, JDBC 설정
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.ch.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/ch02?serverTimezone=UTC");
        dataSource.setUsername("root");
        dataSource.setPassword("");
        return dataSource;
    }

    //엔티티 매니저 팩토리를 기반으로 트랜잭션 매니저 빈을 생성, DB와 모든 상호작용은 트랜잭션 경계 내에서 일어나야 하며,
    //스프링 데이터는 트랜잭션 매니저 빈을 필요로 합니다.
    @Bean
    public JpaTransactionManager transactionManager(EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }

    //JPA가 하이버네이트와 상호작용하는데 필요한 JPA 공급자 어탭터 빈을 생성합니다.
    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
        HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();

        //이 공급자 어탭터가 MYSQL DB에 접근하도록 구성
        jpaVendorAdapter.setDatabase(Database.MYSQL);

        //SQL 코드 표시
        jpaVendorAdapter.setShowSql(true);
        return jpaVendorAdapter;
    }



    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {

        //LocalContainerEntityManagerFactoryBean 생성, JPA 표준 컨테이너 부트스트랩 계약에 따라 EntityManagerFactory를 생성하는 팩토리 빈입니다.
        LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();

        //데이터 소스를 설정
        localContainerEntityManagerFactoryBean.setDataSource(dataSource());

        //프로퍼티 설정
        Properties properties = new Properties();
        properties.put("hibernate.hbm2ddl.auto", "create");
        localContainerEntityManagerFactoryBean.setJpaProperties(properties);

        //공급자 어탭터를 설정
        localContainerEntityManagerFactoryBean.setJpaVendorAdapter(jpaVendorAdapter());

        //앤티티 클래스를 스캔할 패키지를 설정
        localContainerEntityManagerFactoryBean.setPackagesToScan("com.manning.javapersistence.cho2");
        return localContainerEntityManagerFactoryBean;
    }
}
