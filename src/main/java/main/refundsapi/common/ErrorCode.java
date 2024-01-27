package main.refundsapi.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    /*
     * 400 BAD_REQUEST: 잘못된 요청
     */
    BAD_REQUEST("400", "잘못된 요청입니다.", HttpStatus.BAD_REQUEST),


    /*
     * 401 UNAUTHORIZED: 허용되지 않은 Request Method 호출
     */
    UNAUTHORIZED("401", "권한이 없습니다.", HttpStatus.UNAUTHORIZED),

    /*
     * 405 METHOD_NOT_ALLOWED: 허용되지 않은 Request Method 호출
     */
    METHOD_NOT_ALLOWED("405", "허용되지 않은 메서드입니다.", HttpStatus.METHOD_NOT_ALLOWED),

    /*
     * 500 INTERNAL_SERVER_ERROR: 내부 서버 오류
     */
    INTERNAL_SERVER_ERROR("500", "내부 서버 오류입니다.", HttpStatus.INTERNAL_SERVER_ERROR),

    ;
    private final String code;
    private final String message;
    private final HttpStatus httpStatus;

}
