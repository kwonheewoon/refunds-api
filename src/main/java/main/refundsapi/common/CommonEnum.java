package main.refundsapi.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CommonEnum {


    /*
     *  유저 세무정보 scrap url
     */
    SCRAP_URL("SCRAP_URL", "https://codetest.3o3.co.kr/v1/scrap"),
    STATUS_SUCCESS("success", "성공"),
    STATUS_FAIL("fail", "실패"),
    ;


    private final String name;
    private final String text;
}
