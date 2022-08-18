package main.refundsapi;

import com.querydsl.core.dml.UpdateClause;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
import lombok.extern.slf4j.Slf4j;
import main.refundsapi.common_enum.TaxInfoCodeEnum;
import main.refundsapi.config.QueryDslConfiguration;
import main.refundsapi.dto.UserTaxInfoDto;
import main.refundsapi.entity.QUserTaxInfoEntity;
import main.refundsapi.entity.UserEntity;
import main.refundsapi.entity.UserTaxInfoEntity;
import main.refundsapi.repository.UserRepository;
import main.refundsapi.repository.UserTaxInfoRepository;
import main.refundsapi.util.CommonUtil;
import static org.junit.jupiter.api.Assertions.*;
import org.assertj.core.api.Assertions;
import org.json.simple.JSONObject;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@DataJpaTest
@AutoConfigureTestDatabase
@Import(QueryDslConfiguration.class)
@Slf4j
public class UserTaxInfoQueryRepositoryTest {

    @Autowired
    JPAQueryFactory queryFactory;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserTaxInfoRepository userTaxInfoRepository;

    private UserEntity userEntity;

    @BeforeEach
    void setup(){
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
    public void 유저_세무정보_저장() {
        // given
        var userTaxInfoEntity = UserTaxInfoEntity.builder()
                .userEntity(userEntity)
                .year(CommonUtil.getYear())
                .totalPayment(new BigDecimal(94666.666))
                .totalAmountUsed(new BigDecimal(1333333.333))
                .incomeCls(TaxInfoCodeEnum.searchCode("산출세액"))
                .build();

        // when
        var savedUserTaxInfoEntity = userTaxInfoRepository.save(userTaxInfoEntity);

        // then
        assertNotNull(savedUserTaxInfoEntity);
        Assertions.assertThat(savedUserTaxInfoEntity.getTotalPayment()).isEqualTo(new BigDecimal(94666.666));
        Assertions.assertThat(savedUserTaxInfoEntity.getTotalAmountUsed()).isNotEqualTo(new BigDecimal(1333333.333));
    }

    @Test
    public void 유저_세무정보_수정() {
        // given
        var userTaxInfoEntity = UserTaxInfoEntity.builder()
                .userEntity(userEntity)
                .year(CommonUtil.getYear())
                .totalPayment(new BigDecimal(94666.666))
                .totalAmountUsed(new BigDecimal(1333333.333))
                .incomeCls(TaxInfoCodeEnum.searchCode("산출세액"))
                .build();

        // when
        var savedUserTaxInfoEntity = userTaxInfoRepository.save(userTaxInfoEntity);

        updateUserTaxInfo(
                UserTaxInfoDto.builder()
                        .id(savedUserTaxInfoEntity.getId())
                        .year(CommonUtil.getYear())
                        .totalPayment(new BigDecimal(93333))
                        .totalAmountUsed(new BigDecimal(1334443.333))
                        .build()
        );

        var findUserTaxInfoEntity = userTaxInfoRepository.findByUserEntityAndYear(userEntity, CommonUtil.getYear()).orElseThrow();

        // then
        assertNotNull(savedUserTaxInfoEntity);
        Assertions.assertThat(findUserTaxInfoEntity.getTotalPayment()).isEqualTo(new BigDecimal(93333));
        Assertions.assertThat(findUserTaxInfoEntity.getTotalAmountUsed()).isNotEqualTo(new BigDecimal(1334443.333));
    }


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
