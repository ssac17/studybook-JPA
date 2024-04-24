package com.manning.javapersistence.ch02.repositories;

import com.manning.javapersistence.ch02.Message;
import org.springframework.data.repository.CrudRepository;
/**
 * CrudRepository에서 상속된 save나 findAll, findById 같은 메서드를 호출할수 있습니다.
 * 스프링 데이터 JPA는 MessageRepository 인터페이스를 구현하는 프록시(proxy) 클래스를 생성합니다.
 */
public interface MessageRepository extends CrudRepository<Message, Long> {
}
