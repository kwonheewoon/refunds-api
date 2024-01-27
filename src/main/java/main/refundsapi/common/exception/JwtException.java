package main.refundsapi.common.exception;

import main.refundsapi.common.ErrorCode;
import org.springframework.http.HttpStatus;

public class JwtException extends RuntimeException {
    private final String code;
    private final HttpStatus httpStatus;

    public JwtException(ErrorCode resCode) {
        super(resCode.getMessage());
        this.code = resCode.getCode();
        this.httpStatus = resCode.getHttpStatus();
    }

    public JwtException(ErrorCode resCode, String message) {
        super(message);
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

