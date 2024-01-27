package main.refundsapi.common.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import java.util.Arrays;
import java.util.List;

@Configuration
@SecurityScheme(
        type = SecuritySchemeType.APIKEY, in = SecuritySchemeIn.HEADER,
        name = "Authorization", description = "Auth Token"
)
public class SwaggerConfig {

    @Bean
    public OpenAPI gardenOpenAPI()  {
        return new OpenAPI()
                .info(
                        new Info().title("세금환급 조회 API")
                                .description("세금환급 조회 Rest API 문서입니다.")
                                .version("v1.0")
                )
                .security(
                        List.of(new SecurityRequirement().addList("Authorization"))
                );
    }

}
