package main.refundsapi;

import com.querydsl.core.dml.UpdateClause;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
import lombok.extern.slf4j.Slf4j;
import main.refundsapi.common_enum.TaxInfoCodeEnum;
import main.refundsapi.config.QueryDslConfiguration;
import main.refundsapi.dto.UserTaxResultApiDto;
import main.refundsapi.dto.UserTaxResultDto;
import main.refundsapi.entity.*;
import main.refundsapi.repository.UserRepository;
import main.refundsapi.repository.UserTaxInfoRepository;
import main.refundsapi.repository.UserTaxResultQueryRepository;
import main.refundsapi.repository.UserTaxResultRepository;
import main.refundsapi.util.CommonUtil;
import main.refundsapi.util.SeedUtil;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase
@Import(QueryDslConfiguration.class)
@Slf4j
public class UserTaxResultQueryRepositoryTest {
    @Autowired
    JPAQueryFactory queryFactory;

    private UserEntity userEntity;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserTaxResultRepository userTaxResultRepository;

    @Autowired
    private UserTaxInfoRepository userTaxInfoRepository;


    @Before
    public void setup(){

        // 유저정보 저장
        userEntity = userRepository.save(
                UserEntity.builder()
                        .name("홍길동")
                        .password("123456")
                        .userId("hong12")
                        .regNo("860824-1655068")
                        .build()
        );
    }

    @Test
    public void 유저_환급액_저장_조회(){

        // 유저 세무정보 entity 생성
        var userTaxInfoEntity = UserTaxInfoEntity.builder()
                .userEntity(userEntity)
                .year(CommonUtil.getYear())
                .totalPayment(new BigDecimal(94666.666))
                .totalAmountUsed(new BigDecimal(1333333.333))
                .incomeCls(TaxInfoCodeEnum.searchCode("산출세액"))
                .build();

        // 유저 세무정보 저장
        var savedUserTaxInfoEntity = userTaxInfoRepository.save(userTaxInfoEntity);

        // 유저 세무정보 환급액 저장
        var savedUserTaxResultEntity =  userTaxResultRepository.save(
                UserTaxResultEntity.builder()
                .userEntity(userEntity)
                .userTaxInfoEntity(savedUserTaxInfoEntity)
                .refundAmount(new BigDecimal(570000))
                .deductible(new BigDecimal(570000))
                .limitAmount(new BigDecimal(690000))
                .year(CommonUtil.getYear())
                .build()
        );

        // 유저 세무정보 환급액 조회
        var resultData = findRefund("hong12", CommonUtil.getYear()).orElseThrow();

        assertNotNull(resultData);
        Assertions.assertThat(resultData.getRefundAmount().intValue()).isEqualTo(new BigDecimal(570000).intValue());
        Assertions.assertThat(resultData.getDeductible().intValue()).isEqualTo(new BigDecimal(570000).intValue());
        Assertions.assertThat(resultData.getLimitAmount().intValue()).isEqualTo(new BigDecimal(690000).intValue());
    }

    @Test
    public void 유저_환급액_수정_조회(){

        // 유저 세무정보 entity 생성
        var userTaxInfoEntity = UserTaxInfoEntity.builder()
                .userEntity(userEntity)
                .year(CommonUtil.getYear())
                .totalPayment(new BigDecimal(94666.666))
                .totalAmountUsed(new BigDecimal(1333333.333))
                .incomeCls(TaxInfoCodeEnum.searchCode("산출세액"))
                .build();

        // 유저 세무정보 저장
        var savedUserTaxInfoEntity = userTaxInfoRepository.save(userTaxInfoEntity);

        // 유저 세무정보 환급액 저장
        var savedUserTaxResultEntity =  userTaxResultRepository.save(
                UserTaxResultEntity.builder()
                        .userEntity(userEntity)
                        .userTaxInfoEntity(savedUserTaxInfoEntity)
                        .refundAmount(new BigDecimal(570000))
                        .deductible(new BigDecimal(570000))
                        .limitAmount(new BigDecimal(690000))
                        .year(CommonUtil.getYear())
                        .build()
        );

        // 유저 세무정보 환급액 수정
        var updatedCount = updateUserTaxResult(UserTaxResultDto.builder()
                .id(savedUserTaxResultEntity.getId())
                .refundAmount(new BigDecimal(470000))
                .deductible(new BigDecimal(470000))
                .limitAmount(new BigDecimal(590000))
                .year(CommonUtil.getYear())
                .build());

        // 유저 세무정보 환급액 조회
        var resultData = findRefund("hong12", CommonUtil.getYear()).orElseThrow();

        assertNotNull(resultData);
        Assertions.assertThat(updatedCount).isEqualTo(1);
        Assertions.assertThat(resultData.getRefundAmount().intValue()).isEqualTo(new BigDecimal(470000).intValue());
        Assertions.assertThat(resultData.getDeductible().intValue()).isEqualTo(new BigDecimal(470000).intValue());
        Assertions.assertThat(resultData.getLimitAmount().intValue()).isEqualTo(new BigDecimal(590000).intValue());
    }


    public Optional<UserTaxResultApiDto> findRefund(String userId, int year){

        var userEntity = QUserEntity.userEntity;
        var userTaxResultEntity = QUserTaxResultEntity.userTaxResultEntity;

        return Optional.ofNullable(this.queryFactory
                .select(Projections.fields(
                        UserTaxResultApiDto.class,
                        userTaxResultEntity.id.as("id"),
                        userTaxResultEntity.limitAmount.as("limitAmount"),
                        userTaxResultEntity.deductible.as("deductible"),
                        userTaxResultEntity.refundAmount.as("refundAmount"),
                        userEntity.name.as("name")
                ))
                .from(userTaxResultEntity)
                .innerJoin(userEntity)
                .on(userTaxResultEntity.userEntity.id.eq(userEntity.id))
                .where(
                        userEntity.userId.eq(userId),
                        userTaxResultEntity.year.eq(year)
                )
                .fetchOne());
    }


    /**
     * 유저 환급액 계산 결과 데이터 수정
     * */
    public Long updateUserTaxResult(UserTaxResultDto userTaxResultDto) {
        var userTaxResultEntity = QUserTaxResultEntity.userTaxResultEntity;

        UpdateClause<JPAUpdateClause> updateBuilder = this.queryFactory.update(userTaxResultEntity);

        //한도액 수정
        if (null != userTaxResultDto.getLimitAmount() && userTaxResultDto.getLimitAmount().intValue() > 0) {
            updateBuilder.set(userTaxResultEntity.limitAmount, userTaxResultDto.getLimitAmount());
        }
        //공제액 수정
        if (null != userTaxResultDto.getDeductible() && userTaxResultDto.getDeductible().intValue() > 0) {
            updateBuilder.set(userTaxResultEntity.deductible, userTaxResultDto.getDeductible());
        }
        //환급액 수정
        if (null != userTaxResultDto.getRefundAmount() && userTaxResultDto.getRefundAmount().intValue() > 0) {
            updateBuilder.set(userTaxResultEntity.refundAmount, userTaxResultDto.getRefundAmount());
        }



        return updateBuilder
                .set(userTaxResultEntity.lastModifiedDate, LocalDateTime.now())
                .where(userTaxResultEntity.id.eq(userTaxResultDto.getId())
                        .and(userTaxResultEntity.year.eq(userTaxResultDto.getYear())))
                .execute();
    }
}
