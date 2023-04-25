package com.sparta.springhw01.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity // @Entity가 붙은 클래스는 JPA가 관리, 엔티티라 한다. JPA를 사용해서 테이블과 매핑할 클래스는 @Entity 필수
@Table(name = "users") // @Table은 엔티티와 매핑할 테이블 지정
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //자동생성(GeneratredValue) IDENTITY : 데이터베이스에 위임, MYSQL ( SEQUENCE : ORACLE )
    @Column(name = "user_id", unique = true, nullable = false) // 제약조건 추가
    private Long id;

    // nullable: null 허용 여부
    // unique: 중복 허용 여부 (false 일때 중복 허용)
    // 최소 4자 이상, 10자 이하이며 알파벳 소문자(a~z), 숫자(0~9)로 구성
    @Column(nullable = false, unique = true)
    @Pattern(regexp = "[a-z0-9]{4,10}", message = "알파벳 소문자(a~z), 숫자(0~9)로 구성되어야 합니다.")
    private String username;

    // 최소 8자 이상, 15자 이하이며 알파벳 대소문자(a~z, A~Z), 숫자(0~9), 특수문자로 구성
    @Column(nullable = false)
    //@Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*\\W).{8,15}$", message = "알파벳 대소문자(a~z, A~Z), 숫자(0~9)로 구성되어야 합니다.")
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    // EnumType.ORDINAL로 설정하면 Enum Type의 순서가 DB에 저장된다. [0, 1]
    // 순서가 바뀔 경우 숫자값에 해당하는 value 가 달라질 수 있기 때문에 이는 시스템에 아주 큰 문제를 일으킬 수 있음
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.MERGE, orphanRemoval = true)
    private List<Post> postList = new ArrayList<>();

    public User(String username, String password, String email, UserRoleEnum role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }

}