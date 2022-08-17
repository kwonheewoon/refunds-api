package main.refundsapi.service;

import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import main.refundsapi.common_enum.ErrorCode;
import main.refundsapi.common_enum.UserEnum;
import main.refundsapi.dto.UserApiDto;
import main.refundsapi.dto.UserDto;
import main.refundsapi.entity.UserEntity;
import main.refundsapi.exception.CommonException;
import main.refundsapi.repository.UserJoinAccessRepository;
import main.refundsapi.repository.UserQueryRepository;
import main.refundsapi.repository.UserRepository;
import main.refundsapi.response.SucessResponse;
import main.refundsapi.util.SecurityUtil;
import main.refundsapi.util.SeedUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserQueryRepository userQueryRepository;

    private final UserJoinAccessRepository userJoinAccessRepository;

    public ResponseEntity<Object> saveUser(UserDto userDto){

        var findUserEntity = userJoinAccessRepository.findByNameAndRegNo(userDto.getName(),userDto.getRegNo());

        if(findUserEntity.isPresent() && duplicationUser(userDto)){

            return new ResponseEntity<>(new SucessResponse(
                    UserEnum.USER_SAVE_SUCESS,
                    UserApiDto.UserApiDtoConvert(userRepository.save(
                    UserEntity.builder()
                            .name(userDto.getName())
                            .userId(userDto.getUserId())
                            .password(passwordEncoder.encode(userDto.getPassword()))
                            .regNo(userDto.getRegNo())
                            .build()
                    )
            )), HttpStatus.OK);


        }

        return new ResponseEntity<>(new SucessResponse(UserEnum.USER_SAVE_ACCESS, new UserApiDto()), HttpStatus.OK);
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

    public UserApiDto findUser(){
        return UserApiDto.UserApiDtoConvert(
                SecurityUtil.getCurrentUserId()
                .flatMap(userRepository::findByUserId)
                .orElseThrow(() -> new CommonException(ErrorCode.USER_NOT_FOUND)
                )
        );
    }

    public UserApiDto findByUserId(UserDto userDto){
        return UserApiDto.UserApiDtoConvert(
                userRepository.findByUserId(userDto.getUserId()).orElseThrow(() -> new CommonException(ErrorCode.USER_NOT_FOUND))
        );
    }

    public boolean duplicationUser(UserDto userDto){
        var findUserEntity = userRepository.findByUserIdAndName(userDto.getUserId(), userDto.getName());

        if(findUserEntity.isPresent()){
            return false;
        }

        return true;
    }
}
