package main.refundsapi;

import lombok.extern.slf4j.Slf4j;
import main.refundsapi.common_enum.ErrorCode;
import main.refundsapi.common_enum.RefundCalcEnum;
import main.refundsapi.common_enum.TaxInfoCodeEnum;
import main.refundsapi.dto.UserTaxResultApiDto;
import main.refundsapi.dto.UserTaxResultDto;
import main.refundsapi.entity.UserEntity;
import main.refundsapi.entity.UserTaxInfoEntity;
import main.refundsapi.entity.UserTaxResultEntity;
import main.refundsapi.exception.CommonException;
import main.refundsapi.repository.UserRepository;
import main.refundsapi.repository.UserTaxInfoRepository;
import main.refundsapi.repository.UserTaxResultQueryRepository;
import main.refundsapi.repository.UserTaxResultRepository;
import main.refundsapi.util.CommonUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@Slf4j
public class UserRefundServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserTaxInfoRepository userTaxInfoRepository;

    @Mock
    private UserTaxResultRepository userTaxResultRepository;

    @Mock
    private UserTaxResultQueryRepository userTaxResultQueryRepository;

    @Test
    void 한도_금액_계산(){

        //given
        BigDecimal totalPayment = new BigDecimal(40000000);
        BigDecimal refundAmountResult = new BigDecimal(740000);


        //when
        //근로소득 세액공제 한도 계산
        //총급여액 3,300만원 이하
        if(totalPayment.compareTo(RefundCalcEnum.TOTAL_PAYMENT_3300.getAmount())  == -1){
            //한도 74만원 세팅
            refundAmountResult = RefundCalcEnum.TOTAL_PAYMENT_DEDUCTION_74.getAmount();
        }
        //총급여액 3,300만원 초과 7,000만원 이하
        else if((totalPayment.compareTo(RefundCalcEnum.TOTAL_PAYMENT_3300.getAmount())  == 1) &&
                totalPayment.compareTo(RefundCalcEnum.TOTAL_PAYMENT_7000.getAmount())  == -1){

            //74만원 - [(총급여액 -3,300만원) x 0.008] 다만, 위금액이 66만원보다
            //적은경우 66만원
            BigDecimal refundAmount = RefundCalcEnum.TOTAL_PAYMENT_DEDUCTION_74.getAmount().subtract(
                    totalPayment.subtract(RefundCalcEnum.TOTAL_PAYMENT_3300.getAmount())
                        .multiply(new BigDecimal("0.008"))
            );

            refundAmountResult = refundAmount.compareTo(new BigDecimal(660000)) == -1 ?
                    RefundCalcEnum.TOTAL_PAYMENT_DEDUCTION_66.getAmount() :
                    refundAmount;
        }
        //총급여액 7,000만원 초과
        else if(totalPayment.compareTo(RefundCalcEnum.TOTAL_PAYMENT_7000.getAmount())  == 1){
            BigDecimal refundAmount = RefundCalcEnum.TOTAL_PAYMENT_DEDUCTION_66.getAmount().subtract(
                    totalPayment.subtract(RefundCalcEnum.TOTAL_PAYMENT_7000.getAmount())
                        .multiply(new BigDecimal("0.5"))
            );

            refundAmountResult = refundAmount.compareTo(RefundCalcEnum.TOTAL_PAYMENT_DEDUCTION_50.getAmount()) == -1 ?
                    RefundCalcEnum.TOTAL_PAYMENT_DEDUCTION_50.getAmount() :
                    refundAmount;
        }

        //then
        log.info("한도 금액 : {}", refundAmountResult);
        Assertions.assertThat(refundAmountResult.intValue()).isEqualTo(new BigDecimal(684000).intValue());
    }

    @Test
    void 근로소득_세액공제_계산() {

        //given
        // 총사용금액
        BigDecimal totalAmountUsed = new BigDecimal(2000000);
        //산출세액 코드
        String incomeCls = "CALT";

        BigDecimal deductible = null;

        //산출세액이 1,300,000만원 보다 작을경우
        if (totalAmountUsed.compareTo(RefundCalcEnum.CALCULATED_TAX_130.getAmount()) == -1) {
            deductible = totalAmountUsed.multiply(new BigDecimal("0.55"));
        }

        //산출세액이 1,300,000만원 초과일 경우
        else if (totalAmountUsed.compareTo(RefundCalcEnum.CALCULATED_TAX_130.getAmount()) == 1) {
            deductible = RefundCalcEnum.CALCULATED_TAX_71_5.getAmount().add(totalAmountUsed.subtract(
                    RefundCalcEnum.CALCULATED_TAX_130.getAmount()
            ).multiply(new BigDecimal("0.3")));
        }

        log.info("공제액 : {}", deductible);

        Assertions.assertThat(deductible.intValue()).isEqualTo(new BigDecimal(925000).intValue());
    }

    @Test
    void 환급액_계산식(){

        BigDecimal deductible = new BigDecimal("925000.1");
        BigDecimal limitAmount = new BigDecimal("925000.5");


        //log.info("환급액 : {}", Math.min(deductible.intValue(), refundAmountResult.intValue()));
        log.info("환급액 : {}", deductible.min(limitAmount));

        Assertions.assertThat(deductible.min(limitAmount)).isEqualTo(new BigDecimal("925000.1"));
    }


    @Nested
    @DisplayName("환급계산 결과 저장,수정 로직")
    class ScrapJsonParsing {

        private UserTaxResultDto userTaxResultDto;

        private UserTaxResultEntity userTaxResultEntity;

        private UserTaxResultEntity findUserTaxResultEntity;

        private UserTaxResultApiDto findUserTaxResultApiDto;

        @BeforeEach
        void save_setup() {

            UserEntity userEntity = UserEntity.builder().id(1L).build();

            UserTaxInfoEntity findUserTaxInfoEntity = UserTaxInfoEntity.builder()
                    .userEntity(UserEntity.builder().id(1L).build())
                    .year(CommonUtil.getYear())
                    .totalPayment(new BigDecimal(94666.666))
                    .totalAmountUsed(new BigDecimal(1333333.333))
                    .incomeCls(TaxInfoCodeEnum.searchCode("산출세액"))
                    .build();

            findUserTaxResultApiDto = UserTaxResultApiDto.builder()
                    .limitAmount(new BigDecimal(684000))
                    .deductible(new BigDecimal(920000))
                    .refundAmount(new BigDecimal(684000))
                    .build();

            userTaxResultDto = UserTaxResultDto.builder()
                    .id(1L)
                    .year(CommonUtil.getYear())
                    .limitAmount(new BigDecimal(684000))
                    .deductible(new BigDecimal(920000))
                    .refundAmount(new BigDecimal(684000))
                    .build();

            userTaxResultEntity = UserTaxResultEntity.builder()
                    .year(userTaxResultDto.getYear())
                    .limitAmount(new BigDecimal(684000))
                    .deductible(new BigDecimal(920000))
                    .refundAmount(new BigDecimal(684000))
                    .userEntity(userEntity)
                    .userTaxInfoEntity(findUserTaxInfoEntity)
                    .build();

            findUserTaxResultEntity = UserTaxResultEntity.builder()
                    .id(1L)
                    .year(userTaxResultDto.getYear())
                    .limitAmount(new BigDecimal(684000))
                    .deductible(new BigDecimal(920000))
                    .refundAmount(new BigDecimal(684000))
                    .userEntity(userEntity)
                    .userTaxInfoEntity(findUserTaxInfoEntity)
                    .build();
        }

        @Test
        void 환급_계산_결과_저장_수정(){

            given(userTaxResultQueryRepository.findRefund(anyString(), anyInt())).willReturn(Optional.ofNullable(findUserTaxResultApiDto));
            given(userTaxResultQueryRepository.updateUserTaxResult(any(UserTaxResultDto.class))).willReturn(1L);
            given(userTaxResultRepository.save(any(UserTaxResultEntity.class))).willReturn(findUserTaxResultEntity);


            //환급액 저장 수정 여부 확인을 위하여 조회
            var findUserTaxResultDto = userTaxResultQueryRepository.findRefund("hong12", CommonUtil.getYear());

            //환급계산 결과 수정
            var updatedCount = userTaxResultQueryRepository.updateUserTaxResult(userTaxResultDto);

            //환급계산 결과 저장
            var savedUserTaxResultEntity = userTaxResultRepository.save(userTaxResultEntity);

            Assertions.assertThat(findUserTaxResultDto.isPresent()).isTrue();
            assertNotNull(savedUserTaxResultEntity);
            Assertions.assertThat(updatedCount).isEqualTo(1L);
            Assertions.assertThat(savedUserTaxResultEntity.getRefundAmount()).isEqualTo(new BigDecimal(684000));

            verify(userTaxResultQueryRepository).findRefund("hong12", CommonUtil.getYear());
            verify(userTaxResultQueryRepository).updateUserTaxResult(userTaxResultDto);
            verify(userTaxResultRepository).save(userTaxResultEntity);
        }
    }
}

