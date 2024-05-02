package com.manning.javapersistence.ch02;

import com.manning.javapersistence.ch02.configuration.SpringDataConfiguration;
import com.manning.javapersistence.ch02.repositories.MessageRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

//SpringExtension을 사용해 테스트를 확장합니다. 이확장은 스프링 테스트 컨텍스트를 JUnit5 Jupiter 테스트와 통합하는데 사용합니다.
@ExtendWith(SpringExtension.class)
//스프링 테스트 컨텍스트는 설정클래스로 정의된 빈을 사용합니다.
@ContextConfiguration(classes = SpringDataConfiguration.class)
public class HelloWorldSpringJPATest {

    @Autowired
    private MessageRepository messageRepository;

    @Test
    public void storeLoadMessage() {
        Message message = new Message();
        message.setText("Hello World from Spring Data JPA!");

        //객체를 영속화, save 메서드는 CrudRepository 인퍼페이스로부터 상속되며, 프록시 클래스가 생성될때 스프링 데이터 JPA에서 해당 메서드 본문을 생성합니다.
        messageRepository.save(message);

        //리포지토리에서 메시지를 조회
        List<Message> messages = (List<Message>) messageRepository.findAll();

        //결과값 확인
        assertAll(
                () -> assertEquals(1, messages.size()),
                () -> assertEquals("Hello World from Spring Data JPA!", messages.get(0).getText())
        );
    }
}
