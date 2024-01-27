package main.refundsapi.user.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import main.refundsapi.user.domain.entity.User;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "사용자 정보 API DTO")
public class UserApiDto {

    @Schema(description = "기본키", example = "1L", hidden = true)
    private Long id;

    @Schema(description = "이름", nullable = true, example = "홍길동")
    private String name;

    @Schema(description = "아이디", nullable = true, example = "hong123")
    private String userId;

    @Schema(description = "주민번호", nullable = true, example = "860824-1655068")
    private String regNo;

    public static UserApiDto UserApiDtoConvert(User user){
        return UserApiDto.builder()
                .id(user.getId())
                .name(user.getName())
                .userId(user.getUserId())
                .regNo(user.getRegNo())
                .build();
    }
}
