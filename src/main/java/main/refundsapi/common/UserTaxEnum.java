package main.refundsapi.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserTaxEnum {

    /*
     * -1 : 근로소득 세액공제 한도 계산 오류가 발생하였습니다.
     */
    LIMIT_AMOUNT_CALC_FAIL("FAIL", "근로소득 세액공제 한도 계산 오류가 발생하였습니다."),

    /*
     * -1 : 근로소득 세액공제 계산 오류가 발생하였습니다.
     */
    DEDUCTIBLE__CALC_FAIL("FAIL", "근로소득 세액공제 계산 오류가 발생하였습니다."),

    /*
     * -1 : 환급액 계산 오류가 발생하였습니다.
     */
    REFUND_AMOUNT_CALC_FAIL("-1", "환급액 계산 오류가 발생하였습니다."),

    /*
     * 200040 : 유저의 세무정보 스크랩이 완료되었습니다.
     */
    USER_FIND_SCRAP_SUCESS("200040", "유저의 세무정보 스크랩이 완료되었습니다."),

    /*
     * 200050 : 환급액 계산이 완료되었습니다.
     */
    USER_REFUND_CALC_SUCESS("200050", "환급액 계산이 완료되었습니다.");

    private final String code;
    private final String message;
}
