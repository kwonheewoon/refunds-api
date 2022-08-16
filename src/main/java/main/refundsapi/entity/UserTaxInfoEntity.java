package main.refundsapi.entity;


import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "user_tax_info")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
public class UserTaxInfoEntity extends BaseEntity{

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue
    @Comment("세무정보 기본키 id")
    private Long id;

    @Column(name = "year", columnDefinition = "int", length = 4, nullable = false)
    @Comment("세무정보 년도")
    private int year;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", columnDefinition = "BIGINT", nullable = false)
    @Comment("사용자 아이디")
    private UserEntity userEntity;

    @Column(name = "total_payment", columnDefinition = "DECIMAL(10,2)",  nullable = false)
    @Comment("총 지급액")
    private BigDecimal totalPayment;

    @Column(name = "total_amount_used", columnDefinition = "DECIMAL(10,2)",  nullable = false)
    @Comment("총 사용 금액")
    private BigDecimal totalAmountUsed;
    
    //calculated tax 산출세액
    @Column(name = "income_cls", columnDefinition = "varchar2", length = 4,  nullable = false)
    @Comment("소득구분")
    private String incomeCls;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "userTaxInfoEntity")
    private UserTaxResultEntity taxResultEntity;

    public void setUserEntity(UserEntity userEntity){
        this.userEntity = userEntity;
    }

}
