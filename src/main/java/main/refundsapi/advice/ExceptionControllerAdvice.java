package main.refundsapi.advice;

import lombok.extern.slf4j.Slf4j;
import main.refundsapi.common.CommonEnum;
import main.refundsapi.common.GlobalErrorCode;
import main.refundsapi.common.exception.CustomExceptionInterface;
import main.refundsapi.refund.exception.RefundException;
import main.refundsapi.user.exception.UserException;
import main.refundsapi.common.response.ErrorResponse;
import main.refundsapi.scrap.exception.ScrapException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.nio.file.AccessDeniedException;

@ControllerAdvice
@Slf4j
public class ExceptionControllerAdvice {

    // 400
    @ExceptionHandler({
            UserException.class,
            ScrapException.class,
            RefundException.class
    })
    public ResponseEntity<ErrorResponse> BadRequestException(final CustomExceptionInterface ex) {
        log.error("CommonException: {}", ex.getCode());

        return ResponseEntity
                .status(ex.getHttpStatus())
                .body(new ErrorResponse(CommonEnum.STATUS_FAIL.getName(), ex.getCode(), ex.getMessage()));
    }

    // 401
    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(final AccessDeniedException ex) {
        log.warn("error", ex);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(CommonEnum.STATUS_FAIL.getName(), String.valueOf(HttpStatus.UNAUTHORIZED.value()), GlobalErrorCode.UNAUTHORIZED.getMessage()));
    }

    // 401
    @ExceptionHandler({BadCredentialsException.class})
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(final BadCredentialsException ex) {
        log.warn("error", ex);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(CommonEnum.STATUS_FAIL.getName(), String.valueOf(HttpStatus.UNAUTHORIZED.value()), GlobalErrorCode.UNAUTHORIZED.getMessage()));
    }

    // 500
    @ExceptionHandler({ Exception.class })
    public ResponseEntity<ErrorResponse> handleAll(final Exception ex) {
        log.info(ex.getClass().getName());
        log.error("error", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(CommonEnum.STATUS_FAIL.getName(), String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), GlobalErrorCode.INTERNAL_SERVER_ERROR.getMessage()));
    }

}
