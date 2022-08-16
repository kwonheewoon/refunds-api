package main.refundsapi;

import lombok.extern.slf4j.Slf4j;
import main.refundsapi.common_enum.RefundCalcEnum;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

@Slf4j
public class UserRefundServiceTest {


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

        BigDecimal deductible = new BigDecimal("684000.0");
        BigDecimal refundAmountResult = new BigDecimal("925000.0");


        log.info("환급액 : {}", Math.min(deductible.intValue(), refundAmountResult.intValue()));

        Assertions.assertThat(deductible.intValue()).isEqualTo(new BigDecimal("684000.0").intValue());
    }
}

