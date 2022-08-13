package main.refundsapi.entity;


import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name = "user_join_access")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
public class UserJoinAccessEntity extends BaseEntity{

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue
    @Comment("기본키 id")
    private Long id;

    @Column(name = "name", columnDefinition = "varchar2", length = 10, nullable = false)
    @Comment("사용자 이름")
    private String name;

    @Column(name = "reg_no", columnDefinition = "char", length = 14, nullable = false)
    @Comment("사용자 주민등록 번호")
    private String regNo;
}
