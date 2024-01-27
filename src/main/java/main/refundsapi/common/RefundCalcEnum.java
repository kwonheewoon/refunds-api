package main.refundsapi.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public enum RefundCalcEnum {



    /****************************
    한도금액 계산 Bigdecimal 변수
     ****************************/
    /*
     * 총 급여액 : 33,000,000 원
     */
    TOTAL_PAYMENT_3300(new BigDecimal(33000000), "한도 계산식 3,300만원"),

    /*
     * 총 급여액 : 70,000,000 원
     */
    TOTAL_PAYMENT_7000(new BigDecimal(70000000), "한도 계산식 7,000만원"),

    /*
     * 공제요건 액: 740,000 원
     */
    TOTAL_PAYMENT_DEDUCTION_74(new BigDecimal(740000), "한도 공제요건 액 74만원"),

    /*
     * 공제요건 액: 660,000 원
     */
    TOTAL_PAYMENT_DEDUCTION_66(new BigDecimal(660000), "한도 공제요건 액 66만원"),


    /*
     * 공제요건 액: 500,000 원
     */
    TOTAL_PAYMENT_DEDUCTION_50(new BigDecimal(500000), "한도 공제요건 액 50만원"),

    /*
     * 한도 계산 소수 0.008
     */
    CALC_DECIMAL_0_008(new BigDecimal("0.008"), "한도 계산 소수 0.008"),

    /*
     * 한도 계산 소수 0.5
     */
    CALC_DECIMAL_0_5(new BigDecimal("0.5"), "한도 계산 소수 0.5"),



    /****************************
     근로소득 세액공제 계산 Bigdecimal 변수
     ****************************/
    /* calculated tax
     * 공제요건 액: 1,300,000 원
     */
    CALCULATED_TAX_130(new BigDecimal(1300000), "세액공제 1,300,000원"),

    CALCULATED_TAX_71_5(new BigDecimal(715000), "세액공제 715,000원"),

    /*
     * 근로소득 세액공제 계산 소수 0.55
     */
    CALC_DECIMAL_0_55(new BigDecimal("0.55"), "근로소득 세액공제 계산 소수 0.55"),
    /*
     * 근로소득 세액공제 계산 소수 0.3
     */
    CALC_DECIMAL_0_3(new BigDecimal("0.3"), "근로소득 세액공제 계산 소수 0.3"),

    /*
     * 근로소득 세액공제 계산 소수 0.12
     */
    CALC_DECIMAL_0_12(new BigDecimal("0.12"), "근로소득 세액공제 계산 소수 0.12"),

    /*
     * 근로소득 세액공제 계산 소수 0.15
     */
    CALC_DECIMAL_0_15(new BigDecimal("0.15"), "근로소득 세액공제 계산 소수 0.15"),
    ;



    private final BigDecimal amount;
    private final String message;
}
