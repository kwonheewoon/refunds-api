package main.refundsapi.common;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum TaxInfoCodeEnum {

    /*
     * 소득구분 산출세액 코드 : CAL
     */
    CALCULATED_TAX("산출세액", "CALT");

    private final String name;
    private final String code;

    public static String searchCode(String name){
        return Arrays.stream(values()).filter(data -> data.name.equals(name)).findFirst().orElseThrow().getCode();
    }
}
