package main.refundsapi.user;


import lombok.extern.slf4j.Slf4j;
import lombok.val;
import main.refundsapi.user.domain.dto.UserApiDto;
import main.refundsapi.user.domain.dto.UserSignupDto;
import main.refundsapi.user.domain.entity.BaseEntity;
import main.refundsapi.user.domain.entity.User;
import main.refundsapi.user.domain.entity.UserJoinAccess;
import main.refundsapi.user.enums.UserCode;
import main.refundsapi.user.exception.UserException;
import main.refundsapi.user.repository.UserJoinAccessRepository;
import main.refundsapi.user.repository.UserRepository;
import main.refundsapi.user.service.UserService;
import main.refundsapi.util.AESUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;


@ExtendWith(MockitoExtension.class)
@Slf4j
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Mock
    private UserJoinAccessRepository userJoinAccessRepository;

    @Nested
    @DisplayName("유저 저장 로직")
    class userSave{

        private UserSignupDto userSignupDto;
        private User saveUser;
        private User returnUser;
        private UserJoinAccess userJoinAccess;

        @BeforeEach
        void save_setup(){
            userSignupDto = UserSignupDto.builder().name("홍길동").password("123456").userId("hong123").regNo("860824-1655068").build();

            saveUser = User.builder()
                    .name(userSignupDto.getName())
                    .userId(userSignupDto.getUserId())
                    .password("$2a$10$zEDBXI8DWvxk/useN8Dv1ORHbJE5AQkt08UE6M81RbiMEZHY/gCLW")
                    .regNo(AESUtil.encrypt(userSignupDto.getRegNo()))
                    .build();

            returnUser = User.builder()
                    .id(10L)
                    .name(userSignupDto.getName())
                    .userId(userSignupDto.getUserId())
                    .password("$2a$10$zEDBXI8DWvxk/useN8Dv1ORHbJE5AQkt08UE6M81RbiMEZHY/gCLW")
                    .regNo(AESUtil.encrypt(userSignupDto.getRegNo()))
                    .build();

            userJoinAccess = UserJoinAccess.builder().name("홍길동").regNo(AESUtil.decrypt("aGp6hc05mNo6Il/4re7e4g==")).build();
        }

        @Test
        public void 회원가입_성공() {
            // Given
            given(userJoinAccessRepository.countByNameAndRegNo(anyString(), anyString()))
                    .willReturn(1);
            given(userRepository.countByUserId(anyString()))
                    .willReturn(0);
            given(userRepository.save(any(User.class)))
                    .willReturn(new User(1L, "hong123", "$2a$10$Wh5hBRJ7oFfCOvA1rC/Vs.OvrFP1Gf9q7SFixksEq6haXNWvQ8.36\t", "홍길동", "aGp6hc05mNo6Il/4re7e4g==\n", new BaseEntity()));
            given(passwordEncoder.encode(anyString())).willReturn("aGp6hc05mNo6Il/4re7e4g==");

            // When
            UserApiDto result = userService.saveUser(userSignupDto);

            // Then
            assertNotNull(result);
            assertEquals(userSignupDto.getName(), result.getName());
            assertEquals(userSignupDto.getUserId(), result.getUserId());
            // 추가적인 검증 로직
        }

        @Test
        public void 회원가입_실패_중복_회원가입() {
            // Given
            given(userJoinAccessRepository.countByNameAndRegNo(anyString(), anyString()))
                    .willReturn(1);
            given(userRepository.countByUserId(anyString()))
                    .willReturn(1);

            // When
            val throwable = assertThrows(UserException.class, () -> userService.saveUser(userSignupDto));

            // Then
            assertEquals(UserCode.USER_SAVE_ACCESS.getCode(), throwable.getCode());

        }

        @Test
        public void 회원가입_실패_중복_가입제한자() {
            // Given
            given(userJoinAccessRepository.countByNameAndRegNo(anyString(), anyString()))
                    .willReturn(0);
            // When
            val throwable = assertThrows(UserException.class, () -> userService.saveUser(userSignupDto));

            // Then
            assertEquals(UserCode.USER_SAVE_ACCESS.getCode(), throwable.getCode());

        }
    }
}
