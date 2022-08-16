package main.refundsapi.entity;


import lombok.*;
import main.refundsapi.converter.CryptoConverter;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user")
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

    //주민등록번호 등록,조회시 SEED_CBC 자동 암복호화
    @Convert(converter = CryptoConverter.class)
    @Column(name = "reg_no", columnDefinition = "char", length = 14, nullable = false)
    @Comment("사용자 주민등록 번호")
    private String regNo;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userEntity")
    private List<UserTaxInfoEntity> userTaxInfoEntityList = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "userEntity")
    private List<UserTaxResultEntity> userTaxResultEntityList  = new ArrayList<>();
}
