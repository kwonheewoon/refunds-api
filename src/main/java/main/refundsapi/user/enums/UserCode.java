package main.refundsapi.user.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import main.refundsapi.common.CustomCodeInterface;
import main.refundsapi.common.exception.CustomExceptionInterface;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum UserCode implements CustomCodeInterface {

    /*
    * UserCode : 200001 ~ 200050
    * */

    /*
     * 200001 OK : 유저 정보가 정상적으로 등록 되었습니다.
     */
    USER_SAVE_SUCESS("200001", "유저 정보가 정상적으로 등록 되었습니다.", HttpStatus.OK),

    /*
     * 400001 OK : 유저 정보가 정상적으로 등록 되지 않았습니다. (필수 요청값을 확인해 주세요.)
     */
    USER_SAVE_FAIL("400001", "유저 정보가 정상적으로 등록 되지 않았습니다. (필수 요청값을 확인해 주세요.)", HttpStatus.BAD_REQUEST),


    /*
     * 200002 OK : 로그인이 정상적으로 완료 되었습니다.
     */
    USER_LOGIN_SUCESS("200002", "로그인이 정상적으로 완료되어 토큰이 발급 되었습니다.", HttpStatus.OK),


    /*
     * 200003 OK : 유저 가입이 불가능한 정보 입니다.
     */
    USER_SAVE_ACCESS("200003", "유저 가입이 불가능한 정보 입니다.", HttpStatus.OK),

    /*
     * 200004 OK : 로그인 정보가 잘못 되었습니다.
     */
    USER_LOGIN_FAIL("200004", "로그인 정보가 잘못 되었습니다.", HttpStatus.OK),

    /*
     * 200005 OK : 유저 정보가 정상적으로 조회 되었습니다.
     */
    USER_FIND_SUCESS("200005", "유저 정보가 정상적으로 조회 되었습니다.", HttpStatus.OK),

    /*
     * 200006 OK: 유저 정보를 찾을 수 없습니다.
     */
    USER_NOT_FOUND("200006", "유저 정보를 찾을 수 없습니다.", HttpStatus.OK),;

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
}
