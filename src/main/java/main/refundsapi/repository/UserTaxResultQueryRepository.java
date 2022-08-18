package main.refundsapi.repository;

import com.querydsl.core.dml.UpdateClause;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
import lombok.RequiredArgsConstructor;
import main.refundsapi.dto.UserTaxInfoDto;
import main.refundsapi.dto.UserTaxResultApiDto;
import main.refundsapi.dto.UserTaxResultDto;
import main.refundsapi.entity.QUserEntity;
import main.refundsapi.entity.QUserTaxInfoEntity;
import main.refundsapi.entity.QUserTaxResultEntity;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserTaxResultQueryRepository {

    private final JPAQueryFactory queryFactory;


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
