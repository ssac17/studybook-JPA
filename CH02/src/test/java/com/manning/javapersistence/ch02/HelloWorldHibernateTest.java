package com.manning.javapersistence.ch02;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.Test;

import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HelloWorldHibernateTest {

    private static SessionFactory createSessionFactory() {

        //먼저 configuration 생성
        Configuration configuration = new Configuration();

        //configure메서드 호출, Message를 애너테이션이 지정된 클래스로 추가
        //configure메서드를 실행하면 기본 hibernate.cfg.xml 파일의 내용이 로드 됩니다.
        configuration.configure().addAnnotatedClass(Message.class);

        //serviceRegistry는 SessionFactory에 접근해야 하는 서비스를 관리합니다.
        //서비스는 하이버네이트에 다양한 기능의 착탈식??(pluggable) 구현제를 제공하는 클래스입니다.
        StandardServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                                                .applySettings(configuration.getProperties()).build();

        //앞에서 만든 구성과 레지스트리를 사용해 SessionFactory를 만듭니다.
        return configuration.buildSessionFactory(serviceRegistry);
    }


    @Test
    void storeLoadMessage() {
        //SessionFactory 생성
        //생성메서드에 StandardServiceRegistry, 아래 Session안 인터페이스는 AutoCloseAble이 구현되어 있습니다.
        try(SessionFactory sessionFactory = createSessionFactory();
            Session session = sessionFactory.openSession()) { //try-with-resources

            //표준 트랜잭션 API에 접근, 이 실행 스레드에서 트랜잭션을 시작합니다.
            session.beginTransaction();

            //Message 도메인 인스턴스 생성
            Message message = new Message();
            message.setText("Hello World from Hibernate!");

            //비영속 -> 영속성 컨텍스트에 등록해서 영속화
            //데이터베이스 호출을 즉시 수행하지는 않으며, 네이티브 하이버네이트 API표는 표준 JPA와 매우 유사하며, 대부분의 메서드명이 동일합니다.
            session.persist(message);

            //세션과 데이터베이스를 동기화하고 트랜잭션 커밋시 현재 세션을 자동으로 닫습니다.(AutoCloseAble)
            session.getTransaction().commit();

            //다시 트랜잭션 시작
            session.beginTransaction();

            //CriteriaQuery 인스턴스 생성
            //CriteriaQuery와 CriteriaBuilder는 프로그래밍 방식으로 쿼리를 생성할수 있는 Criteria API에 속합니다.
            CriteriaQuery<Message> criteriaQuery = session.getCriteriaBuilder().createQuery(Message.class);
            criteriaQuery.from(Message.class);

            //getResultList() 메서드를 호출 결과를 가져옵니다.
            List<Message> messages = session.createQuery(criteriaQuery).getResultList();

            //트랜잭션 커밋
            session.getTransaction().commit();

            assertAll(
                    //DB에서 조회된 메시지 크기를 확인
                    () -> assertEquals(1, messages.size()),
                    //영속화한 메시지가 DB에 있는지 확인
                    () -> assertEquals("Hello World from Hibernate!", messages.get(0).getText())
            );
        }

    }
}
