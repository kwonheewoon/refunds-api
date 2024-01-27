package main.refundsapi.scrap.exception;

import main.refundsapi.common.exception.CustomExceptionInterface;
import main.refundsapi.scrap.enums.ScrapCode;
import org.springframework.http.HttpStatus;

public class ScrapException extends RuntimeException implements CustomExceptionInterface {
    private final String code;
    private final HttpStatus httpStatus;

    public ScrapException(ScrapCode resCode) {
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