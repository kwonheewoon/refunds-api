package main.refundsapi.entity;


import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name = "category")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
public class UserEntity extends BaseEntity{

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue
    @Comment("사용자 기본키 id")
    private Long id;

    @Column(name = "user_id", columnDefinition = "varchar2", length = 10, nullable = false)
    @Comment("사용자 아이디")
    private String userId;

    @Column(name = "password", columnDefinition = "varchar2", length = 25, nullable = false)
    @Comment("사용자 비밀번호")
    private String password;

    @Column(name = "name", columnDefinition = "varchar2", length = 10, nullable = false)
    @Comment("사용자 이름")
    private String name;

    @Column(name = "reg_no", columnDefinition = "char", length = 14, nullable = false)
    @Comment("사용자 주민등록 번호")
    private String regNo;
}
