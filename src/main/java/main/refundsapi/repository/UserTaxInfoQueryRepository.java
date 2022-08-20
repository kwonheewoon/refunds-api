package main.refundsapi.repository;

import com.querydsl.core.dml.UpdateClause;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
import lombok.RequiredArgsConstructor;
import main.refundsapi.dto.UserTaxInfoDto;
import main.refundsapi.entity.QUserTaxInfoEntity;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
@RequiredArgsConstructor
public class UserTaxInfoQueryRepository {

    private final JPAQueryFactory queryFactory;

    /**
     * 유저 세무정보 수정
     * */
    public Long updateUserTaxInfo(UserTaxInfoDto userTaxInfoDto) {
        var userTaxInfoEntity = QUserTaxInfoEntity.userTaxInfoEntity;

        UpdateClause<JPAUpdateClause> updateBuilder = this.queryFactory.update(userTaxInfoEntity);

        //총 지급액 수정
        if (null != userTaxInfoDto.getTotalPayment() && userTaxInfoDto.getTotalPayment().intValue() > 0) {
            updateBuilder.set(userTaxInfoEntity.totalPayment, userTaxInfoDto.getTotalPayment());
        }
        //총 사용 금액 수정
        if (null != userTaxInfoDto.getTotalAmountUsed() && userTaxInfoDto.getTotalAmountUsed().intValue() > 0) {
            updateBuilder.set(userTaxInfoEntity.totalAmountUsed, userTaxInfoDto.getTotalAmountUsed());
        }
        //소득구분 수정
        if (!userTaxInfoDto.getIncomeCls().isEmpty()) {
            updateBuilder.set(userTaxInfoEntity.incomeCls, userTaxInfoDto.getIncomeCls());
        }



        return updateBuilder
                .set(userTaxInfoEntity.lastModifiedDate, LocalDateTime.now())
                .where(userTaxInfoEntity.id.eq(userTaxInfoDto.getId())
                        .and(userTaxInfoEntity.year.eq(userTaxInfoDto.getYear())))
                .execute();
    }


}
