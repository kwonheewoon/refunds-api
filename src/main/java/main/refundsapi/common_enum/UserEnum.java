package main.refundsapi.common_enum;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserEnum {

    /*
     * 200010 : 유저 정보가 정상적으로 등록 되었습니다.
     */
    USER_SAVE_SUCESS("200010", "유저 정보가 정상적으로 등록 되었습니다."),

    /*
     * 200011 : 유저 정보가 정상적으로 등록 되지 않았습니다. (필수 요청값을 확인해 주세요.)
     */
    USER_SAVE_FAIL("400010", "유저 정보가 정상적으로 등록 되지 않았습니다. (필수 요청값을 확인해 주세요.)"),


    /*
     * 200020 : 로그인이 정상적으로 완료 되었습니다.
     */
    USER_LOGIN_SUCESS("200020", "로그인이 정상적으로 완료되어 토큰이 발급 되었습니다."),


    /*
     * 400010 : 유저 가입이 불가능한 정보 입니다.
     */
    USER_SAVE_ACCESS("400044", "유저 가입이 불가능한 정보 입니다."),

    /*
     * 400020 : 로그인 정보가 잘못 되었습니다.
     */
    USER_LOGIN_FAIL("400020", "로그인 정보가 잘못 되었습니다."),

    /*
     * 200030 : 유저 정보가 정상적으로 조회 되었습니다.
     */
    USER_FIND_SUCESS("200030", "유저 정보가 정상적으로 조회 되었습니다.");

    private final String code;
    private final String message;
}
