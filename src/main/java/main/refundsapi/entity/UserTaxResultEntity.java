package main.refundsapi.entity;


import lombok.*;
import main.refundsapi.dto.UserTaxResultDto;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "user_tax_result")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
public class UserTaxResultEntity extends BaseEntity{

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue
    @Comment("세무결과 기본키 id")
    private Long id;

    @Column(name = "year", columnDefinition = "int", length = 4, nullable = false)
    @Comment("세무결과 년도")
    private int year;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", columnDefinition = "BIGINT", nullable = false)
    @Comment("사용자 아이디")
    private UserEntity userEntity;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_tax_info_id", columnDefinition = "BIGINT", nullable = false)
    @Comment("사용자 세무정보 id")
    private UserTaxInfoEntity userTaxInfoEntity;

    @Column(name = "limit_amount", columnDefinition = "DECIMAL(10,2)",  nullable = false)
    @Comment("한도 금액")
    private BigDecimal limitAmount;

    @Column(name = "deductible", columnDefinition = "DECIMAL(10,2)",  nullable = false)
    @Comment("공제액")
    private BigDecimal deductible;

    @Column(name = "refund_amount", columnDefinition = "DECIMAL(10,2)",  nullable = false)
    @Comment("환금액")
    private BigDecimal refundAmount;


    public static UserTaxResultEntity entityConvert(UserTaxResultDto userTaxResultDto){
        return UserTaxResultEntity.builder()
                .year(userTaxResultDto.getYear())
                .limitAmount(userTaxResultDto.getLimitAmount())
                .deductible(userTaxResultDto.getDeductible())
                .refundAmount(userTaxResultDto.getRefundAmount())
                .build();
    }

}
