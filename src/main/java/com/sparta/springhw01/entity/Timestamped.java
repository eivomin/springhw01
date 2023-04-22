package com.sparta.springhw01.entity;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

/*
* 최 상단 RootClass에 @EnableJpaAuditing을 추가함으로써 Audit 기능 사용 가능
* */
@Getter
@MappedSuperclass // 부모 클래스임을 명시하며 상속받는 클래스에서 멤버변수가 컬럼이 되도록 함
@EntityListeners(AuditingEntityListener.class) // 값이 변경되었을 때 자동으로 기록
public class Timestamped {

    @CreatedDate // Entity가 생성되어 저장될 때 시간이 자동 저장
    private LocalDateTime createdAt;

    @LastModifiedDate // 조회한 Entity의 값을 변경할 때 시간이 자동 저장
    private LocalDateTime modifiedAt;
}

