package main.refundsapi.advice;

import lombok.extern.slf4j.Slf4j;
import main.refundsapi.common_enum.ErrorCode;
import main.refundsapi.exception.CommonException;
import main.refundsapi.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
                .body(new ErrorResponse(ex.getErrorCode()));
    }

    // 401
    @ExceptionHandler({ AccessDeniedException.class })
    public ResponseEntity handleAccessDeniedException(final AccessDeniedException ex) {
        log.warn("error", ex);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }

    // 500
    @ExceptionHandler({ Exception.class })
    public ResponseEntity<Object> handleAll(final Exception ex) {
        log.info(ex.getClass().getName());
        log.error("error", ex);
        return new ResponseEntity<>(ErrorCode.INTERNAL_SERVER_ERROR.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
