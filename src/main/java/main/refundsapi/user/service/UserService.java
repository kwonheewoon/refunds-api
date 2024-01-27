package main.refundsapi.user.service;

import lombok.RequiredArgsConstructor;
import main.refundsapi.user.enums.UserCode;
import main.refundsapi.user.domain.dto.UserApiDto;
import main.refundsapi.user.domain.dto.UserSignupDto;
import main.refundsapi.user.domain.entity.User;
import main.refundsapi.user.exception.UserException;
import main.refundsapi.user.repository.UserJoinAccessRepository;
import main.refundsapi.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserJoinAccessRepository userJoinAccessRepository;

    /**
     * 유저 정보 저장
     */
    @Transactional
    public UserApiDto saveUser(UserSignupDto userSignupDto){

        // 가입 제한자 여부 검사
        if(isSignupRestricted(userSignupDto)){
            throw new UserException(UserCode.USER_SAVE_ACCESS);
        }

        // 중복 유저 체크
        if(isUserDuplicated(userSignupDto)){
            throw new UserException(UserCode.USER_SAVE_ACCESS);
        }

        // 유저 정보 저장
        var savedUser = userRepository.save(
                User.builder()
                        .name(userSignupDto.getName())
                        .userId(userSignupDto.getUserId())
                        .password(passwordEncoder.encode(userSignupDto.getPassword()))
                        .regNo(userSignupDto.getRegNo())
                        .build()
        );

        return UserApiDto.UserApiDtoConvert(savedUser);
    }

    /**
     * 가입 제한자 조회
     */
    @Transactional
    public boolean isSignupRestricted(UserSignupDto userSignupDto){
        return userJoinAccessRepository.countByNameAndRegNo(userSignupDto.getName(), userSignupDto.getRegNo()) < 1;
    }

    /**
     * 유저 중복 또는 가입 제한자 여부 체크
     */
    @Transactional
    public boolean isUserDuplicated(UserSignupDto userSignupDto){
        return userRepository.countByUserId(userSignupDto.getUserId()) > 0;
    }
}
