package main.refundsapi.common_enum;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CommonEnum {


    /*
     *  : 로그인 정보가 잘못 되었습니다.
     */
    SCRAP_URL("SCRAP_URL", "https://codetest.3o3.co.kr/v1/scrap");


    private final String name;
    private final String text;
}
