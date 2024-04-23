package com.manning.javapersistence.ch02;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 일반적으로 영속성 클래스의 일반적인 속성은 private필드와 public getter/setter 메서드로 구현합니다.
 */
@Entity //모든 영속성 엔티티 클래스에는 작어도 @Entity 애터테이션을 있어야 합니다.
public class Message {

    @Id //모든 영속성 엔티티 클래스에는 @Id 애너테이션이 지정된 식별자 속성이 있어야합니다.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 이 애너테이션을 사용하면 자동으로 자동으로 식별자 값을 생성할수 있습니다.
    private Long id;

    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
