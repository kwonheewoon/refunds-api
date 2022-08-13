package main.refundsapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import main.refundsapi.entity.UserEntity;

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
    private String password;

    /**/
    private String regNo;

    public static UserApiDto UserApiDtoConvert(UserEntity userEntity){
        return UserApiDto.builder()
                .id(userEntity.getId())
                .password(userEntity.getPassword())
                .userId(userEntity.getUserId())
                .password(userEntity.getPassword())
                .regNo(userEntity.getRegNo())
                .build();
    }
}
