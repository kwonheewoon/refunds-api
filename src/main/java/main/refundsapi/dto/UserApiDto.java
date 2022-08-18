package main.refundsapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import main.refundsapi.entity.UserEntity;
import main.refundsapi.util.SeedUtil;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserApiDto {

    /**/
    private Long id;

    /**/
    private String name;

    /**/
    private String userId;

    /**/
    private String regNo;

    public static UserApiDto UserApiDtoConvert(UserEntity userEntity){
        return UserApiDto.builder()
                .id(userEntity.getId())
                .name(userEntity.getName())
                .userId(userEntity.getUserId())
                .regNo(userEntity.getRegNo())
                .build();
    }
}
