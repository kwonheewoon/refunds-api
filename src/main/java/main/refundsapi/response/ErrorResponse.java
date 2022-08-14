package main.refundsapi.response;

import lombok.Getter;
import main.refundsapi.common_enum.ErrorCode;
import org.json.simple.JSONObject;

@Getter
public class ErrorResponse {

    //private final LocalDateTime timestamp = LocalDateTime.now();
    //private final int status;
    //private final String error;
    private final int code;
    private final String message;
    private JSONObject result = new JSONObject();

    public ErrorResponse(ErrorCode errorCode) {
        //this.status = errorCode.getStatus().value();
        //this.error = errorCode.getStatus().name();
        this.code = errorCode.getStatus().value();
        this.message = errorCode.getMessage();
        this.result = (JSONObject) result.put("result", null);
    }

}
