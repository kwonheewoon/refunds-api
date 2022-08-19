package main.refundsapi.dto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    /**/
    @ApiModelProperty(hidden = true)
    private Long id;

    /**/
    @ApiModelProperty(example = "홍길동")
    private String name;

    /**/
    @ApiModelProperty(example = "hong123")
    private String userId;

    /**/
    @ApiModelProperty(example = "123456")
    private String password;

    /**/
    @ApiModelProperty(example = "860824-1655068")
    private String regNo;
}
