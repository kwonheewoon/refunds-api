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
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.Optional;

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
    @DisplayName("유저 저장 로직")
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
        void 유저_저장(){

            given(userRepository.save(any(UserEntity.class))).willReturn(returnUserEntity);

            var resultEntity = userRepository.save(saveUserEntity);

            assertNotNull(resultEntity);
            Assertions.assertThat(saveUserEntity.getName()).isEqualTo(returnUserEntity.getName());
            Assertions.assertThat(resultEntity.getId()).isEqualTo(10L);

            verify(userRepository).save(refEq(saveUserEntity));

        }

        @Test
        void 유저_가능자_여부_중복가입_여부_필터링(){

            given(userJoinAccessRepository.findByNameAndRegNo(anyString(), anyString())).willReturn(Optional.of(userJoinAccessEntity));
            given(userRepository.findByUserIdAndName(anyString(), anyString())).willReturn(Optional.ofNullable(returnUserEntity));

            var resultEntity = userJoinAccessRepository.findByNameAndRegNo("홍길동", SeedUtil.encrypt("860824-1655068"));


            //유저가입 가능자 여부 && 중복유저 가입 여부 필터링
            Assertions.assertThat(resultEntity.isPresent() && duplicationUser(userDto)).isFalse();

            assertNotNull(resultEntity);

            verify(userJoinAccessRepository).findByNameAndRegNo("홍길동", SeedUtil.encrypt("860824-1655068"));
            verify(userRepository).findByUserIdAndName("hong12", "홍길동");
        }

    }

    @Nested
    @DisplayName("유저 로그인 로직")
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
        void 유저_로그인(){

            given(userRepository.findByUserId(anyString())).willReturn(Optional.of(returnUserEntity));

            var resultEntity = userRepository.findByUserId("hong12").orElseThrow();
            var resultUserEntity = createUser(resultEntity);

            assertNotNull(resultEntity);
            assertNotNull(resultUserEntity);
            Assertions.assertThat(resultUserEntity.getUsername()).isEqualTo("hong12");
            verify(userRepository).findByUserId("hong12");
        }
    }

    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    private User createUser(UserEntity userEntity) {
        if (null == userEntity) {
            throw new CommonException(ErrorCode.USER_NOT_FOUND);
        }

        return new User(userEntity.getUserId(),
                userEntity.getPassword(),
                Arrays.asList());
    }

    /**
     * 유저 중복 체크를 위한 유저 정보 조회
     * */
    public boolean duplicationUser(UserDto userDto){
        var findUserEntity = userRepository.findByUserIdAndName(userDto.getUserId(), userDto.getName());

        if(findUserEntity.isPresent()){
            return false;
        }

        return true;
    }
}
