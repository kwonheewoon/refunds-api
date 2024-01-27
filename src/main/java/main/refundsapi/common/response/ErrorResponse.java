package main.refundsapi.common.response;

import lombok.Getter;
import main.refundsapi.user.enums.UserCode;
import org.json.simple.JSONObject;

@Getter
public class ErrorResponse {

    private final String status;
    private JSONObject errors = new JSONObject();

    public ErrorResponse(String status, String errorCode, String errorMessage) {
        this.status = status;
        this.errors.put("code", errorCode);
        this.errors.put("message", errorMessage);

    }

//    public ErrorResponse(String status, ErrorCode errorCode) {
//        this.status = status;
//        this.errors.put("code", errorCode.getStatus().value());
//        this.errors.put("message", errorCode.getMessage());
//
//    }

    public ErrorResponse(String status, UserCode userEnum) {
        this.status = status;
        this.errors.put("code", userEnum.getCode());
        this.errors.put("message", userEnum.getMessage());

    }

}
