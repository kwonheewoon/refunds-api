package main.refundsapi.common_enum;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserTaxEnum {

    /*
     * -1 : 근로소득 세액공제 한도 계산 오류가 발생하였습니다.
     */
    LIMIT_AMOUNT_CALC_FAIL("-1", "근로소득 세액공제 한도 계산 오류가 발생하였습니다."),

    /*
     * -1 : 근로소득 세액공제 계산 오류가 발생하였습니다.
     */
    DEDUCTIBLE__CALC_FAIL("-1", "근로소득 세액공제 계산 오류가 발생하였습니다."),

    /*
     * -1 : 환급액 계산 오류가 발생하였습니다.
     */
    REFUND_AMOUNT_CALC_FAIL("-1", "환급액 계산 오류가 발생하였습니다."),

    /*
     * 400020 : 로그인 정보가 잘못 되었습니다.
     */
    USER_LOGIN_FAIL("400020", "로그인 정보가 잘못 되었습니다.");

    private final String code;
    private final String message;
}
