package main.refundsapi.user.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@AllArgsConstructor
@Schema(description = "토큰 액세스 DTO")
public class TokenDto {

    @Schema(description = "accessToken", nullable = true, example = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJra...")
    private String accessToken;
}
