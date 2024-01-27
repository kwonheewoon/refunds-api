package main.refundsapi.user.domain.entity;


import jakarta.persistence.*;
import lombok.*;
import main.refundsapi.common.converter.CryptoConverter;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;


import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
public class User {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue
    @Comment("사용자 기본키 id")
    private Long id;

    @Column(name = "user_id", columnDefinition = "varchar", length = 10, nullable = false)
    @Comment("사용자 아이디")
    private String userId;

    @Column(name = "password", columnDefinition = "varchar", length = 25, nullable = false)
    @Comment("사용자 비밀번호")
    private String password;

    @Column(name = "name", columnDefinition = "varchar", length = 10, nullable = false)
    @Comment("사용자 이름")
    private String name;

    //주민등록번호 등록,조회시 AES 자동 암복호화
    @Convert(converter = CryptoConverter.class)
    @Column(name = "reg_no", columnDefinition = "varchar", length = 50, nullable = false)
    @Comment("사용자 주민등록 번호")
    private String regNo;

    @Embedded
    private BaseEntity baseEntity;
}
