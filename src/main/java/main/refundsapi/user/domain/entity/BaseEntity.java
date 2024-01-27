package main.refundsapi.user.domain.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EntityListeners;
import lombok.Getter;
import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;

@Getter
@Embeddable
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

    @CreatedDate
    @Comment("등록 일자")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Comment("마지막 수정 일자")
    private LocalDateTime updatedAt;
}