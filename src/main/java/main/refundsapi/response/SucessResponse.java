package main.refundsapi.response;

import lombok.Getter;
import main.refundsapi.common_enum.UserEnum;

@Getter
public class SucessResponse<T>{

    //private final LocalDateTime timestamp = LocalDateTime.now();
    //private final int status;
    //private final String error;
    private final String code;
    private final String message;
    private T result;

    public SucessResponse(UserEnum userEnum, T parameter) {
        //this.status = errorCode.getStatus().value();
        //this.error = errorCode.getStatus().name();
        this.code = userEnum.getCode();
        this.message = userEnum.getMessage();
        this.result = parameter;
    }

    public SucessResponse(String code, String message) {
        //this.status = errorCode.getStatus().value();
        //this.error = errorCode.getStatus().name();
        this.code = code;
        this.message = message;
    }

}
