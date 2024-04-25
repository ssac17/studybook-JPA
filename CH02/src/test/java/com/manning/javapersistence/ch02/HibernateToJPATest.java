package com.manning.javapersistence.ch02;

import org.hibernate.cfg.Configuration;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Enumeration;
import java.util.HashMap;

public class HibernateToJPATest {

    //Hibernate -> JPA
    private static EntityManagerFactory createEntityManagerFactory() {
        //새 하이버네이트 구성
        Configuration configuration = new Configuration();

        //기본 hibernate.cfg.xml 파일의 내용을 구성에 추가하는 configure 메서드를 호출, Message를 명시적으로 추가 합니다.
        configuration.configure().addAnnotatedClass(Message.class);

        //기존 프로퍼티를 채울 새 해시 맵을 만듭니다.
        HashMap<String, String> properties = new HashMap<>();

        //하이버네이트 구성으로부터 모든 프로퍼티 이름을 가져옵니다.
        Enumeration<?> propertyNames = configuration.getProperties().propertyNames();

        while (propertyNames.hasMoreElements()) {
            String element = (String) propertyNames.nextElement();
            properties.put(element, configuration.getProperties().getProperty(element));
        }

        //새 EntityManagerFactory를 반환하는데, 이때 ch02라는 영속성 단위의 이름과 앞에서 생성한 프로퍼티 맵을 전달합니다.
        return Persistence.createEntityManagerFactory("ch02", properties);
    }
}
