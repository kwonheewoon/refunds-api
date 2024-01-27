package main.refundsapi.scrap.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import main.refundsapi.common.CustomCodeInterface;
import main.refundsapi.common.exception.CustomExceptionInterface;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ScrapCode implements CustomCodeInterface {

    /*
     * ScrapCode : 200051 ~ 2000100
     * */


    /*
     * 200051 : 스크랩에 성공 하였습니다.
     */
    SCRAP_SUCCESS("200051", "스크랩에 성공 하였습니다.", HttpStatus.OK),


    /*
     * 200052 : 스크랩에 실패하였습니다.
     */
    SCRAP_FAIL("200052", "스크랩에 실패하였습니다.", HttpStatus.OK),

    /*
    *  200053 : 요청하신 값은 스크랩 가능유저가 아닙니다.
    * */
    NOT_SCRAP_USER("200053", "요청하신 값은 스크랩 가능유저가 아닙니다.", HttpStatus.OK),

   ;

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
}
