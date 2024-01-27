package main.refundsapi.common.config.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import main.refundsapi.common.ErrorCode;
import main.refundsapi.user.enums.UserCode;
import main.refundsapi.login.domain.dto.LoginDto;
import main.refundsapi.user.domain.dto.TokenDto;
import main.refundsapi.common.exception.JwtException;
import main.refundsapi.user.exception.UserException;
import main.refundsapi.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Collections;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class TokenProvider {


    @Value("${jwt.secret}") private String secret;
    @Value("${jwt.token-validity-in-seconds}") private long tokenValidityInMilliseconds;

    private final UserRepository userRepository;

    private final PasswordEncoder bCryptPasswordEncoder;


    public TokenDto createToken(LoginDto loginDto) {
        var findUser = userRepository.findByUserId(loginDto.getUserId()).orElseThrow(() -> new UserException(UserCode.USER_NOT_FOUND));

        if(bCryptPasswordEncoder.matches(loginDto.getPassword(), findUser.getPassword())){
            long now = (new Date()).getTime();
            Date validity = new Date(now + this.tokenValidityInMilliseconds);

            return new TokenDto(Jwts.builder()
                    .setSubject(findUser.getUserId())
                    .claim("userId", findUser.getUserId())
                    .claim("name", findUser.getName())
                    .signWith(secretKeyDecode(), SignatureAlgorithm.HS512)
                    .setExpiration(validity)
                    .compact());
        }else{
            throw new UserException(UserCode.USER_LOGIN_FAIL);
        }
    }

    public Authentication getAuthentication(String token) throws JwtException {
        Claims claims = parseClaims(token);
        User principal = new User(claims.getSubject(), "", Collections.emptyList());
        return new UsernamePasswordAuthenticationToken(principal, token, Collections.emptyList());
    }

    private Claims parseClaims(String token) throws JwtException {
        try {
            return Jwts.parserBuilder().setSigningKey(secretKeyDecode()).build().parseClaimsJws(token).getBody();
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            throw new JwtException(ErrorCode.UNAUTHORIZED, "잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            throw new JwtException(ErrorCode.UNAUTHORIZED, "만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            throw new JwtException(ErrorCode.UNAUTHORIZED, "지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            throw new JwtException(ErrorCode.UNAUTHORIZED, "JWT 토큰이 잘못되었습니다.");
        }
    }

    public Key secretKeyDecode() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
