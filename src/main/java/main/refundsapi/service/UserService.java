package main.refundsapi.service;

import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import main.refundsapi.common_enum.CommonEnum;
import main.refundsapi.common_enum.ErrorCode;
import main.refundsapi.common_enum.UserEnum;
import main.refundsapi.dto.UserApiDto;
import main.refundsapi.dto.UserDto;
import main.refundsapi.entity.UserEntity;
import main.refundsapi.exception.CommonException;
import main.refundsapi.repository.UserJoinAccessRepository;
import main.refundsapi.repository.UserQueryRepository;
import main.refundsapi.repository.UserRepository;
import main.refundsapi.response.ErrorResponse;
import main.refundsapi.response.SucessResponse;
import main.refundsapi.util.SecurityUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class UserService<T> {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserQueryRepository userQueryRepository;

    private final UserJoinAccessRepository userJoinAccessRepository;

    /**
     * 유저 정보 저장
     * */
    @Transactional
    public T saveUser(UserDto userDto){

        //유저 가입 제한자 여부 조회
        var findUserEntity = userJoinAccessRepository.findByNameAndRegNo(userDto.getName(),userDto.getRegNo());

        //유저가입 가능자 여부 && 중복유저 가입 여부 필터링
        if(findUserEntity.isPresent() && duplicationUser(userDto)){

            //유저 정보 저장후 리턴된 유저 정보 저장
            var saveUserEntity = userRepository.save(
                    UserEntity.builder()
                            .name(userDto.getName())
                            .userId(userDto.getUserId())
                            .password(passwordEncoder.encode(userDto.getPassword()))
                            .regNo(userDto.getRegNo())
                            .build()
            );

            //유저가입 정상적 완료
            if(null != saveUserEntity){
                return (T) new SucessResponse(CommonEnum.STATUS_SUCCESS.getName(), UserEnum.USER_SAVE_SUCESS, UserApiDto.UserApiDtoConvert(
                                saveUserEntity
                        ));
            }
            //유저가입 실패
            else{
                return (T) new ErrorResponse(CommonEnum.STATUS_FAIL.getName(), UserEnum.USER_SAVE_FAIL);
            }
        }

        return (T) new ErrorResponse(CommonEnum.STATUS_FAIL.getName(), UserEnum.USER_SAVE_ACCESS);
    }

    public void login(UserDto userDto){

        var findUserEntity = userRepository.findByUserIdAndPassword(userDto.getUserId(), userDto.getPassword())
                .orElseThrow(() -> new CommonException(ErrorCode.USER_NOT_FOUND));


        Date now = new Date();

         Jwts.builder()
            .setHeaderParam(Header.TYPE, Header.JWT_TYPE) // (1)
            .setIssuer("fresh") // (2)
            .setIssuedAt(now) // (3)
            .setExpiration(new Date(now.getTime() + Duration.ofMinutes(30).toMillis())) // (4)
            .claim("id", "아이디") // (5)
            .claim("email", "ajufresh@gmail.com")
            .signWith(SignatureAlgorithm.HS256, "secret") // (6)
            .compact();

    }

    /**
     * SecurityContextHolder에서 저장된 userId로 유저 정보 조회
     * */
    @Transactional
    public UserApiDto findUser(){
        return UserApiDto.UserApiDtoConvert(
                SecurityUtil.getCurrentUserId()
                .flatMap(userRepository::findByUserId)
                .orElseThrow(() -> new CommonException(ErrorCode.USER_NOT_FOUND)
                )
        );
    }

    /**
     * 유저 아이디를 기준으로 유저 정보 조회
     * */
    @Transactional
    public UserApiDto findByUserId(String userId){
        return UserApiDto.UserApiDtoConvert(
                userRepository.findByUserId(userId).orElseThrow(() -> new CommonException(ErrorCode.USER_NOT_FOUND))
        );
    }

    /**
    * 유저 중복 체크를 위한 유저 정보 조회
    * */
    @Transactional
    public boolean duplicationUser(UserDto userDto){
        var findUserEntity = userRepository.findByUserId(userDto.getUserId());

        if(findUserEntity.isPresent()){
            return false;
        }

        return true;
    }
}
