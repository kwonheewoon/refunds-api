package main.refundsapi.advice;

import lombok.extern.slf4j.Slf4j;
import main.refundsapi.common_enum.CommonEnum;
import main.refundsapi.common_enum.ErrorCode;
import main.refundsapi.exception.CommonException;
import main.refundsapi.response.ErrorResponse;
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
            CommonException.class
    })
    public ResponseEntity<ErrorResponse> BadRequestException(final CommonException ex) {
        log.error("CommonException: {}", ex.getErrorCode());

        return ResponseEntity
                .status(ex.getErrorCode().getStatus().value())
                .body(new ErrorResponse(CommonEnum.STATUS_FAIL.getName(), ex.getErrorCode()));
    }

    // 401
    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity handleAccessDeniedException(final AccessDeniedException ex) {
        log.warn("error", ex);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(CommonEnum.STATUS_FAIL.getName(), HttpStatus.UNAUTHORIZED.value(), ErrorCode.UNAUTHORIZED.getMessage()));
    }

    // 401
    @ExceptionHandler({BadCredentialsException.class})
    public ResponseEntity handleBadCredentialsException(final BadCredentialsException ex) {
        log.warn("error", ex);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse(CommonEnum.STATUS_FAIL.getName(), HttpStatus.UNAUTHORIZED.value(), ErrorCode.UNAUTHORIZED.getMessage()));
    }

    // 500
    @ExceptionHandler({ Exception.class })
    public ResponseEntity handleAll(final Exception ex) {
        log.info(ex.getClass().getName());
        log.error("error", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(CommonEnum.STATUS_FAIL.getName(), HttpStatus.INTERNAL_SERVER_ERROR.value(), ErrorCode.INTERNAL_SERVER_ERROR.getMessage()));
    }

}
