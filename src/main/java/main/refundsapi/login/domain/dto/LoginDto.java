package main.refundsapi.login.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@Schema(description = "로그인 DTO")
public class LoginDto {

    @Schema(description = "로그인 아이디", nullable = true, example = "hong123")
    private String userId;

    @Schema(description = "로그인 비밀번호", nullable = true, example = "123456")
    private String password;


}
