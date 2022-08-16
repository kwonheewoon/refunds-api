package main.refundsapi;


import lombok.extern.slf4j.Slf4j;
import main.refundsapi.common_enum.ErrorCode;
import main.refundsapi.dto.UserDto;
import main.refundsapi.entity.UserEntity;
import main.refundsapi.entity.UserJoinAccessEntity;
import main.refundsapi.exception.CommonException;
import main.refundsapi.repository.UserJoinAccessRepository;
import main.refundsapi.repository.UserQueryRepository;
import main.refundsapi.repository.UserRepository;
import main.refundsapi.util.SeedUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@Slf4j
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;


    @Mock
    private UserQueryRepository userQueryRepository;

    @Mock
    private UserJoinAccessRepository userJoinAccessRepository;

    @Nested
    @DisplayName("회원 저장 로직")
    class userSave{

        private UserDto userDto;
        private UserEntity saveUserEntity;
        private UserEntity returnUserEntity;
        private UserJoinAccessEntity userJoinAccessEntity;

        @BeforeEach
        void save_setup(){
            userDto = UserDto.builder().name("홍길동").password("123456").userId("hong12").regNo("860824-1655068").build();

            saveUserEntity = UserEntity.builder()
                    .name(userDto.getName())
                    .userId(userDto.getUserId())
                    .password(passwordEncoder().encode(userDto.getPassword()))
                    .regNo(SeedUtil.encrypt(userDto.getRegNo()))
                    .build();

            returnUserEntity = UserEntity.builder()
                    .id(10L)
                    .name(userDto.getName())
                    .userId(userDto.getUserId())
                    .password(passwordEncoder().encode(userDto.getPassword()))
                    .regNo(SeedUtil.encrypt(userDto.getRegNo()))
                    .build();

            userJoinAccessEntity = UserJoinAccessEntity.builder().name("홍길동").regNo(SeedUtil.decrypt("C/NROlRVqioWWy/HOPdDpQ==")).build();
        }

        @Test
        void 회원_저장(){

            given(userRepository.save(any(UserEntity.class))).willReturn(returnUserEntity);

            var resultEntity = userRepository.save(saveUserEntity);

            assertNotNull(resultEntity);
            Assertions.assertThat(saveUserEntity.getName()).isEqualTo(returnUserEntity.getName());
            Assertions.assertThat(resultEntity.getId()).isEqualTo(10L);

            verify(userRepository).save(refEq(saveUserEntity));

        }

        @Test
        void 회원_가능_조회(){

            given(userJoinAccessRepository.findByNameAndRegNo(anyString(), anyString())).willReturn(Optional.of(userJoinAccessEntity));

            var resultEntity = userJoinAccessRepository.findByNameAndRegNo("홍길동", SeedUtil.encrypt("860824-1655068")).orElseThrow();

            assertNotNull(resultEntity);
            Assertions.assertThat(resultEntity.getRegNo()).isEqualTo("860824-1655068");

            verify(userJoinAccessRepository).findByNameAndRegNo("홍길동", SeedUtil.encrypt("860824-1655068"));
        }

        @Test
        void 회원_중복조회(){

            given(userRepository.findByUserIdAndName(anyString(), anyString())).willReturn(Optional.ofNullable(returnUserEntity));

            var findUserEntity = userRepository.findByUserIdAndName("hong12", "홍길동").orElseThrow();

            assertNotNull(findUserEntity);

            verify(userRepository).findByUserIdAndName("hong12", "홍길동");
        }

    }

    @Nested
    @DisplayName("회원 로그인 로직")
    class userLogin{

        private UserDto userDto;
        private UserEntity returnUserEntity;

        @BeforeEach
        void save_setup(){
            userDto = UserDto.builder().name("홍길동").password("123456").userId("hong12").regNo("860824-1655068").build();

            returnUserEntity = UserEntity.builder()
                    .id(10L)
                    .name(userDto.getName())
                    .userId(userDto.getUserId())
                    .password(passwordEncoder().encode(userDto.getPassword()))
                    .regNo(SeedUtil.encrypt(userDto.getRegNo()))
                    .build();

        }

        @Test
        void 회원_로그인(){

            given(userRepository.findByUserId(anyString())).willReturn(Optional.of(returnUserEntity));

            var resultEntity = userRepository.findByUserId("hong12").orElseThrow();

            assertNotNull(resultEntity);

            verify(userRepository).findByUserId("hong12");
        }
    }

    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    private User createUser(String username, UserEntity userEntity) {
        if (null == userEntity) {
            throw new CommonException(ErrorCode.USER_NOT_FOUND);
        }

        return new User(userEntity.getUserId(),
                userEntity.getPassword(),
                Arrays.asList());
    }
}
