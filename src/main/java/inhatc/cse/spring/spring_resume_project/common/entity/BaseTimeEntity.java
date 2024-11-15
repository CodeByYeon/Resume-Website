package inhatc.cse.spring.spring_resume_project.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@EntityListeners(value = {AuditingEntityListener.class})
@MappedSuperclass // 실제로는 이 파일이 테이블로 매핑 안하고 자식 클래스한테 매핑 정보만 제공하는 역할로 만들어주는 어노테이션
@Getter
@Setter
public abstract class BaseTimeEntity {
    @CreatedDate
    @Column(updatable = false) // 등록 시간은 수정되면 안되니까 고정
    private LocalDateTime regeTime; //등록 시간

    @LastModifiedDate
    private LocalDateTime updateTime; //수정 시간

}
