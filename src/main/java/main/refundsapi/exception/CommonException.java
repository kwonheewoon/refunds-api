package main.refundsapi.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import main.refundsapi.common_enum.ErrorCode;

@Getter
@AllArgsConstructor
public class CommonException extends RuntimeException{
    private final ErrorCode errorCode;
}
