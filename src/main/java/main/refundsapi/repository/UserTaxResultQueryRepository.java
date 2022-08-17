package main.refundsapi.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import main.refundsapi.dto.UserTaxResultApiDto;
import main.refundsapi.entity.QUserEntity;
import main.refundsapi.entity.QUserTaxResultEntity;
import org.springframework.stereotype.Repository;

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

}
