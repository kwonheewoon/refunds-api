package main.refundsapi.user.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@Schema(description = "사용자 회원가입 API DTO")
public class UserSignupDto {

    @Schema(description = "기본키", example = "1L", hidden = true)
    private Long id;

    @Schema(description = "이름", nullable = true, example = "홍길동")
    private String name;

    @Schema(description = "아이디", nullable = true, example = "hong123")
    private String userId;

    @Schema(description = "비밀번호", nullable = true, example = "123456")
    private String password;

    @Schema(description = "주민번호", nullable = true, example = "860824-1655068")
    private String regNo;
}
