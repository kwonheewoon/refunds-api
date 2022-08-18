package main.refundsapi.response;

import lombok.Getter;
import main.refundsapi.common_enum.UserEnum;
import main.refundsapi.common_enum.UserTaxEnum;

@Getter
public class SucessResponse<T>{

    private final String code;
    private final String message;
    private T data;
    private final String status;

    public SucessResponse(String status, UserEnum userEnum, T parameter) {
        this.status = status;
        this.code = userEnum.getCode();
        this.message = userEnum.getMessage();
        this.data = parameter;
    }

    public SucessResponse(String status, UserTaxEnum userTaxEnum, T parameter) {
        this.status = status;
        this.code = userTaxEnum.getCode();
        this.message = userTaxEnum.getMessage();
        this.data = parameter;
    }

    public SucessResponse(String status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

}
