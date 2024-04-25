package com.manning.javapersistence.ch02;

import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HelloWorldJPATest {

    @Test
    void storeLoadMessage() {
        /*
            DB와 상호작용하려면 EntityManagerFactory가 필요, 이 API는 영속성 단위를 나타내며,
            대부분의 애플리케이션에는 구성된 영속성 단위 각각에 하나의 EntityManagerFactory가 필요합니다.
            EntityManagerFactory는 스레드에 안전하며 데이터베이스에 접근하는 애플리케이션의 모든 코드에서 이를 공유해야 합니다.
         */
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("ch02");

        try {
            //EntityManager를 생성해서 데이터베이스와의 새 세션을 시작합니다.
            //EntityManager는 모든 영속성 연산의 컨텍스트가 됩니다.
            EntityManager em = emf.createEntityManager();

            //표준 트랜잭션 API에 접근한 후 이 실행 스레드에서 트랜잭션을 시작합니다.
            em.getTransaction().begin();

            //매핑된 도메인 모델 클래스인 Message의 새 인스턴스를 생성하고 text 프로퍼티를 설정합니다.
            Message message = new Message();
            message.setText("Hello World!");

            //비영속(transient) 인스턴스를 영속성 컨텍스트에 등록해서 영속화
            //우리가 해당데이터를 저장하려 한다느걸 하이버네이트도 알지만 데이터베이스 호출을 즉시 수행하지는 않습니다.
            em.persist(message);

            //트랜잭션을 커밋, 자동으로 영속성 컨텍스트를 확인하고 필요한 SQL INSERT문을 실행합니다.
            //하이버네이트는 ID 기본키 컬럼에 대해 자동으로 생성된 값과 TEXT값을 사용해 MESSAGE 테이블에 로우를 삽입합니다.
            em.getTransaction().commit();

            //데이터를 읽기만 하는 경우도 DB와의 모든 상호작용은 트랜잭션 경계 내에서 이뤄져야 하므로 새 트랜잭션을
            //시작합니다. 이제부터 발생 가능한 어떠한 오류도 이전에 커밋된 트랜잭션에는 영향을 미치지 않습니다.
            em.getTransaction().begin();

            //쿼리를 실행해 DB의 모든 Message 인스턴스를 조회합니다. (JPQL 사용)
            List<Message> messages = em.createQuery("select m from Message m", Message.class).getResultList();

            //프로퍼티의 값을 변경할수 있습니다, 로드된 Message가 앞서 로드됐던 영속성 컨텍스트에
            //여전히 연결돼 있기 때문에 하이버네이트가 이를 자동으로 감지합니다.
            messages.get(messages.size() - 1).setText("Hello World from JPA!");

            //커밋시 하이버네이트가 영속성 컨텍스트에서 변경 상태를 확인하고, SQL UPDATE를 자동으로 실행해
            //인메모리 객체와 데이터베이스 상태를 동기화합니다.
            em.getTransaction().commit();


            assertAll(
                    //조회한 메시지 리스트의 크기를 확인
                    () -> assertEquals(1, messages.size()),
                    //영속화한 메시지가 DB에 있는지 확인
                    () -> assertEquals("Hello World from JPA!", messages.get(0).getText())
            );

            //EntityManager를 만들었으므로 닫아야 합니다.
            em.close();
        }finally {
            //EntityManagerFactory도 닫아야 합니다.
            emf.close();
        }
    }

}