package inhatc.cse.spring.spring_resume_project.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(value = {AuditingEntityListener.class})
@MappedSuperclass // 실제로는 이 파일이 테이블로 매핑 안하고 자식 클래스한테 매핑 정보만 제공하는 역할로 만들어주는 어노테이션
@Getter
@Setter
public abstract class BaseEntity extends BaseTimeEntity {
    @CreatedBy
    @Column(updatable = false) //한번 등록된 사람은 수정되면 안되니까
    private String createdBy; //등록한 사람

    @LastModifiedBy //마지막으로 누구에 의해서 수정되었는지 표시
    private String modifiedBy; //수정한 사람


}
