package main.refundsapi.refund.exception;

import main.refundsapi.common.exception.CustomExceptionInterface;
import main.refundsapi.refund.enums.RefundCode;
import org.springframework.http.HttpStatus;

public class RefundException extends RuntimeException implements CustomExceptionInterface {
    private final String code;
    private final HttpStatus httpStatus;

    public RefundException(RefundCode resCode) {
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