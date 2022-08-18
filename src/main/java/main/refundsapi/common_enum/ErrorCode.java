package main.refundsapi.common_enum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    /*
     * 400 BAD_REQUEST: 잘못된 요청
     */
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),

    /*
     * 200 OK: 회원 정보를 찾을 수 없습니다.
     */
    USER_NOT_FOUND(HttpStatus.OK, "회원 정보를 찾을 수 없습니다."),

    /*
     * 401 UNAUTHORIZED: 허용되지 않은 Request Method 호출
     */
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "권한이 없습니다."),

    /*
     * 405 METHOD_NOT_ALLOWED: 허용되지 않은 Request Method 호출
     */
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "허용되지 않은 메서드입니다."),

    /*
     * 500 INTERNAL_SERVER_ERROR: 내부 서버 오류
     */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 오류입니다."),



    /*
     * 200 OK : 유저의 세무정보가 존재하지 않습니다.
     */
    USER_TAX_INFO_NOT_FOUND(HttpStatus.OK, "유저의 세무정보가 존재하지 않습니다."),





    /*
     * 500 INTERNAL_SERVER_ERROR : 근로소득 세액공제 한도 계산 오류가 발생하였습니다.
     */
    LIMIT_AMOUNT_CALC_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "근로소득 세액공제 한도 계산 오류가 발생하였습니다."),

    /*
     * 500 INTERNAL_SERVER_ERROR : 근로소득 세액공제 계산 오류가 발생하였습니다.
     */
    DEDUCTIBLE_CALC_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "근로소득 세액공제 계산 오류가 발생하였습니다."),

    /*
     * 500 INTERNAL_SERVER_ERROR : 환급액 계산 오류가 발생하였습니다.
     */
    REFUND_AMOUNT_CALC_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "환급액 계산 오류가 발생하였습니다."),




    /*
     * 200 OK : 환급액 정보가 존재하지 않습니다.
     */
    USER_TAX_RESULT_NOT_FOUND(HttpStatus.OK, "환급액 정보가 존재하지 않습니다.")

    ;

    private final HttpStatus status;
    private final String message;

}
