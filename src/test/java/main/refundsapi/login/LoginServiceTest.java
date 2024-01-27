package main.refundsapi.login;import lombok.extern.slf4j.Slf4j;import lombok.val;import main.refundsapi.login.service.LoginService;import main.refundsapi.login.domain.dto.LoginDto;import main.refundsapi.user.domain.dto.UserSignupDto;import main.refundsapi.user.domain.entity.User;import main.refundsapi.user.enums.UserCode;import main.refundsapi.user.exception.UserException;import main.refundsapi.user.repository.UserRepository;import org.junit.jupiter.api.Assertions;import org.junit.jupiter.api.BeforeEach;import org.junit.jupiter.api.Test;import org.junit.jupiter.api.extension.ExtendWith;import org.mockito.InjectMocks;import org.mockito.Mock;import org.mockito.junit.jupiter.MockitoExtension;import org.springframework.security.crypto.password.PasswordEncoder;import org.springframework.test.util.ReflectionTestUtils;import java.util.Optional;import static org.junit.jupiter.api.Assertions.assertThrows;import static org.mockito.ArgumentMatchers.anyString;import static org.mockito.BDDMockito.given;@ExtendWith(MockitoExtension.class)@Slf4jpublic class LoginServiceTest {    @Mock    private UserRepository userRepository;    @Mock    private PasswordEncoder bCryptPasswordEncoder;    @InjectMocks    private LoginService loginService;    @BeforeEach    void setUp(){        ReflectionTestUtils.setField(loginService,                "secret",                "a3dvbmhlZXdvb24gdXNlciBhcGkgcHJvamVjdCBzcmMubWFpbi5qYXZhLm1haW4ucmVmdW5kc2FwaSBwYWNrYWdl");        ReflectionTestUtils.setField(loginService,                "tokenValidityInMilliseconds",                86400000);    }    @Test    void 토큰생성_테스트(){        var userDto = UserSignupDto.builder().name("홍길동").password("123456").userId("hong123").regNo("860824-1655068").build();        var returnUser = User.builder()                .id(10L)                .name("홍길동")                .userId("hong123")                .password("aGp6hc05mNo6Il/4re7e4g==")                .regNo("860824-1655068")                .build();        var loginDto = new LoginDto("홍길동", "123456");        given(userRepository.findByUserId(anyString())).willReturn(Optional.of(returnUser));        given(bCryptPasswordEncoder.matches(anyString(), anyString()))                .willReturn(true);        var result = loginService.login(loginDto);        log.info("result = {}", result);    }    @Test    void 토큰생성_실패_로그인실패(){        var userDto = UserSignupDto.builder().name("홍길동").password("123456").userId("hong123").regNo("860824-1655068").build();        var returnUser = User.builder()                .id(10L)                .name("홍길동")                .userId("hong123")                .password("aGp6hc05mNo6Il/4re7e4g==")                .regNo("860824-1655068")                .build();        var loginDto = new LoginDto("홍길동", "123456");        given(userRepository.findByUserId(anyString())).willReturn(Optional.of(returnUser));        given(bCryptPasswordEncoder.matches(anyString(), anyString()))                .willReturn(false);        val throwable = assertThrows(UserException.class, () -> loginService.login(loginDto));        Assertions.assertEquals(UserCode.USER_LOGIN_FAIL.getCode(), throwable.getCode());    }}