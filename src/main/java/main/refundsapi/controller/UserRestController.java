package main.refundsapi.controller;

import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import main.refundsapi.common_enum.CommonEnum;
import main.refundsapi.common_enum.UserEnum;
import main.refundsapi.common_enum.UserTaxEnum;
import main.refundsapi.dto.LoginDto;
import main.refundsapi.dto.TokenDto;
import main.refundsapi.dto.UserDto;
import main.refundsapi.jwt.JwtFilter;
import main.refundsapi.jwt.TokenProvider;
import main.refundsapi.response.SucessResponse;
import main.refundsapi.service.UserTaxInfoService;
import main.refundsapi.service.UserRefundService;
import main.refundsapi.service.UserService;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/szs")
@RequiredArgsConstructor
public class UserRestController {

    private final UserService userService;

    private final UserTaxInfoService userTaxInfoService;

    private final UserRefundService userRefundService;

    private final TokenProvider tokenProvider;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;



    @PostMapping("/signup")
    @ApiResponses({
            @ApiResponse(code = 200, message = "회원가입 성공")
    })
    @ApiOperation(value = "유저 가입 API", notes = "<strong>유저 정보</strong>를 입력받아 등록한다.")
    public ResponseEntity<Object> signup(@RequestBody UserDto userDto){
        return new ResponseEntity<>(userService.saveUser(userDto) , HttpStatus.OK);
    }

    @PostMapping("/login")
    @ApiResponses({
            @ApiResponse(code = 200, message = "로그인 및 토큰 발급 성공")
    })
    @ApiOperation(value = "로그인 및 토큰 발급 API", notes = "<strong>유저 정보</strong>를 로그인 및 토큰을 발급한다.")
    public ResponseEntity<Object> login(@RequestBody LoginDto loginDto){
        
        //유저 정보 조회
        var findUserApiDto = userService.findByUserId(loginDto.getUserId());

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(findUserApiDto.getUserId(), loginDto.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.createToken(authentication);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

        return new ResponseEntity<>(new SucessResponse(CommonEnum.STATUS_SUCCESS.getName(), UserEnum.USER_LOGIN_SUCESS, new TokenDto(jwt)), httpHeaders, HttpStatus.OK);
    }

    @GetMapping("/me")
    @ApiResponses({
            @ApiResponse(code = 200, message = "유저 정보 조회 API")
    })
    @ApiImplicitParams({@ApiImplicitParam(name = "Authorization", value = "JWT Token", required = true, dataType = "string", paramType = "header")})
    @ApiOperation(value = "유저 정보 조회 API", notes = "<strong>JWT 토큰</strong>을 Authorization 헤더로 입력받아 유저 정보를 조회한다.")
    public ResponseEntity<Object> me(){
        return new ResponseEntity<>(new SucessResponse(CommonEnum.STATUS_SUCCESS.getName(), UserEnum.USER_FIND_SUCESS, userService.findUser()), HttpStatus.OK);
    }

    @PostMapping("/scrap")
    @ApiResponses({
            @ApiResponse(code = 200, message = "유저 세무정보 scrap API")
    })
    @ApiOperation(value = "유저 세무정보 scrap API", notes = "<strong>JWT 토큰</strong>을 Authorization 헤더로 입력받아 유저 세무정보 scrap API를 조회 및 세무정보를 저장한다.")
    public ResponseEntity<Object> scrap() throws ParseException {
        return new ResponseEntity<>(userTaxInfoService.findScrap(), HttpStatus.OK);
    }

    @GetMapping("/refund")
    @ApiResponses({
            @ApiResponse(code = 200, message = "유저 환급액 계산 및 조회 API")
    })
    @ApiOperation(value = "유저 환급액 계산 및 조회 API", notes = "<strong>JWT 토큰</strong>을 Authorization 헤더로 입력받아 유저 환급액을 계산및 조회한다.")
    public ResponseEntity<Object> refund() throws ParseException {

        return new ResponseEntity<>(new SucessResponse(CommonEnum.STATUS_SUCCESS.getName(), UserTaxEnum.USER_REFUND_CALC_SUCESS, userRefundService.refund()), HttpStatus.OK);
    }

}
