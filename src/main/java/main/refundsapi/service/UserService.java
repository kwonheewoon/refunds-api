package main.refundsapi.service;

import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import main.refundsapi.common_enum.ErrorCode;
import main.refundsapi.dto.UserApiDto;
import main.refundsapi.dto.UserDto;
import main.refundsapi.entity.UserEntity;
import main.refundsapi.exception.CommonException;
import main.refundsapi.repository.UserQueryRepository;
import main.refundsapi.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final UserQueryRepository userQueryRepository;

    public UserApiDto saveUser(UserDto userDto){

        return UserApiDto.UserApiDtoConvert(userRepository.save(
                UserEntity.builder()
                .name(userDto.getName())
                .userId(userDto.getUserId())
                .password(userDto.getPassword())
                .regNo(userDto.getRegNo())
                .build()
        ));
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
}
