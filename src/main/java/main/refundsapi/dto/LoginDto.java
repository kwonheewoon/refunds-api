package main.refundsapi.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {

    /**/
    @ApiModelProperty(name = "유저 아이디",example = "hong123")
    private String userId;

    /**/
    @ApiModelProperty(example = "123456")
    private String password;


}
