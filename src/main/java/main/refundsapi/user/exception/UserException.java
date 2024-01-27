package main.refundsapi.user.exception;

import main.refundsapi.user.enums.UserCode;
import main.refundsapi.common.exception.CustomExceptionInterface;
import org.springframework.http.HttpStatus;

public class UserException extends RuntimeException implements CustomExceptionInterface {
    private final String code;
    private final HttpStatus httpStatus;

    public UserException(UserCode resCode) {
        super(resCode.getMessage());
        this.code = resCode.getCode();
        this.httpStatus = resCode.getHttpStatus();
    }

    // Getter 메소드 추가
    public String getCode() {
        return code;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}