package main.refundsapi.controller;

import lombok.RequiredArgsConstructor;
import main.refundsapi.common_enum.CommonEnum;
import main.refundsapi.common_enum.UserEnum;
import main.refundsapi.common_enum.UserTaxEnum;
import main.refundsapi.dto.TokenDto;
import main.refundsapi.dto.UserDto;
import main.refundsapi.jwt.JwtFilter;
import main.refundsapi.jwt.TokenProvider;
import main.refundsapi.response.SucessResponse;
import main.refundsapi.service.UserInfoScrapService;
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

    private final UserInfoScrapService userInfoScrapService;

    private final UserRefundService userRefundService;

    private final TokenProvider tokenProvider;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @PostMapping("/signup")
    public ResponseEntity<Object> signup(@RequestBody UserDto userDto){
        return new ResponseEntity<>(userService.saveUser(userDto) , HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody UserDto userDto){
        
        //유저 정보 조회
        var findUserApiDto = userService.findByUserId(userDto);

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(findUserApiDto.getUserId(), userDto.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.createToken(authentication);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

        return new ResponseEntity<>(new SucessResponse(CommonEnum.STATUS_SUCESS.getName(), UserEnum.USER_LOGIN_SUCESS, new TokenDto(jwt)), httpHeaders, HttpStatus.OK);
    }

    @GetMapping("/me")
    public ResponseEntity<Object> me(){
        return new ResponseEntity<>(new SucessResponse(CommonEnum.STATUS_SUCESS.getName(), UserEnum.USER_FIND_SUCESS, userService.findUser()), HttpStatus.OK);
    }

    @PostMapping("/scrap")
    public ResponseEntity<Object> scrap() throws ParseException {
        return new ResponseEntity<>(new SucessResponse(CommonEnum.STATUS_SUCESS.getName(), UserTaxEnum.USER_FIND_SCRAP_SUCESS, userInfoScrapService.findScrap()), HttpStatus.OK);
    }

    @GetMapping("/refund")
    public ResponseEntity<Object> refund() throws ParseException {

        return new ResponseEntity<>(new SucessResponse(CommonEnum.STATUS_SUCESS.getName(), UserTaxEnum.USER_REFUND_CALC_SUCESS, userRefundService.refund()), HttpStatus.OK);
    }

}
