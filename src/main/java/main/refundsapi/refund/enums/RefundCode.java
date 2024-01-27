package main.refundsapi.refund.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import main.refundsapi.common.CustomCodeInterface;
import main.refundsapi.common.exception.CustomExceptionInterface;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum RefundCode implements CustomCodeInterface {

    /*
     * RefundCode OK status : 200101 ~ 2000150
     * */

    /*
     * 200101 : 환급액 계산이 완료되었습니다.
     */
    REFUND_CALC_SUCCESS("200101", "환급액 계산이 완료되었습니다.", HttpStatus.OK),


    /*
     * 500102 : 환급액 계산에 오류가 발생하였습니다.
     */
    REFUND_CALC_FAIL("500102", "환급액 계산에 오류가 발생하였습니다.", HttpStatus.INTERNAL_SERVER_ERROR),

    ;

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
}
