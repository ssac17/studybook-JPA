package com.manning.javapersistence.ch02;

import org.hibernate.SessionFactory;

import javax.persistence.EntityManagerFactory;

public class JPAToHibernateTest {

    //JPA -> Hibernate
    private static SessionFactory getSessionFactory(EntityManagerFactory entityManagerFactory) {
        //EntityManagerFactory에는 JPA 구현체 클래스에 속하는 객체를 반환하는 unwrap 메서드가 있습니다.
        return entityManagerFactory.unwrap(SessionFactory.class);
    }
}
