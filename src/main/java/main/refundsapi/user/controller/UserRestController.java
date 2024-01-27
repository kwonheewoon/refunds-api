package main.refundsapi.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import main.refundsapi.common.CommonEnum;
import main.refundsapi.login.service.LoginService;
import main.refundsapi.user.enums.UserCode;
import main.refundsapi.login.domain.dto.LoginDto;
import main.refundsapi.user.domain.dto.UserApiDto;
import main.refundsapi.user.domain.dto.UserSignupDto;
import main.refundsapi.refund.domain.RefundApiDto;
import main.refundsapi.refund.enums.RefundCode;
import main.refundsapi.common.response.SucessResponse;
import main.refundsapi.user.service.UserService;
import main.refundsapi.taxinfo.service.TaxInfoService;
import main.refundsapi.refund.service.RefundService;
import org.json.simple.parser.ParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/szs")
@RequiredArgsConstructor
public class UserRestController {

    private final UserService userService;

    private final LoginService loginService;

    private final TaxInfoService taxInfoService;

    private final RefundService refundService;




    @Operation(summary = "유저 가입 API",
            description = "<strong>유저 정보</strong>를 입력받아 등록한다."
           )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200001", description = "유저 정보가 정상적으로 등록 되었습니다."),
            @ApiResponse(responseCode = "200003", description = "유저 가입이 불가능한 정보 입니다.")
    })
    @PostMapping("/signup")
    public ResponseEntity<SucessResponse<UserApiDto>> signup(@RequestBody UserSignupDto userSignupDto){
        return ResponseEntity.ok()
                .body(new SucessResponse<>(CommonEnum.STATUS_SUCCESS.getName(), UserCode.USER_SAVE_SUCESS,userService.saveUser(userSignupDto)));
    }

    @PostMapping("/login")
    @Operation(summary = "유저 로그인 API",
            description = "로그인 및 토큰 발급 성공"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200002", description = "로그인이 정상적으로 완료되어 토큰이 발급 되었습니다."),
            @ApiResponse(responseCode = "200004", description = "로그인 정보가 잘못 되었습니다."),
            @ApiResponse(responseCode = "200006", description = "유저 정보를 찾을 수 없습니다.")
    })
    public ResponseEntity<Object> login(@RequestBody LoginDto loginDto){
        return ResponseEntity.ok()
                .body(new SucessResponse<>(CommonEnum.STATUS_SUCCESS.getName(), UserCode.USER_LOGIN_SUCESS,loginService.login(loginDto)));

    }

    @PostMapping("/scrap")
    @Operation(summary = "유저 세무정보 scrap API",
            description = "<strong>JWT 토큰</strong>을 Authorization 헤더로 입력받아 유저 세무정보 scrap API를 조회 및 세무정보를 저장한다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "success"),
            @ApiResponse(responseCode = "200052", description = "스크랩에 실패하였습니다."),
            @ApiResponse(responseCode = "200006", description = "유저 정보를 찾을 수 없습니다.")
    })
    public ResponseEntity<Object> scrap() throws ParseException {
        return ResponseEntity.ok()
                .body(taxInfoService.findScrap());
    }

    @GetMapping("/refund")
    @Operation(summary = "유저 환급액 계산 및 조회 API",
            description = "<strong>JWT 토큰</strong>을 Authorization 헤더로 입력받아 유저 환급액을 계산및 조회한다."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200101", description = "환급액 계산이 완료되었습니다."),
            @ApiResponse(responseCode = "200006", description = "유저 정보를 찾을 수 없습니다."),
            @ApiResponse(responseCode = "500102", description = "환급액 계산에 오류가 발생하였습니다.")
    })
    public ResponseEntity<SucessResponse<RefundApiDto>> refund() {

        return ResponseEntity.ok()
                .body(new SucessResponse<>(CommonEnum.STATUS_SUCCESS.getName(), RefundCode.REFUND_CALC_SUCCESS, refundService.calculateRefund()));
    }

}
